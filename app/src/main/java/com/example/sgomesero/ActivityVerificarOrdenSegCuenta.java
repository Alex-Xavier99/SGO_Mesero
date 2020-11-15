package com.example.sgomesero;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
import java.util.HashMap;
import java.util.Map;

public class ActivityVerificarOrdenSegCuenta extends AppCompatActivity  {

    private TextView subtitle;
    private ListView list_orden;
    private TextView res_total;
    private  Double res;

    private String id_pedido2;
    private String id_emp;
    private String mes_num;
    private String token;

    //Context contexto;
    int posPlato;
    //Lista de menu
    ArrayList<String> listIdPlts= new ArrayList<>();//Lista de valor platos
    ArrayList<String> listIdDtall = new ArrayList<>();//Lista de id detalle
    ArrayList<String> listNomPlts = new ArrayList<>();//Lista de nombre platos
    ArrayList<String> listCantPlts = new ArrayList<>();//Lista de cantidad platos
    ArrayList<String> listpvpPlts = new ArrayList<>();//Lista de pvp platos
    ArrayList<String> listvalorPlts = new ArrayList<>();//Lista de valor platos

    private String [][] menu;
    final LoadingDialog loadingDialog = new LoadingDialog(ActivityVerificarOrdenSegCuenta.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificar_orden_seg_cuenta);

        subtitle = (TextView)findViewById(R.id.txtview_subtitle);
        //Se almacena los parametros por el paso de actividades
        id_emp = getIntent().getStringExtra("id_emp");
        mes_num = getIntent().getStringExtra("mes_num");
        id_pedido2 = getIntent().getStringExtra("id_pedido");
        token = getIntent().getStringExtra("token");
        //Se pasa el parametro del numero de mesa
        subtitle.setText("Mesa " + mes_num);
        //Cast de listas
        list_orden = (ListView)findViewById(R.id.listview_ordenes);
        res_total = (TextView)findViewById(R.id.txtview_totalres);
        loadingDialog.startLoadingDialog();
        buscarDetalle();

    }
    //Buscar en la BDD el datalle de acuerdo al pedido
    public void buscarDetalle(){
        String url = "https://sgo-central-6to.herokuapp.com/api/platopedidos/"+id_pedido2;
        if(!id_pedido2.equals("")) {
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
                                if (respuesta.equals("200")) {
                                    JSONArray arrayPlatos = response.getJSONArray("data");
                                    for (int i = 0; i < arrayPlatos.length(); i++) {
                                        JSONObject jsonProducto = arrayPlatos.getJSONObject(i);
                                        String IdDetalle = jsonProducto.getString("id");
                                        String Idplt= jsonProducto.getString("idPlato");
                                        String nombrePlato = jsonProducto.getString("plt_nom");
                                        String cantPlato = jsonProducto.getString("dtall_cant");
                                        String pvpPlato = jsonProducto.getString("plt_pvp");
                                        String valorPlato = jsonProducto.getString("dtall_valor");

                                        listIdDtall.add(IdDetalle);///Lista ID detalles
                                        listIdPlts.add(Idplt);//Lista ID plato
                                        listNomPlts.add(nombrePlato);//Lista Plato
                                        listCantPlts.add(cantPlato);//Lista descripcion plato
                                        listpvpPlts.add(pvpPlato);//Lista precio
                                        listvalorPlts.add(valorPlato);//Lista total plato cantidad
                                    }
                                    menuVacio(false);
                                } else {
                                    Toast.makeText(ActivityVerificarOrdenSegCuenta.this, "No hay ningun plato disponible.", Toast.LENGTH_LONG).show();
                                    menuVacio(true);
                                }
                                loadingDialog.dismissDialog();
                            } catch (JSONException e) {
                                Toast.makeText(ActivityVerificarOrdenSegCuenta.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                loadingDialog.dismissDialog();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            Toast.makeText(ActivityVerificarOrdenSegCuenta.this, "Error: " + anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                            loadingDialog.dismissDialog();
                        }
                    });
        }
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
    //Actualiza la lista en el caso de eliminar platos del menu
    public void presentarDatos(){
        AdapterSelect apt = new AdapterSelect(this,menu);
        list_orden.setAdapter(apt);

        res = apt.Operacion();
        res_total.setText(String.valueOf(res));

    }

    //Ingresar Datos del Cliente
    public void datosFactura(View view){

        if(!menu[0][0].equals("")) {
            Intent factura = new Intent(this, ActivityIngresarDatosFact.class);
            factura.putExtra("id_pedido", id_pedido2);
            factura.putExtra("id_emp", id_emp);
            factura.putExtra("token", token);

            actualizarEstadoFact(id_pedido2);
            startActivity(factura);
            finish();
        }else
        {
            Toast.makeText(this,"No hay platos disponibles",Toast.LENGTH_LONG).show();
        }
    }
    //Agregar otro plato a la cuenta
    public void agregarPlatoCnta(View view){
        finish();
    }
    //Vaciar las Listas
    public void VaciarListas(){
        //Se vacian las listas
        listIdDtall.clear();
        listNomPlts.clear();
        listCantPlts.clear();
        listpvpPlts.clear();
        listvalorPlts.clear();
        menu = null;
        buscarDetalle();
    }
    @Override
    public  void onBackPressed(){
        finish();
    }
    //Se actualiza el detalle de la factura
    public void actualizarEstadoFact(String pedido){
        String url = "http://sgo-central-6to.herokuapp.com/api/pedidos/" + pedido;
        Map<String,String> datos = new HashMap<>();
        datos.put("idEstado","2");
        JSONObject jsonData = new JSONObject(datos);

        AndroidNetworking.patch(url)
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
                            Toast.makeText(ActivityVerificarOrdenSegCuenta.this, mensaje, Toast.LENGTH_SHORT).show();


                        } catch (JSONException e) {
                            Toast.makeText(ActivityVerificarOrdenSegCuenta.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ActivityVerificarOrdenSegCuenta.this, "Error Servidor: "+anError.getErrorDetail(), Toast.LENGTH_SHORT).show();

                    }
                });
    }
}