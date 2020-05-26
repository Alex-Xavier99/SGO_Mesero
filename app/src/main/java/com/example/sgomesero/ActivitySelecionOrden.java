package com.example.sgomesero;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivitySelecionOrden extends AppCompatActivity {

    private ListView listViewPlts; // Variable lista de platos
    ArrayAdapter<String> adptlistPlts, adptlistDscrpPlts; // lista de platos, lista descripcion
    ArrayList<String> selcItm = new ArrayList<>();
    AdapterContador viewPltsCont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecion_orden);

        listViewPlts = (ListView)findViewById(R.id.list_ordenes);
        adptlistPlts = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);// Lista de Platos
        adptlistDscrpPlts = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);// Lista descripcion de platos
        viewPltsCont = new AdapterContador(this, adptlistPlts, adptlistDscrpPlts);// Adaptador para mostrar los elementos

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
                                for(int i=0;i<arrayPlatos.length();i++){
                                    JSONObject jsonProducto = arrayPlatos.getJSONObject(i);
                                    String nombrePlato = jsonProducto.getString("plt_nom");
                                    String DscrpPlato = jsonProducto.getString("plt_des");
                                    adptlistPlts.add(nombrePlato);
                                    adptlistDscrpPlts.add(DscrpPlato);
                                }
                                adptlistPlts.notifyDataSetChanged();
                                adptlistDscrpPlts.notifyDataSetChanged();
                                listViewPlts.setAdapter(viewPltsCont);
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

    public void Agregar(View view){
        String items="";
        for(String item: selcItm){
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
        /*Intent tporden= new Intent(this,ActivityTipoOrden.class);
        startActivity(tporden);*/
        finish();
    }
    public void PasarActivity(){
        Intent verificar= new Intent(this,ActivityVerificarOrden.class);
        startActivity(verificar);
        finish();
    }
}
