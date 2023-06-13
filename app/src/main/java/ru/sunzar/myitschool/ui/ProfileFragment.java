package ru.sunzar.myitschool.ui;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myitschool.R;
import com.example.myitschool.databinding.FragmentProfileBinding;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import ru.sunzar.myitschool.data.ShopData;
import ru.sunzar.myitschool.data.ProfileData;
import ru.sunzar.myitschool.data.StocksData;
import ru.sunzar.myitschool.data.StocksRepository;
import ru.sunzar.myitschool.data.StocksSearchResponse;
import ru.sunzar.myitschool.utils.Resource;

public class ProfileFragment extends ToolbarBaseFragment {

    private FragmentProfileBinding binding;
    private ShopData.ShopProducts maxBoughtProperty = null;
    private ShopData.ShopProducts maxBoughtTransport = null;
    private ShopData.ShopProducts maxBoughtPhone = null;

    private SharedPreferences sharedProfileData = null;
    private SharedPreferences sharedShopData = null;

    private float total_balance;

    private final StocksRepository repository = new StocksRepository();

    @Override
    public View onCreateChildView(@NonNull ViewGroup parent, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        baseBinding.title.setText("Профиль");
        binding.name.setText(this.getActivity().getSharedPreferences("profile_data", MODE_PRIVATE).getString("name", "Пользователь"));

        binding.chooseName.setOnClickListener(view1 -> {
            onClickButtonChooseName();
        });

        loadPropertyData();

        searchPrice();

        loadName();

    }

    private void onClickButtonChooseName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_window_choose_name, null);
        EditText editName = view.findViewById(R.id.name);
        builder.setView(view)
                .setTitle("Смена имени")
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .setPositiveButton("Применить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (editName.getText().toString().length() > 20) {
                            lengthIsBig();
                        } else {
                            ProfileData.name = editName.getText().toString();
                            saveName();
                            loadName();
                        }
                    }
                });
        builder.show();

