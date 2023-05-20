package ru.sunzar.myitschool;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myitschool.databinding.ItemStockBinding;

import java.util.ArrayList;
import java.util.Collection;

public class StocksAdapter extends RecyclerView.Adapter<StocksAdapter.ViewHolder> {
    private final ArrayList<Item> data = new ArrayList<>();

    public void setData(Collection<Item> newData) {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                ItemStockBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                ).getRoot()
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Item item = data.get(position);
        holder.itemView.setOnClickListener(view -> {
            view.getContext().startActivity(
                    StocksActivity.newIntent(view.getContext(), item.getCurrency())
            );
        });
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemStockBinding itemBinding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemBinding = ItemStockBinding.bind(itemView);
        }

        public void bind(Item data) {
            itemBinding.name.setText(data.getCurrency().getDisplayName());
            itemBinding.price.setText(data.getPrice() + " RUB");
        }
    }
}
