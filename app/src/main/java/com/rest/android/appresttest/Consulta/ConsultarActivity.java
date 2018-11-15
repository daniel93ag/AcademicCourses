package com.rest.android.appresttest.Consulta;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rest.android.appresttest.Clases.Persona;
import com.rest.android.appresttest.Modificar.ModificarActivity;
import com.rest.android.appresttest.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.rest.android.appresttest.Ingresar.IngresarActivity;

public class ConsultarActivity extends AppCompatActivity {

    ListView lstConsulta;
    String itemSelectedID="";
    String itemSelectedCedula="";
    String itemSelectedNombre="";
    String itemSelectedApellido="";
    String itemSelectedCurso="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lstConsulta = (ListView) findViewById(R.id.listViewConsulta);
        registerForContextMenu(lstConsulta);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btnBuscarTodos);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MostrarTodos())
                    Snackbar.make(view, "Mostrando datos...", Snackbar.LENGTH_LONG).setAction("mostrarTodos", null).show();
                else
                    Snackbar.make(view, "Ha ocurrido un error!", Snackbar.LENGTH_LONG).setAction("mostrarTodos", null).show();
            }
        });

        lstConsulta.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // selected item
                String selected = lstConsulta.getItemAtPosition(position).toString();

                String result = selected.substring(selected.indexOf(" ")+1, selected.indexOf("\n"));
                String nombre = selected.substring(selected.indexOf("Nombre:"));
                String nombreElegido= nombre.substring(nombre.indexOf(" ")+1,nombre.indexOf("\n"));
                String cedula = selected.substring(selected.indexOf("Cédula:"));
                String cedulaElegida = cedula.substring(cedula.indexOf(" ")+1,cedula.indexOf("\n"));
                String apellido = selected.substring(selected.indexOf("Apellido:"));
                String apellidoElegido = apellido.substring(apellido.indexOf(" ")+1,apellido.indexOf("\n"));
                String curso = selected.substring(selected.indexOf("Curso:"));
                String cursoElegido = curso.substring(curso.indexOf(" ")+1);
                Log.d("Curso Elegido: ",cursoElegido);
                //Toast toast=Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT);
                //toast.show();
                itemSelectedID=result;
                itemSelectedNombre=nombreElegido;
                itemSelectedCedula=cedulaElegida;
                itemSelectedApellido=apellidoElegido;
                itemSelectedCurso=cursoElegido;
                //EditText edt = (EditText)findViewById(R.id.editTextCodigoPerC);
                //edt.setText(result);


                return false;


            }



        });

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.listViewConsulta) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_item, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.action_eliminar:
                ListView lv = (ListView)findViewById(R.id.listViewConsulta);
                Log.d("To String Item: ",lstConsulta.getItemAtPosition(1).toString());
                EliminarPersona eliminarPersona = new EliminarPersona();
                eliminarPersona.execute();
                if(eliminarPersona.isCancelled()) {
                    Toast toast = Toast.makeText(this, "Ha Ocurrido un error", Toast.LENGTH_SHORT);
                    toast.show();
                    return false;
                }
                else {
                    Toast toast = Toast.makeText(this, "El item se ha eliminado!", Toast.LENGTH_SHORT);
                    toast.show();
                    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btnBuscarTodos);
                    fab.performClick();

                }



                return true;
            case R.id.action_modificar:
                Context context = getApplicationContext();
                Intent intent = new Intent(getApplicationContext(),ModificarActivity.class);
                intent.putExtra("editTextCodigoPerM",itemSelectedID);
                intent.putExtra("editTextCedulaM",itemSelectedCedula);
                intent.putExtra("editTextNombreM",itemSelectedNombre);
                intent.putExtra("editTextApellidoM",itemSelectedApellido);
                intent.putExtra("cursoM",itemSelectedCurso);
                startActivity(intent);
                return true;

            default:
                return super.onContextItemSelected(item);
        }

    }


    public class ConsultarPersonas extends AsyncTask<String, Void, String[]> {


        @Override
        protected String[] doInBackground(String... params) {
            return obtenerDatosBDD();
        }

        protected void onPostExecute(String[] personas) {
            if(personas != null){
                ArrayAdapter<String> adaptador =
                      new ArrayAdapter<String>(ConsultarActivity.this,android.R.layout.simple_list_item_1, personas);

                lstConsulta.setAdapter(adaptador);

            }

        }
        public String[] obtenerDatosBDD(){
            StringBuilder content = new StringBuilder();
            try
            {
                URL url = new URL("http://puceing.edu.ec:13000/DanielAngulo/api/persona/");
                // create a urlconnection object
                URLConnection urlConnection = url.openConnection();
                // wrap the urlconnection in a bufferedreader
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                String line;
                // read from the urlconnection via the bufferedreader
                while ((line = bufferedReader.readLine()) != null)
                {
                    content.append(line + "\n");
                    Log.d("PERSONAS:", line);
                }
                bufferedReader.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            Log.i("PERSONAS:", content.toString());

            JSONObject jsonObject;
            ArrayList<Persona> personasRes = new ArrayList<Persona>();
            ArrayList<String> personas = new ArrayList<String>();
            try {
                JSONArray jArray = new JSONArray(content.toString());
                for (int i = 0; i < jArray.length(); i++) {

                    JSONObject jObject = jArray.getJSONObject(i);
                    String nombre = jObject.getString("nombre");
                    String apellido = jObject.getString("apellido");
                    String codPersona = jObject.getString("codigoPersona");
                    String cedula = jObject.getString("cedula");
                    String curso = jObject.getString("codCurso");

                   personas.add("Código: " + codPersona + "\nNombre: "+nombre +"\nApellido: "+apellido+"\nCédula: "+cedula + "\nCod. Curso: "+curso);
                   Log.i("Persona:", nombre);
                }

            }  catch (JSONException e) {
                e.printStackTrace();
            }

            String[] resultado = new String[personas.size()];

            resultado = personas.toArray(resultado);
            return resultado;

        }
    }




    /*################################# CONSULTAR PERSONAS POR ID ################################*/
    public class ConsultarPersonasPorID extends AsyncTask<String, Void, String[]> {


        @Override
        protected String[] doInBackground(String... params) {
            return obtenerDatosBDD();
        }

        protected void onPostExecute(String[] personas) {
            if(personas != null){


                //String[] personaRes = new String[1];
                //personaRes[0] = personas;

                ArrayAdapter<String> adaptador =
                        new ArrayAdapter<String>(ConsultarActivity.this,android.R.layout.simple_list_item_1, personas);

                lstConsulta.setAdapter(adaptador);
                //lv.setAdapter(new CustomAdapterBuscar(BuscarActivity.this, paradas, null));
            }

        }
        public String[] obtenerDatosBDD(){
            StringBuilder content = new StringBuilder();

            // many of these calls can throw exceptions, so i've just
            // wrapped them all in one try/catch statement.
            try
            {

                EditText tfCodigoPersona = (EditText)findViewById(R.id.editTextCodigoPerC);
                URL url = new URL("http://puceing.edu.ec:13000/DanielAngulo/api/persona/"+tfCodigoPersona.getText());

                // create a urlconnection object
                URLConnection urlConnection = url.openConnection();
                // wrap the urlconnection in a bufferedreader
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                String line;

                // read from the urlconnection via the bufferedreader
                while ((line = bufferedReader.readLine()) != null)
                {
                    content.append(line + "\n");
                    Log.d("PERSONAS:", line);
                }
                bufferedReader.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();

            }
            Log.i("PERSONAS:", content.toString());

            JSONObject jsonObject;
            ArrayList<Persona> personasRes = new ArrayList<Persona>();
            ArrayList<String> personas = new ArrayList<String>();
            try {

                JSONObject jObject = new JSONObject(content.toString());

                    String nombre = jObject.getString("nombre");
                    String apellido = jObject.getString("apellido");
                    String codPersona = jObject.getString("codigoPersona");
                    String cedula = jObject.getString("cedula");
                    String curso = jObject.getString("codCurso");


                    personas.add ("Código: " + codPersona + "\nNombre: "+nombre +"\nApellido: "+apellido+"\nCédula: "+cedula + "\nCod. Curso: "+curso);
                    Log.i("Persona:", nombre);


            }  catch (JSONException e) {
                e.printStackTrace();
            }

            String[] resultado = new String[personas.size()];

            resultado = personas.toArray(resultado);
            return resultado;

        }
    }

    public boolean MostrarTodos() {
        ConsultarPersonas consultaPer = new ConsultarPersonas();
        consultaPer.execute();
        if(consultaPer.isCancelled())
            return false;
        else
            return true;
    }


    //################################# ELIMINAR PERSONA #########################################
    public class EliminarPersona extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            URL url = null;

            try {
                url = new URL("http://puceing.edu.ec:13000/DanielAngulo/api/persona/"+itemSelectedID);
                Log.d("itemID: ",itemSelectedID);
            } catch (MalformedURLException exception) {
                exception.printStackTrace();
            }
            HttpURLConnection httpURLConnection = null;
            try {
                httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setRequestMethod("DELETE");
                httpURLConnection.connect();
                httpURLConnection.getResponseCode();
            } catch (IOException exception) {
                exception.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }
            return null;
        }
    }

    public boolean MostrarPorID(View v) {
        EditText edtCodPer = (EditText)findViewById(R.id.editTextCodigoPerC);
        if(edtCodPer.getText().toString().equals("")){
            Toast toast = Toast.makeText(ConsultarActivity.this, "No ingresó ningún campo", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        else {
            ConsultarPersonasPorID consultaPer = new ConsultarPersonasPorID();
            consultaPer.execute();
            if (consultaPer.isCancelled())
                return false;
            else
                return true;
        }
    }



}
