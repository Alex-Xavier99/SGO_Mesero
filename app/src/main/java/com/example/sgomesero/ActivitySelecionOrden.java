package com.example.sgomesero;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ActivitySelecionOrden extends AppCompatActivity {

    private ListView listorden;
    private String [] ordenes = {"Seco de Carne","Fanta","Encebollado","Gaseosa","Parrillada","Hamburguesa","Hot dog","Ceviche de pesacado","Ceviche de Camaron","Mega Hamburguesa"} ;
    ArrayList<String> seleccionaritems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecion_orden);

        listorden = (ListView)findViewById(R.id.list_ordenes);
        listorden.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        ArrayAdapter<String> lista = new ArrayAdapter<String>(this,R.layout.row_layout,R.id.checkedTextView,ordenes);
        listorden.setAdapter(lista);

        listorden.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String seleccionaritem=((TextView)view).getText().toString();
                if(seleccionaritems.contains(seleccionaritem)){
                    seleccionaritems.remove(seleccionaritem);
                }
                else{
                    seleccionaritems.add(seleccionaritem);
                }
            }
        });
    }
    public void Agregar(View view){
        String items="";
        for(String item:seleccionaritems){
            items+="-"+item+"\n";
        }
        Toast.makeText(this, "Seleccionaste \n"+items, Toast.LENGTH_SHORT).show();
    }
    public void Regresar(View view){
        Intent tipoorden = new Intent(this, ActivityTipoOrden.class);
        startActivity(tipoorden);
        finish();
    }
}
