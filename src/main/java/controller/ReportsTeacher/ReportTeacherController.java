/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.ReportsTeacher;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JOptionPane;
import java.util.Date;
import javax.swing.JFileChooser;
import model.Curse;
import model.Book;
import model.OperationBD;
import model.Teacher;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import view.reportsTeacher.ReportsTeacherJDialog;

/**
 *
 * @author Nerea
 */
public class ReportTeacherController {

    private ReportsTeacherJDialog view;
    private OperationBD model;
    private Teacher profesor;
    private ArrayList<Integer> listaIdAlumnos;

    public ReportTeacherController(ReportsTeacherJDialog view, OperationBD model, Teacher profesor) {
        this.view = view;
        this.model = model;
        this.profesor = profesor;
        this.listaIdAlumnos = new ArrayList<>();
        cargarCursos();
        cargarAlumnosComboBoxBooks();
        filterValorations();
        //VALORATION
        this.view.addExportJButtonValorationActionListener(this.getExportJButtonValorationActionListener());
        this.view.addCancelJButtonValorationActionListener(this.getCancelJButtonValorationActionListener());
        this.view.addCursoJComboBoxValorationActionListener(this.getCursoJComboBoxValorationActionListener());
        //BOOKS
        this.view.addExportJButtonBooksActionListener(this.getExportJButtonBooksActionListener());
        this.view.addCancelJButtonBooksActionListener(this.getCancelJButtonBooksActionListener());
        this.view.addStudentJComboBoxBooksActionListener(this.getStudentJComboBoxBooksActionListener());
        this.view.addCursoJComboBoxBooksActionListener(this.getCursoJComboBoxBooksActionListener());

    }

