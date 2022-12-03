/*
 * Copyright (c) 2014, Lukas Tenbrink.
 * http://lukas.axxim.net
 */

package ivorius.pandorasbox;

import ivorius.pandorasbox.block.BlockPandorasBox;
import ivorius.pandorasbox.block.PBBlocks;
import ivorius.pandorasbox.block.TileEntityPandorasBox;
import ivorius.pandorasbox.commands.CommandPandorasBox;
import ivorius.pandorasbox.effects.PBEffects;
import ivorius.pandorasbox.entitites.EntityPandorasBox;
import ivorius.pandorasbox.entitites.PBEntityList;
import ivorius.pandorasbox.events.PBEventHandler;
import ivorius.pandorasbox.items.ItemPandorasBox;
import ivorius.pandorasbox.network.PacketEntityData;
import ivorius.pandorasbox.network.PacketEntityDataHandler;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Logger;

import static ivorius.pandorasbox.crafting.OreDictionaryConstants.*;

@Mod("pandorasbox")
@Mod.EventBusSubscriber(value = "pandorasbox", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PandorasBox
{
    public static final String MOD_ID = "pandorasbox";
    public static PBProxy proxy;
    public static String basePath = "pandorasbox:";

    public static Logger logger;
    public static Configuration config;

    public static SimpleNetworkWrapper network;

    public static PBEventHandler fmlEventHandler;

    public static void register(Block block, ResourceLocation id, BlockItem item)
    {
        ForgeRegistries.BLOCKS.register(id, block);
        ForgeRegistries.ITEMS.register(id, item);
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();

        config = new Configuration(event.getSuggestedConfigurationFile());
        config.load();
        PBConfig.loadConfig(null);
        config.save();

        fmlEventHandler = new PBEventHandler();
        fmlEventHandler.register();

        PBBlocks.pandorasBox = new BlockPandorasBox(BlockBehaviour.Properties.of(Material.WOOD).strength(0.5f));
        register(PBBlocks.pandorasBox, new ResourceLocation(PandorasBox.MOD_ID, "pandorasBox"), new ItemPandorasBox(PBBlocks.pandorasBox, new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
        ForgeRegistries.ENTITY_TYPES.register(new ResourceLocation(PandorasBox.MOD_ID, "pandoras_box"), EntityType.Builder.<EntityPandorasBox>of(EntityPandorasBox::new, MobCategory.MISC).sized(0.6F, 0.4F).clientTrackingRange(16).updateInterval(Integer.MAX_VALUE).build("pandoras_box"));
        GameRegistry.registerTileEntity(TileEntityPandorasBox.class, new ResourceLocation(PandorasBox.MOD_ID, "pandorasBox"));

        EntityRegistry.registerModEntity(new ResourceLocation(PandorasBox.MOD_ID, "pandorasBox"), EntityPandorasBox.class, "pandorasBox", PBEntityList.pandorasBoxEntityID, this, 256, 20, true);

        proxy.preInit();
        PBEffects.registerEffects();
    }

    @EventHandler
    public void load(FMLInitializationEvent event)
    {
        network = NetworkRegistry.INSTANCE.newSimpleChannel(MOD_ID);
        PandorasBox.network.registerMessage(PacketEntityDataHandler.class, PacketEntityData.class, 1, Side.CLIENT);

        PBEffects.registerEffectCreators();

        if (PBConfig.allowCrafting)
        {
            GameRegistry.addShapedRecipe(new ResourceLocation(PandorasBox.MOD_ID, "pandorasbox"), null, new ItemStack(PBBlocks.pandorasBox),
                    "GRG",
                    "ROR",
                    "#R#",
                    'G', DC_GOLD_INGOT,
                    '#', DC_PLANK_WOOD,
                    'R', DC_REDSTONE_DUST,
                    'O', Items.ENDER_PEARL);
        }

        proxy.load();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {

    }

    @EventHandler
    public void serverStarting(FMLServerStartingEvent evt)
    {
        evt.registerServerCommand(new CommandPandorasBox());
    }
}