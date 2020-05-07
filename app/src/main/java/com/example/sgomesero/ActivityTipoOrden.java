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
    private String subti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_orden);

        subtitle = (TextView)findViewById(R.id.txtview_subtitle);
        imgaperitivo = (ImageButton)findViewById(R.id.img_aperitivo);
        imgsopa = (ImageButton)findViewById(R.id.img_sopa);
        imgfuerte = (ImageButton)findViewById(R.id.img_fuerte);
        imgensalada = (ImageButton)findViewById(R.id.img_ensalada);
        imgmariscos = (ImageButton)findViewById(R.id.img_marisco);
        imgjugos = (ImageButton)findViewById(R.id.img_jugo);
        imgpostre = (ImageButton)findViewById(R.id.img_postre);
        imgpromo = (ImageButton)findViewById(R.id.img_promo);

        subti = getIntent().getStringExtra("m1");
        subtitle.setText(subti);
    }

    //Siguiente activity Verficar Orden
    public void VerificarOrden(View view){
        Intent verificar = new Intent(this, ActivityVerificarOrden.class);
        verificar.putExtra("m1",subti);
        startActivity(verificar);
        finish();
    }
    public void Facturar(View view){
        Intent factura = new Intent(this, ActivityIngresarDatosFact.class);
        factura.putExtra("m1",subti);
        startActivity(factura);
        finish();
    }
    public void Regresar(View view){
        Intent regmesa = new Intent(this, ActivitySeleccionMesa.class);
        startActivity(regmesa);
        finish();
    }
}
