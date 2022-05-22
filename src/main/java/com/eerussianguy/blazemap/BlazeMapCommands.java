package com.eerussianguy.blazemap;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraftforge.server.command.EnumArgument;

import com.eerussianguy.blazemap.api.BlazeMapAPI;
import com.eerussianguy.blazemap.api.BlazeRegistry;
import com.eerussianguy.blazemap.api.mapping.MapType;
import com.eerussianguy.blazemap.feature.maps.MinimapRenderer;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

public class BlazeMapCommands {
    private static final EnumArgument<MinimapRenderer.MinimapSize> MINIMAP_SIZE = EnumArgument.enumArgument(MinimapRenderer.MinimapSize.class);

    public static LiteralArgumentBuilder<CommandSourceStack> create() {
        return Commands.literal("blazemap")
            .then(createDebug())
            .then(createMinimap());
    }

    private static LiteralArgumentBuilder<CommandSourceStack> createDebug() {
        return Commands.literal("debug")
            .then(Commands.literal("on").executes($ -> {
                MinimapRenderer.INSTANCE.setDebugEnabled(true);
                return Command.SINGLE_SUCCESS;
            }))
            .then(Commands.literal("off").executes($ -> {
                MinimapRenderer.INSTANCE.setDebugEnabled(false);
                return Command.SINGLE_SUCCESS;
            }));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> createMinimap() {
        return Commands.literal("minimap")
            .then(minimapSize())
            .then(minimapType());

            /*
            .then(minimapZoom())
             */
    }

    private static LiteralArgumentBuilder<CommandSourceStack> minimapSize() {
        return Commands.literal("size")
            .then(Commands.argument("value", MINIMAP_SIZE)
                .executes(cmd -> {
                    MinimapRenderer.MinimapSize size = cmd.getArgument("value", MinimapRenderer.MinimapSize.class);
                    MinimapRenderer.INSTANCE.setMapSize(size);
                    return Command.SINGLE_SUCCESS;
                })
            );
    }

    private static LiteralArgumentBuilder<CommandSourceStack> minimapType() {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("type");

        for(BlazeRegistry.Key<MapType> key : BlazeMapAPI.MAPTYPES.keys()) {
            final MapType type = BlazeMapAPI.MAPTYPES.get(key);
            builder.then(Commands.literal(key.toString()).executes($ -> {
                MinimapRenderer.INSTANCE.setMapType(type);
                return Command.SINGLE_SUCCESS;
            }));
        }

        return builder;
    }

    private static LiteralArgumentBuilder<CommandSourceStack> minimapZoom() {
        return Commands.literal("zoom")
            .then((ArgumentBuilder<CommandSourceStack, ?>) null);
    }
}