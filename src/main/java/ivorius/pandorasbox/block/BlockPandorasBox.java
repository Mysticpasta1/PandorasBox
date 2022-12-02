/*
 * Copyright (c) 2014, Lukas Tenbrink.
 * http://lukas.axxim.net
 */

package ivorius.pandorasbox.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by lukas on 15.04.14.
 */
public class BlockPandorasBox extends Block
{
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;

    public BlockPandorasBox(BlockBehaviour.Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(AXIS, Direction.Axis.X));
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2)
    {
        return new TileEntityPandorasBox();
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return Block.box(0.2, 0.0, 0.2, 0.8, 0.6, 0.8);
    }

    @Override
    public InteractionResult use(BlockState pState, Level worldIn, BlockPos pos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        BlockEntity tileEntity = worldIn.getBlockEntity(pos);

        if (tileEntity instanceof TileEntityPandorasBox)
            ((TileEntityPandorasBox) tileEntity).spawnPandorasBox();

        worldIn.removeBlock(pos,false);

        return super.use(pState, worldIn, pos, pPlayer, pHand, pHit);
    }



    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        worldIn.setBlockState(pos, this.getDefaultState().withProperty(FACING_PROP, placer.getHorizontalFacing().getOpposite()), 2);
        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if (tileEntity instanceof TileEntityPandorasBox)
            ((TileEntityPandorasBox) tileEntity).setPartialRotationYaw(placer.rotationYaw % 90.0f);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING_PROP, EnumFacing.byHorizontalIndex(meta & 7));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int horizontalIndex = state.getValue(FACING_PROP).getHorizontalIndex();
        return horizontalIndex >= 0 && horizontalIndex < 4 ? horizontalIndex : 0;
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING_PROP);
    }
}
