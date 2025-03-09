package com.example.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor //Genera automatiacamente un constructor para todos los campos de la clase
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true) //Genera un constructor por defecto
public class Ingredient {

    private String id;
    private String name;
    private Type type;

    public enum Type {
        WRAP, PROTEIN, VEGGIES, CHEESE, SAUCE
    }
}
