package com.example.sgomesero;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ActivityVerificarOrden extends AppCompatActivity {

    private String subti;
    private TextView subtitle;
    private ListView listorden;
    private TextView restotal;
    private  Double res;
    ArrayList<String> listIdPlts = new ArrayList<>();
    ArrayList<String> listPlts  = new ArrayList<>();
    ArrayList<String> listPvp = new ArrayList<>();
    int[] cantidad;
    private String [][] menu,menu2;
    /*private String [][] menu = {
            {"100", "Seco de Carne", "1", "2.00", "0.00"},
            {"200", "Fanta", "2", "0.75", "0.00"},
            {"300", "Encebollado", "3", "3.50", "0.00"},
            {"400", "Gaseosa", "1", "0.50", "0.00"},
            {"500", "Parrillada", "2", "5.00", "0.00"},
            {"600", "Hamburguesa", "3", "3.00", "0.00"},
            {"100", "Hot dog", "1", "1.50", "0.00"}
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int i,j;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificar_orden);

        subtitle = (TextView)findViewById(R.id.txtview_subtitle);

        subti = getIntent().getStringExtra("mes_num");
        subtitle.setText(subti);

        Bundle menumat = getIntent().getExtras();
        menu2 = (String [][]) menumat.get("menu");//Recibe una matriz ActivitySeleccionOrden

        listorden = (ListView)findViewById(R.id.listview_ordenes);
        restotal = (TextView)findViewById(R.id.txtview_totalres);


        menu = new String[1][5];
        if(menu.length==1 && menu2 == null) {
            for (i = 0; i < menu.length; i++) {
                for (j = 0; j < menu[i].length; j++) {
                    menu[i][0] = "";
                    menu[i][1] = "";
                    menu[i][2] = "";
                    menu[i][3] = "";
                    menu[i][4] = "";
                }
            }
        }else
            Verificar();
        Actualizar();
        EliminarItemArrayMenu();
    }
    //Actualiza la lista en el caso de eliminar platos del menu
    public void Actualizar(){
        AdapterSelect apt = new AdapterSelect(this,menu);
        listorden.setAdapter(apt);

        res = apt.Operacion();
        restotal.setText(String.valueOf(res));

    }
    //Agregar las ordenes del cliente
    public void AgregarOrden(View view){
        Intent tipoOrden = new Intent(this, ActivityTipoOrden.class);
        startActivity(tipoOrden);
        //finish();
    }
    //Ingresar Datos del Cliente
    public void DatosFactura(View view){
        Intent factura = new Intent(this, ActivityIngresarDatosFact.class);
        factura.putExtra("mes_num",subti);
        startActivity(factura);
        //finish();
    }
    public void Verificar (){
        int i,j;
        menu = new String[menu2.length][5];
            for (i = 0; i < menu.length; i++) {
                menu[i][0] =  menu2[i][0];
                menu[i][1] =  menu2[i][1];
                menu[i][2] =  menu2[i][2];
                menu[i][3] =  menu2[i][3];
                menu[i][4] =  menu2[i][4];
            }
        Actualizar();
    }
    @Override
    public  void onBackPressed(){
        Intent mesa = new Intent(this,ActivitySeleccionMesa.class);
        startActivity(mesa);
        finish();
    }
    //Eliminar los Items de la lista
    private void EliminarItemArrayMenu() {
        listorden.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String probar;
                String msm = "Se elimina " + String.valueOf(menu[position][1]);
                Toast.makeText(getApplicationContext(), msm, Toast.LENGTH_SHORT).show();
                if (position < menu.length - 1) {
                    do {

                        menu[position][0] = menu[position + 1][0];
                        menu[position][1] = menu[position + 1][1];
                        menu[position][2] = menu[position + 1][2];
                        menu[position][3] = menu[position + 1][3];
                        menu[position][4] = menu[position + 1][4];
                        menu[position + 1][0] = "";
                        menu[position + 1][1] = "";
                        menu[position + 1][2] = "";
                        menu[position + 1][3] = "";
                        menu[position + 1][4] = "";
                        probar = menu[position + 1][0];
                        position++;
                    } while (probar.equals("") && position < menu.length - 1);
                } else {
                    menu[position][0] = "";
                    menu[position][1] = "";
                    menu[position][2] = "";
                    menu[position][3] = "";
                    menu[position][4] = "";
                }
                listorden.deferNotifyDataSetChanged();
                Actualizar();
            }

        });
    }
    public void QuitarElementos(int position){

    }
    public void Confirmar(){

    }
    public void Finalizar(){

    }
}
