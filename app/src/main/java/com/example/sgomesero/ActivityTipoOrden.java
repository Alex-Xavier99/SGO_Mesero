package com.example.sgomesero;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class ActivityTipoOrden extends AppCompatActivity {
    private TextView subtitle;
    private ImageButton imgaperitivo,imgsopa,imgfuerte,imgensalada,imgmariscos,imgjugos,imgpostre,imgpromo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_orden);

        imgaperitivo = (ImageButton)findViewById(R.id.img_aperitivo);
        imgsopa = (ImageButton)findViewById(R.id.img_sopa);
        imgfuerte = (ImageButton)findViewById(R.id.img_fuerte);
        imgensalada = (ImageButton)findViewById(R.id.img_ensalada);
        imgmariscos = (ImageButton)findViewById(R.id.img_marisco);
        imgjugos = (ImageButton)findViewById(R.id.img_jugo);
        imgpostre = (ImageButton)findViewById(R.id.img_postre);
        imgpromo = (ImageButton)findViewById(R.id.img_promo);

    }

    //Siguiente activity Verficar Orden
    public void Aperitivo(View view){
        SeleccionarOrden();
    }
    public void Sopas(View view){
        SeleccionarOrden();
    }
    public void PlatosFuertes(View view){
        SeleccionarOrden();
    }
    public void Ensaladas(View view){
        SeleccionarOrden();
    }
    public void Mariscos(View view){
        SeleccionarOrden();
    }
    public void Jugos(View view){
        SeleccionarOrden();
    }
    public void Postres(View view){
        SeleccionarOrden();
    }
    public void Promociones(View view){
        SeleccionarOrden();
    }
    public void SeleccionarOrden(){
        Intent selectorden = new Intent(this, ActivitySelecionOrden.class);
        startActivity(selectorden);
        finish();
    }
    @Override
    public  void onBackPressed(){
        Intent verificar= new Intent(this,ActivityVerificarOrden.class);
        startActivity(verificar);
        finish();
    }
}
