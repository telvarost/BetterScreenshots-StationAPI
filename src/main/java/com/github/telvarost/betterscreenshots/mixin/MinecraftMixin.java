package com.github.telvarost.betterscreenshots.mixin;

import com.github.telvarost.betterscreenshots.IsometricScreenshotRenderer;
import com.github.telvarost.betterscreenshots.ModHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.achievement.Achievements;
import net.minecraft.achievement.IAchievementDescriptionFormat;
import net.minecraft.class_214;
import net.minecraft.class_556;
import net.minecraft.class_596;
import net.minecraft.class_66;
import net.minecraft.client.*;
import net.minecraft.client.gui.Achievement;
import net.minecraft.client.gui.InGame;
import net.minecraft.client.gui.Unused;
import net.minecraft.client.gui.screen.Connecting;
import net.minecraft.client.gui.screen.LevelSaveConflict;
import net.minecraft.client.gui.screen.OutOfMemory;
import net.minecraft.client.gui.screen.ScreenBase;
import net.minecraft.client.gui.screen.container.PlayerInventory;
import net.minecraft.client.gui.screen.ingame.Chat;
import net.minecraft.client.gui.screen.ingame.Death;
import net.minecraft.client.gui.screen.ingame.Sleeping;
import net.minecraft.client.gui.screen.menu.MainMenu;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.Option;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockRenderer;
import net.minecraft.client.render.block.FoliageColour;
import net.minecraft.client.render.block.GrassColour;
import net.minecraft.client.render.block.WaterColour;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.sound.SoundHelper;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.*;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.AbstractClientPlayer;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.level.Level;
import net.minecraft.level.chunk.ChunkCache;
import net.minecraft.level.dimension.DimensionData;
import net.minecraft.level.source.LevelSource;
import net.minecraft.level.storage.LevelStorage;
import net.minecraft.level.storage.McRegionLevelStorage;
import net.minecraft.level.storage.SessionLockException;
import net.minecraft.sortme.GameRenderer;
import net.minecraft.stat.Stats;
import net.minecraft.util.ProgressListenerError;
import net.minecraft.util.ProgressListenerImpl;
import net.minecraft.util.Vec3i;
import net.minecraft.util.io.StatsFileWriter;
import net.minecraft.util.maths.Box;
import net.minecraft.util.maths.MathHelper;
import net.minecraft.util.maths.Vec3f;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Controllers;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.io.File;

@Mixin(Minecraft.class)
@Environment(EnvType.CLIENT)
public abstract class MinecraftMixin implements Runnable {

    @Shadow
    public boolean isApplet = false;

    @Shadow
    public ProgressListenerImpl progressListener = null;

    @Shadow public Canvas canvas;

    @Shadow public int actualWidth;

    @Shadow public int actualHeight;

    @Shadow private boolean isFullscreen;

    @Shadow private File gameDir;

    @Shadow private LevelStorage levelStorage;

    @Shadow public GameOptions options;

    @Shadow public TexturePackManager texturePackManager;

    @Shadow public TextureManager textureManager;

    @Shadow public TextRenderer textRenderer;

    @Shadow public GameRenderer gameRenderer;

    @Shadow public StatsFileWriter statFileWriter;

    @Shadow protected abstract void method_2150();

    @Shadow public class_596 field_2767;

    @Shadow public Session session;

    @Shadow protected abstract void printOpenGLError(String string);

    @Shadow private OcclusionQueryTester occlusionQueryTester;

    @Shadow public SoundHelper soundHelper;

    @Shadow private FlowingLavaTextureBinder lavaTextureBinder;

    @Shadow private FlowingWaterTextureBinder2 waterTextureBinder;

    @Shadow public WorldRenderer worldRenderer;

    @Shadow public ParticleManager particleManager;

    @Shadow public Level level;

    @Shadow private ThreadDownloadResources resourceDownloadThread;

    @Shadow private String serverIp;

    @Shadow private int serverPort;

    @Shadow public abstract void openScreen(ScreenBase arg);

    @Shadow public InGame overlay;

    @Shadow public ScreenBase currentScreen;

    @Shadow public AbstractClientPlayer player;

    @Shadow public abstract void method_2134();

    @Shadow public boolean skipGameRender;

    @Shadow public abstract void lockCursor();

    @Shadow protected MinecraftApplet mcApplet;

    @Shadow private boolean hasCrashed;

    @Shadow public abstract void setLevel(Level arg);

    @Shadow public volatile boolean running;

    @Shadow public abstract void onGameStartupFailure(GameStartupError arg);

    @Shadow public abstract void init();

