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
public class Administrador {
    private int IDUSuario;
    private String tipo;
    private String fecha_ultimo_acceso;
    private String telefono;

    public Administrador(int IDUSuario, String tipo, String fecha_ultimo_acceso, String telefono) {
        this.IDUSuario = IDUSuario;
        this.tipo = tipo;
        this.fecha_ultimo_acceso = fecha_ultimo_acceso;
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return "Administrador{" + "IDUSuario=" + IDUSuario + ", tipo=" + tipo + ", fecha_ultimo_acceso=" + fecha_ultimo_acceso + ", telefono=" + telefono + '}';
    }

    public int getIDUSuario() {
        return IDUSuario;
    }

    public void setIDUSuario(int IDUSuario) {
        this.IDUSuario = IDUSuario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFecha_ultimo_acceso() {
        return fecha_ultimo_acceso;
    }

    public void setFecha_ultimo_acceso(String fecha_ultimo_acceso) {
        this.fecha_ultimo_acceso = fecha_ultimo_acceso;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
