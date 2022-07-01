package net.gudenau.minecraft.tagalias.mixin;

import net.gudenau.minecraft.tagalias.duck.TagGroupLoaderDuck;
import net.minecraft.resource.ResourceManager;
import net.minecraft.tag.TagGroupLoader;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.Map;

@Mixin(TagGroupLoader.class)
public abstract class TagGroupLoaderMixin<T> implements TagGroupLoaderDuck<T> {
    private RegistryKey<T> registry;
    
    @Override
    public void setRegistry(RegistryKey<T> registry) {
        this.registry = registry;
    }
    
    @Inject(
        method = "load",
        at = @At("RETURN")
    )
    private void load(ResourceManager manager, CallbackInfoReturnable<Map<Identifier, Collection<T>>> cir) {
        if(registry == null) {
            return;
        }
    
        TagAliasAccessor.invokeAliasTags(registry, cir.getReturnValue());
    }
}
