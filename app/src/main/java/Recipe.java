import java.util.*;

//Model class for recipes
public class Recipe {
    private final int id;
    private final String name;
    private final String instructions;
    private final List<Ingredient> ingredients;

    public Recipe(int id, String name, String instructions, List<Ingredient> ingredients) {
        this.id = id;
        this.name = name;
        this.instructions = instructions;
        this.ingredients = ingredients;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getInstructions() { return instructions; }
    public List<Ingredient> getIngredients() { return ingredients;}
}
