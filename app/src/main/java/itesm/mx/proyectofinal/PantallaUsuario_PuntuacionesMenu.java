package itesm.mx.proyectofinal;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Date;

import itesm.mx.proyectofinal.DBstuff.DB_Schema;

/**
 * Created by Martin RB on 27/03/2018.
 */

/*
*
* Menu donde se pueden consultar los puntajes de los distintos juegos
*
*/
public class PantallaUsuario_PuntuacionesMenu extends Fragment implements View.OnClickListener{
    Activity a;
    PantallaUsuario.IMyUserScreen userScreen;
    Button btnMano;
    Button btnP2P;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        return inflater.inflate(R.layout.usrscn_scores_menu, container, false);
    }
    @Override
    public void onAttach(Context c){
        super.onAttach(c);
        a = (Activity) c;
        try{
            userScreen = (PantallaUsuario.IMyUserScreen)a;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        // Establecer fragmento anterior (para boton de back)
        userScreen.setBackFragment(new PantallaUsuario());
        establecimientoDeElementos();
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.usrscn_scores_menu_btnP2P:
                consultarP2P();
                break;
            case R.id.usrscn_scores_menu_btnMano:
                consultarMano();
                break;
        }
    }

    // Funciones auxiliares
    private void consultarMano(){
        PantallaUsuario_Puntuaciones puntuaciones = new PantallaUsuario_Puntuaciones();
        Bundle b = new Bundle();
        b.putString("juego", DB_Schema.ManoTable.TABLE);
        puntuaciones.setArguments(b);
        userScreen.endMyLife(puntuaciones);
    }
    private void consultarP2P(){
        PantallaUsuario_Puntuaciones puntuaciones = new PantallaUsuario_Puntuaciones();
        Bundle b = new Bundle();
        b.putString("juego", DB_Schema.P2PTable.TABLE);
        puntuaciones.setArguments(b);
        userScreen.endMyLife(puntuaciones);
    }
    private void establecimientoDeElementos(){
        btnMano = a.findViewById(R.id.usrscn_scores_menu_btnMano);
        btnP2P = a.findViewById(R.id.usrscn_scores_menu_btnP2P);
        btnP2P.setOnClickListener(this);
        btnMano.setOnClickListener(this);
    }
}
