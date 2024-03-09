package com.github.telvarost.betterscreenshots.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.dimension.Dimension;
import net.minecraft.sortme.GameRenderer;
import net.minecraft.util.hit.HitResult;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow private Minecraft minecraft;

    @Shadow private float field_2350;

    @Shadow private double field_2331;

    @Shadow private double field_2332;

    @Shadow private double field_2333;

    @Shadow protected abstract float method_1848(float f);

    @Inject(method = "method_1845", at = @At("HEAD"), cancellable = true)
    private void betterScreenshots_method_1845(float f, int i, CallbackInfo ci) {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        float f3 = 0.07F;
        if(this.minecraft.options.anaglyph3d) {
            GL11.glTranslatef((float)(i * 2 - 1) * f3, 0.0F, 0.0F);
        }

        if(this.field_2331 != 1.0D) {
            GL11.glTranslatef((float)this.field_2332, (float)(-this.field_2333), 0.0F);
            GL11.glScaled(this.field_2331, this.field_2331, 1.0D);
        }

        GLU.gluPerspective(this.method_1848(f), (float)this.minecraft.actualWidth / (float)this.minecraft.actualHeight, 0.05F, this.field_2350 * 2.0F);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }

    @Redirect(
            method = "delta",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/WorldRenderer;method_1554(Lnet/minecraft/entity/player/PlayerBase;Lnet/minecraft/util/hit/HitResult;ILnet/minecraft/item/ItemInstance;F)V"
            )
    )
    public void betterScreenshots_delta(WorldRenderer instance, PlayerBase arg, HitResult arg2, int i, ItemInstance arg3, float f) {
        if (!this.minecraft.options.hideHud) {
            instance.method_1554(arg, arg2, i, arg3, f);
        }
    }
}