//        DialogWindowChooseName dialogWindowChooseName = new DialogWindowChooseName();
//        dialogWindowChooseName.show(getChildFragmentManager(), "dialog");
    }

    private void loadPropertyData() {
        sharedShopData = this.getActivity().getSharedPreferences("shop_data", MODE_PRIVATE);
        for (ShopData.ShopProducts products : ShopData.ShopProducts.values()) {
            ShopData.setIsBought(products, sharedShopData.getBoolean(products.getDisplayName(), false));
        }

        for (ShopData.ShopProducts value : ShopData.ShopProducts.values()) {
            if (value.getType() == "property") {
                if (ShopData.getIsBought(value) == true) {
                    maxBoughtProperty = value;
                }
            } else if (value.getType() == "transport") {
                if (ShopData.getIsBought(value) == true) {
                    maxBoughtTransport = value;
                }
            } else if (value.getType() == "phone") {
                if (ShopData.getIsBought(value) == true) {
                    maxBoughtPhone = value;
                }
            }
        }
        if (maxBoughtProperty != null) {
            binding.property.setText("Имущество: " + maxBoughtProperty.getDisplayName());
        } else {
            binding.property.setText("Имущество: Нет");
        }
        if (maxBoughtTransport != null) {
            binding.transport.setText("Транспорт: " + maxBoughtTransport.getDisplayName());
        } else {
            binding.transport.setText("Транспорт: Нет");
        }
        if (maxBoughtPhone != null) {
            binding.phone.setText("Телефон: " + maxBoughtPhone.getDisplayName());
        } else {
            binding.phone.setText("Телефон: Нет");
        }
    }

    private void loadStatisticData() {
        sharedProfileData = this.getActivity().getSharedPreferences("profile_data", MODE_PRIVATE);

        ProfileData.amount_of_clicks = sharedProfileData.getInt("amount_of_clicks_pref", 0);
        ProfileData.max_of_total_money = sharedProfileData.getFloat("max_of_total_money_pref", 0f);
        ProfileData.max_of_ethereum = sharedProfileData.getFloat("max_of_ethereum_pref", 0f);
        ProfileData.max_of_rubles = sharedProfileData.getFloat("max_of_rubles_pref", 0f);

        binding.amountOfClicks.setText("Кликов: " + ProfileData.amount_of_clicks);
        binding.maxOfTotalMoney.setText("Макс. баланс: " + ProfileData.max_of_total_money);
        binding.maxOfEthereum.setText("Макс. ETH: " + ProfileData.max_of_ethereum);
        binding.maxOfRubles.setText("Макс. RUB: " + ProfileData.max_of_rubles);
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
                binding.progressBar.setVisibility(View.GONE);
                binding.scrollInfo.setVisibility(View.VISIBLE);
                binding.avatar.setVisibility(View.VISIBLE);
                binding.constraintName.setVisibility(View.VISIBLE);
            } else if (stocksSearchResponseResource instanceof Resource.Error) {
                showError();
                binding.progressBar.setVisibility(View.GONE);
                binding.scrollInfo.setVisibility(View.VISIBLE);
                binding.titleStatistic.setVisibility(View.VISIBLE);
                binding.cardviewStatistic.setVisibility(View.VISIBLE);
                binding.avatar.setVisibility(View.VISIBLE);
                binding.constraintName.setVisibility(View.VISIBLE);
            } else if (stocksSearchResponseResource instanceof Resource.Loading) {
                showLoading(); //Не забудь скрыть загрузку в других состояниях
            }
        });
    }

    private void onUpdateData(Resource.Success<StocksSearchResponse> state) {
        Map<String, Float> currencies = state.getValue().getCurrencies();
        Map<String, Float> currenciesNew = new HashMap<>();
        if (currencies == null) return;

        currencies.forEach((name, price) -> {
            if (name != "rub") {
                float temp = 1 / price;
                temp = (float) Math.floor((temp + (Math.random() * temp / 100)) * 100) / 100f;
                currenciesNew.put(name, temp);
            } else {
                currenciesNew.put(name, price);
            }
        });
        total_balance = 0;
        for (StocksData.Currency value : StocksData.Currency.values()) {
            total_balance += StocksData.getCurrency(value).getValue() * currenciesNew.getOrDefault(value.getApiName(), 0f);
        }
        if (total_balance > ProfileData.max_of_total_money) {
            ProfileData.max_of_total_money = total_balance;
            SharedPreferences.Editor editorProfileData = this.getActivity().getSharedPreferences("profile_data", MODE_PRIVATE).edit();
            editorProfileData.putFloat("max_of_total_money_pref", ProfileData.max_of_total_money);
            editorProfileData.apply();
        }
        loadStatisticData();
    }

    private void showLoading() {
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    private void showError() {
        Toast.makeText(this.getActivity(), "Если у Вас всё в порядке с интернетом, то биржа закрыта и загрузить свежие данные по статистике невозможно, приходите позже", Toast.LENGTH_LONG).show();
    }

    private void saveStatisticToShared() {
        SharedPreferences.Editor editorProfileData = this.getActivity().getSharedPreferences("profile_data", MODE_PRIVATE).edit();

        editorProfileData.putInt("amount_of_clicks_pref", ProfileData.amount_of_clicks);
        editorProfileData.putFloat("max_of_total_money_pref", ProfileData.max_of_total_money);
        editorProfileData.putFloat("max_of_ethereum_pref", ProfileData.max_of_ethereum);
        editorProfileData.putFloat("max_of_rubles_pref", ProfileData.max_of_rubles);
        editorProfileData.apply();
    }

    private void saveName() {
        SharedPreferences sharedProfileData = this.getContext().getSharedPreferences("profile_data", MODE_PRIVATE);
        SharedPreferences.Editor editorProfileData = sharedProfileData.edit();
        editorProfileData.putString("name_pref", ProfileData.name);
        editorProfileData.apply();
    }

    private void loadName() {
        SharedPreferences sharedProfileData = this.getContext().getSharedPreferences("profile_data", MODE_PRIVATE);
        ProfileData.name = sharedProfileData.getString("name_pref", "Пользователь");
        binding.name.setText(ProfileData.name);
    }

    private void lengthIsBig() {
        Toast.makeText(this.getActivity(), "Длина имени должна быть меньше 20 символов", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        saveStatisticToShared();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadName();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        saveStatisticToShared();
    }

    //@Override
    //public void applyText(String name) {
    //    binding.name.setText(name);
    //    this.getActivity().getSharedPreferences("profile_data", MODE_PRIVATE).edit().putString("name", name);
    //}
}
