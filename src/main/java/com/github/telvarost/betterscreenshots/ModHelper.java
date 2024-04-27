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

    public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
    private int customResolutionPhotoLineHeight;
    private DataOutputStream customResolutionPhotoStream;
    private byte[] customResolutionPhotoData;
    private int[] customResolutionPhotoImageData;
    private File customResolutionPhotoFile;
    private BufferedImage customResolutionPhotoImage;
    private boolean useTarga;

    public ModHelper(File file1, int i2, int i3, int i4, boolean z5) throws IOException {
        this(file1, (String)null, i2, i3, i4, z5);
    }

    public ModHelper(File file1, String string10, int i2, int i3, int i4, boolean z5) throws IOException {
        if(!z5) {
            this.customResolutionPhotoImage = new BufferedImage(i2, i3, 1);
        }

        this.useTarga = z5;
        Config.config.customResolutionPhotoWidth = i2;
        Config.config.customResolutionPhotoHeight = i3;
        this.customResolutionPhotoLineHeight = i4;
        File file6 = new File(file1, "screenshots");
        file6.mkdir();
        String string7 = "custom_" + dateFormat.format(new Date());
        if(string10 == null) {
            for(int i8 = 1; (this.customResolutionPhotoFile = new File(file6, string7 + (i8 == 1 ? "" : "_" + i8) + (z5 ? ".tga" : ".png"))).exists(); ++i8) {
            }
        } else {
            this.customResolutionPhotoFile = new File(file6, string10);
        }

        this.customResolutionPhotoData = new byte[i2 * i4 * 3];
        this.customResolutionPhotoStream = new DataOutputStream(new FileOutputStream(this.customResolutionPhotoFile));
        if(z5) {
            byte[] b9 = new byte[18];
            b9[2] = 2;
            b9[12] = (byte)(i2 % 256);
            b9[13] = (byte)(i2 / 256);
            b9[14] = (byte)(i3 % 256);
            b9[15] = (byte)(i3 / 256);
            b9[16] = 24;
            this.customResolutionPhotoStream.write(b9);
        } else {
            this.customResolutionPhotoImageData = new int[i2 * i3];
        }

    }

    public void saveCustomResolutionPhotoPart(ByteBuffer byteBuffer1, int i2, int i3, int i4, int i5) throws IOException {
        int i6 = i4;
        int i7 = i5;
        if(i4 > Config.config.customResolutionPhotoWidth - i2) {
            i6 = Config.config.customResolutionPhotoWidth - i2;
        }

        if(i5 > Config.config.customResolutionPhotoHeight - i3) {
            i7 = Config.config.customResolutionPhotoHeight - i3;
        }

        this.customResolutionPhotoLineHeight = i7;
        for(int i8 = 0; i8 < i7; ++i8) {
            byteBuffer1.position((i5 - i7) * i4 * 3 + i8 * i4 * 3);
            int i9 = (i2 + i8 * Config.config.customResolutionPhotoWidth) * 3;
            byteBuffer1.get(this.customResolutionPhotoData, i9, i6 * 3);
        }

    }

    public void saveCustomResolutionPhotoLine(int i1) throws IOException {
        if(this.useTarga) {
            this.customResolutionPhotoStream.write(this.customResolutionPhotoData, 0, Config.config.customResolutionPhotoWidth * 3 * this.customResolutionPhotoLineHeight);
        } else {
            for(int i2 = 0; i2 < Config.config.customResolutionPhotoWidth; ++i2) {
                for(int i3 = 0; i3 < this.customResolutionPhotoLineHeight; ++i3) {
                    int i4 = i2 + (this.customResolutionPhotoLineHeight - i3 - 1) * Config.config.customResolutionPhotoWidth;
                    int i5 = this.customResolutionPhotoData[i4 * 3 + 0] & 255;
                    int i6 = this.customResolutionPhotoData[i4 * 3 + 1] & 255;
                    int i7 = this.customResolutionPhotoData[i4 * 3 + 2] & 255;
                    int i8 = 0xFF000000 | i5 << 16 | i6 << 8 | i7;
                    this.customResolutionPhotoImageData[i2 + (i1 + i3) * Config.config.customResolutionPhotoWidth] = i8;
                }
            }
        }

    }

    public String saveCustomResolutionPhotoScreenshot() throws IOException {
        if(!this.useTarga) {
            this.customResolutionPhotoImage.setRGB(0, 0, Config.config.customResolutionPhotoWidth, Config.config.customResolutionPhotoHeight, this.customResolutionPhotoImageData, 0, Config.config.customResolutionPhotoWidth);
            ImageIO.write(this.customResolutionPhotoImage, "png", this.customResolutionPhotoStream);
        }

        this.customResolutionPhotoStream.close();
        return "Saved screenshot as " + this.customResolutionPhotoFile.getName();
    }

    public static String mainSaveCustomResolutionPhotoScreenshot(Minecraft instance, File file1, int i2, int i3, int i4, int i5, boolean z6) {
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
                    screenShotHelper7.saveCustomResolutionPhotoPart(byteBuffer6, i15, i14, i2, i3);
                }

                screenShotHelper7.saveCustomResolutionPhotoLine(i14);
            }

            return screenShotHelper7.saveCustomResolutionPhotoScreenshot();
        } catch (OutOfMemoryError e) {
            return "Failed to save: " + "Out of memory";
        } catch (Exception exception24) {
            exception24.printStackTrace();
            return "Failed to save: " + exception24;
        }
    }

    public static class ModHelperFields {
        public static Boolean isTakingIsometricScreenshot = false;
    }
}
