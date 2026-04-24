/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Nerea
 */
public class Curse {
    private int IDCurso;
    private String nombre;

    @Override
    public String toString() {
        return this.nombre;
    }

    public Curse(int IDCurso, String nombre) {
        this.IDCurso = IDCurso;
        this.nombre = nombre;
    }

    public int getIDCurso() {
        return IDCurso;
    }

    public void setIDCurso(int IDCurso) {
        this.IDCurso = IDCurso;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
