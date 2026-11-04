package com.artillexstudios.axsellwands.hooks;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ExcellentShopLimitsTest {
    @Test void respectsLimitAndPartialStacks() {
        assertEquals(3, ExcellentShopLimits.allowedUnits(64, 1, 10, 7, -1, 0));
        assertEquals(0, ExcellentShopLimits.allowedUnits(64, 1, 10, 10, -1, 0));
    }
    @Test void handlesUnlimitedUnitSizesAndStock() {
        assertEquals(3, ExcellentShopLimits.allowedUnits(14, 4, -1, 0, -1, 0));
        assertEquals(2, ExcellentShopLimits.allowedUnits(64, 1, -1, 0, 2, 0));
        assertEquals(0, ExcellentShopLimits.allowedUnits(3, 4, -1, 0, -1, 0));
    }
    @Test void previewReservesAcrossStacksWithoutChangingLimit() {
        assertEquals(2, ExcellentShopLimits.allowedUnits(64, 1, 10, 5, 10, 3));
        assertEquals(0, ExcellentShopLimits.allowedUnits(64, 1, 10, 5, 10, 5));
    }
}
