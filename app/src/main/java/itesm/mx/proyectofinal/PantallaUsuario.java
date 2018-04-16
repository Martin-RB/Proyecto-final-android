package itesm.mx.proyectofinal;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import itesm.mx.proyectofinal.DBstuff.DB_Operations;
import itesm.mx.proyectofinal.DBstuff.DB_Schema;

/**
 * Created by Martin RB on 26/03/2018.
 */

public class PantallaUsuario extends Fragment implements View.OnClickListener{

    Activity a;
    // Interfaz de acceso para el main activity
    IMyUserScreen userScreen;
    DB_Operations ops;
    // Variable que almacena la una foto. Cuando el usuario usa cancelar. La foto se restaura
    byte[] antiguaFoto;
    int aa;

    EditText
            etNombreUsuario;
    TextView
            tvNombreUsuario;
    Button
            btnPuntuaciones,
            btnEditar,
            btnFinEditar,
            btnEditarFoto,
            btnCancelarEditar;
    ImageView
            ivFoto;


    @Override
    // Obtencion de la actividad y el enlace usando la interfaz
    public void onAttach(Context con){
        super.onAttach(con);
        a = (Activity) con;
        try{
            userScreen = (IMyUserScreen)a;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        return inflater.inflate(R.layout.usr_scn, container, false);
    }
    /* Inicialización: Se inicia aqui y no en el onStart() para evitar problemas con los intents de
    * la camara*/
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        // Obtener acceso a funciones de la base de datos
        ops = new DB_Operations(a);
        // Establecer fragmento para hacer back
        userScreen.setBackFragment(null);

        establecimientoDeElementos();
        cargarDatos();
    }
    @Override
    public void onStart(){
        super.onStart();
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
    }

    @Override
    public void onStop(){
        super.onStop();
    }
    @Override
    public void onDestroyView(){
        super.onDestroyView();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
    }
    @Override
    public void onDetach(){
        super.onDetach();
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.usrscn_btnScores:
                mostrarPuntajes();
                break;
            case R.id.usrscn_btnEdith:
                editarDatos();
                break;
            case R.id.usrscn_btnFinishEdith:
                finEditarDatos();
                break;
            case R.id.usrscn_btnPhotoEdith:
                tomarFoto();
                break;
            case R.id.usrscn_btnCancelEdith:
                cancelarEditarDatos();
                break;
            default:
                // Try catch vacio. Adrmiralo un momento
                try {
                    throw new Exception("No existe el boton: " + v.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
    @Override // Cosas de la camara
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
            // Request de foto
            case 0:
                // Foto tomada
                if(resultCode == -1){
                    Bundle dat = data.getExtras();
                    ivFoto.setImageBitmap((Bitmap)dat.get("data"));
                }
                break;
        }
    }


    // Control de acceso a la base de datos
    @Override
    public void onPause(){
        super.onPause();
        ops.publicClose();
    }
    @Override
    public void onResume(){
        super.onResume();
        ops.publicOpen();
    }


    // Funciones auxiliares
    private Bitmap getBitmapFromImageView(ImageView iv){
        Bitmap b;
        Drawable d;

        d = iv.getDrawable();
        b = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(b);
        d.setBounds(0,0,canvas.getWidth(), canvas.getHeight());
        d.draw(canvas);
        return b;
    }
    private void establecimientoDeElementos(){
        tvNombreUsuario = a.findViewById(R.id.usrscn_tvUsrName);
        etNombreUsuario = a.findViewById(R.id.usrscn_etUsrName);
        btnPuntuaciones = a.findViewById(R.id.usrscn_btnScores);
        btnEditar = a.findViewById(R.id.usrscn_btnEdith);
        btnFinEditar = a.findViewById(R.id.usrscn_btnFinishEdith);
        btnEditarFoto = a.findViewById(R.id.usrscn_btnPhotoEdith);
        btnCancelarEditar = a.findViewById(R.id.usrscn_btnCancelEdith);
        ivFoto = a.findViewById(R.id.usrscn_ivPhoto);
        btnEditarFoto.setOnClickListener(this);
        btnEditar.setOnClickListener(this);
        btnFinEditar.setOnClickListener(this);
        btnPuntuaciones.setOnClickListener(this);
        btnCancelarEditar.setOnClickListener(this);
        btnFinEditar.setVisibility(View.INVISIBLE);
        btnCancelarEditar.setVisibility(View.INVISIBLE);
    }

    private void mostrarPuntajes(){
        userScreen.endMyLife(new PantallaUsuario_PuntuacionesMenu());
    }

    // *Inicializadores
    private void cargarDatos(){
        String nombre;
        byte[] foto;

        // Cargar nombre
        nombre = ops.obtenerNombre();
        if(nombre != null){
            tvNombreUsuario.setText(nombre);
        }
        else{
            tvNombreUsuario.setText("I AM ERROR");
        }

        // Cargar foto
        foto = ops.obtenerFoto();
        if(foto != null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(foto, 0, foto.length);
            ivFoto.setImageBitmap(bitmap);
        }
        else{
            ivFoto.setImageResource(R.drawable.ic_launcher_background);
        }
    }

    // *Edicion de datos
    // **Inicializar edicion de datos
    private void editarDatos(){
        userScreen.setBackFragment(this);
        tvNombreUsuario.setVisibility(View.INVISIBLE);
        etNombreUsuario.setVisibility(View.VISIBLE);
        btnEditarFoto.setVisibility(View.VISIBLE);
        btnEditar.setVisibility(View.INVISIBLE);
        btnPuntuaciones.setVisibility(View.INVISIBLE);
        btnFinEditar.setVisibility(View.VISIBLE);
        btnCancelarEditar.setVisibility(View.VISIBLE);
        etNombreUsuario.setText(tvNombreUsuario.getText().toString());

        // Guardar foto actual por si se cancela la operacion
        Bitmap b = getBitmapFromImageView(ivFoto);
        ByteArrayOutputStream s = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, s);
        antiguaFoto = s.toByteArray();
    }
    // **Cuando el usuario presiona el boton de tomar foto
    private void tomarFoto(){
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(i.resolveActivity(a.getPackageManager()) != null){
            startActivityForResult(i, 0);
        }
    }
    // **El usuario acepto los cambios
    private void finEditarDatos(){
        String nuevoNombre;
        byte[] nuevaFoto;

        userScreen.setBackFragment(null);
        nuevoNombre = etNombreUsuario.getText().toString();
        Bitmap b = getBitmapFromImageView(ivFoto);
        ByteArrayOutputStream s = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, s);
        nuevaFoto = s.toByteArray();

