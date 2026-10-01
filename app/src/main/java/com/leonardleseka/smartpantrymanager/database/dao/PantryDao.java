package com.leonardleseka.smartpantrymanager.database.dao;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.leonardleseka.smartpantrymanager.database.entity.PantryItem;
import java.util.List;
@Dao
public interface PantryDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insert(PantryItem pantryItem);
    @Update
    int update(PantryItem pantryItem);
    @Delete
    int delete(PantryItem pantryItem);
    @Query("SELECT * FROM pantry_items ORDER BY name ASC")
    List<PantryItem> getAll();
    @Query("SELECT * FROM pantry_items WHERE id = :id LIMIT 1")
    PantryItem getById(int id);
    @Query(
            "SELECT * FROM pantry_items " +
                    "WHERE normalizedName = :normalizedName " +
                    "AND unit = :unit LIMIT 1"
    )
    PantryItem findByNameAndUnit(
            String normalizedName,
            String unit
    );
    @Query("SELECT COUNT(*) FROM pantry_items")
    int getCount();
    @Query("DELETE FROM pantry_items")
    void deleteAll();
}
