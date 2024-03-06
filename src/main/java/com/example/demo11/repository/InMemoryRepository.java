package com.example.demo11.repository;


import com.example.demo11.domain.Entity;
import com.example.demo11.validator.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryRepository <ID, E extends Entity<ID>> implements Repository<ID,E> {
    private Validator<E> validator;
    Map<ID, E> entities;

    /***
     * Constructor pentru Repository
     * @param validator: obiect de tip validator ce valideaza datele
     */
    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities = new HashMap<ID, E>();
    }

    /***
     *  Gaseste un utilizator dupa id
     * @param id -the id of the entity to be returned
     *           id must not be null
     * @return: obiect Utilizator
     */
    @Override
    public Optional<E> findOne(ID id) {
        if (id == null)
            throw new IllegalArgumentException("id must be not null");

        return Optional.ofNullable(entities.get(id));
    }

    /**
     * returneaza toti utilizatorii
     * @return: map de tipul id, entity
     */
    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    /***
     *  Salveaza o entitate
     * @param entity
     *         entity must be not null
     * @return: E entity
     */
    @Override
    public Optional<E> save(E entity) {
        if (entity == null)
            throw new IllegalArgumentException("entity must be not null");
        validator.validate(entity);
//        if (entities.get(entity.getId()) != null) {
//            return entity;
//        }
//        else {
//            entities.put(entity.getId(), entity);
//        }
//        return null;
        return Optional.ofNullable(entities.putIfAbsent(entity.getId(), entity));

    }

    /***
     * Sterge o entitate
     * @param id
     *      id must be not null
     * @return: entitatea stearsa
     */
    @Override
    public Optional<E> delete(ID id) {
        if(this.findOne(id) == null)
            throw new IllegalArgumentException("Nu exista utilizator cu acest id!");
        return Optional.ofNullable(entities.remove(id));
    }

    /***
     * Actualizeaza o entitate
     * @param entity
     *          entity must not be null
     * @return entitatea actualizata
     */
    @Override
    public Optional<E> update(E entity) {

        if (entity == null)
            throw new IllegalArgumentException("entity must be not null!");
        validator.validate(entity);

        entities.put(entity.getId(), entity);

        return Optional.ofNullable(entities.computeIfPresent(entity.getId(), (k,v) -> entity));

    }

}
