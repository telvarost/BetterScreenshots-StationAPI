package com.github.telvarost.hudtweaks.mixin;

import com.github.telvarost.hudtweaks.IsometricScreenshotRenderer;
import com.github.telvarost.hudtweaks.ModHelper;
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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
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



    @Inject(method = "checkTakingScreenshot", at = @At("HEAD"), cancellable = true)
    private void checkTakingScreenshot(CallbackInfo ci) {
        if (Keyboard.isKeyDown(60)) {
            if (!this.isTakingScreenshot) {
                this.isTakingScreenshot = true;
                //this.overlay.addChatMessage(ScreenshotManager.takeScreenshot(gameDirectory, this.actualWidth, this.actualHeight));
                if(this.level != null && (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))) {
                    this.overlay.addChatMessage(ModHelper.mainSaveHugeScreenshot((Minecraft) (Object)this, this.gameDirectory, this.actualWidth, this.actualHeight, ModHelper.ModHelperFields.hugeWidth, ModHelper.ModHelperFields.hugeHeight, (System.getProperty("os.name").toLowerCase().contains("mac")) ? Keyboard.isKeyDown(Keyboard.KEY_LMETA) || Keyboard.isKeyDown(Keyboard.KEY_RMETA) : Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)));
                } else {
                    this.overlay.addChatMessage(ScreenshotManager.takeScreenshot(gameDirectory, this.actualWidth, this.actualHeight));
                }
            }
        } else {
            this.isTakingScreenshot = false;
        }
        ci.cancel();
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

    @Inject(method = "toggleFullscreen", at = @At("HEAD"), cancellable = true)
    public void toggleFullscreen(CallbackInfo ci) {
        try {
            this.isFullscreen = !this.isFullscreen;
            if (this.isFullscreen) {
                Display.setDisplayMode(Display.getDesktopDisplayMode());
                this.actualWidth = Display.getDisplayMode().getWidth();
                this.actualHeight = Display.getDisplayMode().getHeight();
                if (this.actualWidth <= 0) {
                    this.actualWidth = 1;
                }

                if (this.actualHeight <= 0) {
                    this.actualHeight = 1;
                }
            } else {
                if (this.canvas != null) {
                    this.actualWidth = this.canvas.getWidth();
                    this.actualHeight = this.canvas.getHeight();
                } else {
                    this.actualWidth = this.width;
                    this.actualHeight = this.height;
                }

                if (this.actualWidth <= 0) {
                    this.actualWidth = 1;
                }

                if (this.actualHeight <= 0) {
                    this.actualHeight = 1;
                }
            }

            if (this.currentScreen != null) {
                this.updateScreenResolution(this.actualWidth, this.actualHeight);
            }

            Display.setFullscreen(this.isFullscreen);
            Display.setVSyncEnabled(true);
            Display.update();
        } catch (Exception var2) {
            var2.printStackTrace();
        }
        ci.cancel();
    }


    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void tick(CallbackInfo ci) {
        if (this.ticksPlayed == 6000) {
            this.startLoginThread();
        }

        this.statFileWriter.tick();
        if(!this.paused) {
            this.overlay.runTick();
        }
        this.gameRenderer.method_1838(1.0F);
        int var3;
        if (this.player != null) {
            LevelSource var1 = this.level.getCache();
            if (var1 instanceof ChunkCache) {
                ChunkCache var2 = (ChunkCache)var1;
                var3 = MathHelper.floor((float)((int)this.player.x)) >> 4;
                int var4 = MathHelper.floor((float)((int)this.player.z)) >> 4;
                var2.method_1242(var3, var4);
            }
        }

        if (!this.paused && this.level != null) {
            this.interactionManager.tick();
        }

        GL11.glBindTexture(3553, this.textureManager.getTextureId("/terrain.png"));
        if (!this.paused) {
            this.textureManager.tick();
        }

        if (this.currentScreen == null && this.player != null) {
            if (this.player.health <= 0) {
                this.openScreen((ScreenBase)null);
            } else if (this.player.isSleeping() && this.level != null && this.level.isClientSide) {
                this.openScreen(new Sleeping());
            }
        } else if (this.currentScreen != null && this.currentScreen instanceof Sleeping && !this.player.isSleeping()) {
            this.openScreen((ScreenBase)null);
        }

        if (this.currentScreen != null) {
            this.attackCooldown = 10000;
            this.mouseTicksProcessed = this.ticksPlayed + 10000;
        }

        if (this.currentScreen != null) {
            this.currentScreen.method_130();
            if (this.currentScreen != null) {
                this.currentScreen.smokeRenderer.render();
                this.currentScreen.tick();
            }
        }

        if (this.currentScreen == null || this.currentScreen.passEvents) {
            label301:
            while(true) {
                while(true) {
                    while(true) {
                        long var5;
                        do {
                            if (!Mouse.next()) {
                                if (this.attackCooldown > 0) {
                                    --this.attackCooldown;
                                }

                                while(true) {
                                    while(true) {
                                        do {
                                            if (!Keyboard.next()) {
                                                if (this.currentScreen == null) {
                                                    if (Mouse.isButtonDown(0) && (float)(this.ticksPlayed - this.mouseTicksProcessed) >= this.tickTimer.tickrate / 4.0F && this.hasFocus) {
                                                        this.method_2107(0);
                                                        this.mouseTicksProcessed = this.ticksPlayed;
                                                    }

                                                    if (Mouse.isButtonDown(1) && (float)(this.ticksPlayed - this.mouseTicksProcessed) >= this.tickTimer.tickrate / 4.0F && this.hasFocus) {
                                                        this.method_2107(1);
                                                        this.mouseTicksProcessed = this.ticksPlayed;
                                                    }
                                                }

                                                this.method_2110(0, this.currentScreen == null && Mouse.isButtonDown(0) && this.hasFocus);
                                                break label301;
                                            }

                                            this.player.method_136(Keyboard.getEventKey(), Keyboard.getEventKeyState());
                                        } while(!Keyboard.getEventKeyState());

                                        if (Keyboard.getEventKey() == 87) {
                                            this.toggleFullscreen();
                                        } else {
                                            if (this.currentScreen != null) {
                                                this.currentScreen.onKeyboardEvent();
                                            } else {
                                                if (Keyboard.getEventKey() == 1) {
                                                    this.openPauseMenu();
                                                }

                                                if (Keyboard.getEventKey() == 31 && Keyboard.isKeyDown(61)) {
                                                    this.forceResourceReload();
                                                }

                                                if (Keyboard.getEventKey() == 59) {
                                                    this.options.hideHud = !this.options.hideHud;
                                                }

                                                if (Keyboard.getEventKey() == 61) {
                                                    this.options.debugHud = !this.options.debugHud;
                                                }

                                                if (Keyboard.getEventKey() == 63) {
                                                    this.options.thirdPerson = !this.options.thirdPerson;
                                                }

                                                if(this.level != null && (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) && Keyboard.getEventKey() == Keyboard.KEY_F7) {
                                                    (new IsometricScreenshotRenderer((Minecraft) (Object)this)).doRender();
                                                }

                                                if (Keyboard.getEventKey() == 66) {
                                                    this.options.cinematicMode = !this.options.cinematicMode;
                                                }

                                                if (Keyboard.getEventKey() == this.options.inventoryKey.key) {
                                                    this.openScreen(new PlayerInventory(this.player));
                                                }

                                                if (Keyboard.getEventKey() == this.options.dropKey.key) {
                                                    this.player.dropSelectedItem();
                                                }

                                                if (this.hasLevel() && Keyboard.getEventKey() == this.options.chatKey.key) {
                                                    this.openScreen(new Chat());
                                                }
                                            }

                                            for(int var6 = 0; var6 < 9; ++var6) {
                                                if (Keyboard.getEventKey() == 2 + var6) {
                                                    this.player.inventory.selectedHotbarSlot = var6;
                                                }
                                            }

                                            if (Keyboard.getEventKey() == this.options.fogKey.key) {
                                                this.options.changeOption(Option.field_1101, !Keyboard.isKeyDown(42) && !Keyboard.isKeyDown(54) ? 1 : -1);
                                            }
                                        }
                                    }
                                }
                            }

                            var5 = System.currentTimeMillis() - this.lastTickTime;
                        } while(var5 > 200L);

                        var3 = Mouse.getEventDWheel();
                        if (var3 != 0) {
                            this.player.inventory.scrollInHotbar(var3);
                            if (this.options.field_1445) {
                                if (var3 > 0) {
                                    var3 = 1;
                                }

                                if (var3 < 0) {
                                    var3 = -1;
                                }

                                GameOptions var10000 = this.options;
                                var10000.field_1448 += (float)var3 * 0.25F;
                            }
                        }

                        if (this.currentScreen == null) {
                            if (!this.hasFocus && Mouse.getEventButtonState()) {
                                this.lockCursor();
                            } else {
                                if (Mouse.getEventButton() == 0 && Mouse.getEventButtonState()) {
                                    this.method_2107(0);
                                    this.mouseTicksProcessed = this.ticksPlayed;
                                }

                                if (Mouse.getEventButton() == 1 && Mouse.getEventButtonState()) {
                                    this.method_2107(1);
                                    this.mouseTicksProcessed = this.ticksPlayed;
                                }

                                if (Mouse.getEventButton() == 2 && Mouse.getEventButtonState()) {
                                    this.method_2103();
                                }
                            }
                        } else if (this.currentScreen != null) {
                            this.currentScreen.onMouseEvent();
                        }
                    }
                }
            }
        }

        if (this.level != null) {
            if (this.player != null) {
                ++this.spawnMobCounter;
                if (this.spawnMobCounter == 30) {
                    this.spawnMobCounter = 0;
                    this.level.method_287(this.player);
                }
            }

            this.level.difficulty = this.options.difficulty;
            if (this.level.isClientSide) {
                //this.level.difficulty = 3;
                this.level.difficulty = 1;
            }

            if (!this.paused) {
                this.gameRenderer.method_1837();
            }

            if (!this.paused) {
                this.worldRenderer.method_1557();
            }

            if (!this.paused) {
                if (this.level.field_210 > 0) {
                    --this.level.field_210;
                }

                this.level.method_227();
            }

            if (!this.paused || this.hasLevel()) {
                this.level.method_196(this.options.difficulty > 0, true);
                this.level.method_242();
            }

            if (!this.paused && this.level != null) {
                this.level.method_294(MathHelper.floor(this.player.x), MathHelper.floor(this.player.y), MathHelper.floor(this.player.z));
            }

            if (!this.paused) {
                this.particleManager.method_320();
            }
        }

        this.lastTickTime = System.currentTimeMillis();
        ci.cancel();
    }


    @Inject(method = "createOrLoadWorld", at = @At("HEAD"), cancellable = true)
    public void betterScreenshots_createOrLoadWorld(String string, String string2, long l, CallbackInfo ci) {
        this.setLevel((Level)null);
        System.gc();
        if (this.levelStorage.isOld(string)) {
            this.convertWorldFormat(string, string2);
        } else {
            if(this.progressListener != null) {
                this.progressListener.notifyWithGameRunning("Switching level");
                this.progressListener.method_1796("");
            }

            DimensionData var5 = this.levelStorage.createDimensionFile(string, false);
            Level var6 = null;
            var6 = new Level(var5, string2, l);
            if (var6.field_215) {
                this.statFileWriter.incrementStat(Stats.createWorld, 1);
                this.statFileWriter.incrementStat(Stats.startGame, 1);
                this.notifyStatus(var6, "Generating level");
            } else {
                this.statFileWriter.incrementStat(Stats.loadWorld, 1);
                this.statFileWriter.incrementStat(Stats.startGame, 1);
                this.notifyStatus(var6, "Loading level");
            }
        }
        ci.cancel();
    }


    @Inject(method = "showLevelProgress", at = @At("HEAD"), cancellable = true)
    public void betterScreenshots_showLevelProgress(Level arg, String string, PlayerBase arg2, CallbackInfo ci) {
        this.statFileWriter.method_1991();
        this.statFileWriter.sync();
        this.viewEntity = null;
        if(this.progressListener != null) {
            this.progressListener.notifyWithGameRunning(string);
            this.progressListener.method_1796("");
        }
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
                arg.spawnEntity(this.player);
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
            this.waterTextureBinder.id = this.textureManager.getTextureId("/misc/water.png");
            this.lavaTextureBinder.id = 0;
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
            this.interactionManager.method_1718(this.player);
            if (arg.field_215) {
                arg.saveLevel(this.progressListener);
            }

            this.viewEntity = this.player;
        } else {
            this.levelStorage.method_1003();
            this.player = null;
        }

        System.gc();
        this.lastTickTime = 0L;
        ci.cancel();
    }

    @Inject(method = "convertWorldFormat", at = @At("HEAD"), cancellable = true)
    private void betterScreenshots_convertWorldFormat(String string, String string2, CallbackInfo ci) {
        //this.progressListener.notifyWithGameRunning("Converting World to " + this.levelStorage.getLevelFormat());
        this.progressListener.notifyWithGameRunning("Converting " + string2 + " to " + this.levelStorage.getLevelFormat());
        this.progressListener.method_1796("This may take a while :)");
        this.levelStorage.convertLevel(string, this.progressListener);
        this.createOrLoadWorld(string, string2, 0L);
        ci.cancel();
    }

    @Inject(method = "method_2130", at = @At("HEAD"), cancellable = true)
    private void betterScreenshots_method_2130(String string, CallbackInfo ci) {
        if(this.progressListener != null) {
            this.progressListener.notifyWithGameRunning(string);
            this.progressListener.method_1796("Building terrain");
        }
        short var2 = 128;
        int var3 = 0;
        int var4 = var2 * 2 / 16 + 1;
        var4 *= var4;
        LevelSource var5 = this.level.getCache();
        Vec3i var6 = this.level.getSpawnPosition();
        if (this.player != null) {
            var6.x = (int)this.player.x;
            var6.z = (int)this.player.z;
        }

        if (var5 instanceof ChunkCache) {
            ChunkCache var7 = (ChunkCache)var5;
            var7.method_1242(var6.x >> 4, var6.z >> 4);
        }

        for(int var10 = -var2; var10 <= var2; var10 += 16) {
            for(int var8 = -var2; var8 <= var2; var8 += 16) {
                if(this.progressListener != null) {
                    this.progressListener.progressStagePercentage(var3++ * 100 / var4);
                }
                this.level.getTileId(var6.x + var10, 64, var6.z + var8);

                while(this.level.method_232()) {
                }
            }
        }

        if(this.progressListener != null) {
            this.progressListener.method_1796("Simulating world for a bit");
        }
        boolean var9 = true;
        this.level.method_292();
        ci.cancel();
    }

    @Inject(method = "loadSoundFromDir", at = @At("HEAD"), cancellable = true)
    public void betterScreenshots_loadSoundFromDir(String string, File file, CallbackInfo ci) {
        int var3 = string.indexOf("/");
        String var4 = string.substring(0, var3);
        string = string.substring(var3 + 1);
//        if (var4.equalsIgnoreCase("sound")) {
//            this.soundHelper.addSound(string, file);
//        } else if (var4.equalsIgnoreCase("newsound")) {
//            this.soundHelper.addSound(string, file);
//        } else if (var4.equalsIgnoreCase("streaming")) {
//            this.soundHelper.addStreaming(string, file);
//        } else if (var4.equalsIgnoreCase("music")) {
//            this.soundHelper.addMusic(string, file);
//        } else if (var4.equalsIgnoreCase("newmusic")) {
//            this.soundHelper.addMusic(string, file);
//        }
        if(!var4.equalsIgnoreCase("sound") && !var4.equalsIgnoreCase("newsound")) {
            if(var4.equalsIgnoreCase("streaming")) {
                this.soundHelper.addStreaming(string, file);
            } else if(var4.equalsIgnoreCase("music") || var4.equalsIgnoreCase("newmusic")) {
                this.soundHelper.addMusic(string, file);
            }
        } else {
            this.soundHelper.addSound(string, file);
        }
        ci.cancel();
    }
}
