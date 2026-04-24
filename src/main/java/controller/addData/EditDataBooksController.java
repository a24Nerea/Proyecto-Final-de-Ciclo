/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.addData;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import model.Book;
import model.OperationBD;
import view.addData.AddEditDataBooksJDialog;

/**
 *
 * @author nerea.salgado
 */
public class EditDataBooksController {

    private AddEditDataBooksJDialog view;
    private OperationBD model;
    private String isbn;
    private Book book = new Book();

    public EditDataBooksController(AddEditDataBooksJDialog view, OperationBD model, String isbn) {
        this.view = view;
        this.model = model;
        this.isbn = isbn;
        cargarDatos();
        this.view.setIsbnJTextFieldEditable(Boolean.FALSE);
        this.view.addSaveJButtonActionListener(this.getSaveJButtonActionListener());
        this.view.addCancelJButtonActionListener(this.getCancelJButtonActionListener());
    }

    private void cargarDatos() {
        ArrayList<Book> listaBook = model.listaTodosLibro();
        for (Book books : listaBook) {
            if (books.getISBN().equals(isbn)) {
                book = books;
            }
        }
        if (book != null) {
            view.setISBNTextField(book.getISBN());
            view.setTituloJTextField(book.getTitulo());
            view.setAutorTextField(book.getAutor());
            view.setGeneroJTextField(book.getGenero());
            view.setTematicaJTextField(book.getTematica());
            view.setEdadRecomendadaJTextField(String.valueOf(book.getEdad()));
            view.setEjemplaresJTextField(String.valueOf(book.getEjemplar()));
            view.setEditorialJTextField(book.getEditorial());
        }
    }

    public ActionListener getSaveJButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                
                
                String isbn = view.getISBNJTextField();
                String titulo = view.getTituloJTextField();
                String autor = view.getAutorJTextField();
                String genero = view.getGeneroJTextField();
                String tematica = view.getTematicaJTextField();
                String edadRecomendada = view.getEdadRecomendadaJTextField();
                String ejemplares = view.getEjemplaresJTextField();
                String editorial = view.getEditorialJTextField();

                if (isbn.isEmpty() || titulo.isEmpty() || autor.isEmpty() || genero.isEmpty() || tematica.isEmpty() || edadRecomendada.isEmpty() || ejemplares.isEmpty() || editorial.isEmpty()) {
                    JOptionPane.showMessageDialog(view, "Rellena los campos", "Campos vacíos", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                book.setISBN(isbn);
                book.setTitulo(titulo);
                book.setAutor(autor);
                book.setGenero(genero);
                book.setTematica(tematica);
                int edadRecomendadaInt = Integer.parseInt(edadRecomendada);
                book.setEdad(edadRecomendadaInt);
                int ejemplaresInt = Integer.parseInt(ejemplares);
                book.setEjemplar(ejemplaresInt);
                book.setEditorial(editorial);

                model.updateBook(book, isbn);
                JOptionPane.showMessageDialog(view, "Libro guardado correctamente", "Libro guardado", JOptionPane.INFORMATION_MESSAGE);
                view.dispose();
            }
        };
        return al;
    }

    public ActionListener getCancelJButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                view.dispose();
            }
        };
        return al;
    }

}
