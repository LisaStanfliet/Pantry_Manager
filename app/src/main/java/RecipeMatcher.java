import java.util.*;

public class RecipeMatcher {

    public static List<Recipe> findMatches(List<Recipe> recipes, List<Items> items) {
        List<Recipe> matches = new ArrayList<>();
        for (Recipe recipe : recipes) {
        if (canMakeRecipe(recipe, items)) {
            matches.add(recipe);
        }
        }
        return matches;
    }

    public static boolean canMakeRecipe(Recipe recipe, List<Items> items) {
        Map<String, Double> availableItems = new HashMap<>();
        //Combine multiple pantry items into one with item name and base unit
        for (Items item : items) {
            String name = normalizeName(item.getName());
            String unit = normalizeUnit(item.getUnit());
            double quantity = baseQuantity(item.getQuantity(), item.getUnit());
            String key = name +"|"+ unit;
            availableItems.put(key, availableItems.getOrDefault(key, 0.0) + quantity);
        }

        //Rejects recipe if anything is missing or not enough of an ingredient
        for (Ingredient requiredItem : recipe.getIngredients()) {
            String name = normalizeName(requiredItem.getName());
            String unit = normalizeUnit(requiredItem.getUnit());
            String key = name +"|"+ unit;
            double neededQuantity = baseQuantity(requiredItem.getQuantity(), requiredItem.getUnit());
            double haveQuantity = availableItems.getOrDefault(key, 0.0);

            if (haveQuantity +0.000001 < neededQuantity) {
                return false;
            }
        }
        return true;
    }

    public static String normalizeName(String name) {
        String cleaned = name == null ? "" : name.trim().toLowerCase(Locale.ROOT);
        cleaned = cleaned.replaceAll("\\s+", "");
        switch (cleaned) {
            case "tomato":
            case "tomatoes":
                return "tomato";
            case "potato":
            case "potatoes":
                return "potatoes";
            case "egg":
            case "eggs":
                return "egg";
            case "banana":
            case "bananas":
                return "banana";
            case "carrot":
            case "carrots":
                return "carrot";
            case "pea":
            case "peas":
                return "pea";
            case "pepper":
            case "peppers":
                return "pepper";
            case "oat":
            case "oats":
                return "oat";
            case "apple":
            case "apples":
                return "apple";
            default:
                return cleaned;
        }
    }

    //Converts supported units to consistent base unit.
    public static String normalizeUnit(String unit) {
        String u = unit == null ? "" : unit.trim().toLowerCase(Locale.ROOT);
        switch (u) {
            case "kg":
            case "g":
                return "g";
            case "l":
            case "ml":
                return "ml";
            case "tbsp":
            case "tsps":
            case "tsp":
                return "tsp";
            case "cup":
            case "cups":
                return "cup";
            case "piece":
            case "pieces":
                return "piece";
        }
        return u;
    }

    //Converts quantity to base unit
    public static double baseQuantity(double quantity, String unit) {
        String u = unit == null ? "" : unit.trim().toLowerCase(Locale.ROOT);
        switch (u) {
            case "kg":
            case "l":
                return quantity * 1000;
        }
        return quantity;
    }
}
