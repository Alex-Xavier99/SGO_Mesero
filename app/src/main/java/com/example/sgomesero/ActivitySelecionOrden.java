package com.example.sgomesero;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
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
import java.util.HashMap;
import java.util.Map;

public class ActivitySelecionOrden extends AppCompatActivity {

    private ListView listViewPlts; // Variable lista de platos
    //Listas
    ArrayList<String> selcItm = new ArrayList<>();
    ArrayList<String> adptlistPlts = new ArrayList<String>();// Lista de Platos
    ArrayList<String> adptlistDscrpPlts = new ArrayList<String>(); //lista descripcion
    ArrayList<String> listpvpPlts =  new ArrayList<String>(); //Lista pvp paltos
    ArrayList<String> listIdPlts =  new ArrayList<String>(); //Lista id platos
    //Menu contiene el detalle del pedido
    String[][] menu;
    //Variables para el paso de parametros
    String TpOrden,url,id_pedido,id_emp;
    //Adaptador de vista
    AdapterContador viewPltsCont ;
    int tmnmenu;
    int[] cantidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecion_orden);


        listViewPlts = (ListView)findViewById(R.id.list_ordenes);
        listIdPlts = new ArrayList<String>();//Lista codigo de platos

        //Almacenamos las variables del paso de activitys
        TpOrden = getIntent().getExtras().getString("TpOrden");
        id_pedido = getIntent().getExtras().getString("id_pedido");
        id_emp = getIntent().getExtras().getString("id_emp");

        presentarPlts();
        //Comprobamos si la variable no es vacia o nula para poder enviar la url
        if(!TpOrden.isEmpty() && !TpOrden.equals(null))
        {
            url = "https://sgo-central-6to.herokuapp.com/api/tipoplatos/"+TpOrden;
        }
        else
            Toast.makeText(this,"No se encuentran Registros"+TpOrden,Toast.LENGTH_LONG).show();

        AndroidNetworking.get(url)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String respuesta = response.getString("status");
                            if(respuesta.equals("202")){
                                JSONArray arrayPlatos = response.getJSONArray("data");
                                for(int i=0;i<arrayPlatos.length();i++){
                                    JSONObject jsonProducto = arrayPlatos.getJSONObject(i);
                                    
                                    String IdPlato = jsonProducto.getString("id");
                                    String nombrePlato = jsonProducto.getString("plt_nom");
                                    String DscrpPlato = jsonProducto.getString("plt_des");
                                    String pvpPlato = jsonProducto.getString("plt_pvp");
                                    //Ingresar los datos en las listas
                                    listIdPlts.add(IdPlato);///Lista ID
                                    adptlistPlts.add(nombrePlato);//Lista Plato
                                    adptlistDscrpPlts.add(DscrpPlato);//Lista descripcion plato
                                    listpvpPlts.add(pvpPlato);//Lista precio

                                }
                                presentarPlts();

                            }else{
                                Toast.makeText(ActivitySelecionOrden.this, "No hay ningun plato disponible.", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(ActivitySelecionOrden.this, "Error: "+e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ActivitySelecionOrden.this, "Error de Servidor: "+anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
    public void presentarPlts(){
        cantidad = new int[listIdPlts.size()];
        for(int j = 0 ;j<listIdPlts.size();j++){
            cantidad[j]=0;
        }
        viewPltsCont = new AdapterContador(this, adptlistPlts, adptlistDscrpPlts,cantidad);// Adaptador para mostrar los elementos
        listViewPlts.setAdapter(viewPltsCont);

    }
    public void Agregar(View view){
        pltsPrincipales();

    }
    //Se ingresa a la matriz menu los datos validos
    public void pltsPrincipales(){
        int j;
        tmnmenu = contDifCero();
        j = 0;
        menu = new String[tmnmenu][5];
       for(int i=0;i<listIdPlts.size(); i++)
       {
           if(cantidad[i] != 0) {
               menu[j][0] = listIdPlts.get(i);
               menu[j][1] = adptlistPlts.get(i);
               menu[j][2] = String.valueOf(cantidad[i]);
               menu[j][3] = listpvpPlts.get(i);
               menu[j][4] = String.valueOf(Integer.parseInt(menu[j][2])*Double.parseDouble(menu[j][3]));
               ingresarPltsOrden(menu[j][0], menu[j][2],menu[j][4]);
               j++;
           }
       }
        new Handler().postDelayed(new Runnable() {

            public void run() {
                finish();
            }
        }, 500);


    }
    //La funcion cuenta el numero de platos que se va ingresar
    public int contDifCero(){
        int i, cont = 0;
        cantidad = viewPltsCont.Cantidad();
        for (i=0;i<listIdPlts.size();i++) {
            if (cantidad[i] != 0) {
                cont++;
            }
        }
        return cont;
    }
    //Se ingresa los pedido a la Base de datos
    private void ingresarPltsOrden(String idplato, String dtallcant,String dtallvalor) {

        Map<String,String> datos = new HashMap<>();

            datos.put("idPedido", id_pedido);
            datos.put("idPlato",  idplato);
            datos.put("dtall_cant", dtallcant);
            datos.put("dtall_valor",  dtallvalor);

        JSONObject jsonData = new JSONObject(datos);

        AndroidNetworking.post("https://sgo-central-6to.herokuapp.com/api/detalles")
                .addJSONObjectBody(jsonData)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String mensaje = response.getString("message");
                            Toast.makeText(ActivitySelecionOrden.this, mensaje , Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            Toast.makeText(ActivitySelecionOrden.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ActivitySelecionOrden.this, "Error:   " + anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void Cancelar(View view){
        finish();
    }
    @Override
    public  void onBackPressed(){
        finish();
    }
}
