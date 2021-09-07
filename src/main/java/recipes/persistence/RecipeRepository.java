package recipes.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import recipes.model.Recipe;

import java.util.List;

@Repository
public interface RecipeRepository extends CrudRepository<Recipe, Long> {
    /**
     * Search all recipes in the database by category (Case ignore)
     *
     * @param category Search Category
     * @return List of recipes by category
     */
    List<Recipe> findAllByCategoryIgnoreCase(String category);

    /**
     * @param name
     * @return
     */
    List<Recipe> findAllByNameIgnoreCaseContaining(String name);
}

