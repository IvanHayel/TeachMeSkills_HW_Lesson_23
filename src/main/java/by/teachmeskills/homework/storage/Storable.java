package by.teachmeskills.homework.storage;

import by.teachmeskills.homework.entity.Entity;

import java.util.List;

public interface Storable<K, T extends Entity> {
    List<T> getAll();

    T getByKey(K key);

    boolean save(T entity);

    boolean contains(K key);

    T removeByKey(K key);

    List<T> removeAll();

    List<T> removeAll(List<T> entities);
}