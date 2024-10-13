package com.github.telvarost.betterscreenshots.mixin;

import com.github.telvarost.betterscreenshots.Config;
import com.github.telvarost.betterscreenshots.IsometricScreenshotRenderer;
import com.github.telvarost.betterscreenshots.KeyBindingListener;
import com.github.telvarost.betterscreenshots.ModHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.*;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.render.*;
import net.minecraft.client.render.texture.LavaSprite;
import net.minecraft.client.render.texture.WaterSprite;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.stat.PlayerStats;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.chunk.LegacyChunkCache;
import net.minecraft.world.storage.WorldStorageSource;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Environment(EnvType.CLIENT)
@Mixin(Minecraft.class)
public abstract class MinecraftMixin implements Runnable {

    @Shadow public ProgressRenderer progressRenderer;

    @Shadow public int displayWidth;

    @Shadow public int displayHeight;

    @Shadow private WorldStorageSource worldStorageSource;

    @Shadow public GameOptions options;

    @Shadow public TextureManager textureManager;

    @Shadow public PlayerStats stats;

    @Shadow public SoundManager soundManager;

    @Shadow private LavaSprite lavaSprite;

    @Shadow private WaterSprite waterSprite;

    @Shadow public WorldRenderer worldRenderer;

    @Shadow public ParticleManager particleManager;

    @Shadow public World world;

    @Shadow public InGameHud inGameHud;

    @Shadow public ClientPlayerEntity player;

    @Shadow public volatile boolean paused;

    @Shadow public InteractionManager interactionManager;

    @Shadow public abstract boolean isWorldRemote();

    @Shadow private boolean screenshotKeyDown;

    @Shadow private static File runDirectoryCache;

    @Shadow private int attackCooldown;

    @Shadow private long lastTickTime;

    @Shadow public LivingEntity camera;

    @Shadow protected abstract void prepareWorld(String string);

    @Redirect(
            method = "setScreen",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;clearChat()V"
            )
    )
    public void betterScreenShots_openScreen(InGameHud instance) {
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
            method = "handleScreenshotKey",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/Minecraft;screenshotKeyDown:Z",
                    opcode = Opcodes.PUTFIELD,
                    ordinal = 1
            )
    )
    private void checkTakingScreenshot(Minecraft instance, boolean value) {
        if (!Keyboard.isKeyDown(KeyBindingListener.takeCustomResolutionScreenshot.code)) {
            this.screenshotKeyDown = false;
        }
    }

    @Inject(method = "handleMouseDown", at = @At("HEAD"), cancellable = true)
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
                    target = "Lnet/minecraft/client/gui/hud/InGameHud;tick()V"
            )
    )
    public void betterScreenshots_tickOverlayRunTick(InGameHud instance) {
        if (!this.paused) {
            this.inGameHud.tick();
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
        if(Keyboard.isKeyDown(KeyBindingListener.takeCustomResolutionScreenshot.code)) {
            if(this.world != null) {
                this.screenshotKeyDown = true;
                this.inGameHud.addChatMessage(
                        ModHelper.mainSaveCustomResolutionPhotoScreenshot((Minecraft) (Object)this,
                                this.runDirectoryCache,
                                this.displayWidth,
                                this.displayHeight,
                                Config.config.customResolutionPhotoWidth,
                                Config.config.customResolutionPhotoHeight,
                                (System.getProperty("os.name").toLowerCase().contains("mac"))
                                        ? Keyboard.isKeyDown(Keyboard.KEY_LMETA) || Keyboard.isKeyDown(Keyboard.KEY_RMETA)
                                        : Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)
                        )
                );
            }
        } else {
            if (!Keyboard.isKeyDown(60)) {
                this.screenshotKeyDown = false;
            }
        }

        /** - Check for ISOMETRIC_PHOTO keybinding pressed */
        if(this.world != null && eventKey == KeyBindingListener.takeIsometricScreenshot.code) {
            this.progressRenderer.progressStart("Taking isometric screenshot");
            IsometricScreenshotRenderer isoRenderer = (new IsometricScreenshotRenderer((Minecraft) (Object)this, this.runDirectoryCache));
            isoRenderer.doRender();
        }

        return eventKey;
    }

    @Inject(method = "setWorld(Lnet/minecraft/world/World;Ljava/lang/String;Lnet/minecraft/entity/player/PlayerEntity;)V", at = @At("HEAD"), cancellable = true)
    public void betterScreenshots_showLevelProgress(World arg, String string, PlayerEntity arg2, CallbackInfo ci) {
        this.stats.method_1991();
        this.stats.save();
        this.camera = null;
        if(this.progressRenderer != null) { // Addition
            this.progressRenderer.progressStart(string);
            this.progressRenderer.progressStage("");
        } // Addition
        this.soundManager.playStreaming((String)null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        if (this.world != null) {
            this.world.savingProgress(this.progressRenderer);
        }

        this.world = arg;
        if (arg != null) {
            //this.interactionManager.method_1710(arg);
            if (!this.isWorldRemote()) {
                if (arg2 == null) {
                    this.player = (ClientPlayerEntity)arg.getPlayerForProxy(ClientPlayerEntity.class);
                }
            } else if (this.player != null) {
                this.player.teleportTop();
//                if (arg != null) {
//                    arg.spawnEntity(this.player);
//                }
                arg.spawnEntity(this.player); // Addition
            }

            if (!arg.isRemote) {
                this.prepareWorld(string);
            }

            this.interactionManager.setWorld(arg);
            if (this.player == null) {
                this.player = (ClientPlayerEntity)this.interactionManager.createPlayer(arg);
                this.player.teleportTop();
                this.interactionManager.preparePlayer(this.player);
            }

            this.player.input = new KeyboardInput(this.options);
            if (this.worldRenderer != null) {
                this.worldRenderer.setWorld(arg);
            }

            if (this.particleManager != null) {
                this.particleManager.setWorld(arg);
            }

            //this.interactionManager.method_1718(this.player);
            this.waterSprite.copyTo = this.textureManager.getTextureId("/misc/water.png"); // Addition
            this.lavaSprite.copyTo = 0; // Addition
            if (arg2 != null) {
                arg.saveWorldData();
            }

            ChunkSource var4 = arg.getChunkSource();
            if (var4 instanceof LegacyChunkCache) {
                LegacyChunkCache var5 = (LegacyChunkCache)var4;
                int var6 = MathHelper.floor((float)((int)this.player.x)) >> 4;
                int var7 = MathHelper.floor((float)((int)this.player.z)) >> 4;
                var5.setSpawnPoint(var6, var7);
            }

            arg.addPlayer(this.player);
            this.interactionManager.preparePlayerRespawn(this.player); // Addition
            if (arg.newWorld) {
                arg.savingProgress(this.progressRenderer);
            }

            this.camera = this.player;
        } else {
            this.worldStorageSource.flush(); // Addition
            this.player = null;
        }

        System.gc();
        this.lastTickTime = 0L;
        ci.cancel();
    }
}
