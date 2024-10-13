package com.github.telvarost.betterscreenshots.mixin;

import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GameRenderer.class)
public interface GameRendererInvoker {
    @Accessor("viewDistance")
    public void setupCameraTransform(float viewDistance);

    @Accessor("zoom")
    public void setCameraZoom(double zoom);

    @Accessor("zoomX")
    public void setCameraYaw(double zoomX);

    @Accessor("zoomY")
    public void setCameraPitch(double zoomY);

    @Invoker("applyFog")
    public abstract void setupFog(int i, float f);

    @Invoker("updateSkyAndFogColors")
    public abstract void updateFogColor(float f);

    @Invoker("renderSnow")
    public abstract void renderRainSnow(float f);
}
