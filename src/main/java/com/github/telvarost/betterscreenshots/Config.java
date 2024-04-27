package com.github.telvarost.betterscreenshots;

import blue.endless.jankson.Comment;
import net.glasslauncher.mods.api.gcapi.api.ConfigName;
import net.glasslauncher.mods.api.gcapi.api.GConfig;
import net.glasslauncher.mods.api.gcapi.api.MaxLength;

public class Config {

    @GConfig(value = "config", visibleName = "BetterScreenshots")
    public static ConfigFields config = new ConfigFields();

    public static class ConfigFields {
        @ConfigName("Add Basic Screenshots To Clipboard")
        public Boolean addBasicScreenshotsToClipboard = true;

        @ConfigName("Custom Screenshot Height In Pixels")
        @MaxLength(36863)
        @Comment("Default Value: 2240")
        public Integer customResolutionPhotoHeight = 2240;

        @ConfigName("Custom Screenshot Width In Pixels")
        @MaxLength(36863)
        @Comment("Default Value: 7680")
        public Integer customResolutionPhotoWidth = 7680;

        @ConfigName("Disable Rendering Nether Bedrock")
        @Comment("Reload world for changes to take effect")
        public Boolean disableRenderingNetherBedrock = false;

        @ConfigName("Isometric Screenshot Resolution")
        @MaxLength(255)
        @Comment("Default Value: 8")
        public Integer isometricPhotoScale = 8;

        @ConfigName("Isometric Screenshot Rotation")
        public IsometricAngleEnum isometricPhotoRotation = IsometricAngleEnum.ANGLE_0_DEGREES;

        @ConfigName("Isometric Screenshot Rotation Offset")
        @MaxLength(90)
        @Comment("Zero degrees recommended for best angle")
        public Integer isometricPhotoRotationOffset = 0;

        @ConfigName("Mirror Isometric Screenshot")
        @Comment("Flips the isometric screenshot horizontally")
        public Boolean mirrorIsometricScreenshot = false;
    }
}