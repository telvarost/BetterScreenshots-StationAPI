package com.github.telvarost.betterscreenshots.mixin;

import com.github.telvarost.betterscreenshots.Config;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.level.BlockView;
import net.modificationstation.stationapi.api.entity.player.PlayerHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBase.class)
public class BedrockMixin {
    @Shadow @Final public int id;

    @Shadow @Final public static BlockBase BEDROCK;

    public BedrockMixin(int i, int j) {
    }

    @Inject(
            method = "isSideRendered",
            at = @At("HEAD"),
            cancellable = true
    )
    @Environment(EnvType.CLIENT)
    public void isSideRendered(BlockView arg, int i, int j, int k, int l, CallbackInfoReturnable<Boolean> cir) {
        if (BEDROCK.id == this.id) {
            PlayerBase player = PlayerHelper.getPlayerFromGame();

            if (  (null != player)
               && (-1 == player.dimensionId)
               && (Config.ConfigFields.disableRenderingNetherBedrock)
            ) {
                cir.setReturnValue(false);
            }
        }
    }
}