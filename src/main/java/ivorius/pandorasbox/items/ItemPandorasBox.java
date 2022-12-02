/*
 * Copyright (c) 2014, Lukas Tenbrink.
 * http://lukas.axxim.net
 */

package ivorius.pandorasbox.items;

import ivorius.pandorasbox.effectcreators.PBECRegistry;
import ivorius.pandorasbox.entitites.EntityPandorasBox;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class ItemPandorasBox extends BlockItem
{
    public ItemPandorasBox(Block block, Item.Properties properties)
    {
        super(block, properties);
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemStackIn = pPlayer.getItemInHand(pUsedHand);

        executeRandomEffect(pLevel, pPlayer);

        itemStackIn.shrink(1);
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    public static EntityPandorasBox executeRandomEffect(Level world, LivingEntity entity)
    {
        return PBECRegistry.spawnPandorasBox(world, entity.getRandom(), true, entity);
    }
}
