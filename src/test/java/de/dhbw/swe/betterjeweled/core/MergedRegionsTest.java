package de.dhbw.swe.betterjeweled.core;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class MergedRegionsTest {

    private static final Crystal RED = new Crystal(Color.RED);
    private static final Crystal GREEN = new Crystal(Color.GREEN);
    private static final Crystal BLUE = new Crystal(Color.BLUE);

    @Test
    void regionsIntersect() {
        CrystalRegion region1 = new CrystalRegion(0, 0, 1, 3);
        CrystalRegion region2 = new CrystalRegion(0, 0, 3, 1);

        assertTrue(region1.intersects(region2));
    }

    @Test
    void regionsDontIntersect() {
        CrystalRegion region1 = new CrystalRegion(0, 0, 1, 3);
        CrystalRegion region2 = new CrystalRegion(1, 1, 4, 2);

        assertFalse(region1.intersects(region2));
    }

    @Test
    void mergedRegionOverlapsNormalRegion() {
        CrystalRegion region1 = new CrystalRegion(1, 1, 2, 4);
        CrystalRegion region2 = new CrystalRegion(1, 1, 4, 2);
        region1.mergeWith(region2);

        CrystalRegion region3 = new CrystalRegion(3, 0, 4, 3);
        CrystalRegion region4 = new CrystalRegion(0, 3, 3, 4);
        CrystalRegion region5 = new CrystalRegion(0, 0, 1, 3);

        assertTrue(region1.intersects(region3));
        assertTrue(region3.intersects(region1));
        assertTrue(region1.intersects(region4));
        assertTrue(region4.intersects(region1));
        assertFalse(region1.intersects(region5));
        assertFalse(region5.intersects(region1));
    }

    @Test
    void mergedRegionOverlapsMergedRegion() {
        CrystalRegion region1 = new CrystalRegion(1, 1, 2, 4);
        CrystalRegion region2 = new CrystalRegion(1, 1, 4, 2);
        assertTrue(region1.intersects(region2));
        region1.mergeWith(region2);

        CrystalRegion region3 = new CrystalRegion(3, 0, 4, 4);
        CrystalRegion region4 = new CrystalRegion(3, 3, 6, 4);
        assertTrue(region3.intersects(region4));
        region3.mergeWith(region4);

        assertTrue(region1.intersects(region3));
    }
}
