/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Nerea
 */
public class Recomendation {

    private int IDRecomendacion;
    private String ISBN;
    private int IDCurso;
    private int IDProfesor;
    private String comentario;

    public Recomendation(int IDRecomendacion, String ISBN, int IDCurso, int IDProfesor, String comentario) {
        this.IDRecomendacion = IDRecomendacion;
        this.ISBN = ISBN;
        this.IDCurso = IDCurso;
        this.IDProfesor = IDProfesor;
        this.comentario = comentario;
    }

    public int getIDRecomendacion() {
        return IDRecomendacion;
    }

    public void setIDRecomendacion(int IDRecomendacion) {
        this.IDRecomendacion = IDRecomendacion;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public int getIDCurso() {
        return IDCurso;
    }

    public void setIDCurso(int IDCurso) {
        this.IDCurso = IDCurso;
    }

    public int getIDProfesor() {
        return IDProfesor;
    }

    public void setIDProfesor(int IDProfesor) {
        this.IDProfesor = IDProfesor;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

}