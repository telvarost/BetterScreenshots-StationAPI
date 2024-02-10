package com.github.telvarost.hudtweaks.mixin;

import com.github.telvarost.hudtweaks.ModHelper;
import net.minecraft.block.BlockBase;
import net.minecraft.class_68;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.TileEntityRenderDispatcher;
import net.minecraft.client.render.particle.*;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.Record;
import net.minecraft.level.Level;
import net.minecraft.level.dimension.Dimension;
import net.minecraft.tileentity.TileEntityBase;
import net.minecraft.util.maths.MathHelper;
import net.minecraft.util.maths.Vec3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Random;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {

	@Shadow private int field_1783;

	@Shadow private int field_1784;

	@Shadow private int field_1785;

	@Shadow private int field_1786;

	@Shadow private Minecraft client;

	@Shadow private Level level;

	@Shadow private TextureManager textureManager;

	@Shadow public List field_1795;

	@Shadow public abstract void addParticle(String string, double d, double e, double f, double g, double h, double i);

	@Inject(method = "method_1544", at = @At("HEAD"), cancellable = true)
	public void method_1544(Vec3f arg, class_68 arg2, float f, CallbackInfo ci) {
		if (this.field_1783 > 0) {
			--this.field_1783;
		} else {
			TileEntityRenderDispatcher.INSTANCE.method_1274(this.level, this.textureManager, this.client.textRenderer, this.client.viewEntity, f);
			EntityRenderDispatcher.INSTANCE.method_1917(this.level, this.textureManager, this.client.textRenderer, this.client.viewEntity, this.client.options, f);
			this.field_1784 = 0;
			this.field_1785 = 0;
			this.field_1786 = 0;
			Living var4 = this.client.viewEntity;
			EntityRenderDispatcher.field_2490 = var4.prevRenderX + (var4.x - var4.prevRenderX) * (double)f;
			EntityRenderDispatcher.field_2491 = var4.prevRenderY + (var4.y - var4.prevRenderY) * (double)f;
			EntityRenderDispatcher.field_2492 = var4.prevRenderZ + (var4.z - var4.prevRenderZ) * (double)f;
			TileEntityRenderDispatcher.renderOffsetX = var4.prevRenderX + (var4.x - var4.prevRenderX) * (double)f;
			TileEntityRenderDispatcher.renderOffsetY = var4.prevRenderY + (var4.y - var4.prevRenderY) * (double)f;
			TileEntityRenderDispatcher.renderOffsetZ = var4.prevRenderZ + (var4.z - var4.prevRenderZ) * (double)f;
			List var5 = this.level.getEntities();
			this.field_1784 = var5.size();

			int var6;
			EntityBase var7;
			for(var6 = 0; var6 < this.level.field_201.size(); ++var6) {
				var7 = (EntityBase)this.level.field_201.get(var6);
				++this.field_1785;
				if (var7.shouldRenderFrom(arg)) {
					EntityRenderDispatcher.INSTANCE.method_1921(var7, f);
				}
			}

			for(var6 = 0; var6 < var5.size(); ++var6) {
				var7 = (EntityBase)var5.get(var6);
				if (var7.shouldRenderFrom(arg) && (var7.field_1622 || arg2.method_2007(var7.boundingBox)) && (var7 != this.client.viewEntity || this.client.options.thirdPerson || this.client.viewEntity.isSleeping()) || ModHelper.ModHelperFields.isTakingIsometricScreenshot) {
					int var8 = MathHelper.floor(var7.y);
					if (var8 < 0) {
						var8 = 0;
					}

					if (var8 >= 128) {
						var8 = 127;
					}

					if (this.level.isTileLoaded(MathHelper.floor(var7.x), var8, MathHelper.floor(var7.z))) {
						++this.field_1785;
						EntityRenderDispatcher.INSTANCE.method_1921(var7, f);
					}
				}
			}

			for(var6 = 0; var6 < this.field_1795.size(); ++var6) {
				TileEntityRenderDispatcher.INSTANCE.method_1278((TileEntityBase)this.field_1795.get(var6), f);
			}
		}
		ci.cancel();
	}

	@ModifyConstant(
			method = "method_1552",
			constant = @Constant(floatValue = 0.33F)
	)
	public float method_1552(float f) {
		return 12.33F;
	}

	@Inject(method = "addParticle", at = @At("HEAD"), cancellable = true)
	public void betterScreenshots_addParticle(String string, double d, double e, double f, double g, double h, double i, CallbackInfo ci) {
		if (this.client != null && this.client.viewEntity != null && this.client.particleManager != null) {
			double var14 = this.client.viewEntity.x - d;
			double var16 = this.client.viewEntity.y - e;
			double var18 = this.client.viewEntity.z - f;
			double var20 = 16.0;
			if (!(var14 * var14 + var16 * var16 + var18 * var18 > var20 * var20)) {
				if (string.equals("bubble")) {
					this.client.particleManager.addParticle(new Bubble(this.level, d, e, f, g, h, i));
				} else if (string.equals("smoke")) {
					this.client.particleManager.addParticle(new Smoke(this.level, d, e, f, g, h, i));
				} else if (string.equals("note")) {
					this.client.particleManager.addParticle(new Note(this.level, d, e, f, g, h, i));
				} else if (string.equals("portal")) {
					this.client.particleManager.addParticle(new Portal(this.level, d, e, f, g, h, i));
				} else if (string.equals("explode")) {
					this.client.particleManager.addParticle(new Explosion(this.level, d, e, f, g, h, i));
				} else if (string.equals("flame")) {
					this.client.particleManager.addParticle(new Fire(this.level, d, e, f, g, h, i));
				} else if (string.equals("lava")) {
					this.client.particleManager.addParticle(new Lava(this.level, d, e, f));
				} else if (string.equals("footstep")) {
					this.client.particleManager.addParticle(new Footstep(this.textureManager, this.level, d, e, f));
				} else if (string.equals("splash")) {
					this.client.particleManager.addParticle(new Water(this.level, d, e, f, g, h, i));
				} else if (string.equals("largesmoke")) {
					this.client.particleManager.addParticle(new Smoke(this.level, d, e, f, g, h, i, 2.5F));
				} else if (string.equals("reddust")) {
					this.client.particleManager.addParticle(new Redstone(this.level, d, e, f, (float)g, (float)h, (float)i));
				} else if (string.equals("snowballpoof")) {
					this.client.particleManager.addParticle(new Poof(this.level, d, e, f, ItemBase.snowball));
				} else if (string.equals("snowshovel")) {
					this.client.particleManager.addParticle(new SnowPuff(this.level, d, e, f, g, h, i));
				} else if (string.equals("slime")) {
					this.client.particleManager.addParticle(new Poof(this.level, d, e, f, ItemBase.slimeball));
				} else if (string.equals("heart")) {
					this.client.particleManager.addParticle(new Heart(this.level, d, e, f, g, h, i));
				} else if(string.startsWith("iconcrack_")) {
					String string22[] = string.split("_", 3);
					int i23 = Integer.parseInt(string22[1]);
					this.client.particleManager.addParticle(new Poof(this.level, d, e, f, ItemBase.byId[i23]));
				} else if(string.startsWith("tilecrack_")) {
					String string25[] = string.split("_", 3);
					int i26 = Integer.parseInt(string25[1]);
					int i27 = Integer.parseInt(string25[2]);
					int i28 = MathHelper.floor(d);
					int i29 = MathHelper.floor(e);
					int i30 = MathHelper.floor(f);
					this.client.particleManager.addParticle((new Digging(this.level, d, e, f, g, h, i, BlockBase.BY_ID[i26], 1, i27)).method_1856(i28, i29, i30));
				} else if(string.startsWith("tiledust_")) {
					String string31[] = string.split("_", 3);
					int i32 = Integer.parseInt(string31[1]);
					int i33 = Integer.parseInt(string31[2]);
					int i34 = MathHelper.floor(d);
					int i35 = MathHelper.floor(e);
					int i36 = MathHelper.floor(f);
					Digging entityDiggingFX36 = new Digging(this.level, d, e, f, g, h, i, BlockBase.BY_ID[i32], 1, i33);
					entityDiggingFX36.velocityX = g;
					entityDiggingFX36.velocityY = h;
					entityDiggingFX36.velocityZ = i;
					this.client.particleManager.addParticle(entityDiggingFX36.method_1856(i34, i35, i36));
				}
			}
		}
		ci.cancel();
	}

	@Inject(method = "playLevelEvent", at = @At("HEAD"), cancellable = true)
	public void playLevelEvent(PlayerBase arg, int i, int j, int k, int l, int m, CallbackInfo ci) {
		Random var7 = this.level.rand;
		int var16;
		switch (i) {
			case 1000:
				//this.level.playSound((double)j, (double)k, (double)l, "random.click", 1.0F, 1.0F);
				this.level.playSound((double)j, (double)k, (double)l, m == 1 ? "random.wood click" : "random.click", 1.0F, 1.0F);
				break;
			case 1001:
				//this.level.playSound((double)j, (double)k, (double)l, "random.click", 1.0F, 1.2F);
				this.level.playSound((double)j, (double)k, (double)l, m == 1 ? "random.wood click" : "random.click", 1.0F, 1.2F);
				break;
			case 1002:
				this.level.playSound((double)j, (double)k, (double)l, "random.bow", 1.0F, 1.2F);
				break;
			case 1003:
				if (Math.random() < 0.5) {
					this.level.playSound((double)j + 0.5, (double)k + 0.5, (double)l + 0.5, "random.door_open", 1.0F, this.level.rand.nextFloat() * 0.1F + 0.9F);
				} else {
					this.level.playSound((double)j + 0.5, (double)k + 0.5, (double)l + 0.5, "random.door_close", 1.0F, this.level.rand.nextFloat() * 0.1F + 0.9F);
				}
				break;
			case 1004:
				this.level.playSound((double)((float)j + 0.5F), (double)((float)k + 0.5F), (double)((float)l + 0.5F), "random.fizz", 0.5F, 2.6F + (var7.nextFloat() - var7.nextFloat()) * 0.8F);
				break;
			case 1005:
				if (ItemBase.byId[m] instanceof Record) {
					this.level.method_179(((Record)ItemBase.byId[m]).title, j, k, l);
				} else {
					this.level.method_179((String)null, j, k, l);
				}
				break;
			case 2000:
				int var8 = m % 3 - 1;
				int var9 = m / 3 % 3 - 1;
				double var10 = (double)j + (double)var8 * 0.6 + 0.5;
				double var12 = (double)k + 0.5;
				double var14 = (double)l + (double)var9 * 0.6 + 0.5;

				for(var16 = 0; var16 < 10; ++var16) {
					double var31 = var7.nextDouble() * 0.2 + 0.01;
					double var19 = var10 + (double)var8 * 0.01 + (var7.nextDouble() - 0.5) * (double)var9 * 0.5;
					double var21 = var12 + (var7.nextDouble() - 0.5) * 0.5;
					double var23 = var14 + (double)var9 * 0.01 + (var7.nextDouble() - 0.5) * (double)var8 * 0.5;
					double var25 = (double)var8 * var31 + var7.nextGaussian() * 0.01;
					double var27 = -0.03 + var7.nextGaussian() * 0.01;
					double var29 = (double)var9 * var31 + var7.nextGaussian() * 0.01;
					this.addParticle("smoke", var19, var21, var23, var25, var27, var29);
				}

				return;
			case 2001:
				var16 = m & 255;
				if (var16 > 0) {
					BlockBase var17 = BlockBase.BY_ID[var16];
					this.client.soundHelper.playSound(var17.sounds.getBreakSound(), (float)j + 0.5F, (float)k + 0.5F, (float)l + 0.5F, (var17.sounds.getVolume() + 1.0F) / 2.0F, var17.sounds.getPitch() * 0.8F);
				}

				this.client.particleManager.addTileBreakParticles(j, k, l, m & 255, m >> 8 & 255);
				break;
			case 2004:
				for(int i17 = 0; i17 < 20; ++i17) {
					double d31 = (double)j + 0.5D + ((double)this.level.rand.nextFloat() - 0.5D) * 2.0D;
					double d33 = (double)k + 0.5D + ((double)this.level.rand.nextFloat() - 0.5D) * 2.0D;
					double d34 = (double)l + 0.5D + ((double)this.level.rand.nextFloat() - 0.5D) * 2.0D;
					this.level.addParticle("smoke", d31, d33, d34, 0.0D, 0.0D, 0.0D);
					this.level.addParticle("flame", d31, d33, d34, 0.0D, 0.0D, 0.0D);
				}

				break;
		}
		ci.cancel();
	}
}
