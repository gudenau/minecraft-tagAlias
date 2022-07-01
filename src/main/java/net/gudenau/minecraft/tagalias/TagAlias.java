package net.gudenau.minecraft.tagalias;

import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;

import java.util.*;
import java.util.stream.Collectors;

public final class TagAlias {
	private static final Map<RegistryKey<?>, Set<Set<Identifier>>> ALIASES = new HashMap<>();
	private static volatile boolean locked = false;
	
	@SafeVarargs
	public static <T> void registerAliases(TagKey<T>... tags) {
		Objects.requireNonNull(tags, "tags can't be null");
		
		registerAliases(Set.of(tags));
	}
	
	public static <T> void registerAliases(Collection<TagKey<T>> tags) {
		Objects.requireNonNull(tags, "tags can't be null");
		
		if (tags.size() <= 1) {
			return;
		}
		
		var registry = tags.stream().findAny().get().registry();
		if (tags.stream().anyMatch((tag) -> tag.registry() != registry)) {
			throw new IllegalArgumentException("Tags don't share a registry");
		}
		
		registerAliases(registry, tags.stream().map(TagKey::id).collect(Collectors.toUnmodifiableSet()));
	}
	
	private static void registerAliases(RegistryKey<?> registry, Collection<Identifier> aliases) {
		if (locked) {
			throw new IllegalStateException("Attempted to register alias too late");
		}
		
		var aliasSetSet = ALIASES.computeIfAbsent(registry, (key) -> new HashSet<>());
		var newAliases = new HashSet<>(aliases);
		
		var iterator = aliasSetSet.iterator();
		while (iterator.hasNext()) {
			var set = iterator.next();
			for (var id : newAliases) {
				if (set.contains(id)) {
					iterator.remove();
					newAliases.addAll(set);
					break;
				}
			}
		}
		
		aliasSetSet.add(newAliases);
	}
	
	@SuppressWarnings("unused")
	private static <T> void aliasTags(RegistryKey<T> registry, Map<Identifier, Collection<T>> tagMap) {
		locked = true;
		
		var aliases = ALIASES.get(registry);
		if (aliases == null) {
			return;
		}
		
		for (Set<Identifier> alias : aliases) {
			Set<T> values = new HashSet<>();
			
			for (var identifier : alias) {
				var tags = tagMap.get(identifier);
				if (tags != null) {
					values.addAll(tags);
				}
			}
			
			for (var identifier : alias) {
				tagMap.put(identifier, new HashSet<>(values));
			}
		}
	}
}
