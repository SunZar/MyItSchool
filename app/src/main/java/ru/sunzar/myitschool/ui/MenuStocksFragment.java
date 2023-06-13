package ru.sunzar.myitschool.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myitschool.databinding.FragmentMenuStocksBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import ru.sunzar.myitschool.data.StocksRepositoryData;
import ru.sunzar.myitschool.data.Item;
import ru.sunzar.myitschool.data.StocksRepository;
import ru.sunzar.myitschool.data.StocksSearchResponse;
import ru.sunzar.myitschool.utils.Resource;

public class MenuStocksFragment extends ToolbarBaseFragment {
    private FragmentMenuStocksBinding binding;

    private final StocksRepository repository = new StocksRepository();
    private final StocksRepositoryData shortRepository = new StocksRepositoryData();

    private final StocksAdapter adapter = new StocksAdapter();

    @Override
    public View onCreateChildView(@NonNull ViewGroup parent, @Nullable Bundle savedInstanceState) {
        binding = FragmentMenuStocksBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.container.setAdapter(adapter);
//        addItems();
        searchPrice();
        baseBinding.title.setText("Биржа");
        ///SharedPreferences sp = getSharedPreferences("mining_data", MODE_PRIVATE);
        ///binding.stocksText.setText(sp.getInt("btc_count_mining_pref", 0) + "");
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }

    public void pasteStocksFragment(Bundle bundle) {

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
                baseBinding.progressBar.setVisibility(View.GONE);
            } else if (stocksSearchResponseResource instanceof Resource.Error) {
                showError();
                baseBinding.progressBar.setVisibility(View.GONE);
            } else if (stocksSearchResponseResource instanceof Resource.Loading) {
                showLoading(); //Не забудь скрыть загрузку в других состояниях
            }
        });
    }

    private void onUpdateData(Resource.Success<StocksSearchResponse> state) {
        state.getValue().getCurrencies().forEach((currency, aDouble) -> {
            Log.d("LOG", currency + ": " + aDouble);
        });
        ArrayList<Item> itemsTemp = new ArrayList<>();
        shortRepository.getItems().forEach(item -> {
            if (item.getCurrency().getApiName() != "rub") {
                item.setPrice(state.getValue().getCurrencies().get(item.getCurrency().getApiName()));
                itemsTemp.add(item);
            }
        });
        shortRepository.setItems(itemsTemp);
        adapter.setData(shortRepository.getItems());
    }

    private void showLoading() {
        baseBinding.progressBar.setVisibility(View.VISIBLE);
    }

    private void showError() {
        Toast.makeText(this.getActivity(), "Если у Вас всё в порядке с интернетом, то биржа закрыта и загрузить данные невозможно, приходите позже", Toast.LENGTH_LONG).show();
    }
}


//        StocksFragment stocksFragment = new StocksFragment();
//
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.rootContainer, stocksFragment)
//                .commit();

//        plot = (XYPlot) binding.plot;
//        binding.stocks.setOnClickListener(view -> {
//            goSearchStocks();
//        });
//
//        repository.stockSearchLiveData.observe(this, stocksSearchResponseResource -> {
//            Log.d("LOG", "RESPONSE SUCCESS: " + stocksSearchResponseResource.toString());
//            if (stocksSearchResponseResource instanceof Resource.Success) {
//                onUpdateData((Resource.Success<StocksSearchResponse>)stocksSearchResponseResource);
//            } else if (stocksSearchResponseResource instanceof Resource.Error) {
//                showError();
//            } else if (stocksSearchResponseResource instanceof Resource.Loading) {
//                showLoading(); //Не забудь скрыть загрузку в других состояниях
//            }
//        });
//    }
//
//    private void showLoading() {
//
//    }
//
//    private void showError() {
//
//    }

//    private void onUpdateData(Resource.Success<StocksSearchResponse> state) {
//
//        binding.text.setText(state.getValue().getCurrencies().get("usd") + "");
//        state.getValue().getCurrencies().forEach((currency, aDouble) -> {
//            Log.d("LOG", currency + ": " + aDouble);
//        });
//
//        if (flagSearch) {
//            series1Numbers.add(state.getValue().getCurrencies().get("usd"));
//        }
//
//    }
//
//
//    private void goSearchStocks() {
//        String searchRequest = binding.currency.getText().toString().trim();
//
//        flagSearch = true;
//        long longDate = System.currentTimeMillis();
//        long longDateI = longDate;
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String sDate = sdf.format(longDate);
//        binding.text.setText(sDate);
//        for (int i = 0; i < 10; i++) {
//            longDateI = longDateI - 86_400_000;
//            sDate = sdf.format(longDateI);
//            domainLabels.add(sDate);
//            repository.search(sDate);
//        }
//
//        String sum = "";
//        for (int i = 0; i < series1Numbers.toArray().length; i++) {
//            sum = sum + series1Numbers.get(i) + " / ";
//        }
//        binding.text.setText(sum);
//        XYSeries series1 = new SimpleXYSeries(
//                series1Numbers, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series1");
//        LineAndPointFormatter series1Format =
//                new LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels);
//        series1Format.setInterpolationParams(
//                new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));
//        plot.addSeries(series1, series1Format);
//        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
//            @Override
//            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
//                int i = Math.round(((Number) obj).floatValue());
//                return toAppendTo.append(domainLabels.get(i));
//            }
//            @Override
//            public Object parseObject(String source, ParsePosition pos) {
//                return null;
//            }
//        });
//        plot.invalidate();
//    }
//}