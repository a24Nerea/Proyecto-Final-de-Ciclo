/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author Nerea
 */
public class User {

    private int IDUsuario;
    private String nombre;
    private String apellidos;
    private String correo;
    private String contrasenha;

    public User() {
    }

    public User(int IDUsuario, String nombre, String apellidos, String correo, String contrasenha) {
        this.IDUsuario = IDUsuario;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;
        this.contrasenha = contrasenha;
    }

    @Override
    public String toString() {
        return "Usuario{" + "IDUsuario=" + IDUsuario + ", nombre=" + nombre + ", apellidos=" + apellidos + ", correo=" + correo + ", contrasenha=" + contrasenha + '}';
    }

    public String encriptPasword(String passwordText) {
        return BCrypt.hashpw(passwordText, BCrypt.gensalt());
    }

    public boolean verifyPassword(String passwordText) {
        return BCrypt.checkpw(passwordText, this.contrasenha);
    }

    public int getIDUsuario() {
        return IDUsuario;
    }

    public void setIDUsuario(int IDUsuario) {
        this.IDUsuario = IDUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasenha() {
        return contrasenha;
    }

    public void setContrasenha(String contrasenha) {
        this.contrasenha = contrasenha;
    }
}
