package com.github.telvarost.betterscreenshots.mixin;

import com.github.telvarost.betterscreenshots.Config;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.BlockView;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class BedrockMixin {
    @Shadow @Final public int id;

    @Shadow @Final public static Block BEDROCK;

    public BedrockMixin(int i, int j) {
    }

    @Inject(
            method = "isSideVisible",
            at = @At("HEAD"),
            cancellable = true
    )
    @Environment(EnvType.CLIENT)
    public void isSideRendered(BlockView arg, int i, int j, int k, int l, CallbackInfoReturnable<Boolean> cir) {
        if (BEDROCK.id == this.id) {
            PlayerEntity player = PlayerHelper.getPlayerFromGame();

            if (  (null != player)
               && (-1 == player.dimensionId)
               && (Config.config.disableRenderingNetherBedrock)
            ) {
                cir.setReturnValue(false);
            }
        }
    }
}