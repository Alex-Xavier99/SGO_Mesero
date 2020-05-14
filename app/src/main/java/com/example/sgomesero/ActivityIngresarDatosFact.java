package com.example.sgomesero;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActivityIngresarDatosFact extends AppCompatActivity {

    private TextView subtitle;
    private EditText razonsocial, cedula, direccion, telefono,correo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar_datos_fact);

        razonsocial = (EditText)findViewById(R.id.txt_razonsocial);//Servira para ingresar leer los datos de razon social
        cedula = (EditText)findViewById(R.id.txt_cedula);//Servira para leer el dato de cedula
        direccion = (EditText)findViewById(R.id.txt_direccion);//Servira para leer los datos ingresados de la direccion
        telefono = (EditText)findViewById(R.id.txt_telefono);//Servira para leer los datos del telefono
        correo = (EditText)findViewById(R.id.txt_correo);//Servira para leer los datos del correo
    }

    public void CrearCliente(View view){
        ingresarCliente();
    }

    private void ingresarCliente(){
        if(isValidarCampos()){

            String rzso = razonsocial.getText().toString();
            String cdla = cedula.getText().toString();
            String dire = direccion.getText().toString();
            String telf = telefono.getText().toString();
            String corr = correo.getText().toString();


            Map<String,String> datos = new HashMap<>();
            datos.put("cli_ci",cdla);
            datos.put("cli_nom",rzso);
            datos.put("cli_dir",dire);
            datos.put("cli_email",corr);
            datos.put("cli_telf",telf);
            JSONObject jsonData = new JSONObject(datos);

            AndroidNetworking.post("https://safe-bastion-34410.herokuapp.com/api/cliente")
                    .addJSONObjectBody(jsonData)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {

                                String mensaje = response.getString("message");
                                Toast.makeText(ActivityIngresarDatosFact.this, mensaje, Toast.LENGTH_SHORT).show();

                            } catch (JSONException e) {
                                Toast.makeText(ActivityIngresarDatosFact.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            Toast.makeText(ActivityIngresarDatosFact.this, "Error: "+anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }else{
            Toast.makeText(this, "No se puede insertar un cliente si existen campos vacios", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidarCampos(){
        return !razonsocial.getText().toString().trim().isEmpty() &&
                !cedula.getText().toString().trim().isEmpty() &&
                !direccion.getText().toString().trim().isEmpty() &&
                !telefono.getText().toString().trim().isEmpty() &&
                !correo.getText().toString().trim().isEmpty();
    }

    public void DatosFactura(View view){
        Intent verificarorden = new Intent(this, ActivityVerificarOrden.class);
        startActivity(verificarorden);
        finish();
    }
}
