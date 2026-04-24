    /*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
     */
    package controller.addData;

    import java.awt.event.ActionEvent;
    import java.awt.event.ActionListener;
    import javax.swing.JOptionPane;
    import model.Book;
    import model.OperationBD;
    import view.addData.AddEditDataBooksJDialog;
    import view.addData.AddEditDataUserAdminJDialog;

    /**
     *
     * @author nerea.salgado
     */
    public class AddDataBooksController {

        private AddEditDataBooksJDialog view;
        private OperationBD modelOperations;

        public AddDataBooksController(AddEditDataBooksJDialog view, OperationBD modelOperations) {
            this.view = view;
            this.modelOperations = modelOperations;
            this.view.addSaveJButtonActionListener(this.getSaveJButtonActionListener());
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
                    int edadRecomendadaInt = Integer.parseInt(edadRecomendada);
                    int ejemplaresInt = Integer.parseInt(ejemplares);

                    Book book = new Book(isbn, titulo, autor, genero, tematica, edadRecomendadaInt, ejemplaresInt, editorial);
                    modelOperations.addBook(book);

                    JOptionPane.showMessageDialog(view, "Libro añadido correctamente", "Libro añadido", JOptionPane.INFORMATION_MESSAGE);
                    view.dispose();
                }
            };
            return al;
        }
    }
