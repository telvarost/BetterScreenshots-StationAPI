package com.github.telvarost.betterscreenshots;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;

import com.github.telvarost.betterscreenshots.mixin.GameRendererInvoker;
import com.github.telvarost.betterscreenshots.mixin.GameRendererMixin;
import net.minecraft.class_573;
import net.minecraft.client.Minecraft;

import net.minecraft.client.render.RenderHelper;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.level.Level;
import net.minecraft.util.ProgressListener;
import net.minecraft.util.maths.MathHelper;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class IsometricScreenshotRenderer {
    private ProgressListener progressUpdate;
    private Minecraft mc;
    private Level worldObj;
    private WorldRenderer renderGlobal;
    private DecimalFormat decimalFormat = new DecimalFormat("0000");
    private int width;
    private int length;
    private int height = 256;
    private float maxCloudHeight = 108.0F;
    private ByteBuffer byteBuffer;
    private FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(16);

    public IsometricScreenshotRenderer(Minecraft minecraft) {
        this.progressUpdate = minecraft.progressListener;
        this.mc = minecraft;
        this.worldObj = this.mc.level;
        this.renderGlobal = this.mc.worldRenderer;
        this.width = (64 << (3 - this.mc.options.viewDistance)) + 16;
        if(this.width > 416) {
            this.width = 416;
        }

        this.length = this.width;
    }

    private File getOutputFile() {
        File file = null;
        int scrNumber = 0;

        do {
            File home = new File(System.getProperty("user.home", "."));
            file = new File(home, "mc_map_" + this.decimalFormat.format(scrNumber++) + ".png");
        } while(file.exists());

        return file.getAbsoluteFile();
    }

    public void doRender() {
        final int scale = 16; /* <- Image Resolution */ //this.mc.options.guiScale;
        this.progressUpdate.method_1796("Grabbing large screenshot");
        File outputFile = this.getOutputFile();
        this.progressUpdate.notifyIgnoreGameRunning("Rendering");
        this.progressUpdate.progressStagePercentage(0);
        ModHelper.ModHelperFields.isTakingIsometricScreenshot = true;
        double posX = this.mc.viewEntity.prevRenderX;
        double posZ = this.mc.viewEntity.prevRenderY;
        System.out.println(posX + " " + posZ);
        posX -= (MathHelper.floor(posX) >> 4) * 16 + 8;
        posZ -= (MathHelper.floor(posZ) >> 4) * 16 + 8;
        if(posX < 0) {
            posX += 16;
            if(posX > 8) {
                posX -= 8;
            }
        }

        if(posZ < 0) {
            posZ += 16;
            if(posZ > 8) {
                posZ -= 8;
            }
        }

        System.out.println(posX + " " + posZ);

        try {
            int i1 = (this.width * scale) + (this.length * scale);
            int i3 = (this.height * scale) + i1 / 2;
            BufferedImage image = new BufferedImage(i1, i3, 1);
            Graphics graphics = image.getGraphics();
            int dWidth = this.mc.actualWidth;
            int dHeight = this.mc.actualHeight;
            int total = (i1 / dWidth + 1) * (i3 / dHeight + 1);
            int progress = 0;

            for(int i8 = 0; i8 < i1; i8 += dWidth) {
                for(int i9 = 0; i9 < i3; i9 += dHeight) {
                    this.progressUpdate.progressStagePercentage(++progress * 100 / total);
                    float f1 = 0.0F;
                    int i12 = i9 - i3 / 2;
                    int i10 = i8 - i1 / 2;
                    if(this.byteBuffer == null) {
                        this.byteBuffer = BufferUtils.createByteBuffer(dWidth * dHeight << 2);
                    }

                    GL11.glViewport(0, 0, dWidth, dHeight);
                    ((GameRendererInvoker) this.mc.gameRenderer).updateFogColor(0.0F);
                    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
                    GL11.glEnable(GL11.GL_CULL_FACE);
                    ((GameRendererInvoker) this.mc.gameRenderer).setupCameraTransform((float)(512 >> (this.mc.options.viewDistance << 1)));
                    GL11.glMatrixMode(GL11.GL_PROJECTION);
                    GL11.glLoadIdentity();
                    GL11.glOrtho(0.0D, (double)dWidth, 0.0D, (double)dHeight, 10.0D, 10000.0D);
                    GL11.glMatrixMode(GL11.GL_MODELVIEW);
                    GL11.glLoadIdentity();
                    GL11.glTranslatef((float)-i10, (float)-i12, -5000.0F);
                    GL11.glScalef((float)scale, (float)-scale, (float)-scale);
                    this.floatBuffer.clear();
                    this.floatBuffer.put(1.0F).put(-0.5F).put(0.0F).put(0.0F);
                    this.floatBuffer.put(0.0F).put(1.0F).put(-1.0F).put(0.0F);
                    this.floatBuffer.put(1.0F).put(0.5F).put(0.0F).put(0.0F);
                    this.floatBuffer.put(0.0F).put(0.0F).put(0.0F).put(1.0F);
                    this.floatBuffer.flip();
                    GL11.glMultMatrix(this.floatBuffer);
                    GL11.glRotatef(0.0F, 0.0F, 1.0F, 0.0F);
                    GL11.glTranslated(posX, 0, posZ);
                    GL11.glTranslated(-this.mc.viewEntity.prevRenderX, (double)-this.height / 2.0D, -this.mc.viewEntity.prevRenderZ);
                    class_573 frustrum = new FrustrumIsom();
                    this.renderGlobal.method_1550(frustrum, 0.0F);
                    GL11.glTranslated(this.mc.viewEntity.prevRenderX, this.mc.viewEntity.prevRenderY, this.mc.viewEntity.prevRenderZ);
                    this.renderGlobal.method_1549(this.mc.viewEntity, false);
                    ((GameRendererInvoker) this.mc.gameRenderer).setupFog(0, 0.0F);
                    GL11.glEnable(GL11.GL_FOG);
                    GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
                    float f3 = (float)this.height * 8.0F;
                    GL11.glFogf(GL11.GL_FOG_START, 5000.0F - f3);
                    GL11.glFogf(GL11.GL_FOG_END, 5000.0F + f3 * 8.0F);
                    RenderHelper.enableLighting();
                    this.renderGlobal.method_1544(this.mc.viewEntity.method_931(0.0F), frustrum, 0.0F);
                    ((GameRendererInvoker) this.mc.gameRenderer).renderRainSnow(0.0F);
                    RenderHelper.disableLighting();
                    this.renderGlobal.renderSky(0.0F);
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.textureManager.getTextureId("/terrain.png"));
                    if(this.mc.options.ao) {
                        GL11.glShadeModel(GL11.GL_SMOOTH);
                    }

                    this.renderGlobal.method_1548(this.mc.viewEntity, 0, 0.0F);
                    GL11.glShadeModel(GL11.GL_FLAT);
                    if(this.worldObj.dimension.getCloudHeight() < this.maxCloudHeight) {
                        GL11.glPushMatrix();
                        this.renderGlobal.renderClouds(0.0F);
                        GL11.glPopMatrix();
                    }

                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    GL11.glColorMask(false, false, false, false);
                    if(this.mc.options.ao) {
                        GL11.glShadeModel(GL11.GL_SMOOTH);
                    }

                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.mc.textureManager.getTextureId("/terrain.png"));
                    int i11 = this.renderGlobal.method_1548(this.mc.viewEntity, 1, 0.0F);
                    GL11.glShadeModel(GL11.GL_FLAT);
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    GL11.glColorMask(true, true, true, true);
                    if(i11 > 0) {
                        this.renderGlobal.method_1540(1, 0.0F);
                    }

                    GL11.glTranslated(-posX, 0.0D, -posZ);
                    GL11.glDepthMask(true);
                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glDisable(GL11.GL_FOG);
                    this.byteBuffer.clear();
                    GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
                    GL11.glReadPixels(0, 0, dWidth, dHeight, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, this.byteBuffer);
                    BufferedImage image2 = this.getImageFromByteBuffer(dWidth, dHeight);
                    graphics.drawImage(image2, i8, i9, (ImageObserver)null);
                }
            }

            graphics.dispose();
            this.progressUpdate.method_1796("Saving as " + outputFile.toString());
            this.progressUpdate.progressStagePercentage(100);
            FileOutputStream stream = new FileOutputStream(outputFile);
            ImageIO.write(image, "png", stream);
            stream.close();
        } catch (OutOfMemoryError e) {
            this.mc.overlay.addChatMessage("Out of memory. Reduce render distance and try again.");
        } catch (Throwable t) {
            t.printStackTrace();
        }

        ModHelper.ModHelperFields.isTakingIsometricScreenshot = false;
    }

    private BufferedImage getImageFromByteBuffer(int width, int height) {
        this.byteBuffer.position(0).limit(width * height << 2);
        BufferedImage image = new BufferedImage(width, height, 1);
        int[] arrayOfInt = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();

        for(int i = 0; i < width * height; ++i) {
            int r = this.byteBuffer.get(i * 3) & 255;
            int g = this.byteBuffer.get(i * 3 + 1) & 255;
            int b = this.byteBuffer.get(i * 3 + 2) & 255;
            arrayOfInt[i] = (r << 16 | g << 8 | b);
        }

        return image;
    }
}