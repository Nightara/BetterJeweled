package de.dhbw.swe.betterjeweled.core;

import java.util.ArrayList;
import java.util.List;

public class MergedCrystalRegion extends CrystalRegion {

    private List<CrystalRegion> overlappingRegions;

    public MergedCrystalRegion(int startX, int startY, int endX, int endY) {
        super(startX, startY, endX, endY);
        overlappingRegions = new ArrayList<>();
    }

    public void addOverlappingRegion(CrystalRegion toAdd) {
        if(!overlappingRegions.contains(toAdd)) {
            overlappingRegions.add(toAdd);
        }
    }

    @Override
    public int getSize() {
        int size = super.getSize();
        for (CrystalRegion region:overlappingRegions) {
            size += region.getSize() - 1;
        }
        return size;
    }

    @Override
    public boolean contains(int posX, int posY) {
        boolean contains = super.contains(posX, posY);
        for (CrystalRegion region:overlappingRegions) {
            contains = contains || region.contains(posX, posY);
        }
        return contains;
    }

    @Override
    public boolean intersects(CrystalRegion other) {
        boolean intersects = super.intersects(other);
        for (CrystalRegion region:overlappingRegions) {
            intersects = intersects || region.intersects(other);
        }
        return intersects;
    }
}
