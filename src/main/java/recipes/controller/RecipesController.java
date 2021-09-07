package recipes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import recipes.model.Recipe;
import recipes.model.RecipeService;
import recipes.model.User;
import recipes.model.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;


/**
 * The controller class provides interaction by receiving JSON.
 * Actions: Saving, deleting, searching and changing recipes in the database.
 * Controller http://localhost:8881/
 */
@RestController
@RequestMapping("/api/recipe")
public class RecipesController {
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private UserService userService;

    /**
     * Save a new recipe to the DB вy an authenticated user only
     *
     * @param recipe JSON with New Recipe
     * @return ID of the new recipe in the repository
     */
    @PostMapping("/new")
    public Map<String, Long> newRecipe(@Valid @RequestBody Recipe recipe, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        return Map.of("id", recipeService.saveRecipe(recipe, user).getId());
    }

    /**
     * Update a recipe in the database only by the owner of the recipe
     * If the recipe is not in the DB - HttpStatus.NOT_FOUND
     * If the user is not the owner of the recipe - return HttpStatus.FORBIDDEN
     * If recipe in DB and user is owner - updating Recipe and return HttpStatus.NO_CONTENT
     *
     * @param recipe    Recipe for update
     * @param id        ID Recipe in DB
     * @param principal User
     */
    @PutMapping("/{id}")
    public void updateRecipe(@Valid @RequestBody Recipe recipe, @PathVariable long id, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        recipeService.updateRecipeById(id, recipe, user);
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }

    /**
     * Return recipe by ID for all authenticated users
     *
     * @param id ID Recipe in DB
     * @return JSON with recipe or HttpStatus.NOT_FOUND if the recipe is not in the DB
     */
    @GetMapping("/{id}")
    public Recipe getRecipes(@PathVariable long id) {
        return recipeService.findRecipeById(id);
    }

    /**
     * Search for a recipes for the given category. Searches for an exact match. For all authenticated users
     *
     * @param category Category of recipes
     * @return List of recipes by category
     */
    @ResponseBody
    @GetMapping(value = "/search", params = "category")
    public List<Recipe> getRecipesByCategoryEquals(@RequestParam(name = "category", required = false) String category) {
        return recipeService.findRecipeByCategoryEquals(category);
    }

    /**
     * Search for a recipe containing the specified string in the title. For all authenticated users
     *
     * @param name search string in title
     * @return List of recipes with string in title
     */
    @ResponseBody
    @GetMapping(value = "/search", params = "name")
    public List<Recipe> getRecipesByNameContains(@RequestParam(name = "name", required = false) String name) {
        return recipeService.findRecipeByNameContains(name);
    }

    /**
     * Remove recipe from DB by ID only by the owner of the recipe
     * If the recipe is not in the DB - HttpStatus.NOT_FOUND
     * If the user is not the owner of the recipe - return HttpStatus.FORBIDDEN
     * If recipe in DB and user is owner - removing Recipe and return HttpStatus.NO_CONTENT
     *
     * @param id        ID Recipe in DB
     * @param principal User
     */
    @DeleteMapping("/{id}")
    public void deleteRecipeById(@PathVariable long id, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        recipeService.deleteRecipeById(id, user);
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }

    /**
     * Remove all recipes from the database. Only admin permission
     */
    @DeleteMapping("/delete")
    public void deleteAllRecipes() {
        recipeService.deleteAll();
    }
}