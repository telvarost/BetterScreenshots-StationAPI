package com.github.telvarost.hudtweaks.mixin;

import com.github.telvarost.hudtweaks.Config;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Living;
import net.minecraft.sortme.GameRenderer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** - All credit for the code in this class goes to Dany and his mod UniTweaks
 *  See: https://github.com/DanyGames2014/UniTweaks
 */
@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Shadow
    private float field_2350;

    @Shadow private Minecraft minecraft;

    @Inject(method = "method_1842", at = @At(value = "HEAD"))
    public void finalBeta_overrideFogDensity(int f, float par2, CallbackInfo ci) {
        this.field_2350 = (256 >> this.minecraft.options.viewDistance) * 1;//ModOptions.getFogMultiplier();
    }

    @Unique
    public float getFovMultiplier(float f, boolean isHand) {
        Living entity = this.minecraft.viewEntity;
        float fov = 100;//ModOptions.getFovInDegrees();

        if (isHand) {
            fov = 70F;
        }

        if (entity.isInFluid(Material.WATER)) {
            fov *= 60.0F / 70.0F;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
            fov /= 4F;
            minecraft.options.cinematicMode = true;
        } else {
            minecraft.options.cinematicMode = false;
        }

        if (entity.health <= 0) {
            float deathTimeFov = (float) entity.deathTime + f;
            fov /= (1.0F - 500F / (deathTimeFov + 500F)) * 2.0F + 1.0F;
        }

        return fov;
    }

    @Unique
    public float getFovMultiplier(float f) {
        return getFovMultiplier(f, false);
    }

    @Redirect(method = "method_1840", at = @At(value = "INVOKE", target = "Lnet/minecraft/sortme/GameRenderer;method_1848(F)F"))
    public float finalBeta_redirectToCustomFov(GameRenderer instance, float value) {
        return getFovMultiplier(value);
    }

    @Inject(method = "method_1845", at = @At(value = "HEAD"))
    public void finalBeta_adjustHandFov(float f, int i, CallbackInfo ci) {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GLU.gluPerspective(getFovMultiplier(f, true), (float) minecraft.actualWidth / (float) minecraft.actualHeight, 0.05F, field_2350 * 2.0F);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }
}
