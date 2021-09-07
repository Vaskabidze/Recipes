package recipes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;


/**
 * The class describes the essence of the recipe
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "recipe")
public class Recipe implements Comparable<Recipe> {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotBlank
    @Column(name = "name")
    private String name;
    @NotBlank
    @Column(name = "category")
    private String category;
    @NotBlank
    @Column(name = "description")
    private String description;
    @NotEmpty
    @Size(min = 1)
    @Column(name = "ingredients")
    private String[] ingredients;
    @NotEmpty
    @Size(min = 1)
    @Column(name = "directions")
    private String[] directions;
    @Column(name = "date")
    private LocalDateTime date;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public int compareTo(Recipe recipe) {
        return recipe.getDate().compareTo(this.date);
    }
}