        if(nuevoNombre.equals("")){
            Toast.makeText(a, "Escribe un nombre valido", Toast.LENGTH_SHORT).show();
        }
        else if(nuevoNombre.length() > 16){
            Toast.makeText(a, "Nombre muy grande", Toast.LENGTH_SHORT).show();
        }
        else{
            ops.cambiarFoto(nuevaFoto);
            ops.cambiarNombre(nuevoNombre);
            Toast.makeText(a, "Cambios realizados con éxito", Toast.LENGTH_SHORT).show();
        }

        etNombreUsuario.setVisibility(View.INVISIBLE);
        tvNombreUsuario.setText(nuevoNombre);
        tvNombreUsuario.setVisibility(View.VISIBLE);
        btnEditarFoto.setVisibility(View.INVISIBLE);
        btnFinEditar.setVisibility(View.INVISIBLE);userScreen.setBackFragment(null);
        btnCancelarEditar.setVisibility(View.INVISIBLE);
        btnPuntuaciones.setVisibility(View.VISIBLE);
        btnEditar.setVisibility(View.VISIBLE);
    }
    // **El usuario cancelo los cambios
    private void cancelarEditarDatos(){
        Bitmap b = BitmapFactory.decodeByteArray(antiguaFoto, 0, antiguaFoto.length);
        ivFoto.setImageBitmap(b);
        userScreen.setBackFragment(null);

        etNombreUsuario.setVisibility(View.INVISIBLE);
        tvNombreUsuario.setVisibility(View.VISIBLE);
        btnEditarFoto.setVisibility(View.INVISIBLE);
        btnFinEditar.setVisibility(View.INVISIBLE);
        btnCancelarEditar.setVisibility(View.INVISIBLE);
        btnPuntuaciones.setVisibility(View.VISIBLE);
        btnEditar.setVisibility(View.VISIBLE);
    }

    public interface IMyUserScreen{
        // Cambia el fragmento al pasado por argumento
        void endMyLife(Fragment f);
        // Establecer que fragmento será el que se llame cuando se use el boton de back
        void setBackFragment(Fragment f);
    }
}
