package de.dhbw.swe.betterjeweled.core;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.List;
import java.util.Map;

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
        region1 = CrystalRegion.merge(region1, region2);

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
        region1 = CrystalRegion.merge(region1, region2);

        CrystalRegion region3 = new CrystalRegion(3, 0, 4, 4);
        CrystalRegion region4 = new CrystalRegion(3, 3, 6, 4);
        assertTrue(region3.intersects(region4));
        region3 = CrystalRegion.merge(region3, region4);

        assertTrue(region1.intersects(region3));
    }

    private CrystalGrid triggerGridWithOverlap()
    {
        CrystalGrid grid = new CrystalGrid(5, 5, RED, GREEN, BLUE);
        grid.setCrystal(0,0, RED);
        grid.setCrystal(0,1, RED);
        grid.setCrystal(0,2, RED);
        grid.setCrystal(0,3, GREEN);
        grid.setCrystal(0,4, GREEN);

        grid.setCrystal(1,0, RED);
        grid.setCrystal(1,1, BLUE);
        grid.setCrystal(1,2, GREEN);
        grid.setCrystal(1,3, BLUE);
        grid.setCrystal(1,4, GREEN);

        grid.setCrystal(2,0, RED);
        grid.setCrystal(2,1, RED);
        grid.setCrystal(2,2, RED);
        grid.setCrystal(2,3, RED);
        grid.setCrystal(2,4, GREEN);

        return grid;
    }

    /**
     * Chain overlaps refer to, for example,
     * Region 1 overlaps with region 2, and region 2 overlaps with region 3.
     * These Regions should merge into one.
     */
    @Test
    void testMergingChainOverlaps() {
        DefaultRegionFinder finder = new DefaultRegionFinder();

        MergedRegionFinder mergeFinder = new MergedRegionFinder(finder);

        Map<Crystal, List<CrystalRegion>> regions = mergeFinder.findRegions(triggerGridWithOverlap(), new Crystal(Color.RED));
        assertEquals(1, regions.size());

        java.util.List<CrystalRegion> redRegions = regions.get(new Crystal(Color.RED));
        assertEquals(1, redRegions.size());

        CrystalRegion mergedRegion = redRegions.get(0);
        assertEquals(7, mergedRegion.getSize());
    }
}
