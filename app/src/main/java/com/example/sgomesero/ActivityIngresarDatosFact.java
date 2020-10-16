package com.example.sgomesero;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ActivityIngresarDatosFact extends AppCompatActivity implements DialogBuscarCliente.Finalizarcuadrodialogo{

    private TextView subtitle;
    private EditText razonsocial, cedula, direccion, telefono,correo;
    private Button btnBuscar;

    private String id_emp;
    private String id_pedido;

    private String id_cliente;
    private String id_fact;
    DialogBuscarCliente dialogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar_datos_fact);

        razonsocial = (EditText)findViewById(R.id.txt_razonsocial);//Servira para ingresar leer los datos de razon social
        cedula = (EditText)findViewById(R.id.txt_cedula);//Servira para leer el dato de cedula
        direccion = (EditText)findViewById(R.id.txt_direccion);//Servira para leer los datos ingresados de la direccion
        telefono = (EditText)findViewById(R.id.txt_telefono);//Servira para leer los datos del telefono
        correo = (EditText)findViewById(R.id.txt_correo);//Servira para leer los datos del correo
        btnBuscar = (Button)findViewById(R.id.btn_Aceptar);

        //Se almacena el paso de parametros
        id_emp = getIntent().getStringExtra("id_emp");
        id_pedido = getIntent().getStringExtra("id_pedido");
        id_cliente = "";
        id_fact = "";

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogo = new DialogBuscarCliente(ActivityIngresarDatosFact.this,ActivityIngresarDatosFact.this);
            }
        });

    }

    public void CrearCliente(View view){
        ingresarCliente();
    }

    private void ingresarCliente(){
        if(isValidarCampos()){

            String rzso = razonsocial.getText().toString();
            String cdla = cedula.getText().toString();
            String dire = direccion.getText().toString();
            String telf = telefono.getText().toString();
            String corr = correo.getText().toString();


            Map<String,String> datos = new HashMap<>();
            datos.put("cli_ci",cdla);
            datos.put("cli_nom",rzso);
            datos.put("cli_dir",dire);
            datos.put("cli_email",corr);
            datos.put("cli_telf",telf);
            JSONObject jsonData = new JSONObject(datos);

            AndroidNetworking.post("https://sgo-central-6to.herokuapp.com/api/clientes")
                    .addJSONObjectBody(jsonData)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {

                                String mensaje = response.getString("message");
                                Toast.makeText(ActivityIngresarDatosFact.this, mensaje, Toast.LENGTH_SHORT).show();
                                String status = response.getString("status");
                                if (status.equals("202")) {
                                    JSONObject datos = response.getJSONObject("data");
                                    id_cliente = datos.getString("id");
                                }

                            } catch (JSONException e) {
                                Toast.makeText(ActivityIngresarDatosFact.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            Toast.makeText(ActivityIngresarDatosFact.this, "Error: "+anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }else{
            Toast.makeText(this, "No se puede insertar un cliente si existen campos vacios", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidarCampos(){
        return !razonsocial.getText().toString().trim().isEmpty() &&
                !cedula.getText().toString().trim().isEmpty() &&
                !direccion.getText().toString().trim().isEmpty() &&
                !telefono.getText().toString().trim().isEmpty() &&
                !correo.getText().toString().trim().isEmpty();
    }
    @Override // Recibe la cedula del cliente buscado
    public void ResultadoCuadroDialogo(String cedulacliente) {
        bucarCliente(cedulacliente);
    }
    //Se busca al cliente en la BDD
    public void bucarCliente(final String cedulaBsqd){

        if(!cedulaBsqd.equals("")) {
        String url = "https://sgo-central-6to.herokuapp.com/api/cedulaclientes/"+ cedulaBsqd;
            AndroidNetworking.get(url)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String respuesta = response.getString("status");
                                if (respuesta.equals("202")) {
                                    JSONArray arrayDatos = response.getJSONArray("data");
                                    for (int i = 0; i < arrayDatos.length(); i++) {
                                        JSONObject jsonDatos = arrayDatos.getJSONObject(i);
                                        String rzsocial = jsonDatos.getString("cli_nom");
                                        String dircn = jsonDatos.getString("cli_dir");
                                        String telf = jsonDatos.getString("cli_telf");
                                        String mail = jsonDatos.getString("cli_email");
                                        cedula.setText(cedulaBsqd);
                                        razonsocial.setText(rzsocial);
                                        direccion.setText(dircn);
                                        telefono.setText(telf);
                                        correo.setText(mail);
                                        id_cliente = jsonDatos.getString("id");
                                    }

                                } else {
                                    Toast.makeText(ActivityIngresarDatosFact.this, "No se ha registrado el cliente.", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(ActivityIngresarDatosFact.this, "Error1: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            Toast.makeText(ActivityIngresarDatosFact.this, "Error2: " + anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    //Con el boton Facturar finalizamos la orden
    public void finalizarOrden(View view){

       if(isValidarCampos()){
           generarFactura(id_emp, id_cliente);
       }
       else{
           Toast.makeText(this, "No se puede facturar, existen campos vacios", Toast.LENGTH_LONG).show();
       }
    }
    //Generamos la fatura
    public void generarFactura(String empleado, String cliente){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());

        Map<String,String> datos = new HashMap<>();
        datos.put("idEmpleado",empleado);
        datos.put("idCliente",cliente);
        datos.put("fct_fch",date);
        JSONObject jsonData = new JSONObject(datos);

        AndroidNetworking.post("https://sgo-central-6to.herokuapp.com/api/facs")
                .addJSONObjectBody(jsonData)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String mensaje = response.getString("message");
                            Toast.makeText(ActivityIngresarDatosFact.this, mensaje, Toast.LENGTH_SHORT).show();
                            String status = response.getString("status");
                            if(status.equals("202")){
                                JSONObject datos = response.getJSONObject("data");
                                id_fact = datos.getString("id");
                                if(!id_fact.equals("")){
                                    actualizarDetalle(id_pedido);

                                }
                            }

                        } catch (JSONException e) {
                            Toast.makeText(ActivityIngresarDatosFact.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ActivityIngresarDatosFact.this, "ErrorFacs: "+anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
    //Se actualiza el detalle de la factura
    public void actualizarDetalle(String pedido){
        String url = "https://sgo-central-6to.herokuapp.com/api/acfacdetalles/" + pedido;
        Map<String,String> datos = new HashMap<>();
        datos.put("idFac",id_fact);
        JSONObject jsonData = new JSONObject(datos);

        AndroidNetworking.patch(url)
                .addJSONObjectBody(jsonData)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String mensaje = response.getString("message");
                            Toast.makeText(ActivityIngresarDatosFact.this, mensaje, Toast.LENGTH_SHORT).show();
                            if(!id_emp.equals("")) {
                                actualizarEstadoPedido(id_pedido);
                                finish();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(ActivityIngresarDatosFact.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ActivityIngresarDatosFact.this, "ErrorDet: "+anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    //Se actualiza el estado del pedido
    public void actualizarEstadoPedido(String pedido){
        String url = "https://sgo-central-6to.herokuapp.com/api/pedidos/" + pedido;
        Map<String,String> datos = new HashMap<>();
        datos.put("ped_terminado","true");
        JSONObject jsonData = new JSONObject(datos);

        AndroidNetworking.patch(url)
                .addJSONObjectBody(jsonData)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String mensaje = response.getString("message");
                            Toast.makeText(ActivityIngresarDatosFact.this, mensaje, Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            Toast.makeText(ActivityIngresarDatosFact.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ActivityIngresarDatosFact.this, "ErrorPed: "+anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                    }
                });
    }



    @Override
    public  void onBackPressed(){
        /*Intent verificar = new Intent(this,ActivityVerificarOrden.class);
        startActivity(verificar);*/
        finish();
    }


}
