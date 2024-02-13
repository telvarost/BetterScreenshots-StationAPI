package com.github.telvarost.betterscreenshots.mixin;

import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.class_556;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.dimension.Dimension;
import net.minecraft.sortme.GameRenderer;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.maths.MathHelper;
import net.minecraft.util.maths.Vec3f;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.nio.FloatBuffer;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow private Minecraft minecraft;

    @Shadow private float field_2327;

    @Shadow private float field_2365;

    @Shadow private boolean field_2330;

    @Shadow private float field_2362;

    @Shadow private float field_2364;

    @Shadow private float field_2363;

    @Shadow private float field_2361;

    @Shadow private float field_2360;

    @Shadow private float field_2359;

    @Shadow private float field_2329;

    @Shadow private float field_2328;

    @Shadow private float field_2350;

    @Shadow private double field_2331;

    @Shadow private double field_2332;

    @Shadow private double field_2333;

    @Shadow protected abstract void method_1851(float f);

    @Shadow private int field_2351;

    @Shadow protected abstract void method_1850(float f);

    @Shadow protected abstract void method_1849(float f);

    @Shadow protected abstract float method_1848(float f);

    @Shadow public class_556 field_2342;

    @Shadow public abstract void method_1838(float f);

    @Shadow public static int field_2341;

    @Shadow protected abstract void method_1852(float f);

    @Shadow protected abstract void method_1842(int i, float f);

    @Shadow protected abstract void method_1847(float f);

    @Shadow private EntityBase field_2352;

    @Shadow protected abstract void method_1840(float f, int i);

    @Shadow protected abstract void method_1845(float f, int i);

    @Shadow protected abstract FloatBuffer method_1839(float f, float g, float h, float i);

    @Shadow private float field_2346;

    @Shadow private float field_2347;

    @Shadow private float field_2348;

