package com.example.sgomesero;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

import java.util.ArrayList;

public class ActivityVerificarOrden extends AppCompatActivity {

    private TextView subtitle;
    private ListView list_orden;
    private TextView res_total;
    private  Double res;

    private String id_emp;
    private String mes_num;
    private String id_pedido;

    ArrayList<String> listIdDtall = new ArrayList<>();//Lista de id detalle
    ArrayList<String> listNomPlts = new ArrayList<>();//Lista de nombre platos
    ArrayList<String> listCantPlts = new ArrayList<>();//Lista de cantidad platos
    ArrayList<String> listpvpPlts = new ArrayList<>();//Lista de pvp platos
    ArrayList<String> listvalorPlts = new ArrayList<>();//Lista de valor platos

    private String [][] menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificar_orden);

        subtitle = (TextView)findViewById(R.id.txtview_subtitle);

        id_emp = getIntent().getStringExtra("id_emp");
        mes_num = getIntent().getStringExtra("mes_num");
        id_pedido = getIntent().getStringExtra("id_pedido");

        Toast.makeText(this,"Id pedido: " + id_pedido,Toast.LENGTH_SHORT).show();

        subtitle.setText("Mesa " + mes_num);


        list_orden = (ListView)findViewById(R.id.listview_ordenes);
        res_total = (TextView)findViewById(R.id.txtview_totalres);
        buscarDetalle();
        eliminarItemArrayMenu();

    }
    //Se muestra un menu vacio en el caso de no existir datos en la BDD
    public void menuVacio(boolean vacio){
        int i,j;
        if(vacio) {
            menu = new String[1][5];
            for (i = 0; i < menu.length; i++) {
                for (j = 0; j < menu[i].length; j++) {
                    menu[i][0] = "";
                    menu[i][1] = "";
                    menu[i][2] = "";
                    menu[i][3] = "";
                    menu[i][4] = "";
                }
            }
        }else
            mostrarDetalle();
        presentarDatos();
    }
    //Actualiza la lista en el caso de eliminar platos del menu
    public void presentarDatos(){
        AdapterSelect apt = new AdapterSelect(this,menu);
        list_orden.setAdapter(apt);

        res = apt.Operacion();
        res_total.setText(String.valueOf(res));

    }
    //Agregar las ordenes del cliente
    public void agregarOrden(View view){
        Intent tipoOrden = new Intent(this, ActivityTipoOrden.class);
        tipoOrden.putExtra("id_pedido",id_pedido);
        tipoOrden.putExtra("id_emp",id_emp);
        startActivity(tipoOrden);
    }
    //Ingresar Datos del Cliente
    public void datosFactura(View view){
        Intent factura = new Intent(this, ActivityIngresarDatosFact.class);
        factura.putExtra("id_pedido",id_pedido);
        factura.putExtra("id_emp",id_emp);
        startActivity(factura);
        finish();
    }
    //Se coloca en la matriz para poder mostrar los datos
    public void mostrarDetalle(){
        int i,j;
        menu = new String[listIdDtall.size()][5];
            for (i = 0; i < menu.length; i++) {
                menu[i][0] = listIdDtall.get(i);
                menu[i][1] = listNomPlts.get(i);
                menu[i][2] = listCantPlts.get(i);
                menu[i][3] = listpvpPlts.get(i);
                menu[i][4] = listvalorPlts.get(i);
            }
    }
    //Buscar en la BDD el datalle de acuerdo al pedido
    public void buscarDetalle(){
        String url = "http://safe-bastion-34410.herokuapp.com/api/platopedidos/"+id_pedido;
        if(!id_pedido.equals("")) {
            AndroidNetworking.get(url)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String respuesta = response.getString("status");
                                if (respuesta.equals("200")) {
                                    JSONArray arrayPlatos = response.getJSONArray("data");
                                    for (int i = 0; i < arrayPlatos.length(); i++) {
                                        JSONObject jsonProducto = arrayPlatos.getJSONObject(i);
                                        String IdDetalle = jsonProducto.getString("id");
                                        String nombrePlato = jsonProducto.getString("plt_nom");
                                        String cantPlato = jsonProducto.getString("dtall_cant");
                                        String pvpPlato = jsonProducto.getString("plt_pvp");
                                        String valorPlato = jsonProducto.getString("dtall_valor");

                                        listIdDtall.add(IdDetalle);///Lista ID
                                        listNomPlts.add(nombrePlato);//Lista Plato
                                        listCantPlts.add(cantPlato);//Lista descripcion plato
                                        listpvpPlts.add(pvpPlato);//Lista precio
                                        listvalorPlts.add(valorPlato);//Lista total plato cantidad
                                    }
                                    menuVacio(false);
                                } else {
                                    Toast.makeText(ActivityVerificarOrden.this, "No hay ningun plato disponible.", Toast.LENGTH_LONG).show();
                                    menuVacio(true);
                                }
                            } catch (JSONException e) {
                                Toast.makeText(ActivityVerificarOrden.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            Toast.makeText(ActivityVerificarOrden.this, "Error: " + anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    //Eliminar los Items de la lista
    private void eliminarItemArrayMenu() {
        list_orden.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long lon) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(ActivityVerificarOrden.this);
                alerta.setMessage("¿Desea eliminar " + listNomPlts.get(position) + " ?")
                        .setCancelable(true)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent activityLogin = new Intent(ActivityVerificarOrden.this, ActivityLogin.class);
                                eliminaItemBDD(position);
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog titulo = alerta.create();
                titulo.setTitle("Eliminar Plato");
                titulo.show();
            }

        });
    }
    //Eliman los datos de la base de datos
    public void eliminaItemBDD(int position){
        String id = listIdDtall.get(position);
        String url = "https://safe-bastion-34410.herokuapp.com/api/detalles/"+id;

        JSONObject jsonData = new JSONObject();
        AndroidNetworking.delete(url)
                .addJSONObjectBody(jsonData)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String mensaje = response.getString("message");
                            Toast.makeText(ActivityVerificarOrden.this, mensaje, Toast.LENGTH_SHORT).show();
                            //Se vacian las listas
                            listIdDtall.clear();
                            listNomPlts.clear();
                            listCantPlts.clear();
                            listpvpPlts.clear();
                            listvalorPlts.clear();
                            menu = null;
                            buscarDetalle();
                        } catch (JSONException e) {
                            Toast.makeText(ActivityVerificarOrden.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ActivityVerificarOrden.this, "Error:   " + anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
    @Override
    public  void onBackPressed(){
        finish();
    }

    @Override
    public  void onRestart(){
        super.onRestart();
        //Se vacian las listas
        listIdDtall.clear();
        listNomPlts.clear();
        listCantPlts.clear();
        listpvpPlts.clear();
        listvalorPlts.clear();
        menu = null;

        buscarDetalle();
    }
}
