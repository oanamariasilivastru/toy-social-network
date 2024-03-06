package com.example.demo11.validator;

public interface Validator<T>{
    void validate(T var1)throws ValidationException;
}
