package pelagic_prehistory.client.menu;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import pelagic_prehistory.PelagicPrehistory;
import pelagic_prehistory.menu.AnalyzerMenu;

public class AnalyzerScreen extends AbstractContainerScreen<AnalyzerMenu> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(PelagicPrehistory.MODID, "textures/gui/analyzer.png");

    private static final int IMAGE_WIDTH = 176;
    private static final int IMAGE_HEIGHT = 166;

    private static final int PROGRESS_X = 51;
    private static final int PROGRESS_Y = 25;
    private static final int PROGRESS_WIDTH = 74;
    private static final int PROGRESS_HEIGHT = 28;

    public AnalyzerScreen(AnalyzerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        // render background
        blit(pPoseStack, this.leftPos, this.topPos, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
        // render progress
        final int progressHeight = getMenu().getScaledProgress(PROGRESS_HEIGHT);
        if(progressHeight > 0) {
            blit(pPoseStack, this.leftPos + PROGRESS_X, this.topPos + PROGRESS_Y, IMAGE_WIDTH, 0, PROGRESS_WIDTH, progressHeight);
        }
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pPoseStack, pMouseX, pMouseY);
    }
}
