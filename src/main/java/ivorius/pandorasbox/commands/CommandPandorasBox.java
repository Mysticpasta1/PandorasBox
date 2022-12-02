/*
 * Copyright (c) 2014, Lukas Tenbrink.
 * http://lukas.axxim.net
 */

package ivorius.pandorasbox.commands;

import ivorius.pandorasbox.effectcreators.PBECRegistry;
import ivorius.pandorasbox.entitites.EntityPandorasBox;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import java.util.Objects;

public class CommandPandorasBox extends CommandBase
{
    @Override
    public String getName()
    {
        return "pandora";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.pandora.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length < 2) {
            throw new WrongUsageException("commands.pandora.usage.noArgs");
        }
        Entity player;
        player = getEntity(server, sender, args[0]);

        EntityPandorasBox box;

        String effectName = args[1];

        if (effectName != null)
        {
            box = PBECRegistry.spawnPandorasBox(player.world, player.getEntityWorld().rand, effectName, player);

            if (box != null) box.setCanGenerateMoreEffectsAfterwards(false);
        }
        else box = PBECRegistry.spawnPandorasBox(player.world, player.getEntityWorld().rand, true, player);

        if (box != null)
        {
            if (args.length == 3) {
                if(Objects.equals(args[2], "true")) {
                    box.setInvisible(true);
                    box.stopFloating();
                }
            }
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
}
