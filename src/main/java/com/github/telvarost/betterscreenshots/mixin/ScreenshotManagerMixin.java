package com.github.telvarost.betterscreenshots.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.ScreenshotManager;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.util.Date;

@Mixin(ScreenshotManager.class)
@Environment(EnvType.CLIENT)
public class ScreenshotManagerMixin {

    @Shadow private static ByteBuffer field_1006;

    @Shadow private static int[] field_1008;

    @Shadow private static byte[] field_1007;

    @Shadow private static DateFormat dateFormat;

//    @Inject(method = "takeScreenshot", at = @At("HEAD"), cancellable = true)
//    private static void takeScreenshot(File file, int i, int j, CallbackInfoReturnable<String> cir) {
//        try {
//            File var3 = new File(file, "screenshots");
//            var3.mkdir();
//            if (field_1006 == null || field_1006.capacity() < i * j) {
//                field_1006 = BufferUtils.createByteBuffer(i * j * 3);
//            }
//
//            if (field_1008 == null || field_1008.length < i * j * 3) {
//                field_1007 = new byte[i * j * 3];
//                field_1008 = new int[i * j];
//            }
//
//            GL11.glPixelStorei(3333, 1);
//            GL11.glPixelStorei(3317, 1);
//            field_1006.clear();
//            GL11.glReadPixels(0, 0, i, j, 6407, 5121, field_1006);
//            field_1006.clear();
//            String var4 = "" + dateFormat.format(new Date());
//
//            File var5;
//            for(int var6 = 1; (var5 = new File(var3, var4 + (var6 == 1 ? "" : "_" + var6) + ".png")).exists(); ++var6) {
//            }
//
//            field_1006.get(field_1007);
//
//            for(int var7 = 0; var7 < i; ++var7) {
//                for(int var8 = 0; var8 < j; ++var8) {
//                    int var9 = var7 + (j - var8 - 1) * i;
//                    int var10 = field_1007[var9 * 3 + 0] & 255;
//                    int var11 = field_1007[var9 * 3 + 1] & 255;
//                    int var12 = field_1007[var9 * 3 + 2] & 255;
//                    int var13 = -16777216 | var10 << 16 | var11 << 8 | var12;
//                    field_1008[var7 + var8 * i] = var13;
//                }
//            }
//
//            BufferedImage var15 = new BufferedImage(i, j, 1);
//            var15.setRGB(0, 0, i, j, field_1008, 0, i);
//            ImageIO.write(var15, "png", var5);
//            cir.setReturnValue("Saved screenshot as " + var5.getName());
//        } catch (OutOfMemoryError e) {
//            cir.setReturnValue("Failed to save: " + "Out of memory");
//        } catch (Exception var14) {
//            var14.printStackTrace();
//            cir.setReturnValue("Failed to save: " + var14);
//        }
//    }
}
