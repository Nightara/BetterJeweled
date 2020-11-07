package de.dhbw.swe.betterjeweled.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MergedRegionsTest {

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
        MergedCrystalRegion region1 = new MergedCrystalRegion(1, 1, 2, 4);
        CrystalRegion region2 = new CrystalRegion(1, 1, 4, 2);
        region1.addOverlappingRegion(region2);

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
        MergedCrystalRegion region1 = new MergedCrystalRegion(1, 1, 2, 4);
        CrystalRegion region2 = new CrystalRegion(1, 1, 4, 2);
        assertTrue(region1.intersects(region2));
        region1.addOverlappingRegion(region2);

        MergedCrystalRegion region3 = new MergedCrystalRegion(3, 0, 4, 4);
        CrystalRegion region4 = new CrystalRegion(3, 3, 6, 4);
        assertTrue(region3.intersects(region4));
        region3.addOverlappingRegion(region4);

        assertTrue(region1.intersects(region3));
    }

}
