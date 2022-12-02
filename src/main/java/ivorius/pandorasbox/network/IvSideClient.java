package ivorius.pandorasbox.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class IvSideClient
{
    @SideOnly(Side.CLIENT)
    public static EntityPlayer getClientPlayer()
    {
        return Minecraft.getMinecraft().player;
    }
    @SideOnly(Side.CLIENT)
    public static World getClientWorld()
    {
        return Minecraft.getMinecraft().world;
    }
}