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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ActivityVerificarOrden extends AppCompatActivity implements DialogModificarPlato.FinalizarCuadroDialogo, DialogSelectPlato.FinalizarCuadroDialogo,DialogCantidadFact.FinalizarCuadroDialogo {

    private TextView subtitle;
    private ListView list_orden;
    private TextView res_total;
    private  Double res;
    //Almacena las variables de paso de parametros
    private String id_emp;
    private String mes_num;
    private String id_pedido;
    private  String id_pedido2;
    private  String id_Estado;
    private String token;

    //Lista de menu
    ArrayList<String> listIdPlts= new ArrayList<>();//Lista de valor platos
    ArrayList<String> listIdDtall = new ArrayList<>();//Lista de id detalle
    ArrayList<String> listNomPlts = new ArrayList<>();//Lista de nombre platos
    ArrayList<String> listCantPlts = new ArrayList<>();//Lista de cantidad platos
    ArrayList<String> listpvpPlts = new ArrayList<>();//Lista de pvp platos
    ArrayList<String> listvalorPlts = new ArrayList<>();//Lista de valor platos

    private String [][] menu;
    //Contexto para cuadro de dialogo
    Context contexto;
    int posPlato;
    //Dialogo de carga
    final LoadingDialog loadingDialog = new LoadingDialog(ActivityVerificarOrden.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificar_orden);

        subtitle = (TextView)findViewById(R.id.txtview_subtitle);
        //Se almacena los parametros por el paso de actividades
        id_emp = getIntent().getStringExtra("id_emp");
        mes_num = getIntent().getStringExtra("mes_num");
        id_pedido = getIntent().getStringExtra("id_pedido");
        id_pedido2 = getIntent().getStringExtra("id_pedido2");
        token = getIntent().getStringExtra("token");
        //Se pasa el parametro del numero de mesa
        subtitle.setText("Mesa " + mes_num);

        //Cast de listas
        list_orden = (ListView)findViewById(R.id.listview_ordenes);
        res_total = (TextView)findViewById(R.id.txtview_totalres);
        loadingDialog.startLoadingDialog();
        buscarDetalle();
        seleccionarOpcionPlato();

        contexto = this;

    }
    //Cambiar valor id pedido 2
    public void cambiarIdPedido2(String idpedido2){
        id_pedido2 = idpedido2;
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
    //Agregar las ordenes del cliente
    public void agregarOrden(View view){
        Intent tipoOrden = new Intent(this, ActivityTipoOrden.class);
        tipoOrden.putExtra("id_pedido",id_pedido);
        tipoOrden.putExtra("id_emp",id_emp);
        tipoOrden.putExtra("token", token);
        startActivity(tipoOrden);
    }
    //Ingresar Datos del Cliente
    public void datosFactura(View view){

        if(!menu[0][0].equals("")) {
            Intent factura = new Intent(this, ActivityIngresarDatosFact.class);
            factura.putExtra("id_pedido", id_pedido);
            factura.putExtra("id_emp", id_emp);
            factura.putExtra("token", token);
            startActivity(factura);
            finish();
        }else
        {
            Toast.makeText(this,"No hay platos disponibles",Toast.LENGTH_LONG).show();
        }
    }

    //Boton Ver activity verificar segunda orden
    public void verSegundaOrden(View view){
        if(!id_pedido2.isEmpty()) {
            IniciarActivitySegundaCnta();
        }
        else
            Toast.makeText(ActivityVerificarOrden.this, "No existe ordenes en la segunda cuenta", Toast.LENGTH_LONG).show();
    }

    //Buscar en la BDD el datalle de acuerdo al pedido
    public void buscarDetalle(){

        String url = "https://sgo-central-6to.herokuapp.com/api/platopedidos/"+id_pedido;
        if(!id_pedido.equals("")) {
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
                                    Toast.makeText(ActivityVerificarOrden.this, "No hay ningun plato disponible.", Toast.LENGTH_LONG).show();

                                    menuVacio(true);
                                }
                                loadingDialog.dismissDialog();
                            } catch (JSONException e) {
                                Toast.makeText(ActivityVerificarOrden.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                loadingDialog.dismissDialog();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            Toast.makeText(ActivityVerificarOrden.this, "Error: " + anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                            loadingDialog.dismissDialog();
                        }
                    });
        }
    }
    //Eliminar los Items de la lista
    private void seleccionarOpcionPlato() {
        list_orden.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long lon) {
                //Llamada al cuadro de dialogo
                posPlato = position;
                new DialogSelectPlato(contexto, ActivityVerificarOrden.this,listNomPlts.get(posPlato));
            }

        });
    }
    //Resultados del cuadro de dialogo Select Plato
    @Override
    public void ResultadoCuadroDialogSelectPlato(String Opcion) {
        if(Opcion.equals("Modificar"))
            new DialogModificarPlato(contexto, ActivityVerificarOrden.this,listNomPlts.get(posPlato),listCantPlts.get(posPlato));
        else if(Opcion.equals("Dividir")){
            loadingDialog.startLoadingDialog();
            if(id_pedido2.isEmpty()){
                verificarIdEstadoFact(id_pedido2);
                new DialogCantidadFact(contexto,ActivityVerificarOrden.this,listNomPlts.get(posPlato),listCantPlts.get(posPlato));

            }
            else {
                verificarIdEstadoFact(id_pedido2);
                new DialogCantidadFact(contexto, ActivityVerificarOrden.this, listNomPlts.get(posPlato), listCantPlts.get(posPlato));
            }
        }
        else {
            loadingDialog.startLoadingDialog();
            eliminaItemBDD(posPlato);
        }
    }

    //Resultados del Cudadro de dialogo Modificar
    @Override
    public void ResultadoCuadroDialogModificar(String cant) {
        int cantidad = Integer.parseInt(cant);
        if(cantidad!=0){
            loadingDialog.startLoadingDialog();
            actualizarDetalle(posPlato,cantidad);
        }
    }
    @Override
    //Resultados del cuadro de dialogo Cantidad a facturar
    public void ResultadoCuadroDialogCantFacturar(String cantNew, String cantAnt) {

        if(!cantAnt.equals("0")){
            ResultadoCuadroDialogModificar(cantAnt);
        }
        else {
            eliminaItemBDD(posPlato);
        }
        if(!cantNew.equals("0")){
            Double pvp = Double.parseDouble(listpvpPlts.get(posPlato));// precio de venta
            String total = String.valueOf(Integer.valueOf(cantNew)*pvp);// calcular el total
            ingresarNewDetalle(listIdPlts.get(posPlato),cantNew,total);
            IniciarActivitySegundaCnta();
        }
    }
    //Iniciar la Activity Verificar Orden Segunda Cuenta
    public void  IniciarActivitySegundaCnta(){

            Intent segundaCuenta = new Intent(this, ActivityVerificarOrdenSegCuenta.class);
            segundaCuenta.putExtra("id_pedido", id_pedido2);
            segundaCuenta.putExtra("id_emp", id_emp);
            segundaCuenta.putExtra("mes_num", mes_num);
            segundaCuenta.putExtra("token", token);
            startActivity(segundaCuenta);

    }
    //Se actualiza la cantidad y el valor del detalle
    public void actualizarDetalle(int posicion, int cantidad){

        String id = listIdDtall.get(posicion);//Obtener el id del detalle
        Double pvp = Double.parseDouble(listpvpPlts.get(posicion));// precio de venta
        String total = String.valueOf(cantidad*pvp);// calcular el total

        String url = "https://sgo-central-6to.herokuapp.com/api/detalles/" + id;
        Map<String,String> datos = new HashMap<>();
        //enviar datos a la BDD
        datos.put("dtall_cant",String.valueOf(cantidad));
        datos.put("dtall_valor",total);
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
                            Toast.makeText(ActivityVerificarOrden.this, mensaje, Toast.LENGTH_SHORT).show();
                            loadingDialog.dismissDialog();
                            VaciarListas();

                        } catch (JSONException e) {
                            Toast.makeText(ActivityVerificarOrden.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            loadingDialog.dismissDialog();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ActivityVerificarOrden.this, "Error del servidor: "+anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                        loadingDialog.dismissDialog();
                    }
                });
    }
    //Eliman los datos de la base de datos
    public void eliminaItemBDD(int position){
        String id = listIdDtall.get(position);
        String url = "https://sgo-central-6to.herokuapp.com/api/detalles/"+id;

        JSONObject jsonData = new JSONObject();
        AndroidNetworking.delete(url)
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
                            Toast.makeText(ActivityVerificarOrden.this, mensaje, Toast.LENGTH_SHORT).show();
                            loadingDialog.dismissDialog();
                            VaciarListas();
                        } catch (JSONException e) {
                            Toast.makeText(ActivityVerificarOrden.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            loadingDialog.dismissDialog();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ActivityVerificarOrden.this, "Error:   " + anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                        loadingDialog.dismissDialog();
                    }
                });

    }
    //Verificar si IdEstado Factura es diferente de 1 para crear otro pedido
    public void verificarIdEstadoFact(String id){
        if(!id.equals("")) {
                 String url = "http://sgo-central-6to.herokuapp.com/api/pedidos/" + id;
                AndroidNetworking.get(url)
                        .addHeaders("Content-type", "application/json")
                        .addHeaders("Authorization", token)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String respuesta = response.getString("status");
                                    if (respuesta.equals("202")) {
                                        JSONObject datos = response.getJSONObject("data");
                                        id_Estado = datos.getString("idEstado");
                                        //Si el estado no es 1 se genera un nuevo pedido
                                        if(!id_Estado.equals("1")) {
                                            id_pedido2="";
                                            generarNuevoPedido();
                                        }
                                    }
                                    loadingDialog.dismissDialog();
                                } catch (JSONException e) {
                                    Toast.makeText(ActivityVerificarOrden.this, "Error:1eSTADP " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    loadingDialog.dismissDialog();
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                Toast.makeText(ActivityVerificarOrden.this, "Error:2eSTADO " + anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                                loadingDialog.dismissDialog();
                            }
                        });
        }
        else
        {
            generarNuevoPedido();
        }

    }
    //Generar nuevo pedido para poder dividir cuenta
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
                            id_pedido2 = data.getString("id");
                            Toast.makeText(ActivityVerificarOrden.this, mensaje, Toast.LENGTH_SHORT).show();

                            loadingDialog.dismissDialog();
                        } catch (JSONException e) {
                            Toast.makeText(ActivityVerificarOrden.this, "ErrorP1: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            loadingDialog.dismissDialog();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ActivityVerificarOrden.this, "Error Servidor: "+anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                        loadingDialog.dismissDialog();
                    }
                });
    }
    //Se ingresa los platos para generar una nueva cuenta
    private void ingresarNewDetalle(final String idplato, final String dtallcant,final String dtallvalor) {

        Map<String,String> datos = new HashMap<>();

        datos.put("idPedido", id_pedido2);
        datos.put("idPlato",  idplato);
        datos.put("dtall_cant", dtallcant);
        datos.put("dtall_valor",  dtallvalor);

        JSONObject jsonData = new JSONObject(datos);

        AndroidNetworking.post("https://sgo-central-6to.herokuapp.com/api/detalles")
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
                            Toast.makeText(ActivityVerificarOrden.this, mensaje, Toast.LENGTH_SHORT).show();
                            loadingDialog.dismissDialog();

                        } catch (JSONException e) {
                            Toast.makeText(ActivityVerificarOrden.this, "Error dTLL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            loadingDialog.dismissDialog();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ActivityVerificarOrden.this, "Error Servidor:   " + anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                        loadingDialog.dismissDialog();
                    }
                });
    }


    public void VaciarListas(){
        //Se vacian las listas
        listIdDtall.clear();
        listIdPlts.clear();;
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

    @Override
    public  void onRestart(){
        super.onRestart();
        VaciarListas();
    }
}