    @Shadow public abstract void scheduleStop();

    @Shadow public volatile boolean paused;

    @Shadow private Timer tickTimer;

    @Shadow private int ticksPlayed;

    @Shadow public abstract void tick();

    @Shadow public BaseClientInteractionManager interactionManager;

    @Shadow public abstract void toggleFullscreen();

    @Shadow protected abstract void method_2111(long l);

    @Shadow private long lastFrameRenderTime;

    @Shadow public Achievement achievement;

    @Shadow protected abstract void checkTakingScreenshot();

    @Shadow protected abstract void updateScreenResolution(int i, int j);

    @Shadow public abstract boolean hasLevel();

    @Shadow public String fpsDebugString;

    @Shadow public abstract void method_2131();

    @Shadow public abstract void stop();

    @Shadow private boolean isTakingScreenshot;

    @Shadow private static File gameDirectory;

    @Shadow private int attackCooldown;

    @Shadow private int width;

    @Shadow private int height;

    @Shadow private long lastTickTime;

    @Shadow private int spawnMobCounter;

    @Shadow public boolean hasFocus;

    @Shadow private int mouseTicksProcessed;

    @Shadow public abstract void openPauseMenu();

    @Shadow protected abstract void forceResourceReload();

    @Shadow protected abstract void startLoginThread();

    @Shadow protected abstract void method_2110(int i, boolean bl);

    @Shadow protected abstract void method_2107(int i);

    @Shadow protected abstract void method_2103();

    @Shadow protected abstract void convertWorldFormat(String string, String string2);

    @Shadow public abstract void notifyStatus(Level arg, String string);

    @Shadow public Living viewEntity;

    @Shadow protected abstract void method_2130(String string);

