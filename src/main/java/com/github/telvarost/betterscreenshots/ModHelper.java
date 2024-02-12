package com.github.telvarost.betterscreenshots;

import com.github.telvarost.betterscreenshots.mixin.GameRendererInvoker;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.achievement.IAchievementDescriptionFormat;
import net.minecraft.client.Minecraft;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ModHelper {

    @Environment(EnvType.CLIENT)
    public static class betterScreenShots_class_637 implements IAchievementDescriptionFormat {
        Minecraft instance;

        public betterScreenShots_class_637(Minecraft minecraft) {
            instance = minecraft;
        }

        public String format(String string) {
            return String.format(string, Keyboard.getKeyName(instance.options.inventoryKey.key));
        }
    }

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
    private int hugeLineHeight;
    private DataOutputStream hugeStream;
    private byte[] hugeData;
    private int[] hugeImageData;
    private File hugeFile;
    private BufferedImage hugeImage;
    private boolean useTarga;

    public ModHelper(File file1, int i2, int i3, int i4, boolean z5) throws IOException {
        this(file1, (String)null, i2, i3, i4, z5);
    }

    public ModHelper(File file1, String string10, int i2, int i3, int i4, boolean z5) throws IOException {
        if(!z5) {
            this.hugeImage = new BufferedImage(i2, i3, 1);
        }

        this.useTarga = z5;
        ModHelperFields.hugeWidth = i2;
        ModHelperFields.hugeHeight = i3;
        this.hugeLineHeight = i4;
        File file6 = new File(file1, "screenshots");
        file6.mkdir();
        String string7 = "huge_" + dateFormat.format(new Date());
        if(string10 == null) {
            for(int i8 = 1; (this.hugeFile = new File(file6, string7 + (i8 == 1 ? "" : "_" + i8) + (z5 ? ".tga" : ".png"))).exists(); ++i8) {
            }
        } else {
            this.hugeFile = new File(file6, string10);
        }

        this.hugeData = new byte[i2 * i4 * 3];
        this.hugeStream = new DataOutputStream(new FileOutputStream(this.hugeFile));
        if(z5) {
            byte[] b9 = new byte[18];
            b9[2] = 2;
            b9[12] = (byte)(i2 % 256);
            b9[13] = (byte)(i2 / 256);
            b9[14] = (byte)(i3 % 256);
            b9[15] = (byte)(i3 / 256);
            b9[16] = 24;
            this.hugeStream.write(b9);
        } else {
            this.hugeImageData = new int[i2 * i3];
        }

    }

    public void saveHugePart(ByteBuffer byteBuffer1, int i2, int i3, int i4, int i5) throws IOException {
        int i6 = i4;
        int i7 = i5;
        if(i4 > ModHelperFields.hugeWidth - i2) {
            i6 = ModHelperFields.hugeWidth - i2;
        }

        if(i5 > ModHelperFields.hugeHeight - i3) {
            i7 = ModHelperFields.hugeHeight - i3;
        }

        this.hugeLineHeight = i7;
        for(int i8 = 0; i8 < i7; ++i8) {
            byteBuffer1.position((i5 - i7) * i4 * 3 + i8 * i4 * 3);
            int i9 = (i2 + i8 * ModHelperFields.hugeWidth) * 3;
            byteBuffer1.get(this.hugeData, i9, i6 * 3);
        }

    }

    public void saveHugeLine(int i1) throws IOException {
        if(this.useTarga) {
            this.hugeStream.write(this.hugeData, 0, ModHelperFields.hugeWidth * 3 * this.hugeLineHeight);
        } else {
            for(int i2 = 0; i2 < ModHelperFields.hugeWidth; ++i2) {
                for(int i3 = 0; i3 < this.hugeLineHeight; ++i3) {
                    int i4 = i2 + (this.hugeLineHeight - i3 - 1) * ModHelperFields.hugeWidth;
                    int i5 = this.hugeData[i4 * 3 + 0] & 255;
                    int i6 = this.hugeData[i4 * 3 + 1] & 255;
                    int i7 = this.hugeData[i4 * 3 + 2] & 255;
                    int i8 = 0xFF000000 | i5 << 16 | i6 << 8 | i7;
                    this.hugeImageData[i2 + (i1 + i3) * ModHelperFields.hugeWidth] = i8;
                }
            }
        }

    }

    public String saveHugeScreenshot() throws IOException {
        if(!this.useTarga) {
            this.hugeImage.setRGB(0, 0, ModHelperFields.hugeWidth, ModHelperFields.hugeHeight, this.hugeImageData, 0, ModHelperFields.hugeWidth);
            ImageIO.write(this.hugeImage, "png", this.hugeStream);
        }

        this.hugeStream.close();
        return "Saved screenshot as " + this.hugeFile.getName();
    }

    public static String mainSaveHugeScreenshot(Minecraft instance, File file1, int i2, int i3, int i4, int i5, boolean z6) {
        try {
            ByteBuffer byteBuffer6 = BufferUtils.createByteBuffer(i2 * i3 * 3);
            ModHelper screenShotHelper7 = new ModHelper(file1, i4, i5, i3, z6);
            double d8 = (double)i4 / (double)i2;
            double d10 = (double)i5 / (double)i3;
            double d12 = d8 > d10 ? d8 : d10;

            for(int i14 = (i5 - 1) / i3 * i3; i14 >= 0; i14 -= i3) {
                for(int i15 = 0; i15 < i4; i15 += i2) {
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, instance.textureManager.getTextureId("/terrain.png"));
                    double d18 = (double)(i4 - i2) / 2.0D * 2.0D - (double)(i15 * 2);
                    double d20 = (double)(i5 - i3) / 2.0D * 2.0D - (double)(i14 * 2);
                    d18 /= (double)i2;
                    d20 /= (double)i3;
                    ((GameRendererInvoker) instance.gameRenderer).setCameraZoom(d12);
                    ((GameRendererInvoker) instance.gameRenderer).setCameraYaw(d18);
                    ((GameRendererInvoker) instance.gameRenderer).setCameraPitch(d20);
                    instance.gameRenderer.delta(1.0F, 0L);
                    ((GameRendererInvoker) instance.gameRenderer).setCameraZoom(1.0F);
                    ((GameRendererInvoker) instance.gameRenderer).setCameraYaw(0.0F);
                    ((GameRendererInvoker) instance.gameRenderer).setCameraPitch(0.0F);
                    Display.update();

                    try {
                        Thread.sleep(10L);
                    } catch (InterruptedException interruptedException23) {
                        interruptedException23.printStackTrace();
                    }

                    Display.update();
                    byteBuffer6.clear();
                    GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
                    GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
                    GL11.glReadPixels(0, 0, i2, i3, z6 ? GL12.GL_BGR : GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, byteBuffer6);
                    screenShotHelper7.saveHugePart(byteBuffer6, i15, i14, i2, i3);
                }

                screenShotHelper7.saveHugeLine(i14);
            }

            return screenShotHelper7.saveHugeScreenshot();
        } catch (OutOfMemoryError e) {
            return "Failed to save: " + "Out of memory";
        } catch (Exception exception24) {
            exception24.printStackTrace();
            return "Failed to save: " + exception24;
        }
    }

    public static class ModHelperFields {

        public static Integer isomScale = 16;

        public static Integer hugeWidth = 7680;

        public static Integer hugeHeight = 2240;

        public static Boolean isTakingIsometricScreenshot = false;
    }
}
