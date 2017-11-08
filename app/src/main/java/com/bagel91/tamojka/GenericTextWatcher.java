package com.bagel91.tamojka;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class GenericTextWatcher implements TextWatcher {

    private EditText view;
    private CallBack mCallBack;

    public GenericTextWatcher(EditText view, CallBack mCallBack) {
        this.view = view;
        this.mCallBack = mCallBack;
    }

    public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                  int i2) {
    }

    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    public void afterTextChanged(Editable editable) {
        String text = editable.toString();

        if (text.length() < 1)
            view.setText("0");
        if (text.length() > 8)
            view.setText("10000000");
        mCallBack.OnDataChanged();

    }
}