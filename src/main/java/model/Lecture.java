/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Date;

/**
 *
 * @author Nerea
 */
public class Lecture {
    private int IDLectura;
    private String Estado;
    private String Fecha_Inicio;
    private String Fecha_Fin;
    private int Paginas_Leidas;
    private double valoracion;
    private String Comentario;
    private int IDUsuario;
    private String ISBN;
    private int IDProfesor;

    public Lecture(int IDLectura, String Estado, String Fecha_Inicio, String Fecha_Fin, int Paginas_Leidas, double valoracion, String Comentario, int IDUsuario, String ISBN, int IDProfesor) {
        this.IDLectura = IDLectura;
        this.Estado = Estado;
        this.Fecha_Inicio = Fecha_Inicio;
        this.Fecha_Fin = Fecha_Fin;
        this.Paginas_Leidas = Paginas_Leidas;
        this.valoracion = valoracion;
        this.Comentario = Comentario;
        this.IDUsuario = IDUsuario;
        this.ISBN = ISBN;
        this.IDProfesor = IDProfesor;
    }

    public int getIDLectura() {
        return IDLectura;
    }

    public void setIDLectura(int IDLectura) {
        this.IDLectura = IDLectura;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String Estado) {
        this.Estado = Estado;
    }

    public String getFecha_Inicio() {
        return Fecha_Inicio;
    }

    public void setFecha_Inicio(String Fecha_Inicio) {
        this.Fecha_Inicio = Fecha_Inicio;
    }

    public String getFecha_Fin() {
        return Fecha_Fin;
    }

    public void setFecha_Fin(String Fecha_Fin) {
        this.Fecha_Fin = Fecha_Fin;
    }

    public int getPaginas_Leidas() {
        return Paginas_Leidas;
    }

    public void setPaginas_Leidas(int Paginas_Leidas) {
        this.Paginas_Leidas = Paginas_Leidas;
    }

    public double getValoracion() {
        return valoracion;
    }

    public void setValoracion(double valoracion) {
        this.valoracion = valoracion;
    }

    public String getComentario() {
        return Comentario;
    }

    public void setComentario(String Comentario) {
        this.Comentario = Comentario;
    }

    public int getIDUsuario() {
        return IDUsuario;
    }

    public void setIDUsuario(int IDUsuario) {
        this.IDUsuario = IDUsuario;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public int getIDProfesor() {
        return IDProfesor;
    }

    public void setIDProfesor(int IDProfesor) {
        this.IDProfesor = IDProfesor;
    }
}
