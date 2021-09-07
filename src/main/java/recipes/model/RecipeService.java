package recipes.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import recipes.persistence.RecipeRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository repository;

    /**
     * Save a new recipe to the database
     *
     * @param recipe new Recipe
     * @param user   Authenticated user
     * @return new Recipe from DB
     */
    public Recipe saveRecipe(Recipe recipe, User user) {
        recipe.setDate(LocalDateTime.now());
        recipe.setUser(user);
        return repository.save(recipe);
    }

    /**
     * Search for a recipe in the database by ID
     *
     * @param id Recipe ID
     * @return Recipe from DB
     * @throws ResponseStatusException - if recipe not fount in DB (HttpStatus.NOT_FOUND)
     */
    public Recipe findRecipeById(long id) throws ResponseStatusException {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    /**
     * Search for a recipes for the given category.Searches for an exact match.
     *
     * @param category Category of recipes
     * @return ist of recipes by category
     */
    public List<Recipe> findRecipeByCategoryEquals(String category) {
        List<Recipe> resultList = repository.findAllByCategoryIgnoreCase(category);
        Collections.sort(resultList);
        return resultList;
    }

    /**
     * Search for a recipe containing the specified string in the title.
     *
     * @param name search string in title
     * @return List of recipes with string in title
     */
    public List<Recipe> findRecipeByNameContains(String name) {
        List<Recipe> resultList = repository.findAllByNameIgnoreCaseContaining(name);
        Collections.sort(resultList);
        return resultList;
    }

    /**
     * Check if the user is the author of the recipe
     *
     * @param recipeId Recipe ID in DB
     * @param user     User
     * @return If the user is the author of the recipe - true, otherwise - false
     */
    public boolean checkOwnerOfRecipe(long recipeId, User user) {
        Recipe recipe = findRecipeById(recipeId);
        return recipe.getUser().getId() == user.getId();
    }

    /**
     * Update a recipe in the database
     *
     * @param recipeId        ID Recipe in DB
     * @param recipeForUpdate Recipe (new version for updating)
     * @param user            User (owner of recipe)
     * @throws ResponseStatusException - If the User is not the author of the recipe (HttpStatus.FORBIDDEN)
     */
    public void updateRecipeById(long recipeId, Recipe recipeForUpdate, User user) throws ResponseStatusException {
        if (checkOwnerOfRecipe(recipeId, user)) {
            recipeForUpdate.setId(recipeId);
            saveRecipe(recipeForUpdate, user);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    /**
     * Remove recipe from DB by ID
     *
     * @param id   ID Recipe in DB
     * @param user User (owner of recipe)
     * @throws ResponseStatusException If the User is not the author of the recipe (HttpStatus.FORBIDDEN)
     */
    public void deleteRecipeById(long id, User user) throws ResponseStatusException {
        if (checkOwnerOfRecipe(id, user)) {
            repository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    /**
     * Remove all recipes from DB
     */
    public void deleteAll() {
        repository.deleteAll();
    }

}
