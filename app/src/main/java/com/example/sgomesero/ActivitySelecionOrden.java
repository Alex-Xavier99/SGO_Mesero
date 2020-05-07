package com.example.sgomesero;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ActivitySelecionOrden extends AppCompatActivity {
    private String subti;
    private ListView listorden;
    private String [] ordenen = {"Seco de Carne","Fanta","Encebollado","Gaseosa","Parrillada","Hamburguesa","Hot dog","Ceviche de pesacado","Ceviche de Camaron"};
    private TextView subtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecion_orden);

        subtitle = (TextView)findViewById(R.id.txtview_subtitle);

        subti = getIntent().getStringExtra("m1");
        subtitle.setText(subti);

        ArrayAdapter<String> lista = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, ordenen);
        listorden.setAdapter(lista);
    }
    public void Regresar(View view){
        Intent tipoorden = new Intent(this, ActivityTipoOrden.class);
        tipoorden.putExtra("m1",subti);
        startActivity(tipoorden);
        finish();
    }
}
