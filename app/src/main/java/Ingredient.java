public class Ingredient {
    private final int id;
    private final int recipeId;
    private final String ItemName;
    private final double ItemQuantity;
    private final String ItemUnit;

    public Ingredient(int id, int recipeId, String name, double quantity, String unit) {
        this.id = id;
        this.recipeId = recipeId;
        this.ItemName = name;
        this.ItemQuantity = quantity;
        this.ItemUnit = unit;
    }

    public int getId() {
        return id;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public String getName() {
        return ItemName;
    }

    public double getQuantity() {
        return ItemQuantity;
    }

    public String getUnit() {
        return ItemUnit;
    }

}
