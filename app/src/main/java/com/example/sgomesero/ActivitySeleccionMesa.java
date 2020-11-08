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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ActivitySeleccionMesa extends AppCompatActivity {

    private Spinner spin;//Sirve para presentar las mesas del restaurante
    private String [] cadspin;//Arreglo con las mesas
    private String id_emp;//id empleado
    private String mes_num;// numero de mesa
    private String token;
    private String id_pedido,id_pedido2;// id pedido

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_mesa);
        //Cast spinner
        spin = (Spinner)findViewById(R.id.spin_selectmesa);
        //Variable (id empleado) para enviar al siguiente activity
        id_emp = getIntent().getStringExtra("id_emp");
        token = getIntent().getStringExtra("token");
        //Variables para el número de mesa y el id pedido
        mes_num = "";
        id_pedido = "";
        id_pedido2="";
        consultarMesas();
    }

    public void consultarMesas(){

        String url = "https://sgo-central-6to.herokuapp.com/api/mesas";
        AndroidNetworking.get(url)
                .addHeaders("Content-type","application/json")
                .addHeaders("Authorization",token)
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
                                //Llenar el arreglo con el numero de mesas
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
                            Toast.makeText(ActivitySeleccionMesa.this, "Error de la Solicitud: "+e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ActivitySeleccionMesa.this, "Error de Servidor: "+anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
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
    }

    public void verificarEstadoMesa(){
        String url = "https://sgo-central-6to.herokuapp.com/api/mesas/" + mes_num;

        AndroidNetworking.get(url)
                .addHeaders("Content-type","application/json")
                .addHeaders("Authorization",token)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            if(status.equals("200")){
                                //Estado mesa: Orden Terminada
                                generarNuevoPedido();
                            }else if(status.equals("202")){
                                //Estado mesa: Orden Iniciada
                                JSONArray pedidoarray = response.getJSONArray("pedidos");
                                int index=0;
                                for(int i =0;i<pedidoarray.length();i++){
                                    index=i;
                                }
                                JSONObject pedido = pedidoarray.getJSONObject(index);
                                id_pedido = pedido.getString("id");
                                if(pedidoarray.length()>1) {
                                    JSONObject pedido2 = pedidoarray.getJSONObject(index-1);
                                    id_pedido2 = pedido2.getString("id");
                                }

                                siguienteActivity();
                            }else{
                                Toast.makeText(ActivitySeleccionMesa.this, "No hay ninguna mesa disponible.", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(ActivitySeleccionMesa.this, "Error de la Solicitud: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ActivitySeleccionMesa.this, "Error de Servidor: "+anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void generarNuevoPedido(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());

        Map<String,String> datos = new HashMap<>();
        datos.put("idEmpleado",id_emp);
        datos.put("idMesa",mes_num);
        datos.put("idEstado","1");
        datos.put("ped_fch",date);
        JSONObject jsonData = new JSONObject(datos);

        AndroidNetworking.post("https://sgo-central-6to.herokuapp.com/api/pedidos")
                .addHeaders("Content-type","application/json")
                .addHeaders("Authorization",token)
                .addJSONObjectBody(jsonData)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String mensaje = response.getString("message");
                            JSONObject data = response.getJSONObject("data");
                            id_pedido = data.getString("id");
                            Toast.makeText(ActivitySeleccionMesa.this, mensaje, Toast.LENGTH_SHORT).show();

                            siguienteActivity();

                        } catch (JSONException e) {
                            Toast.makeText(ActivitySeleccionMesa.this, "Error1: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ActivitySeleccionMesa.this, "Error21: "+anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void siguienteActivity(){
        Intent verificarorden = new Intent(this,ActivityVerificarOrden.class);
        verificarorden.putExtra("id_emp", id_emp);
        verificarorden.putExtra("mes_num", mes_num);
        verificarorden.putExtra("id_pedido",id_pedido);
        verificarorden.putExtra("id_pedido2",id_pedido2);
        verificarorden.putExtra("token", token);
        startActivity(verificarorden);
    }

    @Override
    public  void onBackPressed(){

        AlertDialog.Builder alerta = new AlertDialog.Builder(ActivitySeleccionMesa.this);
        alerta.setMessage("¿Desea cerrar sesión?")
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
        titulo.setTitle("Cerrar sesión");
        titulo.show();

    }
}
