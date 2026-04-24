/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Nerea
 */
public class Book {

    private String ISBN;
    private String Titulo;
    private String Autor;
    private String Genero;
    private String Tematica;
    private int Edad;
    private int Ejemplar;
    private String editorial;

    public Book() {
    }

    public Book(String ISBN, String Titulo, String Autor, String Genero, String Tematica, int Edad, int Ejemplar, String editorial) {
        this.ISBN = ISBN;
        this.Titulo = Titulo;
        this.Autor = Autor;
        this.Genero = Genero;
        this.Tematica = Tematica;
        this.Edad = Edad;
        this.Ejemplar = Ejemplar;
        this.editorial = editorial;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String Titulo) {
        this.Titulo = Titulo;
    }

    public String getAutor() {
        return Autor;
    }

    public void setAutor(String Autor) {
        this.Autor = Autor;
    }

    public String getGenero() {
        return Genero;
    }

    public void setGenero(String Genero) {
        this.Genero = Genero;
    }

    public String getTematica() {
        return Tematica;
    }

    public void setTematica(String Tematica) {
        this.Tematica = Tematica;
    }

    public int getEdad() {
        return Edad;
    }

    public void setEdad(int Edad) {
        this.Edad = Edad;
    }

    public int getEjemplar() {
        return Ejemplar;
    }

    public void setEjemplar(int Ejemplar) {
        this.Ejemplar = Ejemplar;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }
}
