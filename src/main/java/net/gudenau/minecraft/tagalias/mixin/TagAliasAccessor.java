package net.gudenau.minecraft.tagalias.mixin;

import net.gudenau.minecraft.tagalias.TagAlias;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Collection;
import java.util.Map;

// Dirty yes, do I care, no.
@Mixin(TagAlias.class)
interface TagAliasAccessor {
    @Invoker static <T> void invokeAliasTags(RegistryKey<T> registry, Map<Identifier, Collection<T>> tagMap) {}
}
