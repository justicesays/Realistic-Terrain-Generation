package rtg.world.gen.surface;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.ChunkPrimer;

import rtg.api.biome.BiomeConfig;
import rtg.config.rtg.ConfigRTG;
import rtg.util.CellNoise;
import rtg.util.ModPresenceTester;
import rtg.util.OpenSimplexNoise;
import rtg.util.UBColumnCache;

public class SurfaceBase {

    private final static ModPresenceTester undergroundBiomesMod = new ModPresenceTester("UndergroundBiomes");
    // create UBColumnCache only if UB is present
    private static UBColumnCache ubColumnCache = undergroundBiomesMod.present() ? new UBColumnCache() : null;
    protected IBlockState topBlock;
    protected IBlockState fillerBlock;
    protected IBlockState cliffStoneBlock;
    protected IBlockState cliffCobbleBlock;
    protected BiomeConfig biomeConfig;

    public SurfaceBase(BiomeConfig config, Block top, byte topByte, Block fill, byte fillByte) {

        this(config, top.getStateFromMeta(topByte), fill.getStateFromMeta(fillByte));
    }

    public SurfaceBase(BiomeConfig config, Block top, Block fill) {

        this(config, top.getDefaultState(), fill.getDefaultState());
    }

    public SurfaceBase(BiomeConfig config, IBlockState top, IBlockState fill) {

        if (config == null) {
            throw new RuntimeException("Biome config in SurfaceBase is NULL.");
        }

        biomeConfig = config;

        topBlock = top;
        fillerBlock = fill;

        this.initCliffBlocks();

        this.assignUserConfigs(config, top, fill);
    }

    public void paintTerrain(ChunkPrimer primer, int i, int j, int x, int y, int depth, World world, Random rand, OpenSimplexNoise simplex, CellNoise cell, float[] noise, float river, BiomeGenBase[] base) {

    }

    protected IBlockState getShadowStoneBlock(World world, int i, int j, int x, int y, int k) {

        if ((undergroundBiomesMod.present()) && ConfigRTG.enableUBCStoneShadowing) {

            return Blocks.stone.getDefaultState();
        }
        else {

            return Block.getBlockFromName(ConfigRTG.shadowStoneBlockId).getStateFromMeta(ConfigRTG.shadowStoneBlockByte);
        }
    }

    protected IBlockState getShadowDesertBlock(World world, int i, int j, int x, int y, int k) {

        if ((undergroundBiomesMod.present()) && ConfigRTG.enableUBCDesertShadowing) {

            return Blocks.stone.getDefaultState();
        }
        else {

            return Block.getBlockFromName(ConfigRTG.shadowDesertBlockId).getStateFromMeta(ConfigRTG.shadowDesertBlockByte);
        }
    }

    protected IBlockState hcStone(World world, int i, int j, int x, int y, int k) {

            return cliffStoneBlock;
        
    }

    protected IBlockState hcCobble(World world, int worldX, int worldZ, int chunkX, int chunkZ, int worldY) {
     
            return cliffCobbleBlock;
    }

    public IBlockState getTopBlock() {

        return this.topBlock;
    }

    public IBlockState getFillerBlock() {

        return this.fillerBlock;
    }

    private void assignUserConfigs(BiomeConfig config, IBlockState top, IBlockState fill) {

        String userTopBlock = config._string(BiomeConfig.surfaceTopBlockId);
        String userTopBlockMeta = config._string(BiomeConfig.surfaceTopBlockMetaId);
        try {
            if (Block.getBlockFromName(userTopBlock) != null) {
                topBlock = Block.getBlockFromName(userTopBlock).getStateFromMeta(Byte.valueOf(userTopBlockMeta));
            }
            else {
                topBlock = top;
            }
        }
        catch (Exception e) {
            topBlock = top;
        }

        String userFillerBlock = config._string(BiomeConfig.surfaceFillerBlockId);
        String userFillerBlockMeta = config._string(BiomeConfig.surfaceFillerBlockMetaId);
        try {
            if (Block.getBlockFromName(userFillerBlock) != null) {
                fillerBlock = Block.getBlockFromName(userFillerBlock).getStateFromMeta(Integer.parseInt(userFillerBlockMeta));
            }
            else {
                fillerBlock = fill;
            }
        }
        catch (Exception e) {
            fillerBlock = fill;
        }
    }

    protected void initCliffBlocks() {

        cliffStoneBlock = getConfigBlock(
            biomeConfig,
            BiomeConfig.surfaceCliffStoneBlockId,
            BiomeConfig.surfaceCliffStoneBlockMetaId,
            Blocks.stone.getDefaultState()
        );

        cliffCobbleBlock = getConfigBlock(
            biomeConfig,
            BiomeConfig.surfaceCliffCobbleBlockId,
            BiomeConfig.surfaceCliffCobbleBlockMetaId,
            Blocks.cobblestone.getDefaultState()
        );
    }

    protected IBlockState getConfigBlock(BiomeConfig config, String propertyId, String propertyMeta, IBlockState blockDefault) {

        IBlockState blockReturn = blockDefault;
        String userBlockId = config._string(propertyId);
        String userBlockMeta = config._string(propertyMeta);

        try {
            if (Block.getBlockFromName(userBlockId) != null) {
                fillerBlock = Block.getBlockFromName(userBlockId).getStateFromMeta(Integer.parseInt(userBlockMeta));
            }
            else {
                blockReturn = blockDefault;
            }
        }
        catch (Exception e) {
            blockReturn = blockDefault;
        }

        return blockReturn;
    }
}
