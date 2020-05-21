package com.example.sgomesero;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityLogin extends AppCompatActivity {

    //Variables para user y password
    EditText txt_user;
    EditText txt_pasw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Cas de los elementos para usuario y contraseña en el Layout
        txt_user = (EditText)findViewById(R.id.editTextUsuer);
        txt_pasw = (EditText)findViewById(R.id.editTextPassword);
    }

    //Función que valida usuario y contraseña
    public boolean Validar(){
        boolean esVal = false;
        String str_user = txt_user.getText().toString();
        String str_pasw = txt_pasw.getText().toString();

        txt_user.setText("");
        txt_pasw.setText("");

        //Ingresar método de consulta
        //public String Consulta(str_user){}

        if(str_pasw.equals("clave")){
            esVal = true;
        }

        return esVal;
    }

    public void Ingresar(View view){

        if(Validar()){
            IrSguienteActivity();
        } else{
            Toast.makeText(this, "Usuario o contraseña no válidos, pruebe otra vez", Toast.LENGTH_SHORT).show();
        }
    }

    public void IrSguienteActivity(){
        Intent actSelMes = new Intent(this, ActivitySeleccionMesa.class);
        startActivity(actSelMes);
        finish();
    }

}

