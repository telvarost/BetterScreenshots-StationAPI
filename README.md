# BetterScreenshots for Minecraft Beta 1.7.3

Special thanks to Birevan and Exalm for the original version of the mod!
* Mod now has Multiplayer support and is using GlassConfigAPI 2.0+
  * This is a client side mod and is not needed by the server

## List of Changes:

* Ability to change photo key bindings using controls menu in options.
* Ability to disable rendering bedrock in the nether (for nether isometric photos).
* ISOMETRIC_PHOTO_KEYBIND (default F7) — isometric screenshot (PNG)
  * Change the scale for isometric screenshots (default 16) with GlassConfigAPI.
  * Change the rotation angle for Isometric screenshots (default 0°) with GlassConfigAPI.
* CUSTOM_RESOLUTION_PHOTO_KEYBIND (default F6) — custom resolution screenshot (PNG)
  * Left/Right Ctrl/⌘ + CUSTOM_RESOLUTION_PHOTO_KEYBIND — custom resolution screenshot (TGA)
  * Change the resolution for custom screenshots (default 7680×2240) with GlassConfigAPI.

## Installation using Prism Launcher

1. Download an instance of Babric for Prism Launcher: https://github.com/babric/prism-instance
2. Install Java 17, set the instance to use it, and disable compatibility checks on the instance: https://adoptium.net/temurin/releases/
3. Add StationAPI to the mod folder for the instance: https://jenkins.glass-launcher.net/job/StationAPI/lastSuccessfulBuild/
4. (Optional) Add Mod Menu to the mod folder for the instance: https://github.com/calmilamsy/ModMenu/releases
5. (Optional) Add GlassConfigAPI 1.1.6+ to the mod folder for the instance: https://maven.glass-launcher.net/#/releases/net/glasslauncher/mods/GlassConfigAPI
6. Add this mod to the mod folder for the instance: https://github.com/telvarost/BetterScreenshots-StationAPI/releases
7. Run and enjoy! 👍

## Feedback

Got any suggestions on what should be added next? Feel free to share it by [creating an issue](https://github.com/telvarost/BetterScreenshots-StationAPI/issues/new). Know how to code and want to do it yourself? Then look below on how to get started.

## Contributing

Thanks for considering contributing! To get started fork this repository, make your changes, and create a PR. 

If you are new to StationAPI consider watching the following videos on Babric/StationAPI Minecraft modding: https://www.youtube.com/watch?v=9-sVGjnGJ5s&list=PLa2JWzyvH63wGcj5-i0P12VkJG7PDyo9T
