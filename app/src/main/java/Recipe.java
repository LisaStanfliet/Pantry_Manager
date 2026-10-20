import java.util.*;

//Model class for recipes
public class Recipe {
    private final int id;
    private final String name;
    private final String instructions;
    private final List<RecipeItem> ingredients;

    public Recipe(int id, String name, String instructions, List<RecipeItem> Items) {
        this.id = id;
        this.name = name;
        this.instructions = instructions;
        this.items = items == null ? new ArrayList<>() : Items;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getInstructions() { return instructions; }
    public List<RecipeItem> getItems() { return items; }
}
