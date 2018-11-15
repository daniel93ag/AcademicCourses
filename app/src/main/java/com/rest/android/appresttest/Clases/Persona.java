package com.rest.android.appresttest.Clases;

/**
 * Created by DANIEL on 20/6/2016.
 */
public class Persona {
    private String codigoPersona;
    private String nombre;
    private String apellido;
    private String cedula;
    private String codCurso;

    public Persona(String codigoPersona, String nombre, String apellido, String codCurso, String cedula) {
        this.codigoPersona = codigoPersona;
        this.nombre = nombre;
        this.apellido = apellido;
        this.cedula = cedula;
        this.codCurso = codCurso;
    }

    public String getCodigoPersona() {
        return codigoPersona;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getCedula() {
        return cedula;
    }

    public String getCodCurso() {
        return codCurso;
    }

    public void setCodigoPersona(String codigoPersona) { this.codigoPersona = codigoPersona; }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setCedula(String cedula) { this.cedula = cedula; }

    public void setCodCurso(String codCurso) {
        this.codCurso = codCurso;
    }


}
