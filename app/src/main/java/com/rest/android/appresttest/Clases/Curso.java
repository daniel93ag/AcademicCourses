package com.rest.android.appresttest.Clases;

/**
 * Created by DANIEL on 20/6/2016.
 */
public class Curso {
    private String codigoCurso;
    private String tema;
    private double precio;

    public Curso(String codigoCurso, String tema, double precio) {
        this.codigoCurso = codigoCurso;
        this.tema = tema;
        this.precio = precio;
    }

    public String getCodigoCurso() {
        return codigoCurso;
    }

    public String getTema() {
        return tema;
    }

    public double getPrecio() {
        return precio;
    }

    public void setCodigoCurso(String codigoCurso) {
        this.codigoCurso = codigoCurso;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}

