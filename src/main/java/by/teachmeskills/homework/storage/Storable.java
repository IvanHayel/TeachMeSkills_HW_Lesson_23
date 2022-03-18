package by.teachmeskills.homework.storage;

import by.teachmeskills.homework.entity.Entity;

import java.util.List;

// TODO: bad name
public interface Storable<K, T extends Entity> {
    List<T> getAll();

    T getByKey(K key);

    boolean save(T entity);

    // TODO: isContains
    boolean contains(K key);

    // TODO: only by id. You can allow to find a user by several params. But removing only by id.
    T removeByKey(K key);

    // TODO: if you remove them. Why do you need them? And why you allow to do something with removed entities?
    List<T> removeAll();

    List<T> removeAll(List<T> entities);
}