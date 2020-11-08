package com.example.sgomesero;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DialogCantidadFact {
    public interface FinalizarCuadroDialogo{
        void ResultadoCuadroDialogCantFacturar(String cantNew, String cantAnt);
    }
    private FinalizarCuadroDialogo interfaz;
    String plato;
    int cantNew, cantAnt;

    public DialogCantidadFact(final Context contexto, FinalizarCuadroDialogo actividad, String NomPlato, final String Cantidad){

        interfaz = actividad;
        plato = NomPlato;
        cantNew = Integer.parseInt(Cantidad);

        final Dialog dialogo = new Dialog(contexto);
        dialogo.requestWindowFeature((Window.FEATURE_NO_TITLE));
        dialogo.setCancelable(false);
        dialogo.setContentView((R.layout.dialog_cantidad_fact));
        dialogo.getWindow().setLayout(950,740);

        //Casat los objetos de la activity
        TextView pregunta = (TextView) dialogo.findViewById(R.id.Txtv_PltFact);
        final TextView num = (TextView) dialogo.findViewById(R.id.txtview_num);
        Button btnmenos = (Button) dialogo.findViewById(R.id.btn_menos);
        Button btnmas = (Button) dialogo.findViewById(R.id.btn_mas);
        Button btnaceptar = (Button) dialogo.findViewById(R.id.Btn_AceptarFact);
        Button btncancelar = (Button) dialogo.findViewById(R.id.Btn_CancelFact);

        //Presentar Datos
        pregunta.setText(plato);
        num.setText(Cantidad);

        //Boton Aceptar
        btnaceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cantAnt =Integer.valueOf(Cantidad)-cantNew;
                interfaz.ResultadoCuadroDialogCantFacturar(String.valueOf(cantNew),String.valueOf(cantAnt));
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
                if(cantNew != 0) {
                    int cont = cantNew;
                    cantNew = cont-1;
                    num.setText(String.valueOf(cantNew));
                }
            }
        });

        //Funcion contador boton mas
        btnmas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cantNew < Integer.valueOf(Cantidad))
                {
                    int cont = cantNew;
                    cantNew = cont + 1;
                    num.setText(String.valueOf(cantNew));
                }
                else
                    Toast.makeText(contexto, "La cantidad a facturar no puede ser mayor a "+String.valueOf(Cantidad), Toast.LENGTH_LONG).show();
            }
        });
        dialogo.show();
    }
}
