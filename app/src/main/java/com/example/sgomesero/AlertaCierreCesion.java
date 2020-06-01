package com.example.sgomesero;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

public class AlertaCierreCesion {
    private Activity activity;
    private AlertDialog dialog;

    private boolean estado;

    AlertaCierreCesion (Activity myActivity) {
        activity = myActivity;
        estado = false;
    }

    public boolean isEstado() {
        return estado;
    }

    void startLoadingDialog () {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.alerta_cierre_cesion, null));
        builder.setCancelable(true);

        dialog = builder.create();
        dialog.show();
    }

    void dismissDialog(){
        dialog.dismiss();
    }

    public void funcionSi(View view){
        estado = false;
        dialog.dismiss();
    }

    public void funcionNo(View view){
        dialog.dismiss();
    }

}
