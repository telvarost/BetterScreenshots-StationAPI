package com.github.telvarost.betterscreenshots;

import net.glasslauncher.mods.gcapi3.api.*;

public class Config {

    @ConfigRoot(value = "config", visibleName = "BetterScreenshots")
    public static ConfigFields config = new ConfigFields();

    public static class ConfigFields {
        @ConfigEntry(name = "Add Basic Screenshots To Clipboard")
        public Boolean addBasicScreenshotsToClipboard = true;

        @ConfigEntry(
                name = "Custom Screenshot Height In Pixels",
                description = "Default Value: 2240",
                maxLength = 36863
        )
        public Integer customResolutionPhotoHeight = 2240;

        @ConfigEntry(
                name = "Custom Screenshot Width In Pixels",
                description = "Default Value: 7680",
                maxLength = 36863
        )
        public Integer customResolutionPhotoWidth = 7680;

        @ConfigEntry(
                name = "Disable Rendering Nether Bedrock",
                description = "Reload world for changes to take effect"
        )
        public Boolean disableRenderingNetherBedrock = false;

        @ConfigEntry(
                name = "Isometric Screenshot Resolution",
                description = "Default Value: 8",
                maxLength = 255
        )
        public Integer isometricPhotoScale = 8;

        @ConfigEntry(name = "Isometric Screenshot Rotation")
        public IsometricAngleEnum isometricPhotoRotation = IsometricAngleEnum.ANGLE_0_DEGREES;

        @ConfigEntry(
                name = "Isometric Screenshot Rotation Offset",
                description = "Zero degrees recommended for best angle",
                maxLength = 90
        )
        public Integer isometricPhotoRotationOffset = 0;

        @ConfigEntry(
                name = "Mirror Isometric Screenshot",
                description = "Flips the isometric screenshot horizontally"
        )
        public Boolean mirrorIsometricScreenshot = false;
    }
}