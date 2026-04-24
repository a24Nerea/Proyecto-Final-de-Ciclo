/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.admin;

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
import controller.ReportsTeacher.ReportTeacherController;
import controller.addData.AddDataBooksController;
import controller.addData.AddDataUserAdminController;
import controller.addData.EditDataBooksController;
import controller.addData.EditDataUserAdminController;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import model.Administrador;
import model.Book;
import model.Curse;
import model.OperationBD;
import model.User;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import view.MainJFrame;
import view.addData.AddEditDataBooksJDialog;
import view.superAdmin.AdminsDialog;
import view.addData.AddEditDataUserAdminJDialog;
import view.login.LoginJDialog;

/**
 *
 * @author Nerea
 */
public class AdminsController {

    private AdminsDialog view;
    private MainJFrame viewMain;
    private OperationBD modelOperations;
    private AddEditDataUserAdminJDialog viewData;
    private Administrador user;
    private String nombre;

    public AdminsController(AdminsDialog view, OperationBD modelOperationBD, Administrador user, String nombre) {
        this.view = view;
        this.modelOperations = modelOperationBD;
        this.user = user;
        this.nombre = nombre;
        cargarUsuariosJTableUsers();
        cargarCursosJTabelCurse();
        cargarLibrosJTabelBooks();
        cargarReportsJTableReports();
        mostrarGraficaReportsEnPanelVista();
        ocultarVentanas();
        //users
        this.view.addNewUserJButtonUserActionListener(this.getDataUserActionListener());
        this.view.addDeleteJButtonUserActionListener(this.getDeleteJButtonUserActionListener());
        this.view.addEditUserJButtonUserActionListener(this.getEditUserJButtonUserActionListener());
        this.view.addBuscarJButtonUserActionListener(this.getBuscarJButtonUserActionListener());
        this.view.addExportarTableJButtonUserActionListener(this.getExportarTableJButtonUserActionListener());
        //Curse
        this.view.addNewCursoJButtonCursoActionListener(this.getNewCursoJButtonCursoActionListener());
        this.view.addEditCursoJButtonCurso(this.getEditCursoJButtonCurso());
        this.view.addDeleteJButtonCursoActionListener(this.getDeleteJButtonCursoActionListener());
        this.view.addBuscarJButtonCursoActionListener(this.getBuscarJButtonCursoActionListener());
        this.view.addExportarTableJButtonCursoActionListener(this.getExportarTableJButtonCursoActionListener());

        //Reports
        this.view.addExportarTableJButtonReportsActionListener(this.getExportarTableJButtonReportsActionListener());

        //Books
        this.view.addNewBookJButtonBooksActionListener(this.getNewBookJButtonBooksActionListener());
        this.view.addEditBookJButtonBookActionListener(this.getEditBookJButtonBookActionListener());
        this.view.addDeleteJButtonBookActionListener(this.getDeteleBookJButtonBookActionListener());
        this.view.addExportarTableJButtonBooksActionListener(this.getExportarTableJButtonBooksActionListener());
        this.view.addBuscarJButtonBookActionListener(this.getBuscarJButtonBookActionListener());
    }

    private void ocultarVentanas() {
        String tipo = user.getTipo();
        if (tipo.equals("gestor")) {
            this.view.ocultarPestaña(0);
            this.view.ocultarPestaña(0);
            this.view.setSuperAdminJLabelName("Gestor: " + nombre);
        } else if (tipo.equals("reparador")) {
            this.view.setVisibleJButtonExceptReportsExport(Boolean.FALSE);
            this.view.setSuperAdminJLabelName("Reparador: " + nombre);
        } else if (tipo.equals("superAdmin")) {
            this.view.setSuperAdminJLabelName("SuperAdmin: " + nombre);
        }
    }

    public ActionListener getBuscarJButtonBookActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Buscar");
                String campoBusqueda = view.getTituloOrAutorBuscarJTextFieldBook();
                if (campoBusqueda.isEmpty()) {
                    cargarLibrosJTabelBooks();
                    return;
                }

