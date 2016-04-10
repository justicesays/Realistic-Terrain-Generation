package teamrtg.rtg.world.gen.deco;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.terraingen.TerrainGen;
import teamrtg.rtg.util.noise.CellNoise;
import teamrtg.rtg.util.noise.OpenSimplexNoise;
import teamrtg.rtg.world.gen.RealisticBiomeGenerator;
import teamrtg.rtg.world.gen.feature.WorldGenGrass;

import java.util.Random;

import static net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.GRASS;

/**
 * @author WhichOnesPink
 */
public class DecoGrassDoubleTallgrass extends DecoBase {

    public float strengthFactor;
    public int maxY;
    public int loops;
    public int grassChance;
    public int doubleGrassChance;

    public DecoGrassDoubleTallgrass() {
        super();

        /**
         * Default values.
         * These can be overridden when configuring the Deco object in the realistic biome.
         */
        this.maxY = 255; // No height limit by default.
        this.strengthFactor = 0f; // Not sure why it was done like this, but... the higher the value, the more there will be.
        this.loops = 1;
        this.grassChance = 0; // 50% chance for both grass & double grass by default.
        this.doubleGrassChance = 0; // 50% chance for both grass & double grass by default. (If set, overrides grass chance.)

        this.addDecoTypes(DecoType.GRASS, DecoType.GRASS_DOUBLE);
    }

    @Override
    public void generate(RealisticBiomeGenerator biomeGenerator, World world, Random rand, int chunkX, int chunkY, OpenSimplexNoise simplex, CellNoise cell, float strength, float river) {

        if (this.allowed) {

            if (TerrainGen.decorate(world, rand, new BlockPos(chunkX, 0, chunkY), GRASS)) {

                this.loops = (this.strengthFactor > 0f) ? (int) (this.strengthFactor * strength) : this.loops;
                for (int i = 0; i < this.loops; i++) {
                    int intX = chunkX + rand.nextInt(16) + 8;
                    int intY = rand.nextInt(this.maxY);
                    int intZ = chunkY + rand.nextInt(16) + 8;

                    if (intY <= this.maxY) {

                        if (this.doubleGrassChance > 0) {

                            if (rand.nextInt(this.doubleGrassChance) == 0) {

                                (new WorldGenGrass(Blocks.DOUBLE_PLANT, 2)).generate(world, rand, intX, intY, intZ);
                            } else {

                                (new WorldGenGrass(Blocks.TALLGRASS, 1)).generate(world, rand, intX, intY, intZ);
                            }
                        } else if (this.grassChance > 0) {

                            if (rand.nextInt(this.grassChance) == 0) {

                                (new WorldGenGrass(Blocks.TALLGRASS, 1)).generate(world, rand, intX, intY, intZ);
                            } else {

                                (new WorldGenGrass(Blocks.DOUBLE_PLANT, 2)).generate(world, rand, intX, intY, intZ);
                            }
                        } else {

                            if (rand.nextBoolean()) {

                                (new WorldGenGrass(Blocks.TALLGRASS, 1)).generate(world, rand, intX, intY, intZ);
                            } else {

                                (new WorldGenGrass(Blocks.DOUBLE_PLANT, 2)).generate(world, rand, intX, intY, intZ);
                            }
                        }
                    }
                }
            }
        }
    }
}
