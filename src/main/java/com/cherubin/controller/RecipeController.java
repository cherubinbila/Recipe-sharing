package com.cherubin.controller;

import com.cherubin.model.Recipe;
import com.cherubin.model.User;
import com.cherubin.repository.UserRepository;
import com.cherubin.services.RecipeService;
import com.cherubin.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private UserService userService;

    @PostMapping
    public Recipe createRecipe(@RequestBody Recipe recipe, @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserByJwt(jwt);

        Recipe createdRecipe = recipeService.createRecipe(recipe, user);
        return createdRecipe;
    }

    @PutMapping("/{id}")
    public Recipe updateRecipe(@RequestBody Recipe recipe, @PathVariable Long id) throws Exception {


        Recipe updateRecipe = recipeService.updateRecipe(recipe, id);
        return updateRecipe;
    }

    @GetMapping
    public List<Recipe> getAllRecipe() throws Exception {


        List<Recipe> Recipes = recipeService.findAllRecipes();
        return Recipes;
    }

    @DeleteMapping("/{recipeId}")
    public String deleteRecipe(@PathVariable Long recipeId) throws Exception {
        recipeService.deleteRecipe(recipeId);

        return "recette supprimer";
    }

    @PutMapping("/{id}/like/")
    public Recipe likeRecipe(@RequestHeader("Authorization") String jwt, @PathVariable Long id) throws Exception {

        User user = userService.findUserByJwt(jwt);

        Recipe updateRecipe = recipeService.likeRecipe(id, user);
        return updateRecipe;
    }
}
