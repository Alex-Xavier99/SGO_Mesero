package com.example.sgomesero;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AdapterSelect extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater = null;
    String[][] datos;

    public AdapterSelect(Context contexto, String[][] matriz){
        this.context = contexto;
        this.datos = matriz;
        inflater = (LayoutInflater)context.getSystemService(contexto.LAYOUT_INFLATER_SERVICE);
        int i,j;
    }
    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        final View vista1 = inflater.inflate(R.layout.select_item,null);
        TextView cod = vista1.findViewById(R.id.txtview_codigo);
        TextView ordn = vista1.findViewById(R.id.txtview_descrip);
        TextView cant = vista1.findViewById(R.id.txtplain_cant);
        TextView vunit = vista1.findViewById(R.id.txtview_vunit);
        TextView total = vista1.findViewById(R.id.txtview_total);

        cod.setText(datos[i][0]);
        ordn.setText(datos[i][1]);
        cant.setText(datos[i][2]);
        vunit.setText(datos[i][3]);
        total.setText( datos[i][4]);
        return vista1;
    }
    @Override
    public int getCount()  {
        return datos.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    public Double Operacion(){
        int i,j;
        double  res = 0;
        for(i=0;i<datos.length;i++){
            for(j = 0; j<datos[i].length;j++) {
                if(j==4 && datos[i][4]!="")
                    res += Double.parseDouble(datos[i][4]);
            }
        }
        return res;
    }
}