    //VALORATION
    public ActionListener getExportJButtonValorationActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cursoSeleccionado = view.getCurseJComboBoxValoration();
                if (cursoSeleccionado == null) {
                    JOptionPane.showMessageDialog(view, "Selecciona un curso para guardar el informe", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Selecciona un directorio donde guardar el PDF: ");
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int resultado = fileChooser.showSaveDialog(view);
                if (resultado != JFileChooser.APPROVE_OPTION) {
                    JOptionPane.showMessageDialog(view, "Selecciona el directorio para guardar el informe", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                File directorio = fileChooser.getSelectedFile();
                String nombreArchivo = "informe_valoración_" + cursoSeleccionado.replace(" ", "_") + ".pdf";
                File archivoPDF = new File(directorio, nombreArchivo);

                Document documento = new Document(PageSize.A4);
                try {
                    PdfWriter.getInstance(documento, new FileOutputStream(archivoPDF));
                    documento.open();

                    Font tipoLetraTitulo = new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD);
                    Paragraph tituloPDFInforme = new Paragraph("INFORME DE LIBROS MÁS VALORADOS", tipoLetraTitulo);
                    tituloPDFInforme.setAlignment(Element.ALIGN_CENTER);
                    tituloPDFInforme.setSpacingAfter(10);
                    documento.add(tituloPDFInforme);

                    Font subTitulo = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
                    Paragraph subTituloPDF = new Paragraph("Curso: " + cursoSeleccionado, subTitulo);
                    subTituloPDF.setAlignment(Element.ALIGN_CENTER);
                    subTituloPDF.setSpacingAfter(20);
                    documento.add(subTituloPDF);

                    Font fecha = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    Paragraph dateParagraph = new Paragraph("Generado el: " + sdf.format(new Date()), fecha);
                    dateParagraph.setAlignment(Element.ALIGN_RIGHT);
                    dateParagraph.setSpacingAfter(20);
                    documento.add(dateParagraph);

                    int numColumnas = view.getTableValorationJTableValoration().getColumnCount();
                    PdfPTable tabla = new PdfPTable(numColumnas);
                    tabla.setWidthPercentage(100);
                    tabla.setSpacingBefore(10);

                    Font cabecerasLetra = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
                    BaseColor cabecerasColor = new BaseColor(212, 222, 191);

                    for (int i = 0; i < numColumnas; i++) {
                        PdfPCell celda = new PdfPCell(new Phrase(view.getTableValorationJTableValoration().getColumnName(i), cabecerasLetra));
                        celda.setBackgroundColor(cabecerasColor);
                        celda.setPadding(10);
                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tabla.addCell(celda);
                    }

                    Font datosLetra = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL);

                    for (int filas = 0; filas < view.getTableValorationJTableValoration().getRowCount(); filas++) {
                        for (int columnas = 0; columnas < numColumnas; columnas++) {
                            Object valor = view.getTableValorationJTableValoration().getValueAt(filas, columnas);
                            String texto = "";
                            if (valor != null) {
                                texto = valor.toString();
                            } else {
                                texto = "";
                            }
                            PdfPCell celda = new PdfPCell(new Phrase(texto, datosLetra));
                            celda.setPadding(6);
                            tabla.addCell(celda);
                        }
                    }
                    documento.add(tabla);

                    documento.add(new Paragraph("\n"));
                    Font pieFont = new Font(Font.FontFamily.HELVETICA, 9, Font.ITALIC);
                    Paragraph pie = new Paragraph("Informe generado automáticamente por el Sistema de Gestión de Biblioteca", pieFont);
                    pie.setAlignment(Element.ALIGN_CENTER);
                    documento.add(pie);
                    documento.close();

                    JOptionPane.showMessageDialog(view, "PDF guardado en: " + archivoPDF.getAbsolutePath(), "PDF generado", JOptionPane.INFORMATION_MESSAGE);

                    if (Desktop.isDesktopSupported()) {
                        try {
                            Desktop.getDesktop().open(archivoPDF);
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(view, "Error al abrir el PDF", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        return;
                    }

                } catch (FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(view, "No se pudo crear el archivo:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } catch (DocumentException ex) {
                    JOptionPane.showMessageDialog(view, "Error al generar el PDF", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        return al;
    }

    public ActionListener getCancelJButtonValorationActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.dispose();
            }
        };
        return al;
    }

    public ActionListener getCursoJComboBoxValorationActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterValorations();
            }
        };
        return al;
    }

