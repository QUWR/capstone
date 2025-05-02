package org.example.capstone.recipe.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeRequest {

    private String name;
    private String description;
    private String instructions;
    private Integer cookingTime;
    private Integer servings;
    private List<?> ingredients;
}
