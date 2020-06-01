package com.example.sgomesero;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
    private String id_emp;
    private String mes_num;
    private String id_pedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_mesa);
        spin = (Spinner)findViewById(R.id.spin_selectmesa);

        id_emp = getIntent().getStringExtra("id_emp");
        mes_num = "";
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
        mes_num = spin.getSelectedItem().toString();//Selecciona el Item de objeto spinner
        mes_num = mes_num.substring(5).trim();

        verificarEstadoMesa();
        siguienteActivity();
    }

    public void verificarEstadoMesa(){
        String url = "https://safe-bastion-34410.herokuapp.com/api/mesas/" + mes_num;

        AndroidNetworking.get(url)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String respuesta = response.getString("status");
                            if(respuesta.equals("403")){
                                //Estado mesa: Orden Terminada
                                //Crear codigo Generar nueva orden
                            }else if(respuesta.equals("202")){
                                //Estado mesa: Orden Iniciada
                                JSONObject pedido = response.getJSONObject("pedidos");
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
        verificarorden.putExtra("id_emp", id_emp);
        verificarorden.putExtra("mes_num", mes_num);
        verificarorden.putExtra("id_pedido",id_pedido);
        startActivity(verificarorden);
    }

    @Override
    public  void onBackPressed(){

        AlertDialog.Builder alerta = new AlertDialog.Builder(ActivitySeleccionMesa.this);
        alerta.setMessage("¿Desea cerrar cesión?")
                .setCancelable(true)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent activityLogin = new Intent(ActivitySeleccionMesa.this,ActivityLogin.class);
                        startActivity(activityLogin);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog titulo = alerta.create();
        titulo.setTitle("Cerrar cesión");
        titulo.show();

    }
}
