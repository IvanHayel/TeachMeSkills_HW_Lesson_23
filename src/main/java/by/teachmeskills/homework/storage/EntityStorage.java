package by.teachmeskills.homework.storage;

import by.teachmeskills.homework.entity.Entity;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EntityStorage<T extends Entity> {
    private final Set<T> storage = new HashSet<>();

    public List<T> getAll() {
        return new ArrayList<>(storage);
    }

    public T getById(@NonNull Integer id) {
        return storage.stream()
                .filter(entity -> entity.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public boolean save(T entity) {
        return storage.add(entity);
    }

    public boolean isContains(@NonNull Integer id) {
        return storage.stream()
                .mapToInt(T::getId)
                .anyMatch(id::equals);
    }

    public boolean isContains(@NonNull T entity) {
        return storage.stream()
                .anyMatch(entity::equals);
    }

    public T removeById(@NonNull Integer id) {
        T entityToRemove = getById(id);
        return storage.remove(entityToRemove) ? entityToRemove : null;
    }

    public boolean removeAll() {
        List<T> entities = new ArrayList<>(storage);
        return removeAll(entities);
    }

    public boolean removeAll(@NonNull List<T> entities) {
        return storage.removeAll(entities);
    }
}