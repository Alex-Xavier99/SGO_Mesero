package com.example.sgomesero;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class AdapterContador extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater = null;
    ArrayAdapter<String> lista, lista1;


    public AdapterContador(Context contexto, ArrayAdapter<String> lista,  ArrayAdapter<String> lista1) {
        this.context = contexto;
        this.lista = lista;
        this.lista1 = lista1;
        inflater = (LayoutInflater)context.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(final int i, View convertView, ViewGroup parent) {
        final int[] cantidad = new int[lista.getCount()];
        for(int j=0;j<lista.getCount();j++) {
            cantidad[j]=0;
        }

        final View vista1 = inflater.inflate(R.layout.cont_select_orden_item,null);
        TextView plto = vista1.findViewById(R.id.txt_plato);
        TextView dscrp = vista1.findViewById(R.id.txtview_descrip);
        final TextView num = vista1.findViewById(R.id.txtview_num);
        Button btnmenos = vista1.findViewById(R.id.btn_menos);
        Button btnmas = vista1.findViewById(R.id.btn_mas);
        plto.setText(lista.getItem(i));
        dscrp.setText(lista1.getItem(i));
        num.setText(String.valueOf(cantidad[i]));

        //Funcion contador boton menos
        btnmenos.setTag(i);
        btnmenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cantidad[i] != 0) {
                    cantidad[i]--;
                    num.setText(String.valueOf(cantidad[i]));
                }
            }
        });

        //Funcion contador boton mas
        btnmas.setTag(i);
        btnmas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cantidad[i]++;
                num.setText(String.valueOf(cantidad[i]));
            }
        });


        return vista1;
    }
    @Override
    public int getCount() {
        return lista.getCount();
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
