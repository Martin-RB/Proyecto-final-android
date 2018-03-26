package itesm.mx.proyectofinal;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnA, btnB, btnC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnA = findViewById(R.id.btnA);
        btnB = findViewById(R.id.btnB);
        btnC = findViewById(R.id.btnC);
        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnC.setOnClickListener(this);
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btnC:
                cargarUsuario();
                break;
        }
    }
    private void cargarUsuario(){
        if(getFragmentManager().findFragmentById(R.id.pantalla) != null){
            // o nada. idk
            //getFragmentManager().beginTransaction().replace(R.id.pantalla, new PantallaUsuario).commit();
        }
        else{
            getFragmentManager().beginTransaction().add(R.id.pantalla, new PantallaUsuario()).commit();
        }

    }
}
