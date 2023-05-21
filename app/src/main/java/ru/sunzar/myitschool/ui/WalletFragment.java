package ru.sunzar.myitschool.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myitschool.databinding.FragmentWalletBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ru.sunzar.myitschool.data.StocksData;
import ru.sunzar.myitschool.data.StocksRepository;
import ru.sunzar.myitschool.data.StocksSearchResponse;
import ru.sunzar.myitschool.utils.Resource;

public class WalletFragment extends Fragment {
    private FragmentWalletBinding binding;

    private final StocksRepository repository = new StocksRepository();

    private final WalletAdapter adapter = new WalletAdapter();
    private final ArrayList<Float> data_price = new ArrayList<>();
    private final ArrayList<Float> data_count = new ArrayList<>();
    private final ArrayList<String> data_namesId = new ArrayList<>();
    private final ArrayList<String> data_displayNames = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWalletBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.container.setAdapter(adapter);
        searchPrice();
        binding.title.setText("Кошелёк");
    }

    private void searchPrice() {
        long longDate = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sDate = sdf.format(longDate);
        repository.search(sDate);
        repository.stockSearchLiveData.observe(getViewLifecycleOwner(), stocksSearchResponseResource -> {
            Log.d("LOG", "RESPONSE SUCCESS: " + stocksSearchResponseResource.toString());
            if (stocksSearchResponseResource instanceof Resource.Success) {
                onUpdateData((Resource.Success<StocksSearchResponse>) stocksSearchResponseResource);
            } else if (stocksSearchResponseResource instanceof Resource.Error) {
                showError();
            } else if (stocksSearchResponseResource instanceof Resource.Loading) {
                showLoading(); //Не забудь скрыть загрузку в других состояниях
            }
        });
    }

    private void onUpdateData(Resource.Success<StocksSearchResponse> state) {
        Map<String, Float> currencies = state.getValue().getCurrencies();
        Map<String, Float> currenciesNew = new HashMap<>();
        if (currencies == null) return;
        currencies.forEach((currency, aDouble) -> {
            Log.d("LOG", currency + ": " + aDouble);
        });
        currencies.forEach((name, price) -> {
            if (name != "rub") {
                float temp = 1 / price;
                temp = (float) Math.floor((temp + (Math.random() * temp / 100)) * 100) / 100f;
                currenciesNew.put(name, temp);
            } else {
                currenciesNew.put(name, price);
            }
        });
        double total = 0;
        for (StocksData.Currency value : StocksData.Currency.values()) {
            data_price.add(currenciesNew.get(value.getApiName()));
            data_namesId.add(value.getApiName());
            data_displayNames.add(value.getDisplayName());
            if (value.getApiName() != "btc") {
                total += StocksData.getCurrency(value).getValue() * currenciesNew.getOrDefault(value.getApiName(), 0f);
            }
            Log.d("tagg", total + "");
        }
        loadStocksData();
        binding.totalBalance.setText(String.format(
                Locale.getDefault(),
                "%s: %.2f",
                StocksData.Currency.RUB.getApiName().toUpperCase(),
                total
        ) + " RUB");
    }

    private void showLoading() {

    }

    private void showError() {

    }

    private void loadStocksData() {
        for (StocksData.Currency value : StocksData.Currency.values()) {
            data_count.add(StocksData.getCurrency(value).getValue());
        }

        adapter.setData(data_count, data_price, data_namesId, data_displayNames, 10);
        Arrays.asList(StocksData.Currency.values()).forEach(item -> {

        });

        adapter.notifyDataSetChanged();
    }
}
