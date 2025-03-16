package com.example.interfaces;

import org.springframework.data.repository.CrudRepository;

import com.example.model.Ingredient;


public interface IngredientRepository
        extends CrudRepository<Ingredient, String> {

}