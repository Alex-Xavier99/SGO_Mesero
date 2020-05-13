package com.example.sgomesero;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class ActivitySeleccionMesa extends AppCompatActivity {
    private Spinner spin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_mesa);
        spin = (Spinner)findViewById(R.id.spin_selectmesa);


        String [] cadspin = {"Mesa 1","Mesa 2","Mesa 3","Mesa 4","Mesa 5","Mesa 6","Mesa 7"};
        ArrayAdapter<String> opera = new ArrayAdapter<String>(this,R.layout.spinmesaselct,cadspin);//(Contexto, tipo de spiner,nombre del spin)
        spin.setAdapter(opera);
    }
    public void Aceptar(View view){
        String selec, mesa ;
        selec = spin.getSelectedItem().toString();//Selecciona el Item de objeto spinner
        if(selec.equals("Mesa 1")) {
            mesa = "Mesa Nro. 1";
            PasarActivity(mesa);
        }
        else if(selec.equals("Mesa 2")) {
            mesa = "Mesa Nro. 2";
            PasarActivity(mesa);
        }
        else if(selec.equals("Mesa 3")) {
            mesa = "Mesa Nro. 3";
            PasarActivity(mesa);
        }
        else if(selec.equals("Mesa 4")) {
            mesa = "Mesa Nro. 4";
            PasarActivity(mesa);
        }
        else if(selec.equals("Mesa 5")) {
            mesa = "Mesa Nro. 5";
            PasarActivity(mesa);
        }
        else if(selec.equals("Mesa 6")) {
            mesa = "Mesa Nro. 6";
            PasarActivity(mesa);
        }
        else if(selec.equals("Mesa 7")) {
            mesa = "Mesa Nro. 7";
            PasarActivity(mesa);
        }
    }
    public void PasarActivity(String mesa){
        Intent verificarorden = new Intent(this,ActivityVerificarOrden.class);
        verificarorden.putExtra("m1",mesa);
        startActivity(verificarorden);
        finish();
    }
}
