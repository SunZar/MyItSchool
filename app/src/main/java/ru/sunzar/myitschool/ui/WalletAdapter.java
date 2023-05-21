package ru.sunzar.myitschool.ui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myitschool.databinding.ItemWalletStockBinding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.ViewHolder> {
    private final ArrayList<Float> data_price = new ArrayList<>();
    private final ArrayList<Float> data_count = new ArrayList<>();
    private final ArrayList<String> data_namesId = new ArrayList<>();
    private final ArrayList<String> data_displayNames = new ArrayList<>();
    private float rub;

    public void setData(Collection<Float> countData, Collection<Float> priceData, Collection<String> namesId, Collection<String> displayNames, float rub) {
        data_price.clear();
        data_count.clear();
        data_namesId.clear();
        data_displayNames.clear();
        data_price.addAll(priceData);
        data_count.addAll(countData);
        data_namesId.addAll(namesId);
        data_displayNames.addAll(displayNames);
        this.rub = rub;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                ItemWalletStockBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                ).getRoot()
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.itemView.setOnClickListener(view -> {
////            Bundle bundle = new Bundle();
////            bundle.putInt("index", position);
////            context..pasteStocksFragment(bundle);
//            Intent intent = new Intent(context, StocksActivity.class);
//            intent.putExtra("index", position);
//            context.startActivity(intent);
//        });
        holder.bind(data_count.get(position), data_price.get(position), data_namesId.get(position), data_displayNames.get(position), rub);
    }

    @Override
    public int getItemCount() {
        return data_price.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemWalletStockBinding itemBinding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemBinding = ItemWalletStockBinding.bind(itemView);
        }

        public void bind(float count, float price, String data_namesId, String data_displayNames, float rub) {
            itemBinding.id.setText(data_namesId.toUpperCase());
            itemBinding.name.setText("(" + data_displayNames + ")");
            itemBinding.currencyCount.setText(String.format(
                    Locale.getDefault(),
                    "%.2f",
                    count
            ) + " " + data_namesId.toUpperCase());
            itemBinding.currencyRub.setText(String.format(
                    Locale.getDefault(),
                    "%.2f",
                    Math.floor(count * price * 10000) / 10000
            ) + " RUB");
            Log.d("tag", price + "");
        }
    }
}
