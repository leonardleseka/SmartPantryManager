package com.leonardleseka.smartpantrymanager.database.entity;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
@Entity(
        tableName = "pantry_items",
        indices = {
                @Index(value = {"normalizedName", "unit"}, unique = true)
        }
)
public class PantryItem {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String normalizedName;
    private double quantity;
    private String unit;
    private String expiryDate;
    private long createdAt;
    public PantryItem(
            String name,
            String normalizedName,
            double quantity,
            String unit,
            String expiryDate,
            long createdAt
    ) {
        this.name = name;
        this.normalizedName = normalizedName;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
        this.createdAt = createdAt;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public String getNormalizedName() {
        return normalizedName;
    }
    public double getQuantity() {
        return quantity;
    }
    public String getUnit() {
        return unit;
    }
    public String getExpiryDate() {
        return expiryDate;
    }
    public long getCreatedAt() {
        return createdAt;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
    }
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
