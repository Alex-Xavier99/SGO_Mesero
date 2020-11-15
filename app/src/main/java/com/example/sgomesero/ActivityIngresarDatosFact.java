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
    private String token;
    DialogBuscarCliente dialogo;

    final LoadingDialog loadingDialog = new LoadingDialog(ActivityIngresarDatosFact.this);
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
        token = getIntent().getStringExtra("token");
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
        loadingDialog.startLoadingDialog();
        ingresarCliente();
    }

    private void ingresarCliente(){
        if(isValidarParametros()){

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
                                Toast.makeText(ActivityIngresarDatosFact.this, mensaje, Toast.LENGTH_SHORT).show();
                                String status = response.getString("status");
                                if (status.equals("202")) {
                                    JSONObject datos = response.getJSONObject("data");
                                    id_cliente = datos.getString("id");
                                }
                                loadingDialog.dismissDialog();

                            } catch (JSONException e) {
                                Toast.makeText(ActivityIngresarDatosFact.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                loadingDialog.dismissDialog();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            Toast.makeText(ActivityIngresarDatosFact.this, "Error: "+anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                            loadingDialog.dismissDialog();
                        }
                    });
        }else{
            loadingDialog.dismissDialog();
        }
    }
    //Comprobar si el ingreso de datos es vacio y la cedula es correcta
    private boolean isValidarParametros() {
        String cedula1 = cedula.getText().toString().trim();
        String ruc;
        if (isValidarCampos())
        {
            if (cedula1.length() > 10) {
                ruc = cedula1.substring(10);//Se separa el 001 del ruc para verficar si la cedula es valida
                if (ruc.equals("001")) {//se comprueba si es un RUC
                        cedula1 = cedula1.substring(0, 10);// se toma los 10 primeros digitos
                        return validadorDeCedula(cedula1);

                } else {
                    Toast.makeText(ActivityIngresarDatosFact.this, "El número de cédula no es correcto", Toast.LENGTH_LONG).show();
                    return false;
                }
            } else if (cedula1.length() == 10) {//en el caso que solo sean 10 digitos
                if(cedula1.equals("9999999999"))
                    return true;
                else
                    return validadorDeCedula(cedula1);

            } else {
                Toast.makeText(this, "La cédula debe tener 10 dígitos y RUC 13", Toast.LENGTH_LONG).show();
                return false;
            }

         }
        else {
            Toast.makeText(this, "No se puede insertar un cliente si existen campos vacios", Toast.LENGTH_LONG).show();
            return false;
        }

    }
    //Se comprueba si los campos estan vacios o no
     private boolean isValidarCampos(){
         return !razonsocial.getText().toString().trim().isEmpty() &&
                 !cedula.getText().toString().trim().isEmpty() &&
                 !direccion.getText().toString().trim().isEmpty() &&
                 !telefono.getText().toString().trim().isEmpty() &&
                 !correo.getText().toString().trim().isEmpty();
     }
     //Se comprueba si la cedula es valida
    public boolean validadorDeCedula(String cedula) {
        boolean cedulaCorrecta = false;
        String ruc = "";
        try {
                int tercerDigito = Integer.parseInt(cedula.substring(2, 3));
                if (tercerDigito < 6) {
                    // Coeficientes de validación cédula
                    // El decimo digito se lo considera dígito verificador
                    int[] coefValCedula = { 2, 1, 2, 1, 2, 1, 2, 1, 2 };
                    int verificador = Integer.parseInt(cedula.substring(9,10));
                    int suma = 0;
                    int digito = 0;
                    for (int i = 0; i < (cedula.length() - 1); i++) {
                        digito = Integer.parseInt(cedula.substring(i, i + 1))* coefValCedula[i];
                        suma += ((digito % 10) + (digito / 10));
                    }

                    if ((suma % 10 == 0) && (suma % 10 == verificador)) {
                        cedulaCorrecta = true;
                    }
                    else if ((10 - (suma % 10)) == verificador) {
                        cedulaCorrecta = true;
                    } else {
                        cedulaCorrecta = false;
                    }
                } else {
                    cedulaCorrecta = false;
                }
        } catch (NumberFormatException nfe) {
            cedulaCorrecta = false;
        } catch (Exception err) {

            Toast.makeText(ActivityIngresarDatosFact.this, "Una excepcion ocurrio en el proceso de validadcion", Toast.LENGTH_LONG).show();
            cedulaCorrecta = false;
        }

        if (!cedulaCorrecta) {
            Toast.makeText(ActivityIngresarDatosFact.this, "El número de cédula no es correcto", Toast.LENGTH_LONG).show();
        }
        return cedulaCorrecta;
    }

    @Override // Recibe la cedula del cliente buscado
    public void ResultadoCuadroDialogo(String cedulacliente) {
        loadingDialog.startLoadingDialog();
        bucarCliente(cedulacliente);
    }
    //Se busca al cliente en la BDD
    public void bucarCliente(final String cedulaBsqd){

        if(!cedulaBsqd.equals("")) {
        String url = "https://sgo-central-6to.herokuapp.com/api/cedulaclientes/"+ cedulaBsqd;
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
                                loadingDialog.dismissDialog();
                            } catch (JSONException e) {
                                Toast.makeText(ActivityIngresarDatosFact.this, "Error1: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                loadingDialog.dismissDialog();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            Toast.makeText(ActivityIngresarDatosFact.this, "Error2: " + anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                            loadingDialog.dismissDialog();
                        }
                    });
        }
        loadingDialog.dismissDialog();
    }
    //Con el boton Facturar finalizamos la orden
    public void finalizarOrden(View view){

       if(isValidarParametros()){
           loadingDialog.startLoadingDialog();
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
                            Toast.makeText(ActivityIngresarDatosFact.this, mensaje, Toast.LENGTH_SHORT).show();
                            String status = response.getString("status");
                            if(status.equals("202")){
                                JSONObject datos = response.getJSONObject("data");
                                id_fact = datos.getString("id");
                                if(!id_fact.equals("")){
                                    actualizarDetalle(id_pedido);

                                }
                            }
                            loadingDialog.dismissDialog();
                        } catch (JSONException e) {
                            Toast.makeText(ActivityIngresarDatosFact.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            loadingDialog.dismissDialog();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ActivityIngresarDatosFact.this, "ErrorFacs: "+anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                        loadingDialog.dismissDialog();
                    }
                });

    }

    //Boton del consumidor final (Se busca en la base de datos la cedula 999999999)
    public  void ConsumidorFinal(View view){
        loadingDialog.startLoadingDialog();
        bucarCliente("9999999999");
    }

    //Se actualiza el detalle de la factura
    public void actualizarDetalle(String pedido){
        String url = "https://sgo-central-6to.herokuapp.com/api/acfacdetalles/" + pedido;
        Map<String,String> datos = new HashMap<>();
        datos.put("idFac",id_fact);
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
                            Toast.makeText(ActivityIngresarDatosFact.this, mensaje, Toast.LENGTH_SHORT).show();
                            if(!id_emp.equals("")) {
                                String cdlTemp = cedula.getText().toString();

                                /*if(cdlTemp.equals("9999999999")) {
                                    loadingDialog.startLoadingDialog();
                                    actualizarEstadoPedido(id_pedido, true);
                                }
                                else {
                                    loadingDialog.startLoadingDialog();
                                    actualizarEstadoPedido(id_pedido, false);
                                }*/
                                loadingDialog.startLoadingDialog();
                                actualizarEstadoPedido(id_pedido, false);
                                finish();
                                loadingDialog.dismissDialog();
                            }
                            loadingDialog.dismissDialog();
                        } catch (JSONException e) {
                            Toast.makeText(ActivityIngresarDatosFact.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            loadingDialog.dismissDialog();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ActivityIngresarDatosFact.this, "ErrorDet: "+anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                        loadingDialog.dismissDialog();
                    }
                });
    }
    //Se actualiza el estado del pedido
    public void actualizarEstadoPedido(String pedido, boolean estado){
        String url = "https://sgo-central-6to.herokuapp.com/api/pedidos/" + pedido;
        Map<String,String> datos = new HashMap<>();

        datos.put("ped_terminado", String.valueOf(estado));
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
                            Toast.makeText(ActivityIngresarDatosFact.this, mensaje, Toast.LENGTH_SHORT).show();
                            loadingDialog.dismissDialog();

                        } catch (JSONException e) {
                            Toast.makeText(ActivityIngresarDatosFact.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            loadingDialog.dismissDialog();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ActivityIngresarDatosFact.this, "ErrorPed: "+anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                        loadingDialog.dismissDialog();
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
