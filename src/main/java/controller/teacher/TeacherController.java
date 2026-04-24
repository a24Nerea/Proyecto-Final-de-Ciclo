/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.teacher;

import controller.ReportsTeacher.ReportTeacherController;
import controller.books.BooksController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JOptionPane;
import model.Curse;
import model.OperationBD;
import model.Teacher;
import view.books.BooksJDialog;
import view.reportsTeacher.ReportsTeacherJDialog;
import view.teacher.TeacherJDialog;

/**
 *
 * @author Nerea
 */
public class TeacherController {

    private TeacherJDialog view;
    private OperationBD model;
    private Teacher profesor;
    private int idAlumno;

    public TeacherController(TeacherJDialog view, OperationBD model, Teacher profesor) {
        this.view = view;
        this.model = model;
        this.profesor = profesor;
        cargarCursos();
        cargarAlumnos();
        this.view.booksListJButtonActionListener(this.getBooksListJButtonActionListener());
        this.view.addCurseJComboBoxActionListener(this.getCurseJComboBoxActionListener());
        this.view.addInformeJButtonActionListener(this.getInformeJButtonActionListener());
    }
    
    
    public ActionListener getInformeJButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ReportsTeacherJDialog rptjd = new ReportsTeacherJDialog(true, view);
                ReportTeacherController rptc = new ReportTeacherController(rptjd, model, profesor);
                rptjd.setVisible(true);
            }
        };
        return al;
    }

    

   

    public ActionListener getCurseJComboBoxActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarAlumnos();
            }
        };
        return al;
    }

    public ActionListener getBooksListJButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BooksJDialog bkd = new BooksJDialog(true, view);
                BooksController bk = new BooksController(bkd, model, profesor);
                bkd.setVisible(true);
            }
        };
        return al;
    }

    public void cargarAlumnos() {
        String seleccion = view.getCurseJComboBox();
        view.setNameTeacherJLabel("Profesor: " + model.getNameUsuario(profesor.getIDUsuario()));
        if (seleccion == null) {
            return;
        }
        ArrayList<String[]> alumnos = new ArrayList<>();
        if (seleccion.equals("Todos")) {
            ArrayList<Curse> cursoProfesor = model.listCursoOfIDProfesor(profesor.getIDUsuario());
            for (Curse curso : cursoProfesor) {
                alumnos.addAll(model.getAlumnosByCurse(curso.getIDCurso()));
            }
        } else {
            int idCurso = model.getIDCurseByName(seleccion);
            alumnos = model.getAlumnosByCurse(idCurso);
        }

        view.clearTable();

        for (String[] alumno : alumnos) {
            idAlumno = Integer.parseInt(alumno[0]);
            String nombre = alumno[1];
            String apellidos = alumno[2];
            String curso = model.getNombreCursoStudent(idAlumno);

            Vector row = new Vector();
            row.add(nombre);
            row.add(apellidos);
            row.add(curso);
            row.add(model.countStateCompleteUser(idAlumno));
            row.add(model.countStatePendingUser(idAlumno));
            row.add(model.countStateWithOutReadingUser(idAlumno));
            view.addRow(row);
        }
    }

    public void cargarCursos() {
        ArrayList<Curse> cursoProfesor = model.listCursoOfIDProfesor(profesor.getIDUsuario());
        view.clearCurseJComboBox();
        view.addItemsCurseJComboBox("Todos");
        for (Curse cursos : cursoProfesor) {
            view.addItemsCurseJComboBox(cursos.getNombre());
        }
    }

}
