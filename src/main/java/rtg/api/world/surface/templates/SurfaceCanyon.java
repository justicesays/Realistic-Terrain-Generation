package rtg.api.world.surface.templates;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;

import rtg.api.config.BiomeConfig;
import rtg.api.util.CanyonColour;
import rtg.api.util.CliffCalculator;
import rtg.api.world.RTGWorld;
import rtg.api.world.surface.SurfaceBase;

public class SurfaceCanyon extends SurfaceBase {

    private int grassRaise = 0;

    public SurfaceCanyon(BiomeConfig config, IBlockState top, IBlockState fill, int grassHeight) {

        super(config, top, fill);
        grassRaise = grassHeight;
    }

    @Override
    public void paintTerrain(ChunkPrimer primer, int i, int j, int x, int z, int depth, RTGWorld rtgWorld, float[] noise, float river, Biome[] base) {

        Random rand = rtgWorld.rand;
        float c = CliffCalculator.calc(x, z, noise);
        boolean cliff = c > 1.3f;

        for (int k = 255; k > -1; k--) {
            Block b = primer.getBlockState(x, k, z).getBlock();
            if (b == Blocks.AIR) {
                depth = -1;
            }
            else if (b == Blocks.STONE) {
                depth++;

                if (depth > -1 && depth < 12) {
                    if (cliff) {
                        primer.setBlockState(x, k, z, CanyonColour.MESA.getBlockForHeight(i, k, j));
                    }
                    else {
                        if (depth > 4) {
                            primer.setBlockState(x, k, z, CanyonColour.MESA.getBlockForHeight(i, k, j));
                        }
                        else if (k > 74 + grassRaise) {
                            if (rand.nextInt(5) == 0) {
                                primer.setBlockState(x, k, z, Blocks.DIRT.getDefaultState());
                            }
                            else {
                                if (depth == 0) {
                                    primer.setBlockState(x, k, z, topBlock);
                                }
                                else {
                                    primer.setBlockState(x, k, z, fillerBlock);
                                }
                            }
                        }
                        else if (k < 62) {
                            primer.setBlockState(x, k, z, Blocks.DIRT.getDefaultState());
                        }
                        else if (k < 62 + grassRaise) {
                            if (depth == 0) {
                                primer.setBlockState(x, k, z, Blocks.GRASS.getDefaultState());
                            }
                            else {
                                primer.setBlockState(x, k, z, Blocks.DIRT.getDefaultState());
                            }
                        }
                        else if (k < 75 + grassRaise) {
                            if (depth == 0) {
                                int r = (int) ((k - (62 + grassRaise)) / 2f);
                                if (rand.nextInt(r + 1) == 0) {
                                    primer.setBlockState(x, k, z, Blocks.GRASS.getDefaultState());
                                }
                                else if (rand.nextInt((int) (r / 2f) + 1) == 0) {
                                    primer.setBlockState(x, k, z, Blocks.DIRT.getDefaultState());
                                }
                                else {
                                    primer.setBlockState(x, k, z, topBlock);
                                }
                            }
                            else {
                                primer.setBlockState(x, k, z, fillerBlock);
                            }
                        }
                        else {
                            if (depth == 0) {
                                primer.setBlockState(x, k, z, topBlock);
                            }
                            else {
                                primer.setBlockState(x, k, z, fillerBlock);
                            }
                        }
                    }
                }
                else if (k > 63) {
                    primer.setBlockState(x, k, z, CanyonColour.MESA.getBlockForHeight(i, k, j));
                }
            }
        }
    }
}
