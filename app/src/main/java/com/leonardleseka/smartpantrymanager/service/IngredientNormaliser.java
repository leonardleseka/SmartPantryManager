package com.leonardleseka.smartpantrymanager.service;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
public final class IngredientNormaliser {
    private static final Map<String, String> INGREDIENT_ALIASES =
            createIngredientAliases();
    private IngredientNormaliser() {
        // Prevent this utility class from being instantiated.
    }
    public static String normalize(String ingredientName) {
        if (ingredientName == null) {
            return "";
        }
        String normalizedName = ingredientName
                .trim()
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9 ]", "")
                .replaceAll("\\s+", " ");
        if (normalizedName.isEmpty()) {
            return "";
        }
        if (INGREDIENT_ALIASES.containsKey(normalizedName)) {
            return INGREDIENT_ALIASES.get(normalizedName);
        }
        normalizedName = convertPluralToSingular(normalizedName);
        if (INGREDIENT_ALIASES.containsKey(normalizedName)) {
            return INGREDIENT_ALIASES.get(normalizedName);
        }
        return normalizedName;
    }
    private static String convertPluralToSingular(
            String ingredientName
    ) {
        if (ingredientName.endsWith("ies")
                && ingredientName.length() > 3) {
            return ingredientName.substring(
                    0,
                    ingredientName.length() - 3
            ) + "y";
        }
        if (ingredientName.endsWith("oes")
                && ingredientName.length() > 3) {
            return ingredientName.substring(
                    0,
                    ingredientName.length() - 2
            );
        }
        if (ingredientName.endsWith("ches")
                || ingredientName.endsWith("shes")
                || ingredientName.endsWith("xes")
                || ingredientName.endsWith("zes")) {
            return ingredientName.substring(
                    0,
                    ingredientName.length() - 2
            );
        }
        if (ingredientName.endsWith("ses")
                && ingredientName.length() > 3) {
            return ingredientName.substring(
                    0,
                    ingredientName.length() - 2
            );
        }
        if (ingredientName.endsWith("s")
                && !ingredientName.endsWith("ss")
                && ingredientName.length() > 1) {
            return ingredientName.substring(
                    0,
                    ingredientName.length() - 1
            );
        }
        return ingredientName;
    }
    private static Map<String, String> createIngredientAliases() {
        Map<String, String> aliases = new HashMap<>();
        aliases.put("tomatoes", "tomato");
        aliases.put("potatoes", "potato");
        aliases.put("eggs", "egg");
        aliases.put("beans", "bean");
        aliases.put("peas", "pea");
        aliases.put("berries", "berry");
        aliases.put("bananas", "banana");
        aliases.put("carrots", "carrot");
        aliases.put("onions", "onion");
        aliases.put("bell pepper", "pepper");
        aliases.put("bell peppers", "pepper");
        aliases.put("capsicum", "pepper");
        aliases.put("tinned tuna", "tuna");
        aliases.put("canned tuna", "tuna");
        aliases.put("white bread", "bread");
        aliases.put("brown bread", "bread");
        aliases.put("whole wheat bread", "bread");
        aliases.put("cheddar", "cheese");
        aliases.put("cheddar cheese", "cheese");
        aliases.put("cow milk", "milk");
        aliases.put("full cream milk", "milk");
        return aliases;
    }
}
