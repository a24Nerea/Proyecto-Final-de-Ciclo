/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Nerea
 */
public class Teacher {
    private int IDUsuario;
    private int IDDepartamento;

    public Teacher(int IDUsuario, int IDDepartamento) {
        this.IDUsuario = IDUsuario;
        this.IDDepartamento = IDDepartamento;
    }

    public int getIDUsuario() {
        return IDUsuario;
    }

    public void setIDUsuario(int IDUsuario) {
        this.IDUsuario = IDUsuario;
    }

    public int getIDDepartamento() {
        return IDDepartamento;
    }

    public void setIDDepartamento(int IDDepartamento) {
        this.IDDepartamento = IDDepartamento;
    }
}
