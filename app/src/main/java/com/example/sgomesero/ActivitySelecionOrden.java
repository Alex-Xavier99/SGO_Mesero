package com.example.sgomesero;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
import java.util.List;

public class ActivitySelecionOrden extends AppCompatActivity {

    private ListView listViewPlts; // Variable lista de platos

    ArrayList<String> selcItm = new ArrayList<>();
    ArrayList<String> adptlistPlts = new ArrayList<String>();// Lista de Platos
    ArrayList<String> adptlistDscrpPlts = new ArrayList<String>(); //lista descripcion
    ArrayList<String> listpvpPlts =  new ArrayList<String>(); //Lista pvp paltos
    ArrayList<String> listIdPlts =  new ArrayList<String>(); //Lista id platos
    String[][] menu;
    AdapterContador viewPltsCont ;
    int[] cantidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecion_orden);

        listViewPlts = (ListView)findViewById(R.id.list_ordenes);
        listIdPlts = new ArrayList<String>();//Lista codigo de platos
        Actualizar();

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
                                    String IdPlato = jsonProducto.getString("id");
                                    String nombrePlato = jsonProducto.getString("plt_nom");
                                    String DscrpPlato = jsonProducto.getString("plt_des");
                                    String pvpPlato = jsonProducto.getString("plt_pvp");

                                    listIdPlts.add(IdPlato);///Lista ID
                                    adptlistPlts.add(nombrePlato);//Lista Plato
                                    adptlistDscrpPlts.add(DscrpPlato);//Lista descripcion plato
                                    listpvpPlts.add(pvpPlato);//Lista precio

                                }
                                //adptlistPlts.notifyAll();
                                //adptlistDscrpPlts.notify();
                                Actualizar();

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
    public void Actualizar(){
        cantidad = new int[listIdPlts.size()];
        for(int j = 0 ;j<listIdPlts.size();j++){
            cantidad[j]=0;
        }
        viewPltsCont = new AdapterContador(this, adptlistPlts, adptlistDscrpPlts,cantidad);// Adaptador para mostrar los elementos
        listViewPlts.setAdapter(viewPltsCont);
    }
    public void Agregar(View view){

        Intent verificarorden = new Intent(this,ActivityVerificarOrden.class);
        PltsPrincipales();
        verificarorden.putExtra("menu",(String [][]) menu);

        startActivity(verificarorden);
        finish();

    }
    public void PltsPrincipales(){
        int j,cont = ContDifCero();
        j = 0;
        menu = new String[cont][5];
       for(int i=0;i<listIdPlts.size(); i++)
       {
           if(cantidad[i] != 0) {
               menu[j][0] = listIdPlts.get(i);
               menu[j][1] = adptlistPlts.get(i);
               menu[j][2] = String.valueOf(cantidad[i]);
               menu[j][3] = listpvpPlts.get(i);
               menu[j][4] = String.valueOf(Double.parseDouble(menu[j][2])*Double.parseDouble(menu[j][3]));;
               j++;
           }
       }

    }
    public int ContDifCero(){
        int i, cont = 0;
        cantidad = viewPltsCont.Cantidad();
        for (i=0;i<listIdPlts.size();i++) {
            if (cantidad[i] != 0) {
                cont++;
            }
        }
        Toast.makeText(this,"Numero cont" + String.valueOf(cont),Toast.LENGTH_LONG).show();
        return cont;
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
