package io.github.theriverelder.housekeeper.client;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.theriverelder.housekeeper.data.HousekeeperConfig;
import io.github.theriverelder.housekeeper.data.MatchIdRule;
import io.github.theriverelder.housekeeper.networking.Networking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.List;

public class ConfigScreen extends HandledScreen<ConfigScreenHandler> {

    private static final Identifier BG_TEX_ID = new Identifier("minecraft", "textures/gui/container/dispenser.png");

    public ConfigScreen(ConfigScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    public HousekeeperConfig getConfig() {
        return handler.config;
    }

    private ButtonWidget btnSave;
    private Text txtIndex;
    private TextFieldWidget tfdIndex;
    private ButtonWidget btnJump;
    private Text txtName;
    private TextFieldWidget tfdName;
    private Text txtPattern;
    private TextFieldWidget tfdPattern;

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    @Override
    protected void init() {
        super.init();
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;

        this.x = (width - backgroundWidth) / 2;
        this.y = (height - backgroundHeight) / 2;

        btnSave = this.addDrawableChild(new ButtonWidget(x + 10, y + 10, 50, 10, new LiteralText("Save"), this::onBtnSavePressed));
        txtIndex = new LiteralText("Index:");
        tfdIndex = this.addDrawableChild(new TextFieldWidget(textRenderer, x + 100, y + 10, 20, 10, new LiteralText("0")));
        btnJump = this.addDrawableChild(new ButtonWidget(x + 130, y + 10, 50, 10, new LiteralText("Jump"), this::onBtnJumpPressed));
        txtName = new LiteralText("Name:");
        tfdName = this.addDrawableChild(new TextFieldWidget(textRenderer, x + 50, y + 30, 100, 10, new LiteralText("Name")));
        txtPattern = new LiteralText("Pattern:");
        tfdPattern = this.addDrawableChild(new TextFieldWidget(textRenderer, x + 50, y + 50, 100, 10, new LiteralText("Pattern")));

        refillFields();
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BG_TEX_ID);
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        super.drawForeground(matrices, mouseX, mouseY);
        textRenderer.draw(matrices, txtIndex, 70, 10, Color.BLACK.getRGB());
        textRenderer.draw(matrices, txtName, 10, 30, Color.BLACK.getRGB());
        textRenderer.draw(matrices, txtPattern, 10, 50, Color.BLACK.getRGB());
    }

    private void addEntry() {
        HousekeeperConfig.Entry entry = new HousekeeperConfig.Entry();
        entry.name = "Entry-" + getConfig().getEntries().size();
        entry.rule = new MatchIdRule();
        getConfig().add(entry);
    }

    private void refillFields() {
        HousekeeperConfig config = getConfig();
        List<HousekeeperConfig.Entry> entries = config.getEntries();
        if (entries.size() == 0) {
            addEntry();
        }

        int index = config.getCurrentIndex();
        tfdIndex.setText(index >= 0 ? String.valueOf(index) : "");
        if (index < 0) {
            tfdName.setVisible(true);
            tfdPattern.setVisible(true);
            return;
        }

        HousekeeperConfig.Entry entry = entries.get(index);

        tfdName.setText(entry.name);
        tfdPattern.setText(entry.rule.toString());
    }

    private void onBtnSavePressed(ButtonWidget button) {
        HousekeeperConfig config = getConfig();
        int index = config.getCurrentIndex();
        if (index >= 0 && index < config.getEntries().size()) {
            HousekeeperConfig.Entry entry = config.getEntries().get(index);
            entry.name = tfdName.getText();
            if (entry.rule instanceof MatchIdRule rule) {
                rule.setPattern(tfdPattern.getText());
            }
        }

        handler.config.applyOnStack(handler.stack);
        this.client.getNetworkHandler().sendPacket(ClientPlayNetworking.createC2SPacket(Networking.MAIN_HAND_STACK_UPDATE, PacketByteBufs.create().writeItemStack(handler.stack)));
    }

    private void onBtnJumpPressed(ButtonWidget button) {
        HousekeeperConfig config = getConfig();

        String indexStr = tfdIndex.getText();
        int index = -1;
        if (!indexStr.matches("^\\s*$")) {
            try {
                index = Integer.parseInt(indexStr);
            } catch (Exception ignored) {
                index = config.getCurrentIndex();
            }
        }
        if (index == config.getEntries().size() || (config.getEntries().size() == 0 && index == -1)) {
            addEntry();
        }
        config.setCurrentIndex(index);
        refillFields();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE && this.shouldCloseOnEsc()) {
            this.onClose();
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_TAB) {
            boolean bl;
            boolean bl2 = bl = !Screen.hasShiftDown();
            if (!this.changeFocus(bl)) {
                this.changeFocus(bl);
            }
        }
        if (this.getFocused() != null && this.getFocused().keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }
        this.handleHotbarKeyPressed(keyCode, scanCode);
        if (this.focusedSlot != null && this.focusedSlot.hasStack()) {
            if (this.client.options.keyPickItem.matchesKey(keyCode, scanCode)) {
                this.onMouseClick(this.focusedSlot, this.focusedSlot.id, 0, SlotActionType.CLONE);
            } else if (this.client.options.keyDrop.matchesKey(keyCode, scanCode)) {
                this.onMouseClick(this.focusedSlot, this.focusedSlot.id, HandledScreen.hasControlDown() ? 1 : 0, SlotActionType.THROW);
            }
        }
        return true;
    }
}
