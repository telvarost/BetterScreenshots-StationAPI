package com.github.telvarost.betterscreenshots;

import blue.endless.jankson.Comment;
import net.glasslauncher.mods.api.gcapi.api.ConfigName;
import net.glasslauncher.mods.api.gcapi.api.GConfig;
import net.glasslauncher.mods.api.gcapi.api.MaxLength;

public class Config {

    @GConfig(value = "config", visibleName = "AnnoyanceFix Config")
    public static ConfigFields config = new ConfigFields();

    public static class ConfigFields {
//        @ConfigName("Disable BetterScreenshots Mod")
//        public static Boolean disableBetterScreenshots = true;

        @ConfigName("Isometric Screenshot Resolution")
        @MaxLength(255)
        @Comment("Default Value: 16")
        public static Integer isometricPhotoScale = 16;

        @ConfigName("Isometric Screenshot Rotation")
        @MaxLength(3)
        @Comment("0=0deg, 1=90deg, 2=180deg, 3=270deg")
        public static Integer isometricPhotoRotation = 0;

        @ConfigName("Custom Image Width In Pixels")
        @MaxLength(36863)
        @Comment("Default Value: 7680")
        public static Integer customResolutionPhotoWidth = 7680;

        @ConfigName("Custom Image Height In Pixels")
        @MaxLength(36863)
        @Comment("Default Value: 2240")
        public static Integer customResolutionPhotoHeight = 2240;
    }
}