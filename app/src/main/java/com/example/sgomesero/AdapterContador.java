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
    ArrayAdapter<String> lista;


    public AdapterContador(Context contexto, ArrayAdapter<String> lista1) {
        this.context = contexto;
        this.lista = lista1;
        inflater = (LayoutInflater)context.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        final View vista1 = inflater.inflate(R.layout.cont_select_orden_item,null);
        TextView plto = vista1.findViewById(R.id.txt_plato);
        TextView num = vista1.findViewById(R.id.txtview_num);
        Button btnmenos = vista1.findViewById(R.id.btn_menos);
        Button btnmas = vista1.findViewById(R.id.btn_mas);

        plto.setText(lista.getItem(i));
        if(btnmenos.isPressed())
        {
            num.setText("-1");
        }
        if(btnmas.isPressed()){
            num.setText("+1");
        }


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
