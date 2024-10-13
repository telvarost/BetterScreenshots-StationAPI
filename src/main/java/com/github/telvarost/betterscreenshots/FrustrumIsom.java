package com.github.telvarost.betterscreenshots;

import net.minecraft.client.render.FrustumCuller;
import net.minecraft.util.math.Box;

public class FrustrumIsom extends FrustumCuller {
    public boolean intersectsFrustrum(double d1, double d3, double d5, double d7, double d9, double d11) {
        return true;
    }

    public boolean isVisible(Box axisAlignedBB1) {
        return true;
    }
}