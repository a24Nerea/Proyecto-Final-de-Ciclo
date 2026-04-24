/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.books;

import controller.student.StudentController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JOptionPane;
import model.Student;
import model.Curse;
import model.Lecture;
import model.Book;
import model.OperationBD;
import model.Teacher;
import model.Recomendation;
import model.User;
import view.books.BooksJDialog;

/**
 *
 * @author Nerea
 */
public class BooksController {

    private BooksJDialog view;
    private OperationBD model;
    private Student user;
    private StudentController controllerStudent;
    private Teacher profesor;

    public BooksController(BooksJDialog view, OperationBD model, Student user, StudentController controllerStudent) {
        this.view = view;
        this.model = model;
        this.user = user;
        this.controllerStudent = controllerStudent;
        cargarBooks();
        cargarComboBox();
        this.view.setVisibleAddBookToListJButton(true);
        this.view.setVisibleRecomendJButton(false);
        this.view.addBookToListJbuttonActionListener(this.getAddBoksToList());
        this.view.addCancelJButton(this.getCancelJButton());
        this.view.addGeneroJComboBoxActionListener(this.getGeneroJComboBoxActionListener());
        this.view.addEadFilterJComboBoxActionListener(this.getEadFilterJComboBoxActionListener());
        this.view.addTematicaJComboBoxActionListener(this.getTematicaJComboBoxActionListener());
    }

    public BooksController(BooksJDialog view, OperationBD model, Teacher profesor) {
        this.view = view;
        this.model = model;
        this.profesor = profesor;
        cargarBooks();
        cargarComboBox();
        this.view.setVisibleAddBookToListJButton(false);
        this.view.setVisibleRecomendJButton(true);
        this.view.addRecomendJButton(this.getRecomendationJButtonActionListener());
        this.view.addCancelJButton(this.getCancelJButton());
        this.view.addGeneroJComboBoxActionListener(this.getGeneroJComboBoxActionListener());
        this.view.addEadFilterJComboBoxActionListener(this.getEadFilterJComboBoxActionListener());
        this.view.addTematicaJComboBoxActionListener(this.getTematicaJComboBoxActionListener());
    }

    public ActionListener getEadFilterJComboBoxActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String seleccion = view.getEdadFilterJComboBoxString();
                if (!seleccion.equals("--- Selecciona la edad ---")) {
                    ArrayList<Book> listaLibro = model.listaTodosLibro();
                    view.clearTable();
                    int edadSeleccionada = Integer.parseInt(seleccion);
                    for (Book libro : listaLibro) {
                        if (libro.getEdad() == edadSeleccionada) {
                            Vector row = new Vector();
                            row.add(libro.getTitulo());
                            row.add(libro.getAutor());
                            row.add(libro.getGenero());
                            row.add(libro.getTematica());
                            row.add(libro.getEdad());
                            row.add(libro.getEjemplar());
                            row.add(libro.getEditorial());
                            row.add(libro.getISBN());
                            view.addRow(row);
                        }
                    }
                }else{
                    cargarBooks();
                }
            }
        };
        return al;
    }

    public ActionListener getTematicaJComboBoxActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String seleccion = view.getTematicaJComboBox();
                if (!seleccion.equals("--- Selecciona la temática ---")) {
                    ArrayList<Book> listaLibro = model.listaTodosLibro();
                    view.clearTable();
                    for (Book libro : listaLibro) {
                        if (libro.getTematica().toLowerCase().contains(seleccion.toLowerCase())) {
                            Vector row = new Vector();
                            row.add(libro.getTitulo());
                            row.add(libro.getAutor());
                            row.add(libro.getGenero());
                            row.add(libro.getTematica());
                            row.add(libro.getEdad());
                            row.add(libro.getEjemplar());
                            row.add(libro.getEditorial());
                            row.add(libro.getISBN());
                            view.addRow(row);
                        }
                    }
                }else{
                    cargarBooks();
                }
            }
        };
        return al;
    }

    public ActionListener getGeneroJComboBoxActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String seleccion = view.getFilterGeneroJComboBox();
                if (!seleccion.equals("--- Selecciona un genero ---")) {
                    ArrayList<Book> listaLibro = model.listaTodosLibro();
                    view.clearTable();
                    for (Book libro : listaLibro) {
                        if (libro.getGenero().toLowerCase().contains(seleccion.toLowerCase())) {
                            Vector row = new Vector();
                            row.add(libro.getTitulo());
                            row.add(libro.getAutor());
                            row.add(libro.getGenero());
                            row.add(libro.getTematica());
                            row.add(libro.getEdad());
                            row.add(libro.getEjemplar());
                            row.add(libro.getEditorial());
                            row.add(libro.getISBN());
                            view.addRow(row);
                        }
                    }
                }else{
                    cargarBooks();
                }
            }
        };
        return al;
    }

    private void cargarComboBox() {
        ArrayList<String> listaGenero = model.getListGenero();
        view.clearFilterGeneroJComboBox();
        view.addfilterGeneroJComboBox("--- Selecciona un genero ---");
        for (String generos : listaGenero) {
            view.addfilterGeneroJComboBox(generos);
            System.out.println(generos);
        }

        ArrayList<String> listaEdad = model.getEdad();
        view.clearEdadFilterJComboBoxString();
        view.addEdadFilterJComboBoxString("--- Selecciona la edad ---");
        for (String edades : listaEdad) {
            view.addEdadFilterJComboBoxString(edades);
        }

        ArrayList<String> listaTematica = model.getTematica();
        view.clearTematicaJComboBox();
        view.addTematicaJComboBox("--- Selecciona la temática ---");
        for (String tematicas : listaTematica) {
            view.addTematicaJComboBox(tematicas);
        }

    }

    public ActionListener getRecomendationJButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = view.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(view, "Seleccione una recomendacion", "Seleccionar un elemento", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String ISBN = view.getInfo(view.getSelectedRow(), 7);
                String titulo = view.getInfo(view.getSelectedRow(), 0);
                ArrayList<Curse> cursoTeacher = model.listCursoOfIDProfesor(profesor.getIDUsuario());
                if (cursoTeacher.isEmpty()) {
                    JOptionPane.showMessageDialog(view, "No impartes clase a ningun curso", "Curso vacio", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Curse curso = (Curse) JOptionPane.showInputDialog(view, "Selecciona el curso para recomendar " + titulo, "Recomendar libro", JOptionPane.QUESTION_MESSAGE, null, cursoTeacher.toArray(), cursoTeacher.get(0));
                if (curso != null) {
                    String comentario = "";
                    comentario = JOptionPane.showInputDialog(view, "Escribe un comentario: ", "Comentario", JOptionPane.QUESTION_MESSAGE);
                    if (comentario.isEmpty()) {
                        int answer = JOptionPane.showConfirmDialog(view, "¿No quieres añadir comentario?", "Añadir Comentario", JOptionPane.YES_NO_OPTION);
                        if (answer == JOptionPane.YES_OPTION) {
                            comentario = JOptionPane.showInputDialog(view, "Escribe un comentario: ", "Comentario", JOptionPane.QUESTION_MESSAGE);
                        } else {
                            comentario = "";
                        }
                    }
                    Recomendation recomendation = new Recomendation(0, ISBN, curso.getIDCurso(), profesor.getIDUsuario(), comentario);
                    model.addRecomendation(recomendation);
                    JOptionPane.showMessageDialog(view, "Libro recomendado", "Recomendación", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        };
        return al;
    }

    public ActionListener getAddBoksToList() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = view.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(view, "Seleccione una recomendacion", "Seleccionar un elemento", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int selection = JOptionPane.showConfirmDialog(view, "¿Quieres añadir este libro a la lista de lecturas?", "Añadir libro", JOptionPane.YES_NO_OPTION);
                if (selection == JOptionPane.YES_OPTION) {
                    String fechaActual = LocalDate.now().toString();
                    String fechaFin = LocalDate.now().toString();
                    String ISBN = view.getInfo(view.getSelectedRow(), 7);
                    boolean isISBNinLecture = model.isBookInLectureFromStudent(ISBN, user.getIDAlumno());
                    if (isISBNinLecture) {
                        JOptionPane.showMessageDialog(view, "Ya tienes este libro en tu lista de lecturas", "Libro ya añadido", JOptionPane.ERROR_MESSAGE);
                        return;
                    } else {
                        Lecture nuevaLectura = new Lecture(0, "pendiente", fechaActual, fechaFin, 0, 0, "", user.getIDAlumno(), ISBN, 0);
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

    public void cargarBooks() {
        view.clearTable();
        ArrayList<Book> listaLibro = model.listaTodosLibro();
        for (Book libro : listaLibro) {
            Vector row = new Vector();
            row.add(libro.getTitulo());
            row.add(libro.getAutor());
            row.add(libro.getGenero());
            row.add(libro.getTematica());
            row.add(libro.getEdad());
            row.add(libro.getEjemplar());
            row.add(libro.getEditorial());
            row.add(libro.getISBN());
            view.addRow(row);
        }
    }

}
