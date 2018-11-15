package com.rest.android.appresttest.Modificar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rest.android.appresttest.Clases.Curso;
import com.rest.android.appresttest.Clases.Persona;
import com.rest.android.appresttest.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import com.rest.android.appresttest.Consulta.ConsultarActivity;

public class ModificarActivity extends AppCompatActivity {
    Spinner spinner;
    ConsultarCursos consultarCursos;
    public ArrayList<Curso> cursoRes;
    String codCurso;
    String temaCurso;

    String codigoPersona;
    String cedula;
    String nombre;
    String apellido;

    String cursoElegido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar);

        final EditText codigoPerETM = (EditText)findViewById(R.id.editTextCodigoPerM);
        final EditText cedulaETM = (EditText)findViewById(R.id.editTextCedulaM);
        final EditText nombreETM = (EditText)findViewById(R.id.editTextNombreM);
        final EditText apellidoETM = (EditText)findViewById(R.id.editTextApellidoM);




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                temaCurso = spinner.getSelectedItem().toString();
                codigoPersona = codigoPerETM.getText().toString();
                cedula = cedulaETM.getText().toString();
                nombre = nombreETM.getText().toString();
                apellido = apellidoETM.getText().toString();
                if(codigoPerETM.getText().toString().equals("") || cedulaETM.getText().toString().equals("")
                        || nombreETM.getText().toString().equals("") || apellidoETM.getText().toString().equals("") ||
                        cedulaETM.getText().length()>10)
                    Snackbar.make(view, "Ingrese valores válidos", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                else
                if(ModificarPersona()) {
                    Snackbar.make(view, "Datos Ingresados!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }
                else
                    Snackbar.make(view, "Ha ocurrido un error!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
            }
        });

        spinner = (Spinner) findViewById(R.id.spinnerCursosM);
        consultarCursos = new ConsultarCursos();
        consultarCursos.execute();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("editTextCodigoPerM");
            String cedula = extras.getString("editTextCedulaM");
            String nombre = extras.getString("editTextNombreM");
            String apellido = extras.getString("editTextApellidoM");
            cursoElegido = extras.getString("cursoM");
            codigoPerETM.setText(value);
            cedulaETM.setText(cedula);
            nombreETM.setText(nombre);
            apellidoETM.setText(apellido);
            Log.d("CURSO ELEGIDO: ",cursoElegido);




        }

    }



    private class ModificarPersonas extends AsyncTask<Void,Void,Boolean> {
        Persona persona;

        protected void onPreExecute() {
            super.onPreExecute();

            persona = new Persona(codigoPersona,nombre,apellido,codCurso,cedula);

        }

        protected Boolean doInBackground(Void... params) {

            JSONObject jsonObject = new JSONObject();

            try
            {
                //Construimos el objeto cliente en formato JSON
                JSONObject dato = new JSONObject();
               for (int i=0; i<cursoRes.size();i++){
                    if(cursoRes.get(i).getTema().equals(temaCurso)) {
                        codCurso = cursoRes.get(i).getCodigoCurso();
                        persona.setCodCurso(codCurso);
                        Log.d("CodCurso: ",codCurso);
                    }

                }



                Log.d("PERSONA!!!",persona.getCodigoPersona());
                Log.d("PERSONA!!!",persona.getNombre());
                Log.d("PERSONA!!!",persona.getCodCurso());
                Log.d("PERSONA!!!",persona.getCedula());
                Log.d("PERSONA!!!",persona.getApellido());
                //dato.put("Id", Integer.parseInt(txtId.getText().toString()));
                dato.put("codigoPersona", persona.getCodigoPersona().toString());
                dato.put("nombre", persona.getNombre().toString());
                dato.put("apellido", persona.getApellido().toString());
                dato.put("cedula", persona.getCedula().toString());
                dato.put("codCurso", persona.getCodCurso().toString());
                //dato.put("cursos", null);

                URL url=new URL ("http://puceing.edu.ec:13000/DanielAngulo/api/persona");
                String message = dato.toString();
                Log.d("STTONG MESSAGE ",message);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /*milliseconds*/);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("PUT");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setFixedLengthStreamingMode(message.getBytes().length);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.connect();
                OutputStream os = new BufferedOutputStream(conn.getOutputStream());
                os.write(message.getBytes());
                os.flush();
                os.close();




        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
            return true;
        }

        protected void onPostExecute(Boolean result) {


            if (result)
            {
                Log.d("Estado: ","Insertado OK.");
                final EditText codigoPerET = (EditText)findViewById(R.id.editTextCodigoPerM);
                final EditText cedulaET = (EditText)findViewById(R.id.editTextCedulaM);
                final EditText nombreET = (EditText)findViewById(R.id.editTextNombreM);
                final EditText apellidoET = (EditText)findViewById(R.id.editTextApellidoM);
                codigoPerET.setText("");
                cedulaET.setText("");
                nombreET.setText("");
                apellidoET.setText("");

                Intent intent = new Intent(ModificarActivity.this,ConsultarActivity.class);
                startActivity(intent);
                Toast toast = Toast.makeText(ModificarActivity.this, "Se ha modificado el item", Toast.LENGTH_SHORT);
                toast.show();
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
                        new ArrayAdapter<String>(ModificarActivity.this,android.R.layout.simple_spinner_item, cursos);

                // Specify the layout to use when the list of choices appears
                adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
                spinner.setAdapter(adaptador);

                String temaCurso="";
                for (int i=0; i<cursoRes.size();i++){
                    if(cursoRes.get(i).getCodigoCurso().equals(cursoElegido))
                        temaCurso=cursoRes.get(i).getTema();
                }
                spinner.setSelection(((ArrayAdapter)spinner.getAdapter()).getPosition(temaCurso));
                Log.d("SPINNER ELEGIDO: ",spinner.getSelectedItem().toString());
                //lv.setAdapter(new CustomAdapterBuscar(BuscarActivity.this, paradas, null));
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
    public boolean ModificarPersona(){
        ModificarPersonas modificarPersona = new ModificarPersonas();
        modificarPersona.execute();
        if (modificarPersona.isCancelled())
            return false;
        else
            return true;
    }


}
