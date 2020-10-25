package com.example.sgomesero;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class DialogBuscarCliente {
    //public String cedulacliente = "";
    public interface Finalizarcuadrodialogo{
        void ResultadoCuadroDialogo(String cedulacliente);
    }
    private Finalizarcuadrodialogo interfaz;
    public DialogBuscarCliente(Context contexto, Finalizarcuadrodialogo actividad){
        interfaz = actividad;
        final Dialog dialogo = new Dialog(contexto);
        dialogo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogo.setCancelable(false);
        dialogo.setContentView(R.layout.buscar_cliente);
        dialogo.getWindow().setLayout(950,500);
        //dialogo.setCanceledOnTouchOutside(true);
        //Cast de objetos
        final EditText cedula = (EditText) dialogo.findViewById(R.id.txtedit_cedula);
        Button btnbuscar = (Button) dialogo.findViewById(R.id.btn_Aceptar);
        Button btncancerlar = (Button) dialogo.findViewById(R.id.btn_cancelar);

        //Al presionar el boton buscar
        btnbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!cedula.getText().toString().equals("")) {
                    interfaz.ResultadoCuadroDialogo(cedula.getText().toString());
                    dialogo.dismiss();
                }

            }
        });

       btncancerlar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogo.dismiss();
            }
        });
        dialogo.show();
    }
}
