package com.example.sgomesero;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ActivitySelecionOrden extends AppCompatActivity {

    private ListView listorden;
    private String [] ordenes;
    ArrayList<String> seleccionaritems = new ArrayList<>();
    ArrayAdapter<String> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecion_orden);

        listorden = (ListView)findViewById(R.id.list_ordenes);
        lista = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);

        listorden.setAdapter(lista);

        AndroidNetworking.get("https://safe-bastion-34410.herokuapp.com/api/platos")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String respuesta = response.getString("status");
                            if(respuesta.equals("200")){
                                JSONArray arrayPlatos = response.getJSONArray("data:");
                                ordenes = new String[arrayPlatos.length()];
                                for(int i=0;i<arrayPlatos.length();i++){
                                    JSONObject jsonProducto = arrayPlatos.getJSONObject(i);
                                    String nombrePlato = jsonProducto.getString("plt_nom");
                                    lista.add(nombrePlato);
                                }
                                lista.notifyDataSetChanged();
                            }else{
                                Toast.makeText(ActivitySelecionOrden.this, "No hay ningun plato disponible.", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(ActivitySelecionOrden.this, "Error: "+e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ActivitySelecionOrden.this, "Error: "+anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                    }
                });


        /*
        if(ordenes != null) {

            listorden.setAdapter(lista);
            lista = new ArrayAdapter<String>(this, R.layout.row_layout, R.id.checkedTextView, ordenes);


            listorden.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String seleccionaritem = ((TextView) view).getText().toString();
                    if (seleccionaritems.contains(seleccionaritem)) {
                        seleccionaritems.remove(seleccionaritem);
                    } else {
                        seleccionaritems.add(seleccionaritem);
                    }
                }
            });
        }

         */


    }

    public boolean realizaConsulta(){
        boolean es = false;
        AndroidNetworking.get("https://safe-bastion-34410.herokuapp.com/api/platos")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String respuesta = response.getString("status");
                            if(respuesta.equals("200")){
                                JSONArray arrayPlatos = response.getJSONArray("data:");
                                ordenes = new String[arrayPlatos.length()];
                                for(int i=0;i<arrayPlatos.length();i++){
                                    JSONObject jsonProducto = arrayPlatos.getJSONObject(i);
                                    String nombrePlato = jsonProducto.getString("plt_nom");
                                    ordenes[i] = nombrePlato;
                                }
                            }else{
                                Toast.makeText(ActivitySelecionOrden.this, "No hay ningun plato disponible.", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(ActivitySelecionOrden.this, "Error1: "+e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ActivitySelecionOrden.this, "Error2: "+anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                    }
                });

        if (ordenes != null) {
            es=true;
        }

        return es;
    }

    public void Agregar(View view){
        String items="";
        for(String item:seleccionaritems){
            items+="-"+item+"\n";
        }
        Toast.makeText(this, "Seleccionaste \n"+items, Toast.LENGTH_SHORT).show();
        PasarActivity();
    }
    public void Regresar(View view){
        PasarActivity();
    }
    @Override
    public  void onBackPressed(){
        Intent tporden= new Intent(this,ActivityTipoOrden.class);
        startActivity(tporden);
        finish();
    }
    public void PasarActivity(){
        Intent verificar= new Intent(this,ActivityVerificarOrden.class);
        startActivity(verificar);
        finish();
    }
}
