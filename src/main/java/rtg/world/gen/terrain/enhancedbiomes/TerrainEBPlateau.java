package rtg.world.gen.terrain.enhancedbiomes;

import rtg.util.CellNoise;
import rtg.util.OpenSimplexNoise;
import rtg.world.gen.terrain.TerrainBase;

public class TerrainEBPlateau extends TerrainBase
{
    private boolean booRiver;
    private float[] height;
    private int heightLength;
    private float strength;
    private float cWidth;
    private float cHeigth;
    private float cStrength;
    private float base;
    
    /*
     * Example parameters:
     * 
     * allowed to generate rivers?
     * riverGen = true
     * 
     * canyon jump heights
     * heightArray = new float[]{2.0f, 0.5f, 6.5f, 0.5f, 14.0f, 0.5f, 19.0f, 0.5f}
     * 
     * strength of canyon jump heights
     * heightStrength = 35f
     * 
     * canyon width (cliff to cliff)
     * canyonWidth = 160f
     * 
     * canyon heigth (total heigth)
     * canyonHeight = 60f
     * 
     * canyon strength
     * canyonStrength = 40f
     * 
     */
    public TerrainEBPlateau(boolean riverGen, float[] heightArryay, float heightStrength, float canyonWidth, float canyonHeight, float canyonStrength, float baseHeight)
    {
        booRiver = riverGen;
        height = heightArryay;
        strength = heightStrength;
        heightLength = height.length;
        cWidth = canyonWidth;
        cHeigth = canyonHeight;
        cStrength = canyonStrength;
        base = baseHeight;
    }

    @Override
    public float generateNoise(OpenSimplexNoise simplex, CellNoise cell, int x, int y, float border, float river)
    {
        return terrainCanyon(x, y, simplex, river, height, border, strength, heightLength, booRiver);
    }
}