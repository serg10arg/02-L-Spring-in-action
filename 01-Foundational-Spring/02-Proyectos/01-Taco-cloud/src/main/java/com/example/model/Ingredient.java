package com.example.model;

import lombok.Data;

import java.lang.reflect.Type;

@Data
public class Ingredient {
    private final String id;
    private final String name;
    private final Type type;

    public enum Type implements java.lang.reflect.Type {
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }
}
