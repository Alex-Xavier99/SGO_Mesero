package com.example.sgomesero;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class DialogSelectPlato {

    //Interfaz
    private FinalizarCuadroDialogo interfaz;

    public interface FinalizarCuadroDialogo{
        void ResultadoCuadroDialogSelectPlato(Boolean Modificar);
    }

    public DialogSelectPlato(Context contexto, FinalizarCuadroDialogo actividad, String plato){

        interfaz = actividad;

        //funciones del dialogo
        final Dialog dialogo = new Dialog(contexto);
        dialogo.requestWindowFeature((Window.FEATURE_NO_TITLE));
        dialogo.setCancelable(false);
        dialogo.setContentView((R.layout.dialog_select_plato));
        dialogo.getWindow().setLayout(950,550);
        dialogo.setCanceledOnTouchOutside(true);

        // Cast objetos de la activity
        TextView txtplato = (TextView) dialogo.findViewById(R.id.Txt_ModPlato);
        Button Btnmodificar = (Button) dialogo.findViewById(R.id.Btn_ModificarCant);
        Button Btneliminar = (Button) dialogo.findViewById(R.id.Btn_EliminarPlato);
        txtplato.setText(plato);
        //Boton modificar
        Btnmodificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaz.ResultadoCuadroDialogSelectPlato(true);
                dialogo.dismiss();
            }
        });
        //Boton eliminar
        Btneliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaz.ResultadoCuadroDialogSelectPlato(false);
                dialogo.dismiss();
            }
        });
        dialogo.show();
    }
}
