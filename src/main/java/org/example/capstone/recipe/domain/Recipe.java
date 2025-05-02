package org.example.capstone.recipe.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.capstone.ingredient.domain.Ingredient;
import org.example.capstone.nutrition.domain.Nutrition;
import org.example.capstone.user.domain.User;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @OneToMany
    @JoinColumn(name = "ingredient_id")
    private List<Ingredient> ingredients;

    @OneToMany
    @JoinColumn(name = "instruction_id")
    private List<Instruction> instructions;

    @OneToOne
    @JoinColumn(name = "nurition_id")
    private Nutrition nutrition;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
