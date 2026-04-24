/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.student;

import controller.books.BooksController;
import controller.recomendation.RecomendationToStudentController;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.SimpleFormatter;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import model.Student;
import model.Lecture;
import model.Book;
import model.OperationBD;
import model.Recomendation;
import view.books.BooksJDialog;
import view.recomendation.RecomendationToStudentJDialog;
import view.student.StudentJDialog;

/**
 *
 * @author Nerea
 */
public class StudentController {

    private StudentJDialog view;
    private Student user;
    private String nombre;
    private OperationBD model;

    private boolean filterReadAndNotJButton = false;
    private boolean filterPendingJButton = false;
    private boolean filterReadingJButton = false;

    public StudentController(StudentJDialog view, Student user, String nombre, OperationBD model) {
        this.view = view;
        this.user = user;
        this.nombre = nombre;
        this.model = model;
        cargarLecturas();
        showNotificacionLaterCheckRecomedacion();
        this.view.changeColorReadAndNotJButton(Color.GRAY);
        this.view.changeColorPendingJButton(Color.GRAY);
        this.view.changeColorReadingJButton(Color.GRAY);
        this.view.setStudentJLabel("Alumno: " + nombre);
        this.view.addDetailsLectureJButtonActionListener(this.getDetailsLectureJButton());
        this.view.addReadAndNotJButtonActionListener(this.getReadAndNotReadJButton());
        this.view.addPendingJButtonActionListener(this.getPendingJButton());
        this.view.addReadingJButtonActionListener(this.getReadingJButton());
        this.view.addChekReadJButtonActionListener(this.getChekReadJButton());
        this.view.addValorationJButton(this.getAddValorationJButton());
        this.view.addComentJButtonActionListener(this.getAddComentJButton());
        this.view.addUpdatePageJButton(this.getAddPagesJButton());
        this.view.addDeleteJButton(this.getDeleteJButton());
        this.view.addDetailsBookJButtonActionListener(this.getDetailsBookJButton());
        this.view.addRecomendationsJButton(this.getRecomendationsJButton());
        this.view.addBooksInAvailableJButton(this.getBooksInAvailableJButton());
        this.view.changeDateJButtonActionListener(this.getChangeDateJButtonActionListener());
    }

