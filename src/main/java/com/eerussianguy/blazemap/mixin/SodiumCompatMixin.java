package com.eerussianguy.blazemap.mixin;

import com.eerussianguy.blazemap.engine.client.BlazeMapClientEngine;
import com.eerussianguy.blazemap.feature.MDSources;
import com.eerussianguy.blazemap.profiling.Profilers;
import me.jellysquid.mods.sodium.client.render.chunk.lists.ChunkRenderList;
import net.minecraft.world.level.ChunkPos;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkRenderList.class)
public class SodiumCompatMixin {

    @Inject(method = "add", at = @At("HEAD"), remap = false)
    void onAdd(RenderSection render, CallbackInfo ci) {
        Profilers.Client.Mixin.SODIUM_LOAD_PROFILER.hit();
        Profilers.Client.Mixin.SODIUM_TIME_PROFILER.begin();

        BlazeMapClientEngine.onChunkChanged(new ChunkPos(render.getChunkX(), render.getChunkZ()), MDSources.Client.SODIUM);

        Profilers.Client.Mixin.SODIUM_TIME_PROFILER.end();
    }
}