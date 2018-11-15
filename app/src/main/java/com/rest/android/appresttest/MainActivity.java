package com.rest.android.appresttest;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.rest.android.appresttest.Consulta.ConsultarActivity;
import com.rest.android.appresttest.Eliminar.EliminarActivity;
import com.rest.android.appresttest.Ingresar.IngresarActivity;
import com.rest.android.appresttest.Modificar.ModificarActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Inscripción en Cursos");
        getSupportActionBar().setIcon(R.mipmap.app_icon2);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public void mostrarIngreso(View v){
        Intent intent = new Intent(MainActivity.this,IngresarActivity.class);
        startActivity(intent);
    }

    public void mostrarEdicion(View v){
        Intent intent = new Intent(MainActivity.this,ModificarActivity.class);
        startActivity(intent);
    }

    public void mostrarEliminar(View v){
        Intent intent = new Intent(MainActivity.this,EliminarActivity.class);
        startActivity(intent);
    }

    public void mostrarConsultar(View v){
        Intent intent = new Intent(MainActivity.this,ConsultarActivity.class);
        startActivity(intent);
    }
}
