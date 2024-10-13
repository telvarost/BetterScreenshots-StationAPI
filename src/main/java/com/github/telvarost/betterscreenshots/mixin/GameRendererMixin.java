package com.github.telvarost.betterscreenshots.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow private Minecraft client;

    @Shadow private float viewDistance;

    @Shadow private double zoom;

    @Shadow private double zoomX;

    @Shadow private double zoomY;

    @Shadow protected abstract float getFov(float f);

    @Inject(method = "renderFirstPersonHand", at = @At("HEAD"), cancellable = true)
    private void betterScreenshots_method_1845(float f, int i, CallbackInfo ci) {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        float f3 = 0.07F;
        if(this.client.options.anaglyph3d) {
            GL11.glTranslatef((float)(i * 2 - 1) * f3, 0.0F, 0.0F);
        }

        if(this.zoom != 1.0D) {
            GL11.glTranslatef((float)this.zoomX, (float)(-this.zoomY), 0.0F);
            GL11.glScaled(this.zoom, this.zoom, 1.0D);
        }

        GLU.gluPerspective(this.getFov(f), (float)this.client.displayWidth / (float)this.client.displayHeight, 0.05F, this.viewDistance * 2.0F);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }

    @Redirect(
            method = "renderFrame",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/WorldRenderer;renderBlockOutline(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/hit/HitResult;ILnet/minecraft/item/ItemStack;F)V"
            )
    )
    public void betterScreenshots_delta(WorldRenderer instance, PlayerEntity arg, HitResult arg2, int i, ItemStack arg3, float f) {
        if (!this.client.options.hideHud) {
            instance.renderBlockOutline(arg, arg2, i, arg3, f);
        }
    }
}
