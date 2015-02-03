/*
 * Copyright (c) 2014, Lukas Tenbrink.
 * http://lukas.axxim.net
 */

package ivorius.pandorasbox.effects;

import ivorius.pandorasbox.entitites.EntityPandorasBox;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.Random;

/**
 * Created by lukas on 30.03.14.
 */
public class PBEffectGenReplace extends PBEffectGenerate
{
    public Block[] blocks;
    public Block[] blocksToReplace;

    public PBEffectGenReplace()
    {
    }

    public PBEffectGenReplace(int time, double range, int unifiedSeed, Block[] blocks, Block[] blocksToReplace)
    {
        super(time, range, 1, unifiedSeed);

        this.blocks = blocks;
        this.blocksToReplace = blocksToReplace;
    }

    @Override
    public void generateOnBlock(World world, EntityPandorasBox entity, Vec3 effectCenter, Random random, int pass, BlockPos pos, double range)
    {
        if (!world.isRemote)
        {
            Block newBlock = blocks[random.nextInt(blocks.length)];
            Block prevBlock = world.getBlockState(pos).getBlock();
            boolean replace = false;
            for (Block block : blocksToReplace)
            {
                if (prevBlock == block)
                {
                    replace = true;
                }
            }

            if (replace)
            {
                setBlockVarying(world, pos, newBlock, unifiedSeed);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        setNBTBlocks("blocks", blocks, compound);
        setNBTBlocks("blocksToReplace", blocksToReplace, compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);

        blocks = getNBTBlocks("blocks", compound);
        blocksToReplace = getNBTBlocks("blocksToReplace", compound);
    }
}
