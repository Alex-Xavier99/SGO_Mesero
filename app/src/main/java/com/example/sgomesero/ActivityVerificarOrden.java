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

    private TextView subtitle;
    private ListView listorden;
    private TextView restotal;
    private  Double res;

    private String id_emp;
    private String mes_num;
    private String id_pedido;

    ArrayList<String> listIdPlts = new ArrayList<>();
    ArrayList<String> listPlts  = new ArrayList<>();
    ArrayList<String> listPvp = new ArrayList<>();
    int[] cantidad;
    private String [][] menu,menu2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int i,j;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificar_orden);

        subtitle = (TextView)findViewById(R.id.txtview_subtitle);

        id_emp = getIntent().getStringExtra("id_emp");
        mes_num = getIntent().getStringExtra("mes_num");
        id_pedido = getIntent().getStringExtra("id_pedido");

        subtitle.setText("Mesa " + mes_num);

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
            verificar();
        actualizar();
        eliminarItemArrayMenu();
    }

    //Actualiza la lista en el caso de eliminar platos del menu
    public void actualizar(){
        AdapterSelect apt = new AdapterSelect(this,menu);
        listorden.setAdapter(apt);

        res = apt.Operacion();
        restotal.setText(String.valueOf(res));

    }
    //Agregar las ordenes del cliente
    public void agregarOrden(View view){
        Intent tipoOrden = new Intent(this, ActivityTipoOrden.class);
        startActivity(tipoOrden);
    }
    //Ingresar Datos del Cliente
    public void datosFactura(View view){
        Intent factura = new Intent(this, ActivityIngresarDatosFact.class);
        factura.putExtra("mes_num", mes_num);
        startActivity(factura);
    }
    public void verificar(){
        int i,j;
        menu = new String[menu2.length][5];
            for (i = 0; i < menu.length; i++) {
                menu[i][0] =  menu2[i][0];
                menu[i][1] =  menu2[i][1];
                menu[i][2] =  menu2[i][2];
                menu[i][3] =  menu2[i][3];
                menu[i][4] =  menu2[i][4];
            }
        actualizar();
    }
    @Override
    public  void onBackPressed(){

        finish();
    }
    //Eliminar los Items de la lista
    private void eliminarItemArrayMenu() {
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
                actualizar();
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
