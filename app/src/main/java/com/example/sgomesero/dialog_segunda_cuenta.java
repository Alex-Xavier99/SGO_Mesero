package com.example.sgomesero;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class dialog_segunda_cuenta {
    public interface FinalizarCuadroDialogo{
        void ResultadoCuadroDialogSngdCuenta(String idpedido);
    }
    private FinalizarCuadroDialogo interfaz;
    private ListView ListView_OrdnFact, ListView_OrdnSnFact;
    private String token, id_Estado, id_pedido;
    private String mes_num, idped1;
    private String [][] arrayPedidos;

    private ArrayList<String> ListIDpedido = new ArrayList<>();
    private ArrayList<String>  ListNombre = new ArrayList<>();
    private Context contexto1;

    public dialog_segunda_cuenta(Context contexto, FinalizarCuadroDialogo actividad,String Token,String mes_num, String idpedido){
        contexto1 = contexto;
        interfaz = actividad;
        this.mes_num=mes_num;
        idped1 = idpedido;
        final Dialog dialogo = new Dialog(contexto);
        dialogo.requestWindowFeature((Window.FEATURE_NO_TITLE));
        dialogo.setCancelable(false);
        dialogo.setContentView((R.layout.dialog_segunda_cuenta));
        dialogo.getWindow().setLayout(980,999);
        dialogo.setCanceledOnTouchOutside(true);
        token = Token;


        BuscarPedidoMesa();

        ListView_OrdnFact  = (ListView) dialogo.findViewById((R.id.listView_OrdnFact));


        ListView_OrdnFact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long lon) {
                interfaz.ResultadoCuadroDialogSngdCuenta(ListIDpedido.get(position));
                dialogo.dismiss();
            }

        });
        dialogo.show();
    }
    //Se busca los pedidos que tiene la mesa
    public void BuscarPedidoMesa(){
        String url = "https://sgo-central-6to.herokuapp.com/api/mesas/" + mes_num;

        AndroidNetworking.get(url)
                .addHeaders("Content-type","application/json")
                .addHeaders("Authorization",token)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            if(status.equals("202")){
                                //Estado mesa: Orden Iniciada
                                JSONArray pedidoarray = response.getJSONArray("pedidos");
                                String pedidoFact= "";
                                if(pedidoarray.length()>1) {
                                    JSONArray clientesarray = response.getJSONArray("clientes");
                                    arrayPedidos = new String[pedidoarray.length()][3];
                                    for (int j = 0; j < pedidoarray.length(); j++) {
                                        JSONObject pedido = pedidoarray.getJSONObject(j);
                                        String idpedido = pedido.getString("id");
                                        arrayPedidos[j][0] = idpedido;

                                        if(clientesarray.length()>0) {
                                            for (int i = 0; i < clientesarray.length(); i++) {
                                                JSONObject cliente = clientesarray.getJSONObject(i);
                                                String idPedCliente = cliente.getString("id");
                                                if (idpedido.equals(idPedCliente)) {
                                                    arrayPedidos[j][1] = cliente.getString("idFac");
                                                    arrayPedidos[j][2] = cliente.getString("cli_nom");
                                                    pedidoFact = "Factura: " + arrayPedidos[j][1] +" " + arrayPedidos[j][2];
                                                    break;
                                                } else {
                                                    arrayPedidos[j][1] = "";
                                                    arrayPedidos[j][2] = "";
                                                    pedidoFact = "S/N Factura Pedido Nro. " + arrayPedidos[j][0];

                                                }
                                            }
                                        }
                                        else{
                                            pedidoFact = "S/N Factura Pedido Nro. " + arrayPedidos[j][0]  ;//Si no existen clientes
                                        }
                                        if(!idpedido.equals(idped1)) {
                                            //Se añade a la lista el id pedido y en otra lista numero de factura y el nombre del cliente
                                            ListIDpedido.add(arrayPedidos[j][0]);
                                            ListNombre.add(pedidoFact);
                                        }
                                    }

                                }
                                else
                                    interfaz.ResultadoCuadroDialogSngdCuenta("N");

                            }
                            Presentadatos();
                        } catch (JSONException e) {
                            Toast.makeText(contexto1, "Error de la Solicitud: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(contexto1, "Error de Servidor: "+anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void Presentadatos(){
        if(!ListNombre.equals(null) && !ListNombre.isEmpty() && ListNombre.size()>0) {
            ArrayAdapter<String> adptList = new ArrayAdapter<String>(contexto1, android.R.layout.simple_list_item_1, ListNombre);
            ListView_OrdnFact.setAdapter(adptList);
        }
    }
    public void VaciarListas(){
        ListNombre.clear();
        ListIDpedido.clear();
    }
}