//    @Inject(method = "method_1848", at = @At("HEAD"), cancellable = true)
//    private void betterScreenshots_method_1848(float f, CallbackInfoReturnable<Float> cir) {
//        Living var2 = this.minecraft.viewEntity;
//        float var3 = 70.0F;
////        if (var2.isInFluid(Material.WATER)) {
////            var3 = 60.0F;
////        }
//
//        if (var2.health <= 0) {
//            float var4 = (float)var2.deathTime + f;
//            var3 /= (1.0F - 500.0F / (var4 + 500.0F)) * 2.0F + 1.0F;
//        }
//
//        if (var2.isInFluid(Material.WATER)) {
//            var3 *= 0.857143F;
//        }
//
//        cir.setReturnValue(var3 + this.field_2327 + (this.field_2365 - this.field_2327) * f);
//    }
//
//
//    @Inject(method = "method_1851", at = @At("HEAD"), cancellable = true)
//    private void betterScreenshots_method_1851(float f, CallbackInfo ci) {
//        Living var2 = this.minecraft.viewEntity;
//        float var3 = var2.standingEyeHeight - 1.62F;
//        double var4 = var2.prevX + (var2.x - var2.prevX) * (double)f;
//        double var6 = var2.prevY + (var2.y - var2.prevY) * (double)f - (double)var3;
//        double var8 = var2.prevZ + (var2.z - var2.prevZ) * (double)f;
//        GL11.glRotatef(this.field_2329 + (this.field_2328 - this.field_2329) * f, 0.0F, 0.0F, 1.0F);
//        if (var2.isSleeping()) {
//            var3 = (float)((double)var3 + 1.0);
//            GL11.glTranslatef(0.0F, 0.3F, 0.0F);
//            if (!this.minecraft.options.field_1447) {
//                int var10 = this.minecraft.level.getTileId(MathHelper.floor(var2.x), MathHelper.floor(var2.y), MathHelper.floor(var2.z));
//                if (var10 == BlockBase.BED.id) {
//                    int var11 = this.minecraft.level.getTileMeta(MathHelper.floor(var2.x), MathHelper.floor(var2.y), MathHelper.floor(var2.z));
//                    int var12 = var11 & 3;
//                    GL11.glRotatef((float)(var12 * 90), 0.0F, 1.0F, 0.0F);
//                }
//
//                GL11.glRotatef(var2.prevYaw + (var2.yaw - var2.prevYaw) * f + 180.0F, 0.0F, -1.0F, 0.0F);
//                GL11.glRotatef(var2.prevPitch + (var2.pitch - var2.prevPitch) * f, -1.0F, 0.0F, 0.0F);
//            }
//        } else if (this.minecraft.options.thirdPerson) {
//            double var27 = (double)(this.field_2360 + (this.field_2359 - this.field_2360) * f);
//            float var13;
//            float var28;
//            if (this.minecraft.options.field_1447) {
//                var28 = this.field_2362 + (this.field_2361 - this.field_2362) * f;
//                var13 = this.field_2364 + (this.field_2363 - this.field_2364) * f;
//                GL11.glTranslatef(0.0F, 0.0F, (float)(-var27));
//                GL11.glRotatef(var13, 1.0F, 0.0F, 0.0F);
//                GL11.glRotatef(var28, 0.0F, 1.0F, 0.0F);
//            } else {
//                var28 = var2.yaw;
//                var13 = var2.pitch;
//                if (Keyboard.isKeyDown(Keyboard.KEY_F1)) {
//                    var13 += 180.0F;
//                    var27 += 2.0D;
//                }
//
//                double var14 = (double)(-MathHelper.sin(var28 / 180.0F * 3.1415927F) * MathHelper.cos(var13 / 180.0F * 3.1415927F)) * var27;
//                double var16 = (double)(MathHelper.cos(var28 / 180.0F * 3.1415927F) * MathHelper.cos(var13 / 180.0F * 3.1415927F)) * var27;
//                double var18 = (double)(-MathHelper.sin(var13 / 180.0F * 3.1415927F)) * var27;
//
//                for(int var20 = 0; var20 < 8; ++var20) {
//                    float var21 = (float)((var20 & 1) * 2 - 1);
//                    float var22 = (float)((var20 >> 1 & 1) * 2 - 1);
//                    float var23 = (float)((var20 >> 2 & 1) * 2 - 1);
//                    var21 *= 0.1F;
//                    var22 *= 0.1F;
//                    var23 *= 0.1F;
//                    //HitResult var24 = this.minecraft.level.method_160(Vec3f.from(var4 + (double)var21, var6 + (double)var22, var8 + (double)var23), Vec3f.from(var4 - var14 + (double)var21 + (double)var23, var6 - var18 + (double)var22, var8 - var16 + (double)var23));
//                    HitResult var24 = this.minecraft.level.method_162(Vec3f.from(var4 + (double)var21, var6 + (double)var22, var8 + (double)var23), Vec3f.from(var4 - var14 + (double)var21 + (double)var23, var6 - var18 + (double)var22, var8 - var16 + (double)var23), false, true);
//                    if (var24 != null) {
//                        double var25 = var24.field_1988.method_1294(Vec3f.from(var4, var6, var8));
//                        if (var25 < var27) {
//                            var27 = var25;
//                        }
//                    }
//                }
//
//                if (Keyboard.isKeyDown(Keyboard.KEY_F1)) {
//                    GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
//                }
//
//                GL11.glRotatef(var2.pitch - var13, 1.0F, 0.0F, 0.0F);
//                GL11.glRotatef(var2.yaw - var28, 0.0F, 1.0F, 0.0F);
//                GL11.glTranslatef(0.0F, 0.0F, (float)(-var27));
//                GL11.glRotatef(var28 - var2.yaw, 0.0F, 1.0F, 0.0F);
//                GL11.glRotatef(var13 - var2.pitch, 1.0F, 0.0F, 0.0F);
//            }
//        } else {
//            GL11.glTranslatef(0.0F, 0.0F, -0.1F);
//        }
//
//        if (!this.minecraft.options.field_1447) {
//            GL11.glRotatef(var2.prevPitch + (var2.pitch - var2.prevPitch) * f, 1.0F, 0.0F, 0.0F);
//            GL11.glRotatef(var2.prevYaw + (var2.yaw - var2.prevYaw) * f + 180.0F, 0.0F, 1.0F, 0.0F);
//        }
//
//        GL11.glTranslatef(0.0F, var3, 0.0F);
//        var4 = var2.prevX + (var2.x - var2.prevX) * (double)f;
//        var6 = var2.prevY + (var2.y - var2.prevY) * (double)f - (double)var3;
//        var8 = var2.prevZ + (var2.z - var2.prevZ) * (double)f;
//        this.field_2330 = this.minecraft.worldRenderer.method_1538(var4, var6, var8, f);
//        ci.cancel();
//    }

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

    @Redirect(
            method = "method_1842",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/level/dimension/Dimension;blocksCompassAndClock:Z",
                    opcode = Opcodes.GETFIELD
            )
    )
    private boolean betterScreenshots_method_1842(Dimension instance) {
        if (this.minecraft.level.dimension.blocksCompassAndClock) {
            if(this.minecraft.options.fancyGraphics) {
                GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
                GL11.glFogf(GL11.GL_FOG_DENSITY, 4.60517018598809F / this.field_2350 * 0.99F);
            } else {
                GL11.glFogf(GL11.GL_FOG_START, 0.0F);
            }
        } else if(this.minecraft.level.dimension.id == 1 && this.minecraft.options.fancyGraphics) {
            GL11.glFogf(2915, 0.0F);
        }

        return false;
    }
}
