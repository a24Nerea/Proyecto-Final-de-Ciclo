/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.recomendation;

import controller.student.StudentController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import javax.swing.JOptionPane;
import model.Student;
import model.Lecture;
import model.Book;
import model.OperationBD;
import model.Recomendation;
import model.User;
import view.recomendation.RecomendationToStudentJDialog;

/**
 *
 * @author Nerea
 */
public class RecomendationToStudentController {

    private RecomendationToStudentJDialog view;
    private OperationBD model;
    private Student user;
    private StudentController controllerStudent;

    public RecomendationToStudentController(RecomendationToStudentJDialog view, OperationBD model, Student user, StudentController controllerStudent) {
        this.view = view;
        this.model = model;
        this.user = user;
        this.controllerStudent = controllerStudent;
        cargarRecomendaciones();
        this.view.addAddRecomendationJButton(this.getAddRecomendation());
        this.view.addInfoJButton(this.getInfoJButton());
        this.view.addCancelJButton(this.getCancelJButton());
    }

    public ActionListener getAddRecomendation() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = view.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(view, "Seleccione una recomendacion", "Seleccionar un elemento", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String idProfesor = view.getInfo(view.getSelectedRow(), 4);
                String idRecomendacion = view.getInfo(view.getSelectedRow(), 3);
                String ISBN = model.getISBNRecomendacion(Integer.parseInt(idRecomendacion));
                String fechaActual = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                String fechaFin = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                boolean isISBNinLecture = model.isBookInLectureFromStudent(ISBN, user.getIDAlumno());
                if (isISBNinLecture) {
                    JOptionPane.showMessageDialog(view, "Ya tienes este libro en tu lista de lecturas", "Libro ya añadido", JOptionPane.ERROR_MESSAGE);
                    return;
                } else {

                    Lecture nuevaLectura = new Lecture(0, "pendiente", fechaActual, fechaFin, 0, 0, "", user.getIDAlumno(), ISBN, Integer.parseInt(idProfesor));
                    int resultado = model.addLecture(nuevaLectura);
                    if (resultado > 0) {
                        JOptionPane.showMessageDialog(view, "Lectura añadida", "Lectura", JOptionPane.INFORMATION_MESSAGE);
                        controllerStudent.cargarLecturas();
                        view.dispose();
                    } else {
                        JOptionPane.showMessageDialog(view, "Error al añadir la lectura", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        };
        return al;
    }

    public void cargarRecomendaciones() {
        System.out.println("Cargar recomendaciones: ");
        view.clearTable();
        ArrayList<Recomendation> listaRecomendation = model.listaDatosRecomendacion(user.getIDCurso(), user.getIDAlumno());
        for (Recomendation recomendation : listaRecomendation) {
            Vector row = new Vector();
            String tituloLibro = model.getTituloLibro(recomendation.getISBN());
            System.out.println("Titulo libro cargado" + tituloLibro);
            row.add(model.getTituloLibro(recomendation.getISBN()));
            row.add(model.getNameUsuario(recomendation.getIDProfesor()));
            row.add(recomendation.getComentario());
            row.add(recomendation.getIDRecomendacion());
            row.add(recomendation.getIDProfesor());
            view.addRow(row);
        }
    }

    public ActionListener getInfoJButton() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = view.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(view, "Seleccione una recomendacion", "Seleccionar un elemento", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String titulo = view.getInfo(view.getSelectedRow(), 0);
                String profesor = view.getInfo(view.getSelectedRow(), 1);
                String comentario = view.getInfo(view.getSelectedRow(), 2);
                String idRecomendacion = view.getInfo(view.getSelectedRow(), 3);

                String info = "Titulo libro: " + titulo + "\n"
                        + "Profesor: " + profesor + "\n"
                        + "Comentario: " + comentario;
                JOptionPane.showMessageDialog(view, info, "Información Recomendacion", JOptionPane.INFORMATION_MESSAGE);
            }
        };
        return al;
    }

    public ActionListener getCancelJButton() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.dispose();
            }
        };
        return al;
    }
}