    public ActionListener getChangeDateJButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = view.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(view, "Seleccione una lectura", "Seleccionar un elemento", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String idLecturaSelectRow = view.getInfo(view.getSelectedRow(), 8);
                String fechaInicioActual = view.getInfo(view.getSelectedRow(), 1);
                String fechaFinActual = view.getInfo(view.getSelectedRow(), 2);

                String nuevaFechaInicio = JOptionPane.showInputDialog(view, "Introduce una nueva fecha de inicio de lectura (YYYY-MM-DD): \n", fechaInicioActual);
                String nuevaFechaFin = JOptionPane.showInputDialog(view, "Introduce una nueva fecha de fin de lectura (YYYY-MM-DD): \n", fechaFinActual);

                if (nuevaFechaInicio != null && !nuevaFechaInicio.isEmpty()) {
                    if (validarFecha(nuevaFechaInicio)) {
                        model.updateFechaInicioLectura(Integer.parseInt(idLecturaSelectRow), nuevaFechaInicio);
                    } else {
                        JOptionPane.showMessageDialog(view, "Error: formato de fecha incorrecto", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                if (nuevaFechaFin != null && !nuevaFechaFin.isEmpty()) {
                    if (validarFecha(nuevaFechaFin)) {
                        model.updateFechaFinLectura(Integer.parseInt(idLecturaSelectRow), nuevaFechaFin);

                    } else {
                        JOptionPane.showMessageDialog(view, "Error: formato de fecha incorrecto", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                cargarLecturas();
            }
        };
        return al;
    }

    private boolean validarFecha(String fecha) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            simpleDateFormat.setLenient(false);
            simpleDateFormat.parse(fecha);
            return true;
        } catch (ParseException ex) {
            return false;
        }
    }

    public ActionListener getBooksInAvailableJButton() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BooksJDialog bjd = new BooksJDialog(true, view);
                BooksController bkc = new BooksController(bjd, model, user, StudentController.this);
                bjd.setVisible(true);
            }
        };
        return al;
    }

    public void showNotificacionLaterCheckRecomedacion() {
        SwingUtilities.invokeLater(() -> {

            checkRecomendacions();
        });
    }

    public void checkRecomendacions() {
        int conteoRecomendation = model.countRecomendationStudent(user.getIDCurso(), user.getIDAlumno());
        if (conteoRecomendation > 0) {
            JOptionPane.showMessageDialog(view, "Tienes recomendaciones nuevas/pendientes", "Notificación recomendaciones", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public ActionListener getRecomendationsJButton() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RecomendationToStudentJDialog rcd = new RecomendationToStudentJDialog(true, view);
                RecomendationToStudentController rcc = new RecomendationToStudentController(rcd, model, user, StudentController.this);
                rcd.setVisible(true);

            }
        };
        return al;
    }

    public ActionListener getDetailsBookJButton() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = view.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(view, "Seleccione una lectura", "Seleccionar un elemento", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String idLecturaSelectRow = view.getInfo(view.getSelectedRow(), 8);
                Book libro = model.getDetailsBook(Integer.parseInt(idLecturaSelectRow));

                String info = "Titulo: " + libro.getTitulo() + "\n"
                        + "Autor: " + libro.getAutor() + "\n"
                        + "Editorial: " + libro.getEditorial() + "\n"
                        + "Género: " + libro.getGenero() + "\n"
                        + "Temática: " + libro.getTematica() + "\n"
                        + "Edad: " + libro.getEdad() + "\n"
                        + "Ejemplares en biblioteca: " + libro.getEjemplar();
                JOptionPane.showMessageDialog(view, info, "Informacion de la lectura", JOptionPane.INFORMATION_MESSAGE);

            }
        };
        return al;
    }

    public ActionListener getDeleteJButton() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectRow = view.getSelectedRow();
                if (selectRow == -1) {
                    JOptionPane.showMessageDialog(view, "Selecciona una lectura", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int respuesta = JOptionPane.showConfirmDialog(view, "¿Quieres eliminar esta lectura?", "Eliminar Lectura", JOptionPane.YES_NO_OPTION);
                if (respuesta == JOptionPane.YES_OPTION) {
                    String idLecturaSelectRow = view.getInfo(view.getSelectedRow(), 8);
                    model.deleteLecture(Integer.parseInt(idLecturaSelectRow));
                    cargarLecturas();
                }

            }
        };
        return al;
    }

    public ActionListener getAddPagesJButton() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectRow = view.getSelectedRow();
                if (selectRow == -1) {
                    JOptionPane.showMessageDialog(view, "Selecciona una lectura", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String idLecturaSelectRow = view.getInfo(view.getSelectedRow(), 8);
                String pageInicial = model.getPageIDLecture(Integer.valueOf(idLecturaSelectRow));
                String page = (String) JOptionPane.showInputDialog(view, "Actualizar páginas: ", "Páginas", JOptionPane.QUESTION_MESSAGE, null, null, pageInicial);
                if (page != null && !page.isEmpty()) {
                    int newPage = Integer.parseInt(page);
                    System.out.println("Seleccion: " + newPage);
                    model.changeOrAddPageOfLecture(newPage, Integer.valueOf(idLecturaSelectRow));
                    cargarLecturas();

                }

            }
        };
        return al;
    }

    public ActionListener getAddComentJButton() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectRow = view.getSelectedRow();
                if (selectRow == -1) {
                    JOptionPane.showMessageDialog(view, "Selecciona una lectura", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String idLecturaSelectRow = view.getInfo(view.getSelectedRow(), 8);
                String comentarioInicial = model.getComentIDLecture(Integer.valueOf(idLecturaSelectRow));
                String comentario = (String) JOptionPane.showInputDialog(view, "Escribe un comentario: ", "Comentario", JOptionPane.QUESTION_MESSAGE, null, null, comentarioInicial);
                if (comentario != null && !comentario.isEmpty()) {
                    System.out.println("Seleccion: " + comentario);
                    model.changeOrAddComentToLecture(comentario, Integer.valueOf(idLecturaSelectRow));
                    cargarLecturas();
                }
            }
        };
        return al;
    }

    public ActionListener getAddValorationJButton() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectRow = view.getSelectedRow();
                if (selectRow == -1) {
                    JOptionPane.showMessageDialog(view, "Selecciona una lectura", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String idLecturaSelectRow = view.getInfo(view.getSelectedRow(), 8);
                double valorationInicialNumber = model.getValorationIDLecture(Integer.valueOf(idLecturaSelectRow));
                Double[] options = {1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0};
                Double selection = (Double) JOptionPane.showInputDialog(view, "Selecciona la valoración (1-5): ", "Valoración", JOptionPane.QUESTION_MESSAGE, null, options, valorationInicialNumber);
                if (selection != null) {
                    model.changeOrAddValorationToLecture(selection, Integer.parseInt(idLecturaSelectRow));
                    cargarLecturas();
                }
            }
        };
        return al;
    }

    public ActionListener getChekReadJButton() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectRow = view.getSelectedRow();
                if (selectRow == -1) {
                    JOptionPane.showMessageDialog(view, "Selecciona una lectura", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String idLecturaSelectRow = view.getInfo(view.getSelectedRow(), 8);
                String estadoInicial = model.getStateIDLecture(Integer.valueOf(idLecturaSelectRow));
                String[] options = {"completado", "leyendo", "pendiente"};
                String seleccion = (String) JOptionPane.showInputDialog(view, "Selecciona una opcion: ", "Cambiar estado ", JOptionPane.QUESTION_MESSAGE, null, options, estadoInicial);
                if (seleccion != null) {
                    System.out.println("Seleccion: " + seleccion);
                    model.changeStateOfLecture(seleccion, Integer.valueOf(idLecturaSelectRow));
                    cargarLecturas();
                }
            }
        };
        return al;
    }

    public ActionListener getReadAndNotReadJButton() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (filterPendingJButton == true || filterReadingJButton == true) {
                    JOptionPane.showMessageDialog(view, "Desactiva un filtro");
                    return;
                }
                filterReadAndNotJButton = !filterReadAndNotJButton;
                if (filterReadAndNotJButton) {
                    view.changeColorReadAndNotJButton(new Color(180, 225, 180));
                    cargarLecturasFilterReadAndNot("completado");
                    filterPendingJButton = false;
                    filterReadingJButton = false;
                } else {
                    view.changeColorReadAndNotJButton(Color.GRAY);
                    cargarLecturas();
                }
            }
        };
        return al;
    }

    public ActionListener getReadingJButton() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (filterPendingJButton == true || filterReadAndNotJButton == true) {
                    JOptionPane.showMessageDialog(view, "Desactiva un filtro ");
                    return;
                }
                filterReadingJButton = !filterReadingJButton;
                if (filterReadingJButton) {
                    view.changeColorReadingJButton(new Color(180, 225, 180));
                    cargarLecturasFilterReading("leyendo");
                    filterPendingJButton = false;
                    filterReadAndNotJButton = false;
                } else {
                    view.changeColorReadingJButton(Color.GRAY);
                    cargarLecturas();
                }
            }
        };
        return al;
    }

    public ActionListener getPendingJButton() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (filterReadAndNotJButton == true || filterReadingJButton == true) {
                    JOptionPane.showMessageDialog(view, "Desactiva un filtro");
                    return;
                }
                filterPendingJButton = !filterPendingJButton;
                if (filterPendingJButton) {
                    view.changeColorPendingJButton(new Color(180, 225, 180));
                    cargarLecturasFilterPendging("pendiente");
                    filterReadAndNotJButton = false;
                    filterReadingJButton = false;
                } else {
                    view.changeColorPendingJButton(Color.GRAY);
                    cargarLecturas();
                }
            }
        };
        return al;
    }

    public void cargarLecturasFilterReadAndNot(String estado) {
        view.clearTable();
        ArrayList<Lecture> listaLectura = model.listLectura(user.getIDAlumno());
        for (Lecture lectura : listaLectura) {
            if (lectura.getEstado().equalsIgnoreCase(estado)) {
                Vector row = new Vector();
                row.add(lectura.getEstado());
                row.add(lectura.getFecha_Inicio());
                row.add(lectura.getFecha_Fin());
                row.add(lectura.getPaginas_Leidas());
                row.add(lectura.getValoracion());
                row.add(lectura.getComentario());
                String isbn = lectura.getISBN();
                //System.out.println("Buscando libro con ISBN: " + isbn);
                String titulo = model.getTituloLibro(isbn);
                //System.out.println("Resultado: " + titulo);
                row.add(model.getTituloLibro(lectura.getISBN()));
                row.add(model.getNameUsuario(lectura.getIDProfesor()));
                row.add(lectura.getIDLectura());
                view.addRow(row);
            }
        }
    }

    public void cargarLecturasFilterReading(String estado) {
        view.clearTable();
        ArrayList<Lecture> listaLectura = model.listLectura(user.getIDAlumno());
        for (Lecture lectura : listaLectura) {
            if (lectura.getEstado().equalsIgnoreCase(estado)) {
                Vector row = new Vector();
                row.add(lectura.getEstado());
                row.add(lectura.getFecha_Inicio());
                row.add(lectura.getFecha_Fin());
                row.add(lectura.getPaginas_Leidas());
                row.add(lectura.getValoracion());
                row.add(lectura.getComentario());
                String isbn = lectura.getISBN();
                //System.out.println("Buscando libro con ISBN: " + isbn);
                String titulo = model.getTituloLibro(isbn);
                //System.out.println("Resultado: " + titulo);
                row.add(model.getTituloLibro(lectura.getISBN()));
                row.add(model.getNameUsuario(lectura.getIDProfesor()));
                row.add(lectura.getIDLectura());
                view.addRow(row);
            }
        }
    }

    public void cargarLecturasFilterPendging(String estado) {
        view.clearTable();
        ArrayList<Lecture> listaLectura = model.listLectura(user.getIDAlumno());
        for (Lecture lectura : listaLectura) {
            if (lectura.getEstado().equalsIgnoreCase(estado)) {
                Vector row = new Vector();
                row.add(lectura.getEstado());
                row.add(lectura.getFecha_Inicio());
                row.add(lectura.getFecha_Fin());
                row.add(lectura.getPaginas_Leidas());
                row.add(lectura.getValoracion());
                row.add(lectura.getComentario());
                String isbn = lectura.getISBN();
                //System.out.println("Buscando libro con ISBN: " + isbn);
                String titulo = model.getTituloLibro(isbn);
                //System.out.println("Resultado: " + titulo);
                row.add(model.getTituloLibro(lectura.getISBN()));
                row.add(model.getNameUsuario(lectura.getIDProfesor()));
                row.add(lectura.getIDLectura());
                view.addRow(row);
            }
        }
    }

    public void cargarLecturas() {
        view.clearTable();
        ArrayList<Lecture> listaLectura = model.listLectura(user.getIDAlumno());
        for (Lecture lectura : listaLectura) {
            Vector row = new Vector();
            row.add(lectura.getEstado());
            row.add(lectura.getFecha_Inicio());
            row.add(lectura.getFecha_Fin());
            row.add(lectura.getPaginas_Leidas());
            row.add(lectura.getValoracion());
            row.add(lectura.getComentario());

            String isbn = lectura.getISBN();
            //System.out.println("Buscando libro con ISBN: " + isbn);
            String titulo = model.getTituloLibro(isbn);
            //System.out.println("Resultado: " + titulo);
            row.add(model.getTituloLibro(lectura.getISBN()));
            row.add(model.getNameUsuario(lectura.getIDProfesor()));
            row.add(lectura.getIDLectura());
            view.addRow(row);

        }
    }

    public ActionListener getDetailsLectureJButton() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = view.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(view, "Seleccione una lectura", "Seleccionar un elemento", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String estado = view.getInfo(view.getSelectedRow(), 0);
                String fechaInicio = view.getInfo(view.getSelectedRow(), 1);
                String fechaFin = view.getInfo(view.getSelectedRow(), 2);
                String paginasLeidas = view.getInfo(view.getSelectedRow(), 3);
                String valoracion = view.getInfo(view.getSelectedRow(), 4);
                String comentario = view.getInfo(view.getSelectedRow(), 5);
                String tituloLibro = view.getInfo(view.getSelectedRow(), 6);
                String nombreProfesor = view.getInfo(view.getSelectedRow(), 7);
                String idLectura = view.getInfo(view.getSelectedRow(), 8);

                String info = "Estado: " + estado + "\n"
                        + "Fecha Inicio: " + fechaInicio + "\n"
                        + "Fecha Fin: " + fechaFin + "\n"
                        + "Paginas leidas: " + paginasLeidas + "\n"
                        + "Valoracion: " + valoracion + "\n"
                        + "Comentario: " + comentario + "\n"
                        + "Titulo Libro: " + tituloLibro + "\n"
                        + "Nombre profesor: " + nombreProfesor + "\n";
                JOptionPane.showMessageDialog(view, info, "Informacion de la lectura", JOptionPane.INFORMATION_MESSAGE);

            }
        };
        return al;
    }

}
