package com.github.telvarost.betterscreenshots.mixin;

import com.github.telvarost.betterscreenshots.Config;
import com.github.telvarost.betterscreenshots.TransferableImage;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.ScreenshotManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

@Environment(EnvType.CLIENT)
@Mixin(ScreenshotManager.class)
public class ScreenshotManagerMixin {

    @Redirect(
            method = "takeScreenshot",
            at = @At(
                    value = "INVOKE",
                    target = "Ljavax/imageio/ImageIO;write(Ljava/awt/image/RenderedImage;Ljava/lang/String;Ljava/io/File;)Z"
            )
    )
    private static boolean takeScreenshot(RenderedImage image, String formatName, File output) throws IOException {
        if (Config.config.addBasicScreenshotsToClipboard) {
            try {
                TransferableImage trans = new TransferableImage((Image)image);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(trans, (ClipboardOwner)null);
            } catch (Exception e) {
            }
        }

        return ImageIO.write(image, formatName, output);
    }
}
