package ru.sunzar.myitschool.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DialogWindowTutorial extends AppCompatDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Обучение")
                .setMessage("Вы зарабатываете Ethereum на вкладке \"Майнинг\" за клики по видеокарте. " +
                        "Вы можете прокачивать начисляемое количество Ethereum, " +
                        "а так же количество за определённое время (без кликов) и само время. " +
                        "Если вы находитесь на вкладке \"Майнинг\" и не кликаете 5 секунд, ваша " +
                        "видеокарта будет перегреваться, так как нам всегда приходят видеокарты со " +
                        "сломанной системой охлаждения, а кликами мы крутим вентиляторы видеокарты и охлаждаем её. " +
                        "Ethereum можно продать и получить рубли во вкладке \"Биржа\". " +
                        "Рубли и Ethereum - главная валюта. За рубли вы можете купить другие валюты " +
                        "во вкладке \"Биржа\" по сегодняшней стоимости, а в будущем, когда стоимость поднимется, " +
                        "вы их сможете продать и получить прибыль в рублях. Во вкладке \"Кошелёк\" " +
                        "собраны все валюты, их количество и их цена в рублях, " +
                        "а также общая стоимость ваших активов");
        return builder.create();
    }
}
