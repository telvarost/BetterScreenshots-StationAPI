package com.github.telvarost.hudtweaks.mixin;

import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.class_556;
import net.minecraft.class_573;
import net.minecraft.class_598;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.render.RenderHelper;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.level.chunk.ChunkCache;
import net.minecraft.level.source.LevelSource;
import net.minecraft.sortme.GameRenderer;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.maths.MathHelper;
import net.minecraft.util.maths.Vec3f;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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

    @Inject(method = "method_1848", at = @At("HEAD"), cancellable = true)
    private void betterScreenshots_method_1848(float f, CallbackInfoReturnable<Float> cir) {
        Living var2 = this.minecraft.viewEntity;
        float var3 = 70.0F;
//        if (var2.isInFluid(Material.WATER)) {
//            var3 = 60.0F;
//        }

        if (var2.health <= 0) {
            float var4 = (float)var2.deathTime + f;
            var3 /= (1.0F - 500.0F / (var4 + 500.0F)) * 2.0F + 1.0F;
        }

        if (var2.isInFluid(Material.WATER)) {
            var3 *= 0.857143F;
        }

        cir.setReturnValue(var3 + this.field_2327 + (this.field_2365 - this.field_2327) * f);
    }


    @Inject(method = "method_1851", at = @At("HEAD"), cancellable = true)
    private void betterScreenshots_method_1851(float f, CallbackInfo ci) {
        Living var2 = this.minecraft.viewEntity;
        float var3 = var2.standingEyeHeight - 1.62F;
        double var4 = var2.prevX + (var2.x - var2.prevX) * (double)f;
        double var6 = var2.prevY + (var2.y - var2.prevY) * (double)f - (double)var3;
        double var8 = var2.prevZ + (var2.z - var2.prevZ) * (double)f;
        GL11.glRotatef(this.field_2329 + (this.field_2328 - this.field_2329) * f, 0.0F, 0.0F, 1.0F);
        if (var2.isSleeping()) {
            var3 = (float)((double)var3 + 1.0);
            GL11.glTranslatef(0.0F, 0.3F, 0.0F);
            if (!this.minecraft.options.field_1447) {
                int var10 = this.minecraft.level.getTileId(MathHelper.floor(var2.x), MathHelper.floor(var2.y), MathHelper.floor(var2.z));
                if (var10 == BlockBase.BED.id) {
                    int var11 = this.minecraft.level.getTileMeta(MathHelper.floor(var2.x), MathHelper.floor(var2.y), MathHelper.floor(var2.z));
                    int var12 = var11 & 3;
                    GL11.glRotatef((float)(var12 * 90), 0.0F, 1.0F, 0.0F);
                }

                GL11.glRotatef(var2.prevYaw + (var2.yaw - var2.prevYaw) * f + 180.0F, 0.0F, -1.0F, 0.0F);
                GL11.glRotatef(var2.prevPitch + (var2.pitch - var2.prevPitch) * f, -1.0F, 0.0F, 0.0F);
            }
        } else if (this.minecraft.options.thirdPerson) {
            double var27 = (double)(this.field_2360 + (this.field_2359 - this.field_2360) * f);
            float var13;
            float var28;
            if (this.minecraft.options.field_1447) {
                var28 = this.field_2362 + (this.field_2361 - this.field_2362) * f;
                var13 = this.field_2364 + (this.field_2363 - this.field_2364) * f;
                GL11.glTranslatef(0.0F, 0.0F, (float)(-var27));
                GL11.glRotatef(var13, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(var28, 0.0F, 1.0F, 0.0F);
            } else {
                var28 = var2.yaw;
                var13 = var2.pitch;
                if (Keyboard.isKeyDown(Keyboard.KEY_F1)) {
                    var13 += 180.0F;
                    var27 += 2.0D;
                }

                double var14 = (double)(-MathHelper.sin(var28 / 180.0F * 3.1415927F) * MathHelper.cos(var13 / 180.0F * 3.1415927F)) * var27;
                double var16 = (double)(MathHelper.cos(var28 / 180.0F * 3.1415927F) * MathHelper.cos(var13 / 180.0F * 3.1415927F)) * var27;
                double var18 = (double)(-MathHelper.sin(var13 / 180.0F * 3.1415927F)) * var27;

                for(int var20 = 0; var20 < 8; ++var20) {
                    float var21 = (float)((var20 & 1) * 2 - 1);
                    float var22 = (float)((var20 >> 1 & 1) * 2 - 1);
                    float var23 = (float)((var20 >> 2 & 1) * 2 - 1);
                    var21 *= 0.1F;
                    var22 *= 0.1F;
                    var23 *= 0.1F;
                    //HitResult var24 = this.minecraft.level.method_160(Vec3f.from(var4 + (double)var21, var6 + (double)var22, var8 + (double)var23), Vec3f.from(var4 - var14 + (double)var21 + (double)var23, var6 - var18 + (double)var22, var8 - var16 + (double)var23));
                    HitResult var24 = this.minecraft.level.method_162(Vec3f.from(var4 + (double)var21, var6 + (double)var22, var8 + (double)var23), Vec3f.from(var4 - var14 + (double)var21 + (double)var23, var6 - var18 + (double)var22, var8 - var16 + (double)var23), false, true);
                    if (var24 != null) {
                        double var25 = var24.field_1988.method_1294(Vec3f.from(var4, var6, var8));
                        if (var25 < var27) {
                            var27 = var25;
                        }
                    }
                }

                if (Keyboard.isKeyDown(Keyboard.KEY_F1)) {
                    GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
                }

                GL11.glRotatef(var2.pitch - var13, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(var2.yaw - var28, 0.0F, 1.0F, 0.0F);
                GL11.glTranslatef(0.0F, 0.0F, (float)(-var27));
                GL11.glRotatef(var28 - var2.yaw, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(var13 - var2.pitch, 1.0F, 0.0F, 0.0F);
            }
        } else {
            GL11.glTranslatef(0.0F, 0.0F, -0.1F);
        }

        if (!this.minecraft.options.field_1447) {
            GL11.glRotatef(var2.prevPitch + (var2.pitch - var2.prevPitch) * f, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(var2.prevYaw + (var2.yaw - var2.prevYaw) * f + 180.0F, 0.0F, 1.0F, 0.0F);
        }

        GL11.glTranslatef(0.0F, var3, 0.0F);
        var4 = var2.prevX + (var2.x - var2.prevX) * (double)f;
        var6 = var2.prevY + (var2.y - var2.prevY) * (double)f - (double)var3;
        var8 = var2.prevZ + (var2.z - var2.prevZ) * (double)f;
        this.field_2330 = this.minecraft.worldRenderer.method_1538(var4, var6, var8, f);
        ci.cancel();
    }

    // Probably not needed the changes seem to be no different from vanilla
    @Inject(method = "method_1840", at = @At("HEAD"), cancellable = true)
    private void betterScreenshots_method_1840(float f, int i, CallbackInfo ci) {
        this.field_2350 = (float)(256 >> this.minecraft.options.viewDistance);
        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        float var3 = 0.07F;
        if (this.minecraft.options.anaglyph3d) {
            GL11.glTranslatef((float)(-(i * 2 - 1)) * var3, 0.0F, 0.0F);
        }

        if (this.field_2331 != 1.0) {
            GL11.glTranslatef((float)this.field_2332, (float)(-this.field_2333), 0.0F);
            GL11.glScaled(this.field_2331, this.field_2331, 1.0);
//            GLU.gluPerspective(this.method_1848(f), (float)this.minecraft.actualWidth / (float)this.minecraft.actualHeight, 0.05F, this.field_2350 * 2.0F);
//        } else {
//            GLU.gluPerspective(this.method_1848(f), (float)this.minecraft.actualWidth / (float)this.minecraft.actualHeight, 0.05F, this.field_2350 * 2.0F);
        }

        GLU.gluPerspective(this.method_1848(f), (float)this.minecraft.actualWidth / (float)this.minecraft.actualHeight, 0.05F, this.field_2350 * 2.0F);
        GL11.glMatrixMode(5888);
        GL11.glLoadIdentity();
        if (this.minecraft.options.anaglyph3d) {
            GL11.glTranslatef((float)(i * 2 - 1) * 0.1F, 0.0F, 0.0F);
        }

        this.method_1849(f);
        if (this.minecraft.options.bobView) {
            this.method_1850(f);
        }

        float var4 = this.minecraft.player.field_505 + (this.minecraft.player.field_504 - this.minecraft.player.field_505) * f;
        if (var4 > 0.0F) {
            float var5 = 5.0F / (var4 * var4 + 5.0F) - var4 * 0.04F;
            var5 *= var5;
            GL11.glRotatef(((float)this.field_2351 + f) * 20.0F, 0.0F, 1.0F, 1.0F);
            GL11.glScalef(1.0F / var5, 1.0F, 1.0F);
            GL11.glRotatef(-((float)this.field_2351 + f) * 20.0F, 0.0F, 1.0F, 1.0F);
        }

        this.method_1851(f);
        ci.cancel();
    }


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
        GL11.glLoadIdentity();
        if (this.minecraft.options.anaglyph3d) {
            GL11.glTranslatef((float)(i * 2 - 1) * 0.1F, 0.0F, 0.0F);
        }

        GL11.glPushMatrix();
        this.method_1849(f);
        if (this.minecraft.options.bobView) {
            this.method_1850(f);
        }

        if (!this.minecraft.options.thirdPerson && !this.minecraft.viewEntity.isSleeping() && !this.minecraft.options.hideHud) {
            this.field_2342.method_1860(f);
        }

        GL11.glPopMatrix();
        if (!this.minecraft.options.thirdPerson && !this.minecraft.viewEntity.isSleeping()) {
            this.field_2342.method_1864(f);
            this.method_1849(f);
        }

        if (this.minecraft.options.bobView) {
            this.method_1850(f);
        }
        ci.cancel();
    }

    @Inject(method = "delta", at = @At("HEAD"), cancellable = true)
    public void delta(float f, long l, CallbackInfo ci) {
        GL11.glEnable(2884);
        GL11.glEnable(2929);
        if (this.minecraft.viewEntity == null) {
            this.minecraft.viewEntity = this.minecraft.player;
        }

        this.method_1838(f);
        Living var4 = this.minecraft.viewEntity;
        WorldRenderer var5 = this.minecraft.worldRenderer;
        ParticleManager var6 = this.minecraft.particleManager;
        double var7 = var4.prevRenderX + (var4.x - var4.prevRenderX) * (double)f;
        double var9 = var4.prevRenderY + (var4.y - var4.prevRenderY) * (double)f;
        double var11 = var4.prevRenderZ + (var4.z - var4.prevRenderZ) * (double)f;
        LevelSource var13 = this.minecraft.level.getCache();
        int var16;
        if (var13 instanceof ChunkCache) {
            ChunkCache var14 = (ChunkCache)var13;
            int var15 = MathHelper.floor((float)((int)var7)) >> 4;
            var16 = MathHelper.floor((float)((int)var11)) >> 4;
            var14.method_1242(var15, var16);
        }

        for(int var18 = 0; var18 < 2; ++var18) {
            if (this.minecraft.options.anaglyph3d) {
                field_2341 = var18;
                if (field_2341 == 0) {
                    GL11.glColorMask(false, true, true, false);
                } else {
                    GL11.glColorMask(true, false, false, false);
                }
            }

            GL11.glViewport(0, 0, this.minecraft.actualWidth, this.minecraft.actualHeight);
            this.method_1852(f);
            GL11.glClear(16640);
            GL11.glEnable(2884);
            this.method_1840(f, var18);
            class_598.method_1973();
            if (this.minecraft.options.viewDistance < 2) {
                this.method_1842(-1, f);
                var5.renderSky(f);
            }

            GL11.glEnable(2912);
            this.method_1842(1, f);
            if (this.minecraft.options.ao) {
                GL11.glShadeModel(7425);
            }

            class_573 var19 = new class_573();
            var19.method_2006(var7, var9, var11);
            this.minecraft.worldRenderer.method_1550(var19, f);
            if (var18 == 0) {
                while(!this.minecraft.worldRenderer.method_1549(var4, false) && l != 0L) {
                    long var20 = l - System.nanoTime();
                    if (var20 < 0L || var20 > 1000000000L) {
                        break;
                    }
                }
            }

            this.method_1842(0, f);
            GL11.glEnable(2912);
            GL11.glBindTexture(3553, this.minecraft.textureManager.getTextureId("/terrain.png"));
            RenderHelper.disableLighting();
            var5.method_1548(var4, 0, (double)f);
            GL11.glShadeModel(7424);
            RenderHelper.enableLighting();
            var5.method_1544(var4.method_931(f), var19, f);
            var6.method_327(var4, f);
            RenderHelper.disableLighting();
            this.method_1842(0, f);
            var6.method_324(var4, f);
            PlayerBase var21;
            if (this.minecraft.hitResult != null && var4.isInFluid(Material.WATER) && var4 instanceof PlayerBase) {
                var21 = (PlayerBase)var4;
                GL11.glDisable(3008);
                var5.method_1547(var21, this.minecraft.hitResult, 0, var21.inventory.getHeldItem(), f);
                if (!this.minecraft.options.hideHud) {
                    var5.method_1554(var21, this.minecraft.hitResult, 0, var21.inventory.getHeldItem(), f);
                }
                GL11.glEnable(3008);
            }

            GL11.glBlendFunc(770, 771);
            this.method_1842(0, f);
            GL11.glEnable(3042);
            GL11.glDisable(2884);
            GL11.glBindTexture(3553, this.minecraft.textureManager.getTextureId("/terrain.png"));
            if (this.minecraft.options.fancyGraphics) {
                if (this.minecraft.options.ao) {
                    GL11.glShadeModel(7425);
                }

                GL11.glColorMask(false, false, false, false);
                var16 = var5.method_1548(var4, 1, (double)f);
                if (this.minecraft.options.anaglyph3d) {
                    if (field_2341 == 0) {
                        GL11.glColorMask(false, true, true, true);
                    } else {
                        GL11.glColorMask(true, false, false, true);
                    }
                } else {
                    GL11.glColorMask(true, true, true, true);
                }

                if (var16 > 0) {
                    var5.method_1540(1, (double)f);
                }

                GL11.glShadeModel(7424);
            } else {
                var5.method_1548(var4, 1, (double)f);
            }

            GL11.glDepthMask(true);
            GL11.glEnable(2884);
            GL11.glDisable(3042);
            if (this.field_2331 == 1.0 && var4 instanceof PlayerBase && this.minecraft.hitResult != null && !var4.isInFluid(Material.WATER)) {
                var21 = (PlayerBase)var4;
                GL11.glDisable(3008);
                var5.method_1547(var21, this.minecraft.hitResult, 0, var21.inventory.getHeldItem(), f);
                if (!this.minecraft.options.hideHud) {
                    var5.method_1554(var21, this.minecraft.hitResult, 0, var21.inventory.getHeldItem(), f);
                }
                GL11.glEnable(3008);
            }

            this.method_1847(f);
            GL11.glDisable(2912);
            if (this.field_2352 != null) {
            }

            this.method_1842(0, f);
            GL11.glEnable(2912);
            var5.method_1552(f);
            GL11.glDisable(2912);
            this.method_1842(1, f);
            if (this.field_2331 == 1.0) {
                GL11.glClear(256);
                this.method_1845(f, var18);
            }

            if (!this.minecraft.options.anaglyph3d) {
                return;
            }
        }

        GL11.glColorMask(true, true, true, false);
        ci.cancel();
    }

    @Inject(method = "method_1842", at = @At("HEAD"), cancellable = true)
    private void method_1842(int i, float f, CallbackInfo ci) {
        Living var3 = this.minecraft.viewEntity;
        GL11.glFog(2918, this.method_1839(this.field_2346, this.field_2347, this.field_2348, 1.0F));
        GL11.glNormal3f(0.0F, -1.0F, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float var4;
        float var5;
        float var6;
        float var7;
        float var8;
        float var9;
        if (this.field_2330) {
            GL11.glFogi(2917, 2048);
            GL11.glFogf(2914, 0.1F);
            var4 = 1.0F;
            var5 = 1.0F;
            var6 = 1.0F;
            if (this.minecraft.options.anaglyph3d) {
                var7 = (var4 * 30.0F + var5 * 59.0F + var6 * 11.0F) / 100.0F;
                var8 = (var4 * 30.0F + var5 * 70.0F) / 100.0F;
                var9 = (var4 * 30.0F + var6 * 70.0F) / 100.0F;
            }
        } else if (var3.isInFluid(Material.WATER)) {
            GL11.glFogi(2917, 2048);
            GL11.glFogf(2914, 0.1F);
            var4 = 0.4F;
            var5 = 0.4F;
            var6 = 0.9F;
            if (this.minecraft.options.anaglyph3d) {
                var7 = (var4 * 30.0F + var5 * 59.0F + var6 * 11.0F) / 100.0F;
                var8 = (var4 * 30.0F + var5 * 70.0F) / 100.0F;
                var9 = (var4 * 30.0F + var6 * 70.0F) / 100.0F;
            }
        } else if (var3.isInFluid(Material.LAVA)) {
            GL11.glFogi(2917, 2048);
            GL11.glFogf(2914, 2.0F);
            var4 = 0.4F;
            var5 = 0.3F;
            var6 = 0.3F;
            if (this.minecraft.options.anaglyph3d) {
                var7 = (var4 * 30.0F + var5 * 59.0F + var6 * 11.0F) / 100.0F;
                var8 = (var4 * 30.0F + var5 * 70.0F) / 100.0F;
                var9 = (var4 * 30.0F + var6 * 70.0F) / 100.0F;
            }
        } else {
            GL11.glFogi(2917, 9729);
            GL11.glFogf(2915, this.field_2350 * 0.25F);
            GL11.glFogf(2916, this.field_2350);
            if (i < 0) {
                GL11.glFogf(2915, 0.0F);
                GL11.glFogf(2916, this.field_2350 * 0.8F);
            }

            if (GLContext.getCapabilities().GL_NV_fog_distance) {
                GL11.glFogi(34138, 34139);
            }

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
        }

        GL11.glEnable(2903);
        GL11.glColorMaterial(1028, 4608);
        ci.cancel();
    }
}