                ArrayList<Book> listadoLibros = modelOperations.listaTodosLibro();
                view.clearTableBooks();
                for (Book books : listadoLibros) {
                    System.out.println("Comparando: " + books.getTitulo() + " con " + campoBusqueda);
                    if (books.getISBN().toLowerCase().contains(campoBusqueda.toLowerCase()) || books.getTitulo().toLowerCase().contains(campoBusqueda.toLowerCase())) {
                        Vector row = new Vector();
                        row.add(books.getISBN());
                        row.add(books.getTitulo());
                        row.add(books.getAutor());
                        row.add(books.getGenero());
                        row.add(books.getEditorial());
                        row.add(books.getEjemplar());
                        view.addRowBooks(row);
                    }
                }

            }
        };
        return al;
    }

    public ActionListener getExportarTableJButtonBooksActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Selecciona un directorio donde guardar el PDF: ");
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int resultado = fileChooser.showSaveDialog(view);
                if (resultado != JFileChooser.APPROVE_OPTION) {
                    JOptionPane.showMessageDialog(view, "Selecciona el directorio para guardar el informe", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                File directorio = fileChooser.getSelectedFile();
                String nombreArchivo = "LibroRegistrados.pdf";
                File archivoPDF = new File(directorio, nombreArchivo);

                Document documento = new Document(PageSize.A4);
                try {
                    PdfWriter.getInstance(documento, new FileOutputStream(archivoPDF));
                    documento.open();

                    Font tipoLetraTitulo = new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD);
                    Paragraph tituloPDFInforme = new Paragraph("LIBROS REGISTRADOS", tipoLetraTitulo);
                    tituloPDFInforme.setAlignment(Element.ALIGN_CENTER);
                    tituloPDFInforme.setSpacingAfter(10);
                    documento.add(tituloPDFInforme);

                    Font fecha = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    Paragraph dateParagraph = new Paragraph("Generado el: " + sdf.format(new Date()), fecha);
                    dateParagraph.setAlignment(Element.ALIGN_RIGHT);
                    dateParagraph.setSpacingAfter(20);
                    documento.add(dateParagraph);

                    int numColumnas = view.getTabeBooksJTable().getColumnCount();
                    PdfPTable tabla = new PdfPTable(numColumnas);
                    tabla.setWidthPercentage(100);
                    tabla.setSpacingBefore(10);

                    Font cabecerasLetra = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
                    BaseColor cabecerasColor = new BaseColor(249, 231, 159);

                    for (int i = 0; i < numColumnas; i++) {
                        PdfPCell celda = new PdfPCell(new Phrase(view.getTabeBooksJTable().getColumnName(i), cabecerasLetra));
                        celda.setBackgroundColor(cabecerasColor);
                        celda.setPadding(10);
                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tabla.addCell(celda);
                    }
                    Font datosLetra = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL);

                    for (int filas = 0; filas < view.getTabeBooksJTable().getRowCount(); filas++) {
                        for (int columnas = 0; columnas < numColumnas; columnas++) {
                            Object valor = view.getTabeBooksJTable().getValueAt(filas, columnas);
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

    public ActionListener getDeteleBookJButtonBookActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = view.getSelectedRowBooks();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(view, "Seleccione un libro", "Seleccionar un elemento", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String isbn = view.getInfoBooks(view.getSelectedRowBooks(), 0);
                int confirmacion = JOptionPane.showConfirmDialog(viewMain, "¿Quieres eliminar este libro?", "Eliminar libro", JOptionPane.YES_NO_OPTION);
                if (confirmacion == JOptionPane.YES_OPTION) {
                    modelOperations.deleteBook(isbn);
                    JOptionPane.showMessageDialog(viewMain, "Libro eliminado correctamente", "Curso eliminado", JOptionPane.INFORMATION_MESSAGE);
                    cargarLibrosJTabelBooks();
                }
            }
        };
        return al;
    }

    public ActionListener getEditBookJButtonBookActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddEditDataBooksJDialog adbjd = new AddEditDataBooksJDialog(viewMain, true);
                String isbn = view.getInfoBooks(view.getSelectedRowBooks(), 0);
                EditDataBooksController edb = new EditDataBooksController(adbjd, modelOperations, isbn);
                adbjd.setVisible(true);
                cargarLibrosJTabelBooks();
            }
        };
        return al;
    }

    public ActionListener getNewBookJButtonBooksActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddEditDataBooksJDialog adbjd = new AddEditDataBooksJDialog(viewMain, true);
                AddDataBooksController adbc = new AddDataBooksController(adbjd, modelOperations);
                adbjd.setVisible(true);
                cargarLibrosJTabelBooks();
            }
        };
        return al;
    }

    // ======= REPORTS =======  
    public ActionListener getExportarTableJButtonReportsActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Selecciona un directorio donde guardar el PDF: ");
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int resultado = fileChooser.showSaveDialog(view);
                if (resultado != JFileChooser.APPROVE_OPTION) {
                    JOptionPane.showMessageDialog(view, "Selecciona el directorio para guardar el informe", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                File directorio = fileChooser.getSelectedFile();
                String nombreArchivo = "CursosRegistrados.pdf";
                File archivoPDF = new File(directorio, nombreArchivo);

                Document documento = new Document(PageSize.A4);
                try {
                    PdfWriter.getInstance(documento, new FileOutputStream(archivoPDF));
                    documento.open();

                    Font tipoLetraTitulo = new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD);
                    Paragraph tituloPDFInforme = new Paragraph("CURSOS REGISTRADOS", tipoLetraTitulo);
                    tituloPDFInforme.setAlignment(Element.ALIGN_CENTER);
                    tituloPDFInforme.setSpacingAfter(10);
                    documento.add(tituloPDFInforme);

                    Font fecha = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    Paragraph dateParagraph = new Paragraph("Generado el: " + sdf.format(new Date()), fecha);
                    dateParagraph.setAlignment(Element.ALIGN_RIGHT);
                    dateParagraph.setSpacingAfter(20);
                    documento.add(dateParagraph);

                    int numColumnas = view.getTableInformeJTable().getColumnCount();
                    PdfPTable tabla = new PdfPTable(numColumnas);
                    tabla.setWidthPercentage(100);
                    tabla.setSpacingBefore(10);

                    Font cabecerasLetra = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
                    BaseColor cabecerasColor = new BaseColor(249, 231, 159);

                    for (int i = 0; i < numColumnas; i++) {
                        PdfPCell celda = new PdfPCell(new Phrase(view.getTableInformeJTable().getColumnName(i), cabecerasLetra));
                        celda.setBackgroundColor(cabecerasColor);
                        celda.setPadding(10);
                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tabla.addCell(celda);
                    }
                    Font datosLetra = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL);

                    for (int filas = 0; filas < view.getTableInformeJTable().getRowCount(); filas++) {
                        for (int columnas = 0; columnas < numColumnas; columnas++) {
                            Object valor = view.getTableInformeJTable().getValueAt(filas, columnas);
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

                    //GRÁFICA
                    ArrayList<Book> libros = modelOperations.listaTodosLibro();
                    DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
                    for (Book books : libros) {
                        int lecturas = modelOperations.countCompletAllLectureISBN(books.getISBN());
                        if (lecturas > 0) {
                            dataSet.addValue(lecturas, "Lecturas", books.getTitulo());
                        }
                    }
                    JFreeChart grafica = ChartFactory.createBarChart("Libros más leídos", "Libros", "Nº Lecturas", dataSet);

                    CategoryPlot plot = grafica.getCategoryPlot();
                    BarRenderer render = (BarRenderer) plot.getRenderer();
                    grafica.setBackgroundPaint(Color.WHITE);
                    plot.setBackgroundPaint(new Color(245, 245, 245));

                    BufferedImage imagenGrafica = grafica.createBufferedImage(550, 400);
                    Image pdfImage = null;
                    try {
                        pdfImage = Image.getInstance(imagenGrafica, null);
                    } catch (BadElementException ex) {
                        System.getLogger(ReportTeacherController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                    } catch (IOException ex) {
                        System.getLogger(ReportTeacherController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                    }
                    pdfImage.setAlignment(Element.ALIGN_CENTER);
                    documento.add(pdfImage);

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

    private void mostrarGraficaReportsEnPanelVista() {
        ArrayList<Book> libros = modelOperations.listaTodosLibro();
        DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
        for (Book books : libros) {
            int lecturas = modelOperations.countCompletAllLectureISBN(books.getISBN());
            if (lecturas > 0) {
                dataSet.addValue(lecturas, "Lecturas", books.getTitulo());
            }
        }
        JFreeChart grafica = ChartFactory.createBarChart("Libros más leídos", "Libros", "Nº Lecturas", dataSet);

        CategoryPlot plot = grafica.getCategoryPlot();
        BarRenderer render = (BarRenderer) plot.getRenderer();
        grafica.setBackgroundPaint(Color.WHITE);
        plot.setBackgroundPaint(new Color(245, 245, 245));

        ChartPanel chartPanel = new ChartPanel(grafica);
        chartPanel.setPreferredSize(new Dimension(400, 300));
        view.limpiarGraficJPanelReports();
        view.addGraficaGraficJPanelReports(chartPanel);
    }

    // ======= CURSE =======  
    public ActionListener getExportarTableJButtonCursoActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Selecciona un directorio donde guardar el PDF: ");
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int resultado = fileChooser.showSaveDialog(view);
                if (resultado != JFileChooser.APPROVE_OPTION) {
                    JOptionPane.showMessageDialog(view, "Selecciona el directorio para guardar el informe", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                File directorio = fileChooser.getSelectedFile();
                String nombreArchivo = "CursosRegistrados.pdf";
                File archivoPDF = new File(directorio, nombreArchivo);

                Document documento = new Document(PageSize.A4);
                try {
                    PdfWriter.getInstance(documento, new FileOutputStream(archivoPDF));
                    documento.open();

                    Font tipoLetraTitulo = new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD);
                    Paragraph tituloPDFInforme = new Paragraph("CURSOS REGISTRADOS", tipoLetraTitulo);
                    tituloPDFInforme.setAlignment(Element.ALIGN_CENTER);
                    tituloPDFInforme.setSpacingAfter(10);
                    documento.add(tituloPDFInforme);

                    Font fecha = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    Paragraph dateParagraph = new Paragraph("Generado el: " + sdf.format(new Date()), fecha);
                    dateParagraph.setAlignment(Element.ALIGN_RIGHT);
                    dateParagraph.setSpacingAfter(20);
                    documento.add(dateParagraph);

                    int numColumnas = view.getTableCurseJTable().getColumnCount();
                    PdfPTable tabla = new PdfPTable(numColumnas);
                    tabla.setWidthPercentage(100);
                    tabla.setSpacingBefore(10);

                    Font cabecerasLetra = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
                    BaseColor cabecerasColor = new BaseColor(249, 231, 159);

                    for (int i = 0; i < numColumnas; i++) {
                        PdfPCell celda = new PdfPCell(new Phrase(view.getTableCurseJTable().getColumnName(i), cabecerasLetra));
                        celda.setBackgroundColor(cabecerasColor);
                        celda.setPadding(10);
                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tabla.addCell(celda);
                    }
                    Font datosLetra = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL);

                    for (int filas = 0; filas < view.getTableCurseJTable().getRowCount(); filas++) {
                        for (int columnas = 0; columnas < numColumnas; columnas++) {
                            Object valor = view.getTableCurseJTable().getValueAt(filas, columnas);
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

    public ActionListener getBuscarJButtonCursoActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String campoBusqueda = view.getNombreOrIDBuscarJTextFieldCurso();
                if (campoBusqueda.isEmpty()) {
                    cargarUsuariosJTableUsers();
                    return;
                }

                ArrayList<Curse> listadoCurso = modelOperations.listCurse();
                view.clearTableCurse();
                try {
                    int idCurseBusqueda = Integer.parseInt(campoBusqueda);
                    for (Curse curses : listadoCurso) {
                        if (curses.getIDCurso() == idCurseBusqueda) {
                            Vector row = new Vector();
                            row.add(curses.getIDCurso());
                            row.add(curses.getNombre());
                            view.addRowCurse(row);
                        }
                    }
                } catch (NumberFormatException ex) {
                    for (Curse curses : listadoCurso) {
                        if (curses.getNombre().toLowerCase().contains(campoBusqueda.toLowerCase())) {
                            Vector row = new Vector();
                            row.add(curses.getIDCurso());
                            row.add(curses.getNombre());
                            view.addRowCurse(row);

                        }
                    }
                }

            }
        };
        return al;
    }

    public ActionListener getDeleteJButtonCursoActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = view.getSelectedRowCurse();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(view, "Seleccione un curso", "Seleccionar un elemento", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int idCurso = Integer.parseInt(view.getInfoCurse(view.getSelectedRowCurse(), 0));
                int confirmacion = JOptionPane.showConfirmDialog(viewMain, "¿Quieres eliminar este curso?", "Eliminar curso", JOptionPane.YES_NO_OPTION);
                if (confirmacion == JOptionPane.YES_OPTION) {
                    modelOperations.deleteCurso(idCurso);
                    JOptionPane.showMessageDialog(viewMain, "Curso eliminado correctamente", "Curso eliminado", JOptionPane.INFORMATION_MESSAGE);
                    cargarCursosJTabelCurse();
                }
            }
        };
        return al;
    }

    public ActionListener getEditCursoJButtonCurso() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = view.getSelectedRowCurse();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(view, "Seleccione un curso", "Seleccionar un elemento", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String nombreCurso = JOptionPane.showInputDialog(view, "Escribe el nuevo nombre del curso: ", "Nombre curso", JOptionPane.QUESTION_MESSAGE);
                int idCurso = Integer.parseInt(view.getInfoCurse(view.getSelectedRowCurse(), 0));
                modelOperations.updateCurso(idCurso, nombreCurso);
                cargarCursosJTabelCurse();
            }
        };
        return al;
    }

    public ActionListener getNewCursoJButtonCursoActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombreCurso = JOptionPane.showInputDialog(view, "Escribe el nombre del nuevo curso: ", "Nombre curso", JOptionPane.QUESTION_MESSAGE);
                if (nombreCurso != null && !nombreCurso.isEmpty()) {
                    Curse curso = new Curse(0, nombreCurso);
                    modelOperations.addCurso(curso);
                    cargarCursosJTabelCurse();
                }
            }
        };
        return al;
    }

    // ======= USERS =======    
    public ActionListener getExportarTableJButtonUserActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Selecciona un directorio donde guardar el PDF: ");
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int resultado = fileChooser.showSaveDialog(view);
                if (resultado != JFileChooser.APPROVE_OPTION) {
                    JOptionPane.showMessageDialog(view, "Selecciona el directorio para guardar el informe", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                File directorio = fileChooser.getSelectedFile();
                String nombreArchivo = "UsuariosRegistrados.pdf";
                File archivoPDF = new File(directorio, nombreArchivo);

                Document documento = new Document(PageSize.A4);
                try {
                    PdfWriter.getInstance(documento, new FileOutputStream(archivoPDF));
                    documento.open();

                    Font tipoLetraTitulo = new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD);
                    Paragraph tituloPDFInforme = new Paragraph("USUARIOS REGISTRADOS", tipoLetraTitulo);
                    tituloPDFInforme.setAlignment(Element.ALIGN_CENTER);
                    tituloPDFInforme.setSpacingAfter(10);
                    documento.add(tituloPDFInforme);

                    Font fecha = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    Paragraph dateParagraph = new Paragraph("Generado el: " + sdf.format(new Date()), fecha);
                    dateParagraph.setAlignment(Element.ALIGN_RIGHT);
                    dateParagraph.setSpacingAfter(20);
                    documento.add(dateParagraph);

                    int numColumnas = view.getTableUsersJTable().getColumnCount();
                    PdfPTable tabla = new PdfPTable(numColumnas);
                    tabla.setWidthPercentage(100);
                    tabla.setSpacingBefore(10);

                    Font cabecerasLetra = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
                    BaseColor cabecerasColor = new BaseColor(249, 231, 159);

                    for (int i = 0; i < numColumnas; i++) {
                        PdfPCell celda = new PdfPCell(new Phrase(view.getTableUsersJTable().getColumnName(i), cabecerasLetra));
                        celda.setBackgroundColor(cabecerasColor);
                        celda.setPadding(10);
                        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tabla.addCell(celda);
                    }
                    Font datosLetra = new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.NORMAL);

                    for (int filas = 0; filas < view.getTableUsersJTable().getRowCount(); filas++) {
                        for (int columnas = 0; columnas < numColumnas; columnas++) {
                            Object valor = view.getTableUsersJTable().getValueAt(filas, columnas);
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

    public ActionListener getBuscarJButtonUserActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String campoBusqueda = view.getNombreOrIDBuscarJTextFieldUser();
                if (campoBusqueda.isEmpty()) {
                    cargarUsuariosJTableUsers();
                    return;
                }

                ArrayList<User> listadoUsuarios = modelOperations.listUsuario();
                view.clearTableUsers();
                try {
                    int idUsersBusqueda = Integer.parseInt(campoBusqueda);
                    for (User users : listadoUsuarios) {
                        if (users.getIDUsuario() == idUsersBusqueda) {
                            Vector row = new Vector();
                            row.add(users.getIDUsuario());
                            row.add(users.getNombre());
                            row.add(users.getApellidos());
                            row.add(users.getCorreo());

                            String rol = "";
                            if (modelOperations.esAdministrador(users.getIDUsuario())) {
                                String tipo = modelOperations.seleccionarTipoSegunIDUsuario(users.getIDUsuario());
                                rol = tipo;
                            } else if (modelOperations.esProfesor(users.getIDUsuario())) {
                                rol = "Profesor";
                            } else if (modelOperations.esAlumno(users.getIDUsuario())) {
                                rol = "Alumno";
                            }
                            row.add(rol);
                            view.addRowUsers(row);
                        }
                    }
                } catch (NumberFormatException ex) {
                    for (User users : listadoUsuarios) {
                        if (users.getNombre().toLowerCase().contains(campoBusqueda.toLowerCase())) {
                            Vector row = new Vector();
                            row.add(users.getIDUsuario());
                            row.add(users.getNombre());
                            row.add(users.getApellidos());
                            row.add(users.getCorreo());

                            String rol = "";
                            if (modelOperations.esAdministrador(users.getIDUsuario())) {
                                String tipo = modelOperations.seleccionarTipoSegunIDUsuario(users.getIDUsuario());
                                rol = tipo;
                            } else if (modelOperations.esProfesor(users.getIDUsuario())) {
                                rol = "Profesor";
                            } else if (modelOperations.esAlumno(users.getIDUsuario())) {
                                rol = "Alumno";
                            }
                            row.add(rol);
                            view.addRowUsers(row);
                        }
                    }
                }

            }
        };
        return al;
    }

    public ActionListener getEditUserJButtonUserActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int selectedRow = view.getSelectedRowUsers();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(view, "Seleccione un usuario", "Seleccionar un elemento", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int IDUsuario = Integer.parseInt(view.getInfoUsers(view.getSelectedRowUsers(), 0));
                AddEditDataUserAdminJDialog dtj = new AddEditDataUserAdminJDialog(viewMain, true);
                EditDataUserAdminController eduad = new EditDataUserAdminController(dtj, modelOperations, IDUsuario);
                dtj.setVisible(true);
                cargarUsuariosJTableUsers();
            }
        };
        return al;
    }

    public ActionListener getDataUserActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddEditDataUserAdminJDialog dtj = new AddEditDataUserAdminJDialog(viewMain, true);
                AddDataUserAdminController dtc = new AddDataUserAdminController(dtj, modelOperations);
                dtj.setVisible(true);
                cargarUsuariosJTableUsers();

            }
        };
        return al;
    }

    public ActionListener getDeleteJButtonUserActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = view.getSelectedRowUsers();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(view, "Seleccione un usuario", "Seleccionar un elemento", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int idUsuario = Integer.parseInt(view.getInfoUsers(view.getSelectedRowUsers(), 0));
                String nombre = view.getInfoUsers(view.getSelectedRowUsers(), 1);

                int confirmationDelete = JOptionPane.showConfirmDialog(viewMain, "¿Quiere eliminar a " + nombre + "?", "Eliminar usuario", JOptionPane.YES_NO_OPTION);
                if (confirmationDelete == JOptionPane.YES_OPTION) {
                    modelOperations.deleteUser(idUsuario);
                    cargarUsuariosJTableUsers();
                    JOptionPane.showMessageDialog(viewMain, "Usuario " + nombre + " eliminado correctamente", "Usuario eliminado", JOptionPane.INFORMATION_MESSAGE);
                }

            }
        };
        return al;
    }

    private void cargarReportsJTableReports() {
        view.clearTableCurseInforme();
        ArrayList<Book> listaBook = modelOperations.listaTodosLibro();
        for (Book books : listaBook) {
            Vector row = new Vector();
            row.add(books.getTitulo());
            row.add(books.getAutor());
            row.add(modelOperations.countCompletAllLectureISBN(books.getISBN()));
            view.addRowInforme(row);

        }

    }

    private void cargarLibrosJTabelBooks() {
        view.clearTableBooks();
        ArrayList<Book> listaBook = modelOperations.listaTodosLibro();
        for (Book books : listaBook) {
            Vector row = new Vector();
            row.add(books.getISBN());
            row.add(books.getTitulo());
            row.add(books.getAutor());
            row.add(books.getGenero());
            row.add(books.getEditorial());
            row.add(books.getEjemplar());
            view.addRowBooks(row);
        }

    }

    private void cargarCursosJTabelCurse() {
        view.clearTableCurse();
        ArrayList<Curse> listaCurso = modelOperations.listCurse();
        for (Curse cursos : listaCurso) {
            Vector row = new Vector();
            row.add(cursos.getIDCurso());
            row.add(cursos.getNombre());
            view.addRowCurse(row);
        }
    }

    private void cargarUsuariosJTableUsers() {
        view.clearTableUsers();
        ArrayList<User> listaUsuarios = modelOperations.listUsuario();
        for (User users : listaUsuarios) {
            Vector row = new Vector();
            row.add(users.getIDUsuario());
            row.add(users.getNombre());
            row.add(users.getApellidos());
            row.add(users.getCorreo());
            String rol = "";
            if (modelOperations.esAdministrador(users.getIDUsuario())) {
                String tipo = modelOperations.seleccionarTipoSegunIDUsuario(users.getIDUsuario());
                System.out.println("Usuario admin: " + users.getNombre() + " - Tipo: " + tipo);
                rol = tipo;
            } else if (modelOperations.esAlumno(users.getIDUsuario())) {
                rol = "Alumno";
            } else if (modelOperations.esProfesor(users.getIDUsuario())) {
                rol = "Profesor";
            }
            row.add(rol);
            view.addRowUsers(row);
        }
    }

}
