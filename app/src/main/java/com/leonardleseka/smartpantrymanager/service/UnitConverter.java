package com.leonardleseka.smartpantrymanager.service;
import java.util.Locale;
public final class UnitConverter {
    private UnitConverter() {
        // Prevent this utility class from being instantiated.
    }
    public static boolean hasSufficientQuantity(
            double availableQuantity,
            String availableUnit,
            double requiredQuantity,
            String requiredUnit
    ) {
        if (availableQuantity < 0 || requiredQuantity < 0) {
            return false;
        }
        String normalizedAvailableUnit =
                normalizeUnit(availableUnit);
        String normalizedRequiredUnit =
                normalizeUnit(requiredUnit);
        if (!areUnitsCompatible(
                normalizedAvailableUnit,
                normalizedRequiredUnit
        )) {
            return false;
        }
        double convertedAvailableQuantity =
                convertToBaseUnit(
                        availableQuantity,
                        normalizedAvailableUnit
                );
        double convertedRequiredQuantity =
                convertToBaseUnit(
                        requiredQuantity,
                        normalizedRequiredUnit
                );
        return convertedAvailableQuantity
                >= convertedRequiredQuantity;
    }
    public static boolean areUnitsCompatible(
            String firstUnit,
            String secondUnit
    ) {
        String normalizedFirstUnit =
                normalizeUnit(firstUnit);
        String normalizedSecondUnit =
                normalizeUnit(secondUnit);
        if (normalizedFirstUnit.equals(
                normalizedSecondUnit
        )) {
            return true;
        }
        return isWeightUnit(normalizedFirstUnit)
                && isWeightUnit(normalizedSecondUnit)
                || isVolumeUnit(normalizedFirstUnit)
                && isVolumeUnit(normalizedSecondUnit);
    }
    private static double convertToBaseUnit(
            double quantity,
            String unit
    ) {
        switch (unit) {
            case "kg":
                return quantity * 1000;
            case "l":
                return quantity * 1000;
            default:
                return quantity;
        }
    }
    private static boolean isWeightUnit(String unit) {
        return unit.equals("g") || unit.equals("kg");
    }
    private static boolean isVolumeUnit(String unit) {
        return unit.equals("ml") || unit.equals("l");
    }
    private static String normalizeUnit(String unit) {
        if (unit == null) {
            return "";
        }
        String normalizedUnit = unit
                .trim()
                .toLowerCase(Locale.ROOT);
        switch (normalizedUnit) {
            case "grams":
            case "gram":
                return "g";
            case "kilograms":
            case "kilogram":
                return "kg";
            case "millilitres":
            case "millilitre":
            case "milliliters":
            case "milliliter":
                return "ml";
            case "litres":
            case "litre":
            case "liters":
            case "liter":
                return "l";
            case "pieces":
                return "piece";
            case "cups":
                return "cup";
            case "tablespoon":
            case "tablespoons":
                return "tbsp";
            case "teaspoon":
            case "teaspoons":
                return "tsp";
            default:
                return normalizedUnit;
        }
    }
}
