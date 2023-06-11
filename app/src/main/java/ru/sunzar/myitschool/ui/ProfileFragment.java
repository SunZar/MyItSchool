package ru.sunzar.myitschool.ui;

import static android.content.Context.MODE_PRIVATE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myitschool.databinding.FragmentProfileBinding;

import java.util.Locale;

import ru.sunzar.myitschool.data.ShopData;
import ru.sunzar.myitschool.data.StocksData;

public class ProfileFragment extends ToolbarBaseFragment {

    private FragmentProfileBinding binding;
    private ShopData.ShopProducts maxBoughtProperty = null;
    private ShopData.ShopProducts maxBoughtTransport = null;
    private ShopData.ShopProducts maxBoughtPhone = null;

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

        loadStatisticData();

    }

    private void onClickButtonChooseName() {
        DialogWindowChooseName dialogWindowChooseName = new DialogWindowChooseName();
        dialogWindowChooseName.show(getChildFragmentManager(), "dialog");
    }

    private void loadPropertyData() {
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

    }

    //@Override
    //public void applyText(String name) {
    //    binding.name.setText(name);
    //    this.getActivity().getSharedPreferences("profile_data", MODE_PRIVATE).edit().putString("name", name);
    //}
}
