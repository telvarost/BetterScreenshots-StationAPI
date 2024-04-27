package com.github.telvarost.betterscreenshots.mixin;

import com.github.telvarost.betterscreenshots.Config;
import com.github.telvarost.betterscreenshots.IsometricScreenshotRenderer;
import com.github.telvarost.betterscreenshots.KeyBindingListener;
import com.github.telvarost.betterscreenshots.ModHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.*;
import net.minecraft.client.gui.InGame;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.render.*;
import net.minecraft.client.sound.SoundHelper;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.AbstractClientPlayer;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.level.Level;
import net.minecraft.level.chunk.ChunkCache;
import net.minecraft.level.source.LevelSource;
import net.minecraft.level.storage.LevelStorage;
import net.minecraft.util.ProgressListenerImpl;
import net.minecraft.util.io.StatsFileWriter;
import net.minecraft.util.maths.MathHelper;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Mixin(Minecraft.class)
@Environment(EnvType.CLIENT)
public abstract class MinecraftMixin implements Runnable {
    @Shadow
    public ProgressListenerImpl progressListener;

    @Shadow public int actualWidth;

    @Shadow public int actualHeight;

    @Shadow private LevelStorage levelStorage;

    @Shadow public GameOptions options;

    @Shadow public TextureManager textureManager;

    @Shadow public StatsFileWriter statFileWriter;

    @Shadow public SoundHelper soundHelper;

    @Shadow private FlowingLavaTextureBinder lavaTextureBinder;

    @Shadow private FlowingWaterTextureBinder2 waterTextureBinder;

    @Shadow public WorldRenderer worldRenderer;

    @Shadow public ParticleManager particleManager;

    @Shadow public Level level;

    @Shadow public InGame overlay;

    @Shadow public AbstractClientPlayer player;

    @Shadow public volatile boolean paused;

    @Shadow public BaseClientInteractionManager interactionManager;

    @Shadow public abstract boolean hasLevel();

    @Shadow private boolean isTakingScreenshot;

    @Shadow private static File gameDirectory;

    @Shadow private int attackCooldown;

    @Shadow private long lastTickTime;

    @Shadow public Living viewEntity;

    @Shadow protected abstract void method_2130(String string);

    @Redirect(
            method = "openScreen",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/InGame;clearChat()V"
            )
    )
    public void betterScreenShots_openScreen(InGame instance) {
        this.options.debugHud = false;
        instance.clearChat();
    }

