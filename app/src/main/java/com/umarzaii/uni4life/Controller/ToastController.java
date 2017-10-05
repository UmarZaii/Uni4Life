package com.umarzaii.uni4life.Controller;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.umarzaii.uni4life.R;

import es.dmoral.toasty.Toasty;

public class ToastController {

    private Context context;

    public ToastController(Context context) {
        this.context = context;
        toastConfig();
    }

    private void toastConfig() {
        Toasty.Config.getInstance()
                .setErrorColor(ContextCompat.getColor(context,R.color.toastError))
                .setInfoColor(ContextCompat.getColor(context,R.color.toastInfo))
                .setSuccessColor(ContextCompat.getColor(context,R.color.toastSuccess))
                .setWarningColor(ContextCompat.getColor(context,R.color.toastError))
                .apply();
    }

    private void info(String string, Boolean bool) {
        Toasty.info(context, string, Toast.LENGTH_SHORT, bool).show();
    }

    private void success(String string, Boolean bool) {
        Toasty.success(context, string, Toast.LENGTH_SHORT, bool).show();
    }

    private void error(String string, Boolean bool) {
        Toasty.error(context, string, Toast.LENGTH_SHORT, bool).show();
    }
}
