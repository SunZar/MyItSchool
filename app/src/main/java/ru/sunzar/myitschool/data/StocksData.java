package ru.sunzar.myitschool.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class StocksData {
    private final static Map<Currency, MutableLiveData<Float>> currencies = new HashMap<>();

    public enum Currency implements Serializable {
        RUB("Russian rubles", "rub"),
        //BTC("Bitcoin", "btc"),
        EUR("Euro", "eur"),
        MATIC("Matic", "matic"),
        DOGE("Doge coin", "doge"),
        CNY("Chinese yuan", "cny"),
        ETH("Ethereum", "etc"),
        INCH("1inch", "1inch"),
        LTC("Litecoin", "ltc"),
        BNB("Binance coin", "bnb"),
        DOT("Dotcoin", "dot"),
        FLOW("Flow", "flow"),
        KCS("Kucoin", "kcs"),
        TRX("Tron", "trx");


        private final String apiName;
        private final String displayName;

        Currency(String displayName, String apiName) {
            this.apiName = apiName;
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getApiName() {
            return apiName;
        }
    }

    static {
        for (Currency value : Currency.values()) {
            currencies.put(value, new MutableLiveData<>(0f));
        }
    }

    public static LiveData<Float> getCurrency(Currency currency) {
        return currencies.get(currency);
    }

    public static void setCurrency(Currency currency, Float value) {
        currencies.get(currency).setValue(value);
    }
}
