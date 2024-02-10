package com.github.telvarost.hudtweaks;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.util.Date;

public class ModHelper {
    private DateFormat dateFormat;
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


    public static class ModHelperFields {

        public static Integer isomScale = 16;

        public static Integer hugeWidth = 7680;

        public static Integer hugeHeight = 2240;

        public static Boolean isTakingIsometricScreenshot = false;
    }
}
