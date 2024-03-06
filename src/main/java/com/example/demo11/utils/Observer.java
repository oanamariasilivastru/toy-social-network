package com.example.demo11.utils;

public interface Observer<E extends Event>{
    void update(E e);
}
