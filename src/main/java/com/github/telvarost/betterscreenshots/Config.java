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
        public static Integer isomScale = 16;

        @ConfigName("Panorama Image Width In Pixels")
        @MaxLength(36863)
        @Comment("Default Value: 7680")
        public static Integer hugeWidth = 7680;

        @ConfigName("Panorama Image Height In Pixels")
        @MaxLength(36863)
        @Comment("Default Value: 2240")
        public static Integer hugeHeight = 2240;
    }
}