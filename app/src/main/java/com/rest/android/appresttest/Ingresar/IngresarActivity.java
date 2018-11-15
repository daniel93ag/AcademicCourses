package com.rest.android.appresttest.Ingresar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.rest.android.appresttest.Clases.Curso;
import com.rest.android.appresttest.Clases.Persona;
import com.rest.android.appresttest.MainActivity;
import com.rest.android.appresttest.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class IngresarActivity extends AppCompatActivity {
    Spinner spinner;
    ConsultarCursos consultarCursos;
    public ArrayList<Curso> cursoRes;
    String codCurso;
    String temaCurso;

    String codigoPersona;
    String cedula;
    String nombre;
    String apellido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar);
        final EditText codigoPerET = (EditText)findViewById(R.id.editTextCodigoPer);
        final EditText cedulaET = (EditText)findViewById(R.id.editTextCedula);
        final EditText nombreET = (EditText)findViewById(R.id.editTextNombre);
        final EditText apellidoET = (EditText)findViewById(R.id.editTextApellido);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temaCurso = spinner.getSelectedItem().toString();
                codigoPersona = codigoPerET.getText().toString();
                cedula = cedulaET.getText().toString();
                nombre = nombreET.getText().toString();
                apellido = apellidoET.getText().toString();
                if(codigoPerET.getText().toString().equals("") || cedulaET.getText().toString().equals("")
                        || nombreET.getText().toString().equals("") || apellidoET.getText().toString().equals("") ||
                        cedulaET.getText().length()>10)
                    Snackbar.make(view, "Ingrese valores válidos", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                else
                if(ingresarPersona()) {
                    Snackbar.make(view, "Datos Ingresados!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }
                else
                    Snackbar.make(view, "Ha ocurrido un error!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
            }
        });

        spinner = (Spinner) findViewById(R.id.spinnerCursos);
        consultarCursos = new ConsultarCursos();
        consultarCursos.execute();


    }
    private class InsertarPersonas extends AsyncTask<String,Integer,Boolean> {

        protected Boolean doInBackground(String... params) {

            boolean resul = true;


            try
            {
                //Construimos el objeto cliente en formato JSON
                JSONObject dato = new JSONObject();

                for (int i=0; i<cursoRes.size();i++){
                    if(cursoRes.get(i).getTema().equals(temaCurso)) {
                        codCurso = cursoRes.get(i).getCodigoCurso();
                        Log.d("CodCurso: ",codCurso);
                    }
                }
                Persona persona = new Persona(codigoPersona,nombre,apellido,codCurso,cedula);
                //dato.put("Id", Integer.parseInt(txtId.getText().toString()));
                dato.put("codigoPersona", persona.getCodigoPersona());
                dato.put("cedula", persona.getCedula());
                dato.put("nombre", persona.getNombre());
                dato.put("apellido", persona.getApellido());
                dato.put("codCurso", persona.getCodCurso());



                URL url=new URL ("http://puceing.edu.ec:13000/DanielAngulo/api/persona");
                String message = dato.toString();

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /*milliseconds*/);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setFixedLengthStreamingMode(message.getBytes().length);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.connect();
                OutputStream os = new BufferedOutputStream(conn.getOutputStream());
                os.write(message.getBytes());
                os.flush();
                os.close();





            }
            catch(Exception ex)
            {
                Log.e("ServicioRest","Error!", ex);
                Log.d("result 2: ",""+resul);
                resul = false;
            }
            Log.d("result 3: ",""+resul);
            return resul;
        }

        protected void onPostExecute(Boolean result) {


            if (result)
            {
                Log.d("Estado: ","Insertado OK.");
                final EditText codigoPerET = (EditText)findViewById(R.id.editTextCodigoPer);
                final EditText cedulaET = (EditText)findViewById(R.id.editTextCedula);
                final EditText nombreET = (EditText)findViewById(R.id.editTextNombre);
                final EditText apellidoET = (EditText)findViewById(R.id.editTextApellido);
                codigoPerET.setText("");
                cedulaET.setText("");
                nombreET.setText("");
                apellidoET.setText("");
            }
        }
    }



    public class ConsultarCursos extends AsyncTask<String, Void, String[]> {


        @Override
        protected String[] doInBackground(String... params) {
            return obtenerDatosBDD();
        }

        protected void onPostExecute(String[] cursos) {
            if(cursos != null){
               ArrayAdapter<String> adaptador =
                        new ArrayAdapter<String>(IngresarActivity.this,android.R.layout.simple_spinner_item, cursos);
                adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                               spinner.setAdapter(adaptador);

            }

        }
        public String[] obtenerDatosBDD(){
            StringBuilder content = new StringBuilder();

            // many of these calls can throw exceptions, so i've just
            // wrapped them all in one try/catch statement.
            try
            {

                URL url = new URL("http://puceing.edu.ec:13000/DanielAngulo/api/curso/");

                // create a urlconnection object
                URLConnection urlConnection = url.openConnection();
                // wrap the urlconnection in a bufferedreader
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                String line;

                // read from the urlconnection via the bufferedreader
                while ((line = bufferedReader.readLine()) != null)
                {
                    content.append(line + "\n");
                    Log.d("Cursos:", line);
                }
                bufferedReader.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            Log.i("Cursos:", content.toString());

            JSONObject jsonObject;
            cursoRes = new ArrayList<Curso>();
            ArrayList<String> cursos = new ArrayList<String>();
            try {
                JSONArray jArray = new JSONArray(content.toString());
                for (int i = 0; i < jArray.length(); i++) {

                    JSONObject jObject = jArray.getJSONObject(i);
                    String codCurso = jObject.getString("codigoCurso");
                    String temaCurso = jObject.getString("tema");
                    Double precio = jObject.getDouble("precio");
                    Curso curso = new Curso(codCurso,temaCurso,precio);
                    cursoRes.add(curso);


                    cursos.add(temaCurso);
                }

            }  catch (JSONException e) {
                e.printStackTrace();
            }

            String[] resultado = new String[cursos.size()];

            resultado = cursos.toArray(resultado);
            return resultado;

        }
    }
    public boolean ingresarPersona(){
        InsertarPersonas insertarPersonas = new InsertarPersonas();
        insertarPersonas.execute();
        if (insertarPersonas.isCancelled())
            return false;
        else
            return true;
    }


}
