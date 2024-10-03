package com.example.datatestapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

public class DialogUserName extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_user_name, null);
        TextView txtView = dialogView.findViewById(R.id.textView);
        txtView.setText("Enter your Username!");
        Button btnOKI = (Button) dialogView.findViewById(R.id.btnOkiDoki);
        EditText editUserName = dialogView.findViewById(R.id.txtUserName);

        builder.setView(dialogView);
        btnOKI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Items mItem = new Items();
                mItem.setName(editUserName.getText().toString());
                MainActivity callingActivity = (MainActivity) getActivity();
                callingActivity.createNewItem(mItem);
                dismiss();
            }
        });

        return builder.create();
    }
}
