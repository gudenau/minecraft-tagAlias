package net.gudenau.minecraft.tagalias.mixin;

import net.gudenau.minecraft.tagalias.duck.TagGroupLoaderDuck;
import net.minecraft.resource.ResourceManager;
import net.minecraft.tag.TagGroupLoader;
import net.minecraft.tag.TagManagerLoader;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(TagManagerLoader.class)
public abstract class TagManagerLoaderMixin {
    @Inject(
        method = "buildRequiredGroup",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/concurrent/CompletableFuture;supplyAsync(Ljava/util/function/Supplier;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;"
        ),
        locals = LocalCapture.CAPTURE_FAILEXCEPTION
    )
    private <T> void buildRequiredGroup(
        ResourceManager resourceManager, Executor prepareExecutor, DynamicRegistryManager.Entry<T> requirement,
        CallbackInfoReturnable<CompletableFuture<TagManagerLoader.RegistryTags<T>>> cir,
        RegistryKey<T> registryKey, Registry<T> registry, TagGroupLoader<T> tagGroupLoader
    ) {
        var loader = (TagGroupLoader<T> & TagGroupLoaderDuck<T>) tagGroupLoader;
        loader.setRegistry(registryKey);
    }
}
