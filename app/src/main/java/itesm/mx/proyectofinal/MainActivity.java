package itesm.mx.proyectofinal;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import itesm.mx.proyectofinal.DBstuff.DB_Schema;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, PantallaUsuario.IMyUserScreen{

    Button btnA, btnB, btnC;
    // Requerido para saber cual fragmento se llama al hacer click en back
    Fragment fragmentoAnterior;

    @Override
    public void onStart(){
        super.onStart();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnA = findViewById(R.id.a);
        btnB = findViewById(R.id.b);
        btnC = findViewById(R.id.c);
        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnC.setOnClickListener(this);
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.c:
                cargarUsuario();
                break;
            case R.id.b:
                cargarDummy();
                break;
        }
    }


    // Funciones de carga de pantallas. Dummy puede ser desechado
    private void cargarUsuario(){
        PantallaUsuario usuario = new PantallaUsuario();
        Fragment fragmentActual = getFragmentManager().findFragmentById(R.id.pantalla);
        if(fragmentActual != null){
            if(!(fragmentActual instanceof PantallaUsuario))
                getFragmentManager().beginTransaction().replace(R.id.pantalla, usuario).commit();
        }
        else{
            getFragmentManager().beginTransaction().add(R.id.pantalla, usuario).commit();
        }

    }
    private void cargarDummy(){
        PantallaDummy dummy = new PantallaDummy();
        if(getFragmentManager().findFragmentById(R.id.pantalla) != null){
            getFragmentManager().beginTransaction().replace(R.id.pantalla, dummy).commit();
        }
        else{
            getFragmentManager().beginTransaction().add(R.id.pantalla, dummy).commit();
        }
    }


    @Override
    public void endMyLife(Fragment newFragment){
        getFragmentManager().beginTransaction().replace(R.id.pantalla, newFragment).commit();
    }
    @Override
    public void setBackFragment(Fragment backFragment){
        fragmentoAnterior = backFragment;
    }
    // Implementar para la pantalla de usuario
    @Override
    public void onBackPressed(){
        if(fragmentoAnterior != null){
            getFragmentManager().beginTransaction().replace(R.id.pantalla, fragmentoAnterior).commit();
        }
    }
}
