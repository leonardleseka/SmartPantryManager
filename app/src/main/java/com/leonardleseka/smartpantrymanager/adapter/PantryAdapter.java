package com.leonardleseka.smartpantrymanager.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.leonardleseka.smartpantrymanager.R;
import com.leonardleseka.smartpantrymanager.database.entity.PantryItem;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
public class PantryAdapter
        extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {
    public interface PantryItemListener {
        void onEditClick(PantryItem pantryItem);
        void onDeleteClick(PantryItem pantryItem);
    }
    private final List<PantryItem> pantryItems =
            new ArrayList<>();
    private final PantryItemListener listener;
    public PantryAdapter(PantryItemListener listener) {
        this.listener = listener;
    }
    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(
                        R.layout.item_pantry,
                        parent,
                        false
                );
        return new PantryViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(
            @NonNull PantryViewHolder holder,
            int position
    ) {
        PantryItem pantryItem = pantryItems.get(position);
        holder.textViewItemName.setText(
                pantryItem.getName()
        );
        String quantityText = String.format(
                Locale.getDefault(),
                "Quantity: %s %s",
                formatQuantity(pantryItem.getQuantity()),
                pantryItem.getUnit()
        );
        holder.textViewItemQuantity.setText(quantityText);
        String expiryDate = pantryItem.getExpiryDate();
        if (expiryDate == null || expiryDate.trim().isEmpty()) {
            holder.textViewItemExpiry.setText(
                    "Expiry date: Not provided"
            );
        } else {
            holder.textViewItemExpiry.setText(
                    "Expiry date: " + expiryDate
            );
        }
        holder.buttonEditItem.setOnClickListener(
                view -> listener.onEditClick(pantryItem)
        );
        holder.buttonDeleteItem.setOnClickListener(
                view -> listener.onDeleteClick(pantryItem)
        );
    }
    @Override
    public int getItemCount() {
        return pantryItems.size();
    }
    public void setPantryItems(
            List<PantryItem> updatedPantryItems
    ) {
        pantryItems.clear();
        if (updatedPantryItems != null) {
            pantryItems.addAll(updatedPantryItems);
        }
        notifyDataSetChanged();
    }
    private String formatQuantity(double quantity) {
        if (quantity == Math.floor(quantity)) {
            return String.format(
                    Locale.getDefault(),
                    "%.0f",
                    quantity
            );
        }
        return String.format(
                Locale.getDefault(),
                "%.2f",
                quantity
        );
    }
    static class PantryViewHolder
            extends RecyclerView.ViewHolder {
        private final TextView textViewItemName;
        private final TextView textViewItemQuantity;
        private final TextView textViewItemExpiry;
        private final Button buttonEditItem;
        private final Button buttonDeleteItem;
        public PantryViewHolder(
                @NonNull View itemView
        ) {
            super(itemView);
            textViewItemName = itemView.findViewById(
                    R.id.textViewItemName
            );
            textViewItemQuantity = itemView.findViewById(
                    R.id.textViewItemQuantity
            );
            textViewItemExpiry = itemView.findViewById(
                    R.id.textViewItemExpiry
            );
            buttonEditItem = itemView.findViewById(
                    R.id.buttonEditItem
            );
            buttonDeleteItem = itemView.findViewById(
                    R.id.buttonDeleteItem
            );
        }
    }
}
