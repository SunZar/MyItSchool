package ru.sunzar.myitschool.ui;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.content.ContextCompat;

import com.example.myitschool.R;
import com.example.myitschool.databinding.ActivityStocksBinding;
import com.example.myitschool.databinding.DialogWindowChooseNameBinding;
import com.example.myitschool.databinding.FragmentClickerBinding;

import ru.sunzar.myitschool.data.ProfileData;

public class DialogWindowChooseName extends AppCompatDialogFragment {

    private DialogWindowChooseNameBinding binding;
    //private DialogWindowChooseNameListener listener;
    private EditText editName;

    private boolean isChanged = false;

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
                        ProfileData.name = editName.getText().toString();
                        isChanged = true;
                        //listener.applyText(name);
                    }
                });
        editName = view.findViewById(R.id.name);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (isChanged == true) {
            SharedPreferences sharedProfileData = context.getSharedPreferences("profile_data", MODE_PRIVATE);
            SharedPreferences.Editor editorProfileData = sharedProfileData.edit();
            editorProfileData.putString("name_pref", ProfileData.name);
            editorProfileData.apply();
        }

//        listener = (DialogWindowChooseNameListener) context;
    }
//
    //public interface DialogWindowChooseNameListener {
    //    void applyText(String name);
    //}
}
