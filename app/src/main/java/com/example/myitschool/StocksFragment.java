package com.example.myitschool;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myitschool.databinding.FragmentStocksBinding;

public class StocksFragment extends Fragment {
    private FragmentStocksBinding binding;
    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        binding = FragmentStocksBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}
