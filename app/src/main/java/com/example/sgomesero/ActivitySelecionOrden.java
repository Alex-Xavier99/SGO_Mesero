package com.example.sgomesero;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

    ArrayList<String> selcItm = new ArrayList<>();
    ArrayList<String> adptlistPlts = new ArrayList<String>();// Lista de Platos
    ArrayList<String> adptlistDscrpPlts = new ArrayList<String>(); //lista descripcion
    ArrayList<String> listpvpPlts =  new ArrayList<String>(); //Lista pvp paltos
    ArrayList<String> listIdPlts =  new ArrayList<String>(); //Lista id platos
    String[][] menu;
    String TpOrden,url,id_pedido,mes_num;
    AdapterContador viewPltsCont ;
    int tmnmenu;
    int[] cantidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecion_orden);


        listViewPlts = (ListView)findViewById(R.id.list_ordenes);
        listIdPlts = new ArrayList<String>();//Lista codigo de platos
        TpOrden = getIntent().getExtras().getString("TpOrden");

        //id_pedido = getIntent().getExtras().getString("id_pedido");
        id_pedido = "1";
        mes_num = "2";
        Actualizar();
        int a = 1;
        switch(TpOrden){
            case "Aperitivo":
                url = "https://safe-bastion-34410.herokuapp.com/api/tipoplatos/"+TpOrden;
                break;
            case "Sopa":
                url = "https://safe-bastion-34410.herokuapp.com/api/tipoplatos/"+TpOrden;
                break;
            case "Plato_Fuerte":
                url = "https://safe-bastion-34410.herokuapp.com/api/tipoplatos/"+TpOrden;
                break;
            case "Ensalada":
                url = "https://safe-bastion-34410.herokuapp.com/api/tipoplatos/"+TpOrden;
                break;
            case "Marisco":
                url = "https://safe-bastion-34410.herokuapp.com/api/tipoplatos/"+TpOrden;
                break;
            case "Bebida":
                url = "https://safe-bastion-34410.herokuapp.com/api/tipoplatos/"+TpOrden;
                break;
            case "Postre":
                url = "https://safe-bastion-34410.herokuapp.com/api/tipoplatos/"+TpOrden;
                break;
            case "Promoción":
                url = "https://safe-bastion-34410.herokuapp.com/api/tipoplatos/"+TpOrden;
                break;
            default:
                Toast.makeText(this,"No se encuentran Registros"+TpOrden,Toast.LENGTH_LONG).show();
        }

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
        pltsPrincipales();
        ingresarPltsOrden();
        finish();

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
               menu[j][4] = String.valueOf(Integer.parseInt(menu[j][2])*Double.parseDouble(menu[j][3]));;
               j++;
           }
       }

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
        Toast.makeText(this,"Platos Ingresados " + String.valueOf(cont),Toast.LENGTH_LONG).show();
        return cont;
    }
    //Se ingresa los pedido a la Base de datos
    private void ingresarPltsOrden() {

        Map<String, String > datos = new HashMap<>();

        for (int i = 0; i < menu.length; i++) {
            datos.put("idPedido", id_pedido);
            datos.put("idPlato",  menu[i][0]);
            //datos.put("idFac", "1");
            datos.put("dtall_cant", menu[i][2]);
            datos.put("dtall_valor", menu[i][4]);
        }
        JSONObject jsonData = new JSONObject(datos);

        AndroidNetworking.post("https://safe-bastion-34410.herokuapp.com/api/detalles")
                .addJSONObjectBody(jsonData)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String mensaje = response.getString("message");
                            Toast.makeText(ActivitySelecionOrden.this, mensaje, Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            Toast.makeText(ActivitySelecionOrden.this, "Error:1 " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
