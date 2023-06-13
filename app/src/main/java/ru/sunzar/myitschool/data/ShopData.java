package ru.sunzar.myitschool.data;

import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myitschool.R;

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
        COUNTRY_HOUSE("Дача", 1_200_000, 0, "property", "Участок 5 соток: дом, баня, теплица", R.drawable.country_house),
        FLAT("Квартира", 7_500_000, 1, "property", "Трёх-комнатная, 100 кв., улучшенная планировка, хороший ремонт", R.drawable.flat),
        COTTAGE("Коттедж", 11_800_000, 2, "property", "Три этажа, 300 кв., гараж", R.drawable.cottage),
        VILLA("Вилла", 30_000_000, 3, "property", "Вилла на берегу тихого океана с бассейном", R.drawable.villa),
        BYCICLE("Велосипед", 10_000, 4, "transport", "На нём ездил ещё твой дедушка", R.drawable.bycicle),
        SCOOTER("Самокат", 15_000, 5, "transport", "Б/У, весь в царапинах, но ездить можно", R.drawable.scooter),
        ELECTROSCOOTER("Электросамокат", 60_000, 6, "transport", "Разгон до 30 км./ч., запас хода 100 км.", R.drawable.electroscooter),
        LADA_SAMARA("Лада Самара", 120_000, 7, "transport", "На ней только на дачу", R.drawable.lada_samara),
        HYINDAI_SOLARIS("Хёндай Солярис", 600_000, 8, "transport", "Повседневная машина для работяги", R.drawable.hyindai_solaris),
        BMW_5_SERIES("БМВ 5-ой серии", 900_000, 9, "transport", "Для своих лет выглядит солидно", R.drawable.bmw_5_series),
        RANGE_ROVER("Рэндж Ровер", 3_500_000, 10, "transport", "В самый раз для наших дорог", R.drawable.range_rover),
        BMW_M5_F90("БМВ М5 Ф90", 8_900_000, 11, "transport", "4 секунды до 100 км./ч... А куда больше?", R.drawable.bmw_m5_f90),
        HELICOPTER("Вертолёт", 35_000_000, 12, "transport", "Пробки? Не, не слышали...", R.drawable.car),
        BUTTON_PHONE("Кноп. телефон", 5_000, 13, "phone", "Пробки? Не, не слышали...", R.drawable.button_phone),
        HUAWEI_NOVA_5T("Хуавей Нова 5Т", 25_000, 14, "phone", "Телефон разработчика", R.drawable.mobile_phone),
        SAMSUNG_S23_ULTRA("Самсунг С23 Ультра", 100_000, 15, "phone", "Галактический смартфон", R.drawable.mobile_phone);

        private final String displayName;
        private final float price;
        private final int index;
        private final String type;
        private final String description;
        private final int drawable;

        ShopProducts(String displayName, float price, int index, String type, String description, int drawable) {
            this.displayName = displayName;
            this.price = price;
            this.index = index;
            this.type = type;
            this.description = description;
            this.drawable = drawable;
        }

        public String getDisplayName() {
            return displayName;
        }

        public float getPrice() {
            return price;
        }

        public int getIndex() {
            return index;
        }

        public String getType() {
            return type;
        }

        public String getDescription() {
            return description;
        }

        public int getDrawable() {
            return drawable;
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
