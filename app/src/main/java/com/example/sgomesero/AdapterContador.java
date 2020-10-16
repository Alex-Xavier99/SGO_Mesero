package com.example.sgomesero;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class AdapterContador extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater = null;
    List<String> listPlts, listPltsDscrp, listNro;

    final int[] cantidad;


    public AdapterContador(Context contexto, List<String> lstPlts, List<String> lstPltsDscrp,int[] lstPltsNro) {
        this.context = contexto;
        this.listPlts = lstPlts;
        this.listPltsDscrp = lstPltsDscrp;
        this.cantidad = lstPltsNro;
        inflater = (LayoutInflater)context.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);

    }

    @Override//Se agrega el numero de platos que se va a pedir en la orden
    public View getView(final int i, View convertView, ViewGroup parent) {

        final View vista1 = inflater.inflate(R.layout.cont_select_orden_item,null);

        TextView plto = vista1.findViewById(R.id.Txt_plato);
        TextView dscrp = vista1.findViewById(R.id.txtview_descrip);
        final TextView num = vista1.findViewById(R.id.txtview_num);
        Button btnmenos = vista1.findViewById(R.id.btn_menos);
        Button btnmas = vista1.findViewById(R.id.btn_mas);
        plto.setText(listPlts.get(i));
        dscrp.setText(listPltsDscrp.get(i));
        num.setText(String.valueOf(cantidad[i]));

        //Funcion contador boton menos
        btnmenos.setTag(i);
        btnmenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cantidad[i] != 0) {
                    int cont = cantidad[i];
                    cantidad[i] = cont-1;
                    num.setText(String.valueOf(cantidad[i]));
                }
            }
        });

        //Funcion contador boton mas
        btnmas.setTag(i);
        btnmas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cont = cantidad[i];
                cantidad[i] = cont+1;
                num.setText(String.valueOf(cantidad[i]));
            }
        });

        return vista1;
    }
    public int[] Cantidad(){
        return cantidad;
    }
    @Override
    public int getCount() {
        return listPlts.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
