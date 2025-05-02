package org.example.capstone.ingredient.service;


import lombok.RequiredArgsConstructor;
import org.example.capstone.ingredient.repository.IngredientRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IngredientService {

    private final IngredientRepository ingredientRepository;


}
