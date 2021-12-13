package ru.gb.antonov.j710.monolith.entities;

@FunctionalInterface
public interface Buildable<T> {
    T build ();
}
