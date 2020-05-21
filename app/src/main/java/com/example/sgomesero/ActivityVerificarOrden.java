package com.example.sgomesero;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ActivityVerificarOrden extends AppCompatActivity {

    private String subti;
    private TextView subtitle;
    private ListView listorden;
    private TextView restotal;
    private  Double res;
    private String [][] menu = {
            {"100", "Seco de Carne", "1", "2.00", "0.00"},
            {"200", "Fanta", "2", "0.75", "0.00"},
            {"300", "Encebollado", "3", "3.50", "0.00"},
            {"400", "Gaseosa", "1", "0.50", "0.00"},
            {"500", "Parrillada", "2", "5.00", "0.00"},
            {"600", "Hamburguesa", "3", "3.00", "0.00"},
            {"100", "Hot dog", "1", "1.50", "0.00"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificar_orden);

        subtitle = (TextView)findViewById(R.id.txtview_subtitle);

        subti = getIntent().getStringExtra("m1");
        subtitle.setText(subti);

        listorden = (ListView)findViewById(R.id.listview_ordenes);
        restotal = (TextView)findViewById(R.id.txtview_totalres);
        AdapterSelect apt = new AdapterSelect(this,menu);
        listorden.setAdapter(apt);
        res = apt.Operacion();
        restotal.setText(String.valueOf(res));

    }
    public void AgregarOrden(View view){
        Intent tipoOrden = new Intent(this, ActivityTipoOrden.class);
        startActivity(tipoOrden);
        finish();
    }
    public void Eliminar(View view){

    }
    public void Editar(View view){

    }
    public void DatosFactura(View view){
        Intent factura = new Intent(this, ActivityIngresarDatosFact.class);
        factura.putExtra("m1",subti);
        startActivity(factura);
        finish();
    }
    @Override
    public  void onBackPressed(){
        Intent mesa = new Intent(this,ActivitySeleccionMesa.class);
        startActivity(mesa);
        finish();
    }
}
