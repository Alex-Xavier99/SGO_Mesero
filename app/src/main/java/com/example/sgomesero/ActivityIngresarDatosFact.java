package com.example.sgomesero;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ActivityIngresarDatosFact extends AppCompatActivity {

    private TextView subtitle;
    private EditText razonsocial, cedula, direccion, telefono,correo;
    private String subti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar_datos_fact);

        subtitle = (TextView)findViewById(R.id.txtview_nromesa);//Subtitulo de la activity
        razonsocial = (EditText)findViewById(R.id.txt_razonsocial);//Servira para ingresar leer los datos de razon social
        cedula = (EditText)findViewById(R.id.txt_cedula);//Servira para leer el dato de cedula
        direccion = (EditText)findViewById(R.id.txt_direccion);//Servira para leer los datos ingresados de la direccion
        telefono = (EditText)findViewById(R.id.txt_telefono);//Servira para leer los datos del telefono
        correo = (EditText)findViewById(R.id.txt_correo);//Servira para leer los datos del correo

        subti = getIntent().getStringExtra("m1");
        subtitle.setText(subti);
    }
    //Regresea a la Activity Seleccion Mesa
    public void Regresar(View view){
        Intent regmesa = new Intent(this, ActivitySeleccionMesa.class);
        startActivity(regmesa);
        finish();
    }
}
