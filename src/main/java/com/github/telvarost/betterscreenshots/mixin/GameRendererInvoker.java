package com.github.telvarost.betterscreenshots.mixin;

import net.minecraft.sortme.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GameRenderer.class)
public interface GameRendererInvoker {
    @Accessor("field_2350")
    public void setupCameraTransform(float field_2350);

    @Accessor("field_2331")
    public void setCameraZoom(double field_2331);

    @Accessor("field_2332")
    public void setCameraYaw(double field_2332);

    @Accessor("field_2333")
    public void setCameraPitch(double field_2333);

//    @Invoker("method_1842")
//    public abstract void setupFog(int i, float f);

    @Invoker("method_1852")
    public abstract void updateFogColor(float f);

    @Invoker("method_1847")
    public abstract void renderRainSnow(float f);
}
