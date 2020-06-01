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
    private String [] cadspin;
    private String num_mesa;
    private String id_pedido;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_mesa);
        spin = (Spinner)findViewById(R.id.spin_selectmesa);

        num_mesa = "";
        id_pedido = "";

        consultarMesas();
    }

    public void consultarMesas(){
        String url = "https://safe-bastion-34410.herokuapp.com/api/mesas";
        AndroidNetworking.get(url)
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
                                llenarSpinner();
                            }else{
                                Toast.makeText(ActivitySeleccionMesa.this, "No hay ninguna mesa disponible.", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(ActivitySeleccionMesa.this, "Error1: "+e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ActivitySeleccionMesa.this, "Error2: "+anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void llenarSpinner(){
        if(!cadspin.equals(null)) {
            ArrayAdapter<String> opera = new ArrayAdapter<String>(this, R.layout.spinmesaselct, cadspin);//(Contexto, tipo de spiner,nombre del spin)
            spin.setAdapter(opera);
        }
    }

    public void aceptar(View view){
        num_mesa = spin.getSelectedItem().toString();//Selecciona el Item de objeto spinner
        num_mesa = num_mesa.substring(5).trim();

        verificarEstadoMesa();
        siguienteActivity();
    }

    public void verificarEstadoMesa(){
        String url = "https://safe-bastion-34410.herokuapp.com/api/mesas/" + num_mesa;

        AndroidNetworking.get(url)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String respuesta = response.getString("status");
                            if(respuesta.equals("200")){
                                //Estado mesa: Orden Terminada
                                //Crear codigo Generar nueva orden
                            }else if(respuesta.equals("202")){
                                //Estado mesa: Orden Iniciada
                                JSONArray arrayPedidos = response.getJSONArray("pedidos");
                                JSONObject pedido = arrayPedidos.getJSONObject(0);
                                id_pedido=pedido.getString("id");
                            }else{
                                Toast.makeText(ActivitySeleccionMesa.this, "No hay ninguna mesa disponible.", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(ActivitySeleccionMesa.this, "Error1: "+e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ActivitySeleccionMesa.this, "Error2: "+anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void siguienteActivity(){
        Intent verificarorden = new Intent(this,ActivityVerificarOrden.class);
        verificarorden.putExtra("mes_num", num_mesa);
        verificarorden.putExtra("id_pedido",id_pedido);
        startActivity(verificarorden);
    }

    @Override
    public  void onBackPressed(){
        //Crear alerta para cierre de cesión

        finish();
    }
}