//    @Redirect(
//            remap = false,
//            method = "run",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lorg/lwjgl/input/Keyboard;isKeyDown(I)Z",
//                    ordinal = 0
//            )
//    )
//    public boolean betterScreenshots_runIsKeyDownOne(int key) {
//        return !(this.level != null && !Keyboard.isKeyDown(Keyboard.KEY_F7));
//    }
//
//    @Redirect(
//            remap = false,
//            method = "run",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Ljava/lang/Thread;sleep(J)V"
//            )
//    )
//    public void betterScreenshots_runIsKeyDownTwo(long l) {
//        //Thread.sleep(10L);
//    }
//
//    @Redirect(
//            remap = false,
//            method = "run",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lorg/lwjgl/input/Keyboard;isKeyDown(I)Z",
//                    ordinal = 1
//            )
//    )
//    public boolean betterScreenshots_runIsKeyDownTwo(int key) {
//        return (this.level == null || Keyboard.isKeyDown(Keyboard.KEY_F7));
//    }

    @Redirect(
            method = "checkTakingScreenshot",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/Minecraft;isTakingScreenshot:Z",
                    opcode = Opcodes.PUTFIELD,
                    ordinal = 1
            )
    )
    private void checkTakingScreenshot(Minecraft instance, boolean value) {
        if (!Keyboard.isKeyDown(KeyBindingListener.takeCustomResolutionScreenshot.key)) {
            this.isTakingScreenshot = false;
        }
    }

    @Inject(method = "method_2110", at = @At("HEAD"), cancellable = true)
    private void betterScreenshots_method_2110(int i, boolean bl, CallbackInfo ci) {
        if (!bl) {
            this.attackCooldown = 0;
        }
    }

    @Redirect(
            method = "toggleFullscreen",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/opengl/Display;setFullscreen(Z)V"
            )
    )
    public void toggleFullscreen(boolean fullscreen) throws LWJGLException {
        Display.setFullscreen(fullscreen);
        Display.setVSyncEnabled(true);
    }

    @Redirect(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/InGame;runTick()V"
            )
    )
    public void betterScreenshots_tickOverlayRunTick(InGame instance) {
        if (!this.paused) {
            this.overlay.runTick();
        }
    }

    @Redirect(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/input/Keyboard;getEventKey()I",
                    ordinal = 7
            )
    )
    public int betterScreenshots_tickGetEventKey() {
        int eventKey = Keyboard.getEventKey();

        /** - Check for CUSTOM_RESOLUTION_PHOTO keybinding pressed */
        if(Keyboard.isKeyDown(KeyBindingListener.takeCustomResolutionScreenshot.key)) {
            if(this.level != null) {
                this.isTakingScreenshot = true;
                this.overlay.addChatMessage(ModHelper.mainSaveCustomResolutionPhotoScreenshot((Minecraft) (Object)this, this.gameDirectory, this.actualWidth, this.actualHeight, Config.config.customResolutionPhotoWidth, Config.config.customResolutionPhotoHeight, (System.getProperty("os.name").toLowerCase().contains("mac")) ? Keyboard.isKeyDown(Keyboard.KEY_LMETA) || Keyboard.isKeyDown(Keyboard.KEY_RMETA) : Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)));
            }
        } else {
            if (!Keyboard.isKeyDown(60)) {
                this.isTakingScreenshot = false;
            }
        }

        /** - Check for ISOMETRIC_PHOTO keybinding pressed */
        if(this.level != null && eventKey == KeyBindingListener.takeIsometricScreenshot.key) {
            this.progressListener.notifyWithGameRunning("Taking isometric screenshot");
            IsometricScreenshotRenderer isoRenderer = (new IsometricScreenshotRenderer((Minecraft) (Object)this, this.gameDirectory));
            isoRenderer.doRender();
        }

        return eventKey;
    }

    @Inject(method = "showLevelProgress", at = @At("HEAD"), cancellable = true)
    public void betterScreenshots_showLevelProgress(Level arg, String string, PlayerBase arg2, CallbackInfo ci) {
        this.statFileWriter.method_1991();
        this.statFileWriter.sync();
        this.viewEntity = null;
        if(this.progressListener != null) { // Addition
            this.progressListener.notifyWithGameRunning(string);
            this.progressListener.method_1796("");
        } // Addition
        this.soundHelper.playStreaming((String)null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        if (this.level != null) {
            this.level.saveLevel(this.progressListener);
        }

        this.level = arg;
        if (arg != null) {
            //this.interactionManager.method_1710(arg);
            if (!this.hasLevel()) {
                if (arg2 == null) {
                    this.player = (AbstractClientPlayer)arg.method_278(AbstractClientPlayer.class);
                }
            } else if (this.player != null) {
                this.player.afterSpawn();
//                if (arg != null) {
//                    arg.spawnEntity(this.player);
//                }
                arg.spawnEntity(this.player); // Addition
            }

            if (!arg.isServerSide) {
                this.method_2130(string);
            }

            this.interactionManager.method_1710(arg);
            if (this.player == null) {
                this.player = (AbstractClientPlayer)this.interactionManager.method_1717(arg);
                this.player.afterSpawn();
                this.interactionManager.rotatePlayer(this.player);
            }

            this.player.playerKeypressManager = new MovementManager(this.options);
            if (this.worldRenderer != null) {
                this.worldRenderer.method_1546(arg);
            }

            if (this.particleManager != null) {
                this.particleManager.method_323(arg);
            }

            //this.interactionManager.method_1718(this.player);
            this.waterTextureBinder.id = this.textureManager.getTextureId("/misc/water.png"); // Addition
            this.lavaTextureBinder.id = 0; // Addition
            if (arg2 != null) {
                arg.method_285();
            }

            LevelSource var4 = arg.getCache();
            if (var4 instanceof ChunkCache) {
                ChunkCache var5 = (ChunkCache)var4;
                int var6 = MathHelper.floor((float)((int)this.player.x)) >> 4;
                int var7 = MathHelper.floor((float)((int)this.player.z)) >> 4;
                var5.method_1242(var6, var7);
            }

            arg.addPlayer(this.player);
            this.interactionManager.method_1718(this.player); // Addition
            if (arg.field_215) {
                arg.saveLevel(this.progressListener);
            }

            this.viewEntity = this.player;
        } else {
            this.levelStorage.method_1003(); // Addition
            this.player = null;
        }

        System.gc();
        this.lastTickTime = 0L;
        ci.cancel();
    }
}
