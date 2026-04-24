/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Nerea
 */
public class Student {
    private int IDAlumno;
    private int IDCurso;

    public Student(int IDAlumno, int IDCurso) {
        this.IDAlumno = IDAlumno;
        this.IDCurso = IDCurso;
    }

    public int getIDAlumno() {
        return IDAlumno;
    }

    public void setIDAlumno(int IDAlumno) {
        this.IDAlumno = IDAlumno;
    }

    public int getIDCurso() {
        return IDCurso;
    }

    public void setIDCurso(int IDCurso) {
        this.IDCurso = IDCurso;
    }
}
