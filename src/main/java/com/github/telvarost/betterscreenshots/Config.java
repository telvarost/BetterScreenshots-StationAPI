package com.github.telvarost.betterscreenshots;

import blue.endless.jankson.Comment;
import net.glasslauncher.mods.api.gcapi.api.ConfigName;
import net.glasslauncher.mods.api.gcapi.api.GConfig;
import net.glasslauncher.mods.api.gcapi.api.MaxLength;

public class Config {

    @GConfig(value = "config", visibleName = "BetterScreenshots")
    public static ConfigFields config = new ConfigFields();

    public static class ConfigFields {
        @ConfigName("Custom Screenshot Height In Pixels")
        @MaxLength(36863)
        @Comment("Default Value: 2240")
        public static Integer customResolutionPhotoHeight = 2240;

        @ConfigName("Custom Screenshot Width In Pixels")
        @MaxLength(36863)
        @Comment("Default Value: 7680")
        public static Integer customResolutionPhotoWidth = 7680;

        @ConfigName("Disable Rendering Nether Bedrock")
        @Comment("Reload world for changes to take effect")
        public static Boolean disableRenderingNetherBedrock = false;

        @ConfigName("Isometric Screenshot Resolution")
        @MaxLength(255)
        @Comment("Default Value: 8")
        public static Integer isometricPhotoScale = 8;

        @ConfigName("Isometric Screenshot Rotation")
        public static IsometricAngleEnum isometricPhotoRotation = IsometricAngleEnum.ANGLE_0_DEGREES;

        @ConfigName("Isometric Screenshot Rotation Offset")
        @MaxLength(90)
        @Comment("Zero degrees recommended for best angle")
        public static Integer isometricPhotoRotationOffset = 0;
    }
}