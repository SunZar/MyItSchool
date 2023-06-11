package ru.sunzar.myitschool.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ShopData {
    private final static Map<ShopProducts, Boolean> products = new HashMap<>();

    public enum ShopProducts implements Serializable {
        /*GTX_750_Ti("GTX 750 Ti", 100, 0),
        GTX_1050_Ti("GTX 1050 Ti", 100, 1),
        MATIC("GTX 1630", 100, 2),
        DOGE("GTX 1650", 100, 3),
        CNY("GTX 1660 Super", 100, 4),
        ETH("GTX 1660 Ti", 100, 5),
        INCH("RTX 2060", 100, 6),
        LTC("RTX 2060 Super", 100, 7),
        BNB("RTX 3050", 100, 8),
        DOT("RTX 3060", 100, 9),
        FLOW("RTX 3060 Ti", 100, 10),
        KCS("RTX 3070", 100, 11),
        TRX("RTX 3070 Ti", 100, 12),
        TRX("RTX 3080", 100, 12),
        TRX("RTX 4060 Ti", 100, 12),*/
        SMALL_HOUSE("Маленький дом", 1_200_000, 0, "property"),
        MEDIUM_HOUSE("Средний дом", 2_500_000, 1, "property"),
        BIG_HOUSE("Большой дом", 5_800_000, 2, "property"),
        BICYCLE("Велосипед", 20_000, 3, "transport"),
        SCOOTER("Самокат", 15_000, 4, "transport"),
        ELECTROSCOOTER("Электросамокат", 60_000, 5, "transport"),
        CAR("Машина", 400_000, 6, "transport"),
        PUSH_BUTTON_PHONE("Кнопочный телефон", 5_000, 7, "phone"),
        SMART_PHONE("Сенсорный телефон", 20_000, 8, "phone");

        private final String displayName;
        private final float price;
        private final int index;
        private final String type;

        ShopProducts(String displayName, float price, int index, String type) {
            this.displayName = displayName;
            this.price = price;
            this.index = index;
            this.type = type;
        }

        public String getDisplayName() {
            return displayName;
        }

        public float getPrice() {
            return price;
        }

        private int getIndex() {
            return index;
        }

        public String getType() {
            return type;
        }
    }

    static {
        for (ShopProducts value : ShopProducts.values()) {
            products.put(value, false);
        }
    }

    public static Boolean getIsBought(ShopProducts product) {
        return products.get(product);
    }

    public static void setIsBought(ShopProducts product, Boolean isBought) {
        products.put(product, isBought);
    }

    public static ShopProducts getProductByIndex(int index) {
        for (ShopProducts value : ShopProducts.values()) {
            if (value.getIndex() == index) {
                return value;
            }
        }
        return null;
    }
}
