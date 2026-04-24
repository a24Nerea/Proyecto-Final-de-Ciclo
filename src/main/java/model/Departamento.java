/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Nerea
 */
public class Departamento {
    private int IDDepartamento;
    private String nombre;

    public Departamento(int IDDepartamento, String nombre) {
        this.IDDepartamento = IDDepartamento;
        this.nombre = nombre;
    }

    public int getIDDepartamento() {
        return IDDepartamento;
    }

    public void setIDDepartamento(int IDDepartamento) {
        this.IDDepartamento = IDDepartamento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