    public void filterValorations() {
        String cursoSeleccionado = view.getCurseJComboBoxValoration();
        int idCurso = model.getIDCurseByName(cursoSeleccionado);

        ArrayList<Object[]> datos = model.getLibrosConValoracionesWithCurse(idCurso);
        view.clearTableValorationJTable();
        System.out.println("Curso seleccionado: " + cursoSeleccionado);
        System.out.println("ID Curso: " + idCurso);
        System.out.println("Datos recibidos: " + datos.size());

        for (Object[] fila : datos) {
            Vector row = new Vector();
            row.add(fila[0]);
            row.add(fila[1]);
            double media = (double) fila[2];
            row.add(String.format("%.2f", media));
            view.addRowtableValorationJTable(row);
            System.out.println("Título: " + fila[0] + " | Autor: " + fila[1] + " | Media: " + fila[2]);
        }
        System.out.println("Filas añadidas a la tabla: " + view.getTableValorationRowCount());
        if (datos.isEmpty()) {
            JOptionPane.showMessageDialog(view, "No hay valoraciones para este curso", "Sin datos", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    //BOOKS 
    public ActionListener getExportJButtonBooksActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int alumnoSeleccionado = view.getStudentJComboBoxBookIndex();
                String cursoSeleccionado = view.getCurseJComboBoxBooks();
                if (alumnoSeleccionado <= 0 || alumnoSeleccionado >= listaIdAlumnos.size()) {
                    JOptionPane.showMessageDialog(view, "Selecciona un alumno para guardar el informe", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (cursoSeleccionado == null) {
                    JOptionPane.showMessageDialog(view, "Selecciona un curso para guardar el informe", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int alumno = listaIdAlumnos.get(alumnoSeleccionado);
                String nombreAlumno = view.getStudentsJComboBoxBooks();

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Selecciona un directorio: ");
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int resultado = fileChooser.showSaveDialog(view);
                if (resultado != JFileChooser.APPROVE_OPTION) {
                    JOptionPane.showMessageDialog(view, "Selecciona el directorio para guardar el informe", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                File directorio = fileChooser.getSelectedFile();
                String nombreArchivo = "informe_Alumno_" + nombreAlumno.replace(" ", "_") + ".pdf";
                File archivoPDF = new File(directorio, nombreArchivo);

                Document documento = new Document(PageSize.A4);
                try {
                    PdfWriter.getInstance(documento, new FileOutputStream(archivoPDF));
                    documento.open();

                    Font tipoLetraTitulo = new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD);
                    Paragraph tituloPDFInforme = new Paragraph("INFORME DE LECTURA", tipoLetraTitulo);
                    tituloPDFInforme.setAlignment(Element.ALIGN_CENTER);
                    tituloPDFInforme.setSpacingAfter(10);
                    documento.add(tituloPDFInforme);

                    Font subTitulo = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
                    Paragraph subTituloPDF = new Paragraph("Alumn@: " + nombreAlumno + "\n" + "Curso: " + cursoSeleccionado, subTitulo);
                    subTituloPDF.setAlignment(Element.ALIGN_CENTER);
                    subTituloPDF.setSpacingAfter(20);
                    documento.add(subTituloPDF);

                    Font fecha = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    Paragraph dateParagraph = new Paragraph("Generado el: " + sdf.format(new Date()), fecha);
                    dateParagraph.setAlignment(Element.ALIGN_RIGHT);
                    dateParagraph.setSpacingAfter(20);
                    documento.add(dateParagraph);

                    int completados = model.countStateCompleteUser(alumno);
                    int leyendo = model.countStateWithOutReadingUser(alumno);
                    int pendientes = model.countStatePendingUser(alumno);
                    int total = completados + leyendo + pendientes;

                    Font lecturaFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
                    Paragraph lectura = new Paragraph("Resumen de las lecturas: ", lecturaFont);
                    lectura.setSpacingAfter(20);
                    documento.add(lectura);

                    Font datosFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
                    documento.add(new Paragraph("- Libros leídos: " + completados, datosFont));
                    documento.add(new Paragraph("- Libros leyendo: " + leyendo, datosFont));
                    documento.add(new Paragraph("- Libros pendientes: " + pendientes, datosFont));
                    documento.add(new Paragraph("- TOTAL: " + total, datosFont));
                    documento.add(new Paragraph(" "));

                    Paragraph tituloTabla = new Paragraph("Lecturas ", lecturaFont);
                    tituloTabla.setSpacingAfter(10);
                    documento.add(tituloTabla);

                    int columnasNum = view.getTableBooksTableBooks().getColumnCount();
                    PdfPTable tabla = new PdfPTable(columnasNum);
                    tabla.setWidthPercentage(100);
                    tabla.setSpacingAfter(10);

                    Font cabecerasLetra = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
                    BaseColor cabecerasColor = new BaseColor(222, 219, 191);

                    for (int i = 0; i < columnasNum; i++) {
                        PdfPCell celda = new PdfPCell(new Phrase(view.getTableBooksTableBooks().getColumnName(i), cabecerasLetra));
                        celda.setBackgroundColor(cabecerasColor);
                        celda.setPadding(10);
                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tabla.addCell(celda);
                    }
                    Font datosLetra = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL);

                    for (int filas = 0; filas < view.getTableBooksTableBooks().getRowCount(); filas++) {
                        for (int columnas = 0; columnas < columnasNum; columnas++) {
                            Object valor = view.getTableBooksTableBooks().getValueAt(filas, columnas);
                            String texto = "";
                            if (valor != null) {
                                texto = valor.toString();
                            } else {
                                texto = "";
                            }
                            PdfPCell celda = new PdfPCell(new Phrase(texto, datosLetra));
                            celda.setPadding(6);
                            tabla.addCell(celda);
                        }
                    }
                    documento.add(tabla);
                    documento.add(new Paragraph("\n"));

                    //Gráfica
                    DefaultPieDataset dataSet = new DefaultPieDataset();
                    dataSet.setValue("Completados (" + completados + ")", completados);
                    dataSet.setValue("Leyendo (" + leyendo + ")", leyendo);
                    dataSet.setValue("Pendientes (" + pendientes + ")", pendientes);

                    JFreeChart grafica = ChartFactory.createPieChart("Gráfica de progreso de lecturas", dataSet, true, true, false);

                    PiePlot piePlot = (PiePlot) grafica.getPlot();
                    piePlot.setSectionPaint("Completados (" + completados + ")", new Color(76, 175, 80));
                    piePlot.setSectionPaint("Leyendo (" + leyendo + ")", new Color(33, 150, 243));
                    piePlot.setSectionPaint("Pendientes (" + pendientes + ")", new Color(255, 152, 0));

                    grafica.setBackgroundPaint(Color.WHITE);
                    piePlot.setBackgroundPaint(new Color(245, 245, 245));

                    BufferedImage imagenGráfica = grafica.createBufferedImage(500, 300);
                    Image pdfImage = null;
                    try {
                        pdfImage = Image.getInstance(imagenGráfica, null);
                    } catch (BadElementException ex) {
                        System.getLogger(ReportTeacherController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                    } catch (IOException ex) {
                        System.getLogger(ReportTeacherController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                    }
                    pdfImage.setAlignment(Element.ALIGN_CENTER);
                    documento.add(pdfImage);

                    documento.add(new Paragraph("\n"));
                    Font pieFont = new Font(Font.FontFamily.HELVETICA, 9, Font.ITALIC);
                    Paragraph pie = new Paragraph("Informe generado automáticamente por el Sistema de Gestión de Biblioteca", pieFont);
                    pie.setAlignment(Element.ALIGN_CENTER);
                    documento.add(pie);

                    documento.close();

                    JOptionPane.showMessageDialog(view, "PDF guardado en: " + archivoPDF.getAbsolutePath(), "PDF generado", JOptionPane.INFORMATION_MESSAGE);

                    if (Desktop.isDesktopSupported()) {
                        try {
                            Desktop.getDesktop().open(archivoPDF);
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(view, "Error al abrir el PDF", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        return;
                    }

                } catch (DocumentException ex) {
                    System.getLogger(ReportTeacherController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                } catch (FileNotFoundException ex) {
                    System.getLogger(ReportTeacherController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                }

            }
        };
        return al;
    }

    public ActionListener getCancelJButtonBooksActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.dispose();
            }
        };
        return al;
    }

    public ActionListener getStudentJComboBoxBooksActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarLibrosAlumno();
            }
        };
        return al;
    }

    public ActionListener getCursoJComboBoxBooksActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarAlumnosComboBoxBooks();
            }
        };
        return al;
    }

    public void cargarAlumnosComboBoxBooks() {
        String cursoSeleccionado = view.getCurseJComboBoxBooks();
        System.out.println("=== cargarAlumnosComboBoxBooks ===");
        System.out.println("Curso seleccionado: " + cursoSeleccionado);
        int idCurso = model.getIDCurseByName(cursoSeleccionado);
        System.out.println("ID Curso: " + idCurso);
        view.clearStudentsJComboBoxBooks();
        listaIdAlumnos.clear();
        view.addItemsStudentsJComboBoxBooks("Selecciona un estudiante");
        listaIdAlumnos.add(-1); //ponemos como si fuese un valor por defecto
        if (idCurso == -1) {
            System.out.println("No hay curso seleccionado");
            return;
        }
        ArrayList<String[]> alumnos = model.getAlumnosByCurse(idCurso);
        for (String[] alumno : alumnos) {
            listaIdAlumnos.add(Integer.parseInt(alumno[0]));
            String nombreCompleto = alumno[1] + " " + alumno[2];
            System.out.println("  Añadiendo alumno: " + nombreCompleto + " (ID: " + alumno[0] + ")");

            view.addItemsStudentsJComboBoxBooks(nombreCompleto);
        }

        System.out.println("Curso seleccionado: " + cursoSeleccionado);
        System.out.println("ID Curso: " + idCurso);
        System.out.println("Alumnos encontrados: " + alumnos.size());
    }

    public void cargarLibrosAlumno() {
        int indexComboBoxStudents = view.getStudentJComboBoxBookIndex();
        System.out.println("=== cargarLibrosAlumno ===");
        System.out.println("Índice seleccionado: " + indexComboBoxStudents);
        if (indexComboBoxStudents == -1 || indexComboBoxStudents >= listaIdAlumnos.size()) {
            System.out.println("Índice inválido o lista vacía");
            return;
        }
        int idAlumno = listaIdAlumnos.get(indexComboBoxStudents);
        ArrayList<Object[]> libros = model.getLibrosCompletadosPorAlumno(idAlumno);
        System.out.println("Libros encontrados: " + libros.size());
        view.clearTableBooksTable1();

        for (Object[] libro : libros) {
            Vector row = new Vector();
            row.add(libro[0]);
            row.add(libro[1]);
            row.add(libro[2]);
            row.add(libro[3]);
            row.add(libro[4]);
            row.add(libro[5]);
            view.addRowTableBooksTable1(row);
        }
        int completados = model.countStateCompleteUser(idAlumno);
        int leyendo = model.countStateWithOutReadingUser(idAlumno);
        int pendientes = model.countStatePendingUser(idAlumno);
        mostrarGraficaEnBooksReadGraficForStudentPanel(completados, leyendo, pendientes);

    }

    //BOUTH
    public void cargarCursos() {
        ArrayList<Curse> cursoProfesor = model.listCursoOfIDProfesor(profesor.getIDUsuario());
        view.clearCurseJComboBoxBooks();
        view.clearCurseJComboBoxValoration();
        for (Curse cursos : cursoProfesor) {
            view.addItemsCurseJComboBoxBooks(cursos.getNombre());
            view.addItemsCurseJComboBoxValoration(cursos.getNombre());
        }
    }

    private void mostrarGraficaEnBooksReadGraficForStudentPanel(int completados, int leyendo, int pendientes) {
        DefaultPieDataset dataSet = new DefaultPieDataset();
        dataSet.setValue("Completados (" + completados + ")", completados);
        dataSet.setValue("Leyendo (" + leyendo + ")", leyendo);
        dataSet.setValue("Pendientes (" + pendientes + ")", pendientes);

        JFreeChart grafica = ChartFactory.createPieChart("Gráfica de progreso de lecturas", dataSet, true, true, false);

        PiePlot piePlot = (PiePlot) grafica.getPlot();
        piePlot.setSectionPaint("Completados (" + completados + ")", new Color(76, 175, 80));
        piePlot.setSectionPaint("Leyendo (" + leyendo + ")", new Color(33, 150, 243));
        piePlot.setSectionPaint("Pendientes (" + pendientes + ")", new Color(255, 152, 0));

        grafica.setBackgroundPaint(Color.WHITE);
        piePlot.setBackgroundPaint(new Color(245, 245, 245));

        ChartPanel chartPanel = new ChartPanel(grafica);
        chartPanel.setPreferredSize(new Dimension(400, 300));
        view.limpiarBooksReadGraficForStudentPanel();
        view.addGraficaEnBooksReadGraficForStudentPanel(chartPanel);
    }

}
