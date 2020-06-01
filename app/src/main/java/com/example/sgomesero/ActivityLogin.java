package com.example.sgomesero;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActivityLogin extends AppCompatActivity {

    //Variables para user y password
    EditText txt_user;
    EditText txt_pasw;

    //Variable para enviar al siguiente activity
    private String id_empleado;

    final LoadingDialog loadingDialog = new LoadingDialog(ActivityLogin.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Cas de los elementos para usuario y contraseña en el Layout
        txt_user = (EditText)findViewById(R.id.editTextUsuer);
        txt_pasw = (EditText)findViewById(R.id.editTextPassword);

        id_empleado = "";
    }


    private void acceder(String user, String pasw){
            Map<String,String> datos = new HashMap<>();
            datos.put("email",user);
            datos.put("password",pasw);
            JSONObject jsonData = new JSONObject(datos);

            AndroidNetworking.post("https://safe-bastion-34410.herokuapp.com/api/login")
                    .addJSONObjectBody(jsonData)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {

                                String mensaje = response.getString("message");
                                Toast.makeText(ActivityLogin.this, mensaje, Toast.LENGTH_SHORT).show();
                                //id_empleado = response.getString("empleado");
                                id_empleado = "1"; //Linea de prueba

                                if(mensaje.equals("Bienvenido")){
                                    siguienteActivity();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(ActivityLogin.this, "Error1: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                loadingDialog.dismissDialog();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            Toast.makeText(ActivityLogin.this, "Error2: "+anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                            loadingDialog.dismissDialog();
                        }
                    });
    }


    //Función que valida usuario y contraseña
    public void validarUserPasw(View view){
        if(camposLlenos()) {
            String str_user = txt_user.getText().toString();
            String str_pasw = txt_pasw.getText().toString();

            txt_user.setText("");
            txt_pasw.setText("");

            loadingDialog.startLoadingDialog();

            acceder(str_user, str_pasw);
        } else {
            Toast.makeText(this, "No puede dejar los campos vacíos", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean camposLlenos(){
        return !txt_user.getText().toString().trim().isEmpty() &&
                !txt_pasw.getText().toString().trim().isEmpty();
    }

    public void siguienteActivity(){
        Intent actSelMes = new Intent(this, ActivitySeleccionMesa.class);
        actSelMes.putExtra("id_empleado",id_empleado);
        startActivity(actSelMes);
        finish();
        loadingDialog.dismissDialog();
    }

}

