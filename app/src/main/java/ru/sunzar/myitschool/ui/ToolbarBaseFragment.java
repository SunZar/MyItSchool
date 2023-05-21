package ru.sunzar.myitschool.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myitschool.R;
import com.example.myitschool.databinding.FragmentBaseBinding;

import java.util.Locale;

import ru.sunzar.myitschool.data.StocksData;

public abstract class ToolbarBaseFragment extends Fragment {
    protected FragmentBaseBinding baseBinding;

    public ToolbarBaseFragment() {
        super(R.layout.fragment_base);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        baseBinding = FragmentBaseBinding.bind(view);
        baseBinding.content.addView(onCreateChildView(baseBinding.content, savedInstanceState));
        StocksData.getCurrency(StocksData.Currency.RUB).observe(
                getViewLifecycleOwner(), rub -> baseBinding.rub.setText(
                        String.format(Locale.ENGLISH, "%.2f", rub)
                )
        );
        StocksData.getCurrency(StocksData.Currency.ETH).observe(
                getViewLifecycleOwner(), eth -> baseBinding.btc.setText(
                        String.format(Locale.ENGLISH, "%.6f", eth)
                )
        );
    }

    public abstract View onCreateChildView(
            @NonNull ViewGroup parent,
            @Nullable Bundle savedInstanceState
    );

    @Override
    public void onDestroyView() {
        baseBinding = null;
        super.onDestroyView();
    }
}
