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
public class PBEffectGenTransform extends PBEffectGenerate
{
    public Block[] blocks;

    public PBEffectGenTransform()
    {
    }

    public PBEffectGenTransform(int time, double range, int unifiedSeed, Block[] blocks)
    {
        super(time, range, 1, unifiedSeed);

        this.blocks = blocks;
    }

    @Override
    public void generateOnBlock(World world, EntityPandorasBox entity, Vec3 effectCenter, Random random, int pass, BlockPos pos, double range)
    {
        if (!world.isRemote)
        {
            Block block = blocks[random.nextInt(blocks.length)];

            if (world.isBlockNormalCube(pos, false))
            {
                setBlockVarying(world, pos, block, unifiedSeed);
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        setNBTBlocks("blocks", blocks, compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);

        blocks = getNBTBlocks("blocks", compound);
    }
}
