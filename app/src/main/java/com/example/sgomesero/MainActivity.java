package com.example.sgomesero;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Método que retrasa m milisegundos el paso del siguiente activity
        new Handler().postDelayed(new Runnable() {

            public void run() {
                    IrSguienteActivity();
            }
        }, 1000);

    }

    //Función que llama al siguiente activity
    public void IrSguienteActivity(){
        Intent actLogin = new Intent(this, ActivityLogin.class);
        startActivity(actLogin);
        finish();
    }
}
