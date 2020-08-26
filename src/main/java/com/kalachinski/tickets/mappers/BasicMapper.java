package com.kalachinski.tickets.mappers;

public interface BasicMapper<E, D> {
    D convertToDto(E entity);

    E convertToEntity(D entityDto);
}
