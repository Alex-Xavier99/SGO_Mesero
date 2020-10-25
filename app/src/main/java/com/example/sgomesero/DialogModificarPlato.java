package com.example.sgomesero;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class DialogModificarPlato {
    public interface FinalizarCuadroDialogo{
        void ResultadoCuadroDialogModificar(String cant);
    }
     private FinalizarCuadroDialogo interfaz;
     String plato;
     int cant;

    public DialogModificarPlato(Context contexto, FinalizarCuadroDialogo actividad, String NomPlato, final String Cantidad ){
        interfaz = actividad;
        plato = NomPlato;
        cant = Integer.parseInt(Cantidad);
        final Dialog dialogo = new Dialog(contexto);
        dialogo.requestWindowFeature((Window.FEATURE_NO_TITLE));
        dialogo.setCancelable(false);
        dialogo.setContentView((R.layout.dialog_modificar_plato));
        dialogo.getWindow().setLayout(950,740);

        //Casat los objetos de la activity
        TextView pregunta = (TextView) dialogo.findViewById(R.id.Txt_VievPreguntar);
        final TextView num = (TextView) dialogo.findViewById(R.id.txtview_num);
        Button btnmenos = (Button) dialogo.findViewById(R.id.btn_menos);
        Button btnmas = (Button) dialogo.findViewById(R.id.btn_mas);
        Button btnaceptar = (Button) dialogo.findViewById(R.id.btn_AceptarPlt);
        Button btncancelar = (Button) dialogo.findViewById(R.id.btn_cancelar);
        //Presentar Datos
        pregunta.setText(plato);
        num.setText(Cantidad);

        //Boton Aceptar
        btnaceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaz.ResultadoCuadroDialogModificar(String.valueOf(cant));
                dialogo.dismiss();
            }
        });

        //Boton Cancelar
        btncancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogo.dismiss();
            }
        });

        //Funcion contador boton menos
        btnmenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cant != 0) {
                    int cont = cant;
                    cant = cont-1;
                    num.setText(String.valueOf(cant));
                }
            }
        });

        //Funcion contador boton mas
        btnmas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cont = cant;
                cant = cont+1;
                num.setText(String.valueOf(cant));
            }
        });
        dialogo.show();

    }

}