    @Shadow public abstract void createOrLoadWorld(String string, String string2, long l);

    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    public void betterScreenshots_init(CallbackInfo ci) throws LWJGLException {
        if (this.canvas != null) {
            Graphics var1 = this.canvas.getGraphics();
            if (var1 != null) {
                var1.setColor(Color.BLACK);
                var1.fillRect(0, 0, this.actualWidth, this.actualHeight);
                var1.dispose();
            }

            Display.setParent(this.canvas);
        } else if (this.isFullscreen) {
            Display.setFullscreen(true);
            this.actualWidth = Display.getDisplayMode().getWidth();
            this.actualHeight = Display.getDisplayMode().getHeight();
            if (this.actualWidth <= 0) {
                this.actualWidth = 1;
            }

            if (this.actualHeight <= 0) {
                this.actualHeight = 1;
            }
        } else {
            Display.setDisplayMode(new DisplayMode(this.actualWidth, this.actualHeight));
        }

        Display.setTitle("Minecraft Minecraft Beta 1.7.3");
        System.out.println("LWJGL Version: " + Sys.getVersion());

        try {
            //Display.create();
            Display.create((new PixelFormat()).withDepthBits(24));
        } catch (LWJGLException var6) {
            var6.printStackTrace();

            try {
                Thread.sleep(1000L);
            } catch (InterruptedException var5) {
            }

            Display.create();
        }

        this.gameDir = Minecraft.getGameDirectory();
        this.levelStorage = new McRegionLevelStorage(new File(this.gameDir, "saves"));
        this.options = new GameOptions((Minecraft) (Object)this, this.gameDir);
        this.texturePackManager = new TexturePackManager((Minecraft) (Object)this, this.gameDir);
        this.textureManager = new TextureManager(this.texturePackManager, this.options);
        this.method_2150();
        this.textRenderer = new TextRenderer(this.options, "/font/default.png", this.textureManager);
        WaterColour.set(this.textureManager.getColorMap("/misc/watercolor.png"));
        GrassColour.set(this.textureManager.getColorMap("/misc/grasscolor.png"));
        FoliageColour.set(this.textureManager.getColorMap("/misc/foliagecolor.png"));
        this.gameRenderer = new GameRenderer((Minecraft) (Object)this);
        EntityRenderDispatcher.INSTANCE.field_2494 = new class_556((Minecraft) (Object)this);
        this.statFileWriter = new StatsFileWriter(this.session, this.gameDir);
        Achievements.OPEN_INVENTORY.setDescriptionFormat(new ModHelper.betterScreenShots_class_637((Minecraft) (Object)this));
        //this.method_2150();
        Keyboard.create();
        Mouse.create();
        this.field_2767 = new class_596(this.canvas);

        try {
            Controllers.create();
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        this.printOpenGLError("Pre startup");
        GL11.glEnable(3553);
        GL11.glShadeModel(7425);
        GL11.glClearDepth(1.0);
        GL11.glEnable(2929);
        GL11.glDepthFunc(515);
        GL11.glEnable(3008);
        GL11.glAlphaFunc(516, 0.1F);
        GL11.glCullFace(1029);
        GL11.glMatrixMode(5889);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(5888);
        this.printOpenGLError("Startup");
        this.occlusionQueryTester = new OcclusionQueryTester();
        this.soundHelper.acceptOptions(this.options);
        this.textureManager.addTextureBinder(this.lavaTextureBinder);
        this.textureManager.addTextureBinder(this.waterTextureBinder);
        this.textureManager.addTextureBinder(new PortalTextureBinder());
        this.textureManager.addTextureBinder(new CompassTextureBinder((Minecraft) (Object)this));
        this.textureManager.addTextureBinder(new ClockTextureBinder((Minecraft) (Object)this));
        this.textureManager.addTextureBinder(new FlowingWaterTextureBinder());
        this.textureManager.addTextureBinder(new FlowingLavaTextureBinder2());
        this.textureManager.addTextureBinder(new FireTextureBinder(0));
        this.textureManager.addTextureBinder(new FireTextureBinder(1));
        this.worldRenderer = new WorldRenderer((Minecraft) (Object)this, this.textureManager);
        GL11.glViewport(0, 0, this.actualWidth, this.actualHeight);
        this.particleManager = new ParticleManager(this.level, this.textureManager);

        try {
            this.resourceDownloadThread = new ThreadDownloadResources(this.gameDir, (Minecraft) (Object)this);
            this.resourceDownloadThread.start();
        } catch (Exception var3) {
        }

        this.printOpenGLError("Post startup");
        this.overlay = new InGame((Minecraft) (Object)this);
        if (this.serverIp != null) {
            this.openScreen(new Connecting((Minecraft) (Object)this, this.serverIp, this.serverPort));
        } else {
            this.openScreen(new MainMenu());
        }

        this.progressListener = new ProgressListenerImpl((Minecraft) (Object)this);
        ci.cancel();
    }


    @Inject(method = "openScreen", at = @At("HEAD"), cancellable = true)
    public void betterScreenShots_openScreen(ScreenBase arg, CallbackInfo ci) {
        if (!(this.currentScreen instanceof Unused)) {
            if (this.currentScreen != null) {
                this.currentScreen.onClose();
            }

            if (arg instanceof MainMenu) {
                this.statFileWriter.method_1991();
            }

            this.statFileWriter.sync();
            if (arg == null && this.level == null) {
                arg = new MainMenu();
            } else if (arg == null && this.player.health <= 0) {
                arg = new Death();
            }

            if (arg instanceof MainMenu) {
                this.options.debugHud = false;
                this.overlay.clearChat();
            }

            this.currentScreen = (ScreenBase)arg;
            if (arg != null) {
                this.method_2134();
                ScreenScaler var2 = new ScreenScaler(this.options, this.actualWidth, this.actualHeight);
                int var3 = var2.getScaledWidth();
                int var4 = var2.getScaledHeight();
                ((ScreenBase)arg).init((Minecraft) (Object)this, var3, var4);
                this.skipGameRender = false;
            } else {
                this.lockCursor();
            }

        }
        ci.cancel();
    }


    @Inject(method = "stop", at = @At("HEAD"), cancellable = true)
    public void betterScreenshots_stop(CallbackInfo ci) {
        try {

            System.out.println("Stopping!");

            try {
                if(this.level != null) {
                    this.statFileWriter.incrementStat(Stats.leaveGame, 1);
                    if(this.level.isClientSide) {
                        this.level.disconnect();
                    }
                }

                this.setLevel((Level)null);
            } catch (Throwable var8) {
            }


            this.statFileWriter.method_1991();
            this.statFileWriter.sync();
            if (this.mcApplet != null) {
                this.mcApplet.method_2155();
            }

            try {
                if (this.resourceDownloadThread != null) {
                    this.resourceDownloadThread.method_111();
                }
            } catch (Exception var9) {
            }

//            System.out.println("Stopping!");
//
//            try {
//                this.setLevel((Level)null);
//            } catch (Throwable var8) {
//            }

            try {
                class_214.method_740();
            } catch (Throwable var7) {
            }

            this.soundHelper.cleanup();
            Mouse.destroy();
            Keyboard.destroy();
        } finally {
            Display.destroy();
            if (!this.hasCrashed) {
                System.exit(0);
            }

        }

        System.gc();
        ci.cancel();
    }


    @Inject(method = "run", at = @At("HEAD"), cancellable = true, remap = false)
    public void run(CallbackInfo ci) {
        this.running = true;

        try {
            this.init();
        } catch (Exception var17) {
            var17.printStackTrace();
            this.onGameStartupFailure(new GameStartupError("Failed to start game", var17));
            return;
        }

        try {
            long var1 = System.currentTimeMillis();
            int var3 = 0;

            while(this.running) {
                try {
                    //noinspection removal
                    if (this.mcApplet != null && !this.mcApplet.isActive()) {
                        break;
                    }

                    Box.method_85();
                    Vec3f.method_1292();
                    if (this.canvas == null && Display.isCloseRequested()) {
                        this.scheduleStop();
                    }

                    if (this.paused && this.level != null) {
                        float var4 = this.tickTimer.field_2370;
                        this.tickTimer.method_1853();
                        this.tickTimer.field_2370 = var4;
                    } else {
                        this.tickTimer.method_1853();
                    }

                    long var23 = System.nanoTime();

                    for(int var6 = 0; var6 < this.tickTimer.field_2369; ++var6) {
                        ++this.ticksPlayed;

                        try {
                            this.tick();
                        } catch (SessionLockException var16) {
                            this.level = null;
                            this.setLevel((Level)null);
                            this.openScreen(new LevelSaveConflict());
                        }
                    }

                    long var24 = System.nanoTime() - var23;
                    this.printOpenGLError("Pre render");
                    BlockRenderer.fancyGraphics = this.options.fancyGraphics;
                    this.soundHelper.setSoundPosition(this.player, this.tickTimer.field_2370);
                    GL11.glEnable(3553);
                    if (this.level != null) {
                        this.level.method_232();
                    }

                    //if (!Keyboard.isKeyDown(65)) {
                    if(this.level != null && (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) || !Keyboard.isKeyDown(Keyboard.KEY_F7)) {
                        Display.update();
                    }

                    if (this.player != null && this.player.isInsideWall()) {
                        this.options.thirdPerson = false;
                    }

                    if (!this.skipGameRender) {
                        if (this.interactionManager != null) {
                            this.interactionManager.method_1706(this.tickTimer.field_2370);
                        }

                        this.gameRenderer.method_1844(this.tickTimer.field_2370);
                    }

                    GL11.glFlush();
                    if (!Display.isActive()) {
                        if (this.isFullscreen) {
                            this.toggleFullscreen();
                        }
//
//                        Thread.sleep(10L);
                    }

                    if (this.options.debugHud) {
                        this.method_2111(var24);
                    } else {
                        this.lastFrameRenderTime = System.nanoTime();
                    }

                    this.achievement.renderBannerAndLicenseText();
                    Thread.yield();
                    //if (Keyboard.isKeyDown(65)) {
                    if((this.level == null || !Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && !Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) && Keyboard.isKeyDown(Keyboard.KEY_F7)) {
                        Display.update();
                    }

                    this.checkTakingScreenshot();
                    if (this.canvas != null && !this.isFullscreen && (this.canvas.getWidth() != this.actualWidth || this.canvas.getHeight() != this.actualHeight)) {
                        this.actualWidth = this.canvas.getWidth();
                        this.actualHeight = this.canvas.getHeight();
                        if (this.actualWidth <= 0) {
                            this.actualWidth = 1;
                        }

                        if (this.actualHeight <= 0) {
                            this.actualHeight = 1;
                        }

                        this.updateScreenResolution(this.actualWidth, this.actualHeight);
                    }

                    this.printOpenGLError("Post render");
                    ++var3;

                    for(this.paused = !this.hasLevel() && this.currentScreen != null && this.currentScreen.isPauseScreen(); System.currentTimeMillis() >= var1 + 1000L; var3 = 0) {
                        this.fpsDebugString = var3 + " fps, " + class_66.field_230 + " chunk updates";
                        class_66.field_230 = 0;
                        var1 += 1000L;
                    }
                } catch (SessionLockException var18) {
                    this.level = null;
                    this.setLevel((Level)null);
                    this.openScreen(new LevelSaveConflict());
                } catch (OutOfMemoryError var19) {
                    this.method_2131();
                    this.openScreen(new OutOfMemory());
                    System.gc();
                }
            }
        } catch (ProgressListenerError var20) {
        } catch (Throwable var21) {
            this.method_2131();
            var21.printStackTrace();
            this.onGameStartupFailure(new GameStartupError("Unexpected error", var21));
        } finally {
            this.stop();
        }
        ci.cancel();
    }

    @Redirect(
            method = "checkTakingScreenshot",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/Minecraft;isTakingScreenshot:Z",
                    opcode = Opcodes.GETFIELD
            )
    )
    private boolean checkTakingScreenshot(Minecraft instance) {

        this.isTakingScreenshot = true;
        //this.overlay.addChatMessage(ScreenshotManager.takeScreenshot(gameDirectory, this.actualWidth, this.actualHeight));
        if(this.level != null && (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))) {
            this.overlay.addChatMessage(ModHelper.mainSaveHugeScreenshot((Minecraft) (Object)this, this.gameDirectory, this.actualWidth, this.actualHeight, ModHelper.ModHelperFields.hugeWidth, ModHelper.ModHelperFields.hugeHeight, (System.getProperty("os.name").toLowerCase().contains("mac")) ? Keyboard.isKeyDown(Keyboard.KEY_LMETA) || Keyboard.isKeyDown(Keyboard.KEY_RMETA) : Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)));
        } else {
            this.overlay.addChatMessage(ScreenshotManager.takeScreenshot(gameDirectory, this.actualWidth, this.actualHeight));
        }

        return true;
    }


    @Inject(method = "method_2111", at = @At("HEAD"), cancellable = true)
    private void betterScreenshots_method_2111(long l, CallbackInfo ci) {
        if(!Keyboard.isKeyDown(Keyboard.KEY_F6)) {
            ci.cancel();
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

        /** - Add F7 event key */
        if(this.level != null && (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) && eventKey == Keyboard.KEY_F7) {
            IsometricScreenshotRenderer isoRenderer = (new IsometricScreenshotRenderer((Minecraft) (Object)this));
            isoRenderer.doRender();
        }

        return eventKey;
    }


//    @Inject(method = "createOrLoadWorld", at = @At("HEAD"), cancellable = true)
//    public void betterScreenshots_createOrLoadWorld(String string, String string2, long l, CallbackInfo ci) {
//        if (this.progressListener == null)
//        {
//            this.progressListener = new ProgressListenerImpl((Minecraft) (Object)this);
//        }
//    }


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

            if (!arg.isClientSide) {
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

//    @Inject(method = "convertWorldFormat", at = @At("HEAD"), cancellable = true)
//    private void betterScreenshots_convertWorldFormat(String string, String string2, CallbackInfo ci) {
//        //this.progressListener.notifyWithGameRunning("Converting World to " + this.levelStorage.getLevelFormat());
//        this.progressListener.notifyWithGameRunning("Converting " + string2 + " to " + this.levelStorage.getLevelFormat());
//        this.progressListener.method_1796("This may take a while :)");
//        this.levelStorage.convertLevel(string, this.progressListener);
//        this.createOrLoadWorld(string, string2, 0L);
//        ci.cancel();
//    }

//    @Inject(method = "method_2130", at = @At("HEAD"), cancellable = true)
//    private void betterScreenshots_method_2130(String string, CallbackInfo ci) {
//        if(this.progressListener == null) {
//            ci.cancel();
//        }
//        // OR the below solution
//        if (this.progressListener == null)
//        {
//            this.progressListener = new ProgressListenerImpl((Minecraft) (Object)this);
//        }
//    }


//    @Inject(method = "loadSoundFromDir", at = @At("HEAD"), cancellable = true)
//    public void betterScreenshots_loadSoundFromDir(String string, File file, CallbackInfo ci) {
//        int var3 = string.indexOf("/");
//        String var4 = string.substring(0, var3);
//        string = string.substring(var3 + 1);
////        if (var4.equalsIgnoreCase("sound")) {
////            this.soundHelper.addSound(string, file);
////        } else if (var4.equalsIgnoreCase("newsound")) {
////            this.soundHelper.addSound(string, file);
////        } else if (var4.equalsIgnoreCase("streaming")) {
////            this.soundHelper.addStreaming(string, file);
////        } else if (var4.equalsIgnoreCase("music")) {
////            this.soundHelper.addMusic(string, file);
////        } else if (var4.equalsIgnoreCase("newmusic")) {
////            this.soundHelper.addMusic(string, file);
////        }
//        if(!var4.equalsIgnoreCase("sound") && !var4.equalsIgnoreCase("newsound")) {
//            if(var4.equalsIgnoreCase("streaming")) {
//                this.soundHelper.addStreaming(string, file);
//            } else if(var4.equalsIgnoreCase("music") || var4.equalsIgnoreCase("newmusic")) {
//                this.soundHelper.addMusic(string, file);
//            }
//        } else {
//            this.soundHelper.addSound(string, file);
//        }
//        ci.cancel();
//    }
}
