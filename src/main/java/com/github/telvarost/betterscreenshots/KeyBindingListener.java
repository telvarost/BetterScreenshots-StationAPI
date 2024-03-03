package com.github.telvarost.betterscreenshots;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.client.options.KeyBinding;
import net.modificationstation.stationapi.api.client.event.option.KeyBindingRegisterEvent;
import org.lwjgl.input.Keyboard;

public class KeyBindingListener {
    public static KeyBinding takeCustomResolutionScreenshot;
    public static KeyBinding takeIsometricScreenshot;

    @EventListener
    public void registerKeyBindings(KeyBindingRegisterEvent event) {
        event.keyBindings.add(takeCustomResolutionScreenshot = new KeyBinding("Custom Photo", Keyboard.KEY_F6));
        event.keyBindings.add(takeIsometricScreenshot = new KeyBinding("Isometric Photo", Keyboard.KEY_F7));
    }
}