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

    private String id_emp;
    private String mes_num;
    private String id_pedido;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_orden);
        //Cast ImageButton
        imgaperitivo = (ImageButton)findViewById(R.id.img_aperitivo);
        imgsopa = (ImageButton)findViewById(R.id.img_sopa);
        imgfuerte = (ImageButton)findViewById(R.id.img_fuerte);
        imgensalada = (ImageButton)findViewById(R.id.img_ensalada);
        imgmariscos = (ImageButton)findViewById(R.id.img_marisco);
        imgjugos = (ImageButton)findViewById(R.id.img_jugo);
        imgpostre = (ImageButton)findViewById(R.id.img_postre);
        imgpromo = (ImageButton)findViewById(R.id.img_promo);

        id_emp = getIntent().getStringExtra("id_emp");
        mes_num = getIntent().getStringExtra("mes_num");
        id_pedido = getIntent().getStringExtra("id_pedido");
        token = getIntent().getStringExtra("token");
    }

    //Siguiente activity Verficar Orden
    public void Aperitivo(View view){
        SeleccionarOrden("Aperitivo");
    }
    public void Sopas(View view){
        SeleccionarOrden("Sopa");
    }
    public void PlatosFuertes(View view){
        SeleccionarOrden("Plato_Fuerte");
    }
    public void Ensaladas(View view){
        SeleccionarOrden("Ensalada");
    }
    public void Mariscos(View view){
        SeleccionarOrden("Marisco");
    }
    public void Jugos(View view){
        SeleccionarOrden("Bebida");
    }
    public void Postres(View view){
        SeleccionarOrden("Postre");
    }
    public void Promociones(View view){
        SeleccionarOrden("promocion");
    }

    public void SeleccionarOrden(String TipoOrden){
        Intent selectorden = new Intent(this, ActivitySelecionOrden.class);
        //Paso de parametros Tipo de orden, id empleado, id pedido
        selectorden.putExtra("TpOrden",TipoOrden);
        selectorden.putExtra("id_emp", id_emp);
        selectorden.putExtra("id_pedido",id_pedido);
        selectorden.putExtra("token", token);
        startActivity(selectorden);
        finish();
    }
    @Override
    public  void onBackPressed(){
        finish();
    }
}
