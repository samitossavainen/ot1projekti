package com.mokkikodit.tietokanta;

import java.util.List;

public interface CrudRepository<T, ID> {

    List<T> findAll();

    T findById(ID id);

    void save(T entity);

    void update(T entity);

    void delete(ID id);
}

