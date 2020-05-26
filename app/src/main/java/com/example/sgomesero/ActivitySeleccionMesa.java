package com.example.sgomesero;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ActivitySeleccionMesa extends AppCompatActivity {
    private Spinner spin;
    private String [] ordenes;
    String [] cadspin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_mesa);
        spin = (Spinner)findViewById(R.id.spin_selectmesa);


        AndroidNetworking.get("https://safe-bastion-34410.herokuapp.com/api/mesas")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String respuesta = response.getString("status");
                            if(respuesta.equals("200")){
                                JSONArray arrayPlatos = response.getJSONArray("data");
                                cadspin = new String[arrayPlatos.length()];
                                for(int i=0;i<arrayPlatos.length();i++){
                                    JSONObject jsonProducto = arrayPlatos.getJSONObject(i);
                                    String id = jsonProducto.getString("id");
                                    cadspin[i]=("Mesa " + id);
                                }
                                Llenar();
                            }else{
                                Toast.makeText(ActivitySeleccionMesa.this, "No hay ninguna mesa disponible.", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(ActivitySeleccionMesa.this, "Error: "+e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ActivitySeleccionMesa.this, "Error: "+anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                    }
                });



    }

    public void Llenar(){
        if(!cadspin.equals(null)) {
            ArrayAdapter<String> opera = new ArrayAdapter<String>(this, R.layout.spinmesaselct, cadspin);//(Contexto, tipo de spiner,nombre del spin)
            spin.setAdapter(opera);
        }
    }

    public void Aceptar(View view){
        String selec, mesa ;
        selec = spin.getSelectedItem().toString();//Selecciona el Item de objeto spinner

        PasarActivity(selec);
    }
    public void PasarActivity(String mesa){
        Intent verificarorden = new Intent(this,ActivityVerificarOrden.class);
        verificarorden.putExtra("m1",mesa);
        startActivity(verificarorden);
        //finish();
    }
    @Override
    public  void onBackPressed(){
        /*
        Intent login = new Intent(this,ActivityLogin.class);
        startActivity(login);
         */
        finish();
    }
}
