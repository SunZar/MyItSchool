package ru.sunzar.myitschool.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.myitschool.R;
import com.example.myitschool.databinding.ActivityStocksBinding;
import com.example.myitschool.databinding.DialogWindowChooseNameBinding;
import com.example.myitschool.databinding.FragmentClickerBinding;

public class DialogWindowChooseName extends AppCompatDialogFragment {

    private DialogWindowChooseNameBinding binding;
    //private DialogWindowChooseNameListener listener;
    private EditText editName;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //binding = DialogWindowChooseNameBinding.inflate(getLayoutInflater());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_window_choose_name, null);
        builder.setView(view)
                .setTitle("Смена имени")
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Применить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = editName.getText().toString();
                        //listener.applyText(name);
                    }
                });
        editName = view.findViewById(R.id.name);
        return builder.create();
    }

    //@Override
    //public void onAttach(@NonNull Context context) {
    //    super.onAttach(context);
//
    //    listener = (DialogWindowChooseNameListener) context;
    //}
//
    //public interface DialogWindowChooseNameListener {
    //    void applyText(String name);
    //}
}
