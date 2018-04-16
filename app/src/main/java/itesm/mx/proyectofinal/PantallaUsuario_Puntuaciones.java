package itesm.mx.proyectofinal;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import itesm.mx.proyectofinal.DBstuff.ActivityDataPackages.ManoGameData;
import itesm.mx.proyectofinal.DBstuff.ActivityDataPackages.P2PGameData;
import itesm.mx.proyectofinal.DBstuff.DB_Operations;
import itesm.mx.proyectofinal.DBstuff.DB_Schema;

/**
 * Created by Martin RB on 29/03/2018.
 */

/*
*
* Fragmento que muestra las puntuaciones dependiendo del tipo de juego que se haya seleccionado
* TODO: Hacer un nuevo adapter para cada tipo de juego. ArrayAdapter sucks!
*
* */

public class PantallaUsuario_Puntuaciones extends ListFragment implements AdapterView.OnItemClickListener{
    DB_Operations ops;
    Activity a;
    PantallaUsuario.IMyUserScreen userScreen;
    SimpleDateFormat format;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstance){
        return inflater.inflate(R.layout.usrscn_score, container, false);
    }
    @Override
    public void onAttach(Context c){
        super.onAttach(c);
        a = (Activity) c;
        try{
            userScreen = (PantallaUsuario.IMyUserScreen)c;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onStart(){
        super.onStart();
        userScreen.setBackFragment(new PantallaUsuario_PuntuacionesMenu());
        //
        ops = new DB_Operations(getActivity());
        // Formato de fecha
        format = new SimpleDateFormat("dd/MM/YYYY hh:mm");

        Bundle b = getArguments();
        if(b != null && !b.isEmpty()){
            String juego = b.getString("juego");
            switch (juego){
                case DB_Schema.P2PTable.TABLE:
                    P2P();
                    break;
                case DB_Schema.ManoTable.TABLE:
                    mano();
                    break;
            }
        }
        else{
            try{
                throw new Exception("Error: No hay datos de entrada.");
            }catch (Exception e){
                e.printStackTrace();
                return;
            }
        }
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){

    }

    private void P2P(){
        // Datos de la base de datos
        ArrayList<P2PGameData> data;
        // Datos convertidos en string
        ArrayList<String> stringedData = new ArrayList<>();
        // Datos convertidos a arreglos de strings para agregarlos al adaptador
        String[] output = new String[]{};

        // Obtener puntajes de la base de datos
        try {
            data = ops.obtenerScoreP2P();
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }
        // Convertir a strings
        if(data.size() > 0){
            for(P2PGameData gameData: data){
                stringedData.add(
                        gameData.getNombreMio() + " - " + gameData.getPuntajeMio() + " : " +
                                gameData.getPuntajeContrincante() + " - " + gameData.getNombreContrincante() + " :: " +
                                format.format(gameData.getFecha())
                );
            }
        }
        else

        // Convertir strings a arreglo
        output = stringedData.toArray(output);
        // Agregar al adaptador
        setListAdapter(
                new ArrayAdapter<>(
                        getActivity(),
                        android.R.layout.simple_list_item_1,
                        output
                )
        );
    }
    private void mano(){
        ManoGameData data;
        ArrayList<String> strinedData = new ArrayList<>();
        String[] output = new String[]{};

        try {
            data = ops.obtenerScoreMano();
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }

        // Si no existe puntuacion maxima
        if(data == null){
            strinedData.add("No hay puntuaciones maximas. Juega una partida de la mano!");
        }else{
            strinedData.add(
                    String.format(
                            "MAX: %s : %s",
                            data.getPuntaje(),
                            format.format(data.getFecha())
                    )
            );
        }

        String[] s = new String[]{};
        s = strinedData.toArray(s);
        setListAdapter(new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, s));
    }

}
