package com.example.sgomesero;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ActivityLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void Ingresar(View view){
        IrSguienteActivity();
    }

    public void IrSguienteActivity(){
        Intent actSelMes = new Intent(this, ActivitySeleccionMesa.class);
        startActivity(actSelMes);
        finish();
    }


}

