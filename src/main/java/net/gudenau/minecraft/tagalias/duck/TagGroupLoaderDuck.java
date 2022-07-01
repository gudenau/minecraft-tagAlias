package net.gudenau.minecraft.tagalias.duck;

import net.minecraft.util.registry.RegistryKey;

public interface TagGroupLoaderDuck<T> {
    void setRegistry(RegistryKey<T> registry);
}
