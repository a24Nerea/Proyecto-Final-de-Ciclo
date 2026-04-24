/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nerea.salgado
 */
public class OperationBD {

    private Connection conexion;

    public void abrirConexion() {
        try {
            conexion = DriverManager.getConnection("jdbc:sqlite:src/main/resources/biblioteca.sqlite");
            try (Statement stmt = conexion.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON");
            }
            System.out.println("Conectados a la BD");
        } catch (SQLException ex) {
            System.out.println("Error al conectarse con la BD: " + ex.getMessage());
        }
    }

    public void cerrarConexion() {
        try {
            if (conexion != null) {
                conexion.close();
                System.out.println("Conexion cerrada");
            }
        } catch (SQLException ex) {
            System.out.println("Error al cerrar conexion: " + ex.getMessage());
        }

    }

    public int addUsuario(User usuario) {
        String sql = "INSERT INTO Usuario(Nombre, Apellidos, Correo, Contrasenha) values (?,?,?,?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellidos());
            ps.setString(3, usuario.getCorreo());
            ps.setString(4, usuario.getContrasenha());
            return ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error add usuario: " + ex.getMessage());
        }
        return 0;
    }

    public int addBook(Book book) {
        String sql = "INSERT INTO Libro(ISBN, Titulo, Autor, Genero, Tematica, Edad, Ejemplar, Editorial) values (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, book.getISBN());
            ps.setString(2, book.getTitulo());
            ps.setString(3, book.getAutor());
            ps.setString(4, book.getGenero());
            ps.setString(5, book.getTematica());
            ps.setInt(6, book.getEdad());
            ps.setInt(7, book.getEjemplar());
            ps.setString(8, book.getEditorial());
            return ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error addBook: " + ex.getMessage());
        }
        return 0;
    }

    public int addCurso(Curse curso) {
        String sql = "INSERT INTO Curso(Nombre) values (?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, curso.getNombre());
            return ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error addCurso: " + ex.getMessage());
        }
        return 0;
    }

    public int addUsuarioAndGiveID(User usuario) {
        String sql = "INSERT INTO Usuario(Nombre, Apellidos, Correo, Contrasenha) values (?,?,?,?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellidos());
            ps.setString(3, usuario.getCorreo());
            ps.setString(4, usuario.getContrasenha());
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error add usuario: " + ex.getMessage());
        }

        String sql2 = "SELECT last_insert_rowid() as ID";
        try (Statement ps = conexion.createStatement()) {
            ResultSet datos = ps.executeQuery(sql2);
            if (datos.next()) {
                return datos.getInt("ID");
            }
        } catch (SQLException ex) {
            System.out.println("Error addUsuarioAndGiveID: " + ex.getMessage());
        }
        return 0;
    }

    public ArrayList<String> getListaCorreos() {
        ArrayList<String> lista = new ArrayList<>();
        String sql = "SELECT Correo FROM Usuario";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ResultSet datos = ps.executeQuery();
            while (datos.next()) {
                lista.add(datos.getString("Correo"));
            }
        } catch (SQLException ex) {
            System.out.println("Error getListaCorreos : " + ex.getMessage());
        }
        return lista;
    }

    public ArrayList<String> getListGenero() {
        String sql = "SELECT DISTINCT Genero FROM Libro";
        ArrayList<String> lista = new ArrayList<>();
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ResultSet datos = ps.executeQuery();
            while (datos.next()) {
                lista.add(datos.getString("Genero"));
            }
        } catch (SQLException ex) {
            System.out.println("Error getListGenero : " + ex.getMessage());
        }
        return lista;
    }

    public ArrayList<String> getTematica() {
        String sql = "SELECT DISTINCT Tematica FROM Libro";
        ArrayList<String> lista = new ArrayList<>();
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ResultSet datos = ps.executeQuery();
            while (datos.next()) {
                lista.add(datos.getString("Tematica"));
            }
        } catch (SQLException ex) {
            System.out.println("Error getTematica : " + ex.getMessage());
        }
        return lista;
    }

    public ArrayList<String> getEdad() {
        String sql = "SELECT DISTINCT  Edad FROM Libro";
        ArrayList<String> lista = new ArrayList<>();
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ResultSet datos = ps.executeQuery();
            while (datos.next()) {
                lista.add(datos.getString("Edad"));
            }
        } catch (SQLException ex) {
            System.out.println("Error getTematica : " + ex.getMessage());
        }
        return lista;
    }

    public int addStudent(Student student) {
        String sql = "INSERT INTO Alumno(IDUsuario, IDCurso) values (?,?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, student.getIDAlumno());
            ps.setInt(2, student.getIDCurso());
            return ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error add Student: " + ex.getMessage());
        }
        return 0;
    }

    public int addTeacher(Teacher teacher) {
        String sql = "INSERT INTO Profesor(IDUsuario, IDDepartamento) values (?,?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, teacher.getIDUsuario());
            ps.setInt(2, teacher.getIDDepartamento());
            return ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error add Student: " + ex.getMessage());
        }
        return 0;
    }

    public int addProfesor_Curso(int IDUsuario, int IDCurso) {
        String sql = "INSERT INTO Profesor_Curso(IDUsuario, IDCurso) values (?,?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, IDUsuario);
            ps.setInt(2, IDCurso);
            return ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error add Student: " + ex.getMessage());
        }
        return 0;
    }

    public int addAdministrador(Administrador administrador) {
        String sql = "INSERT INTO Administrador(IDUsuario, Tipo, Fecha_ultimo_acceso, Telefono) values (?,?,?,?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, administrador.getIDUSuario());
            ps.setString(2, administrador.getTipo());
            ps.setString(3, administrador.getFecha_ultimo_acceso());
            ps.setString(4, administrador.getTelefono());
            return ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error add administrador: " + ex.getMessage());
        }
        return 0;
    }

    public int addRecomendation(Recomendation recomendation) {
        String sql = "INSERT INTO Recomendacion(ISBN, IDCurso, IDUsuario, Comentario) values (?,?,?,?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, recomendation.getISBN());
            ps.setInt(2, recomendation.getIDCurso());
            ps.setInt(3, recomendation.getIDProfesor());
            ps.setString(4, recomendation.getComentario());
            return ps.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("Error add recomendation: " + ex.getMessage());
        }
        return 0;

    }

    public int addLecture(Lecture lectura) {
        String sql = "INSERT INTO Lectura (Estado, Fecha_Incio, Fecha_Fin, Paginas_Leidas, Valoracion, Comentario, IDUsuario, ISBN, IDProfesor) values(?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, lectura.getEstado());
            ps.setString(2, lectura.getFecha_Inicio());
            ps.setString(3, lectura.getFecha_Fin());
            ps.setInt(4, lectura.getPaginas_Leidas());
            ps.setDouble(5, lectura.getValoracion());
            ps.setString(6, lectura.getComentario());
            ps.setInt(7, lectura.getIDUsuario());
            ps.setString(8, lectura.getISBN());
            ps.setInt(9, lectura.getIDProfesor());
            return ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error add lectura: " + ex.getMessage());
        }
        return 0;
    }

    public ArrayList<Administrador> listAdministrador() {
        String sql = "SELECT * FROM Administrador";
        ArrayList<Administrador> lista = new ArrayList<>();
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ResultSet datos = ps.executeQuery();
            while (datos.next()) {
                lista.add(new Administrador(
                        datos.getInt("IDUsuario"),
                        datos.getString("Tipo"),
                        datos.getString("Fecha_ultimo_acceso"),
                        datos.getString("Telefono")
                ));
            }
        } catch (SQLException ex) {
            System.out.println("Error lista usuario: " + ex.getMessage());
        }
        return lista;
    }

    public ArrayList<User> listUsuario() {
        String sql = "SELECT * FROM Usuario";
        ArrayList<User> lista = new ArrayList<>();
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ResultSet datos = ps.executeQuery();
            while (datos.next()) {
                lista.add(new User(
                        datos.getInt("IDUsuario"),
                        datos.getString("Nombre"),
                        datos.getString("Apellidos"),
                        datos.getString("Correo"),
                        datos.getString("Contrasenha")
                ));
            }
        } catch (SQLException ex) {
            System.out.println("Error lista usuario: " + ex.getMessage());
        }
        return lista;
    }

    public ArrayList<Lecture> listLectura(int idUsuario) {
        String sql = "SELECT * FROM Lectura WHERE IDUsuario = ?";
        ArrayList<Lecture> lista = new ArrayList<>();
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ResultSet datos = ps.executeQuery();
            while (datos.next()) {
                lista.add(new Lecture(
                        datos.getInt("IDLectura"),
                        datos.getString("Estado"),
                        datos.getString("Fecha_Incio"),
                        datos.getString("Fecha_Fin"),
                        datos.getInt("Paginas_Leidas"),
                        datos.getDouble("Valoracion"),
                        datos.getString("Comentario"),
                        datos.getInt("IDUsuario"),
                        datos.getString("ISBN"),
                        datos.getInt("IDProfesor")));
            }
        } catch (SQLException ex) {
            System.out.println("Error lista lecturas: " + ex.getMessage());
        }
        return lista;
    }

    public String getISBNRecomendacion(int IDRecomendacion) {
        String sql = "SELECT ISBN FROM Recomendacion WHERE IDRecomendacion=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, IDRecomendacion);
            ResultSet datos = ps.executeQuery();
            if (datos.next()) {
                return datos.getString("ISBN");
            }
        } catch (SQLException ex) {
            System.out.println("Error get isbn recomendacion: " + ex.getMessage());
        }
        return "null";
    }

    public ArrayList<Recomendation> listaDatosRecomendacion(int idCurso, int IDUsuario) {
        String sql = "SELECT r.* FROM Recomendacion r WHERE r.IDCurso = ? AND NOT EXISTS ( SELECT l.IDLectura  FROM Lectura l WHERE l.ISBN = r.ISBN  AND l.IDUsuario = ?)";
        ArrayList<Recomendation> lista = new ArrayList<>();
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idCurso);
            ps.setInt(2, IDUsuario);
            ResultSet datos = ps.executeQuery();
            while (datos.next()) {
                lista.add(new Recomendation(
                        datos.getInt("IDRecomendacion"),
                        datos.getString("ISBN"),
                        datos.getInt("IDCurso"),
                        datos.getInt("IDUsuario"),
                        datos.getString("Comentario")));
            }
        } catch (SQLException ex) {
            System.out.println("Error en coger datos recomendacion: " + ex.getMessage());
        }
        return lista;
    }

    public int countRecomendationStudent(int idCurso, int idUsuario) {
        String sql = "SELECT COUNT(*) FROM Recomendacion r WHERE r.IDCurso = ? AND NOT EXISTS ( SELECT l.IDLectura  FROM Lectura l WHERE l.ISBN = r.ISBN  AND l.IDUsuario = ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idCurso);
            ps.setInt(2, idUsuario);
            ResultSet datos = ps.executeQuery();
            if (datos.next()) {
                return datos.getInt(1);
            }
        } catch (SQLException ex) {
            System.out.println("Error conteo recomendaciones estudiante: " + ex.getMessage());
        }
        return 0;
    }

    public User cogerDatosUsuarioPorEmail(String email) {
        String sql = "SELECT * FROM Usuario WHERE Correo = ?;";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet datos = ps.executeQuery();
            if (datos.next()) {
                User user = new User(
                        datos.getInt("IDUsuario"),
                        datos.getString("Nombre"),
                        datos.getString("Apellidos"),
                        datos.getString("Correo"),
                        datos.getString("Contrasenha"));
                return user;
            }
        } catch (SQLException ex) {
            System.out.println("Error datos usuario por email: " + ex.getMessage());
        }
        return null;
    }

    public Student cogerDatosAlumno(int idUsuario) {
        String sql = "SELECT * FROM Alumno WHERE IDUsuario = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ResultSet datos = ps.executeQuery();
            if (datos.next()) {
                Student alumno = new Student(
                        datos.getInt("IDUsuario"),
                        datos.getInt("IDCurso"));
                return alumno;
            }

        } catch (SQLException ex) {
            System.out.println("Error datos alumno: " + ex.getMessage());
        }
        return null;
    }

    public String cogerNombresDepartamentoConID(int IDDepartamento) {
        String sql = "SELECT Nombre FROM Departamento WHERE IDDepartamento = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, IDDepartamento);
            ResultSet datos = ps.executeQuery();
            if (datos.next()) {
                return datos.getString("Nombre");
            }

        } catch (SQLException ex) {
            System.out.println("Error datos alumno: " + ex.getMessage());
        }
        return "No tiene nombre departamento";
    }

    public ArrayList<String> cogerNombreDepartamento() {
        String sql = "SELECT Nombre FROM Departamento";
        ArrayList<String> lista = new ArrayList<>();
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ResultSet datos = ps.executeQuery();
            while (datos.next()) {
                lista.add(datos.getString("Nombre"));
            }

        } catch (SQLException ex) {
            System.out.println("Error datos alumno: " + ex.getMessage());
        }
        return lista;
    }

    public ArrayList<String> cogerNombreCurso() {
        String sql = "SELECT Nombre FROM Curso";
        ArrayList<String> lista = new ArrayList<>();
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ResultSet datos = ps.executeQuery();
            while (datos.next()) {
                lista.add(datos.getString("Nombre"));
            }

        } catch (SQLException ex) {
            System.out.println("Error datos alumno: " + ex.getMessage());
        }
        return lista;
    }

    public Teacher cogerDatosProfesor(int idUsuario) {
        String sql = "SELECT * FROM Profesor WHERE IDUsuario = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ResultSet datos = ps.executeQuery();
            if (datos.next()) {
                Teacher profesor = new Teacher(
                        datos.getInt("IDUsuario"),
                        datos.getInt("IDDepartamento"));
                return profesor;
            }

        } catch (SQLException ex) {
            System.out.println("Error datos alumno: " + ex.getMessage());
        }
        return null;
    }

    public Administrador cogerDatosAdmin(int idUsuario) {
        String sql = "SELECT * FROM Administrador WHERE IDUsuario = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ResultSet datos = ps.executeQuery();
            if (datos.next()) {
                Administrador administrador = new Administrador(
                        datos.getInt("IDUsuario"),
                        datos.getString("Tipo"),
                        datos.getString("Fecha_ultimo_acceso"),
                        datos.getString("Telefono"));
                return administrador;
            }

        } catch (SQLException ex) {
            System.out.println("Error datos alumno: " + ex.getMessage());
        }
        return null;
    }

    public boolean isBookInLectureFromStudent(String ISBN, int idUsuario) {
        String sql = "SELECT COUNT(*) FROM Lectura WHERE ISBN = ? AND IDUsuario = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, ISBN);
            ps.setInt(2, idUsuario);
            ResultSet datos = ps.executeQuery();
            if (datos.next()) {
                int columnaCoinciden = datos.getInt(1);
                if (columnaCoinciden > 0) {
                    return true;
                }
            }
        } catch (SQLException ex) {
            System.out.println("ERROR isBookInLecture : " + ex.getMessage());
        }
        return false;
    }

    public Book getDetailsBook(int IDLectura) {
        String sql = "SELECT l.ISBN, l.Titulo, l.Autor, l.Genero, l.Tematica, l.Edad , l.Ejemplar , l.Editorial FROM Libro L JOIN Lectura le ON L.ISBN = le.ISBN WHERE le.IDLectura = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, IDLectura);
            ResultSet datos = ps.executeQuery();
            if (datos.next()) {
                Book libro = new Book(
                        datos.getString("ISBN"),
                        datos.getString("Titulo"),
                        datos.getString("Autor"),
                        datos.getString("Genero"),
                        datos.getString("Tematica"),
                        datos.getInt("Edad"),
                        datos.getInt("Ejemplar"),
                        datos.getString("Editorial"));
                return libro;
            }
        } catch (SQLException ex) {
            System.out.println("Error detalles libro: " + ex.getMessage());
        }
        return null;
    }

    public ArrayList<Curse> listCursoOfIDProfesor(int idProfesor) {
        String sql = "SELECT c.IDCurso, c.Nombre FROM Curso c JOIN Profesor_Curso pc ON c.IDCurso = pc.IDCurso WHERE pc.IDUsuario = ?;";
        ArrayList<Curse> lista = new ArrayList<>();
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idProfesor);
            ResultSet datos = ps.executeQuery();
            while (datos.next()) {
                lista.add(new Curse(
                        datos.getInt("IDCurso"),
                        datos.getString("Nombre")
                ));
            }
        } catch (SQLException ex) {
            System.out.println("Error en listCursoOfIDProfesor: " + ex.getMessage());
        }
        return lista;
    }

    public void selectNameOfCurseInBD() {
        String sql = "SELECT name FROM Curso";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ResultSet datos = ps.executeQuery();
            if (datos.next()) {

            }
        } catch (SQLException ex) {
            System.out.println("error selectNameOfCurseInBD: " + ex.getMessage());
        }
    }

    public boolean esAdministrador(int idUsuario) {
        String sql = "SELECT IDUsuario FROM Administrador WHERE IDUsuario = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ResultSet datos = ps.executeQuery();
            if (datos.next()) {
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("Error es admin: " + ex.getMessage());
        }
        return false;
    }

    public String getEmailUsuario(int idUsuario) {
        String sql = "SELECT Correo FROM Usuario WHERE IDUsuario = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ResultSet datos = ps.executeQuery();
            if (datos.next()) {
                return datos.getString("Correo");
            }
        } catch (SQLException ex) {
            System.getLogger(OperationBD.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return "Email no encontrado";
    }

    public String getApellidoUsuario(int idUsuario) {
        String sql = "SELECT Apellidos FROM Usuario WHERE IDUsuario = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ResultSet datos = ps.executeQuery();
            if (datos.next()) {
                return datos.getString("Apellidos");
            }
        } catch (SQLException ex) {
            System.getLogger(OperationBD.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return "Apellidos no encontrado";
    }

    public String getNameUsuario(int idUsuario) {
        String sql = "SELECT Nombre FROM Usuario WHERE IDUsuario = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ResultSet datos = ps.executeQuery();
            if (datos.next()) {
                return datos.getString("Nombre");
            }
        } catch (SQLException ex) {
            System.getLogger(OperationBD.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return "Nombre no encontrado";
    }

    public ArrayList<Book> listaLibroPorISBN(String ISBN) {
        String sql = "SELECT * FROM Libro WHERE ISBN = ?";
        ArrayList<Book> lista = new ArrayList<>();
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, ISBN);
            ResultSet datos = ps.executeQuery();
            if (datos.next()) {
                lista.add(new Book(
                        datos.getString("ISBN"),
                        datos.getString("Titulo"),
                        datos.getString("Autor"),
                        datos.getString("Genero"),
                        datos.getString("Tematica"),
                        datos.getInt("Edad"),
                        datos.getInt("Ejemplar"),
                        datos.getString("Editorial")));
            }
        } catch (SQLException ex) {
            System.getLogger(model.OperationBD.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return lista;
    }

    public ArrayList<Book> listaTodosLibro() {
        String sql = "SELECT * FROM Libro";
        ArrayList<Book> lista = new ArrayList<>();
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ResultSet datos = ps.executeQuery();
            while (datos.next()) {
                lista.add(new Book(
                        datos.getString("ISBN"),
                        datos.getString("Titulo"),
                        datos.getString("Autor"),
                        datos.getString("Genero"),
                        datos.getString("Tematica"),
                        datos.getInt("Edad"),
                        datos.getInt("Ejemplar"),
                        datos.getString("Editorial")));
            }
        } catch (SQLException ex) {
            System.getLogger(model.OperationBD.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return lista;
    }

    public String getTituloLibro(String ISBN) {
        System.out.println("Get titulo libro");
        String sql = "SELECT Titulo FROM Libro WHERE ISBN = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, ISBN);
            ResultSet datos = ps.executeQuery();
            if (datos.next()) {
                return datos.getString("Titulo");
            }
        } catch (SQLException ex) {
            System.getLogger(model.OperationBD.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return "Libro no encontrado";
    }

    public String getStateIDLecture(int idLectura) {
        String sql = "SELECT Estado FROM Lectura WHERE IDLectura=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idLectura);
            ResultSet datos = ps.executeQuery();
            if (datos.next()) {
                return datos.getString("Estado");
            }
        } catch (SQLException ex) {
            System.getLogger(OperationBD.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return "Estado no encontrado";
    }

    public String getComentIDLecture(int idLectura) {
        String sql = "SELECT Comentario FROM Lectura WHERE IDLectura=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idLectura);
            ResultSet datos = ps.executeQuery();
            if (datos.next()) {
                return datos.getString("Comentario");
            }
        } catch (SQLException ex) {
            System.getLogger(OperationBD.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return "Estado no encontrado";
    }

    public String getPageIDLecture(int idLectura) {
        String sql = "SELECT Paginas_Leidas FROM Lectura WHERE IDLectura=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idLectura);
            ResultSet datos = ps.executeQuery();
            if (datos.next()) {
                return datos.getString("Paginas_Leidas");
            }
        } catch (SQLException ex) {
            System.getLogger(OperationBD.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return "Estado no encontrado";
    }

    public double getValorationIDLecture(int idLectura) {
        String sql = "SELECT Valoracion FROM Lectura WHERE IDLectura=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idLectura);
            ResultSet datos = ps.executeQuery();
            if (datos.next()) {
                return datos.getDouble("Valoracion");
            }
        } catch (SQLException ex) {
            System.getLogger(OperationBD.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return -1;
    }

    public void changeOrAddValorationToLecture(double valoration, int idLectura) {
        String sql = "UPDATE Lectura set Valoracion = ? WHERE IDLectura = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setDouble(1, valoration);
            ps.setInt(2, idLectura);
            ps.executeUpdate();
            System.out.println("Estado actualizado a: " + valoration);
        } catch (SQLException ex) {
            System.out.println("Error cambiar estado de lectura: " + ex.getMessage());
        }
    }

    public void changeOrAddComentToLecture(String comentario, int idLectura) {
        String sql = "UPDATE Lectura set Comentario = ? WHERE IDLectura = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, comentario);
            ps.setInt(2, idLectura);
            ps.executeUpdate();
            System.out.println("Estado actualizado a: " + comentario);
        } catch (SQLException ex) {
            System.out.println("Error cambiar estado de lectura: " + ex.getMessage());
        }
    }

    public void changeOrAddPageOfLecture(int page, int idLectura) {
        String sql = "UPDATE Lectura set Paginas_Leidas = ? WHERE IDLectura = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, page);
            ps.setInt(2, idLectura);
            ps.executeUpdate();
            System.out.println("Estado actualizado a: " + page);
        } catch (SQLException ex) {
            System.out.println("Error cambiar estado de lectura: " + ex.getMessage());
        }
    }

    public void changeStateOfLecture(String newState, int idLectura) {
        String sql = "UPDATE Lectura set Estado = ? WHERE IDLectura = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, newState);
            ps.setInt(2, idLectura);
            ps.executeUpdate();
            System.out.println("Estado actualizado a: " + newState);
        } catch (SQLException ex) {
            System.out.println("Error cambiar estado de lectura: " + ex.getMessage());
        }
    }

    public void updateUsuario(User usuario) {
        String sql = "UPDATE Usuario set Nombre = ?, Apellidos = ?, Correo = ?, Contrasenha = ? WHERE IDUsuario = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellidos());
            ps.setString(3, usuario.getCorreo());
            ps.setString(4, usuario.getContrasenha());
            ps.setInt(5, usuario.getIDUsuario());
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error updateUsuario: " + ex.getMessage());
        }
    }

    public void updateFechaUltimoInicioAdmin(int IDUsuario) {
        String sql = "UPDATE Administrador set Fecha_ultimo_acceso = ? WHERE IDUsuario = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            String fechaActual = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            ps.setString(1, fechaActual);
            ps.setInt(2, IDUsuario);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error updateUsuario: " + ex.getMessage());
        }
    }

    public void updateFechaInicioLectura(int IDLectura, String fechaInicio) {
        String sql = "UPDATE Lectura set Fecha_Incio = ? WHERE IDLectura = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, fechaInicio);
            ps.setInt(2, IDLectura);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error updateUsuario: " + ex.getMessage());
        }
    }
    
     public void updateFechaFinLectura(int IDLectura, String fechaFin) {
        String sql = "UPDATE Lectura set Fecha_Fin = ? WHERE IDLectura = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, fechaFin);
            ps.setInt(2, IDLectura);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error updateUsuario: " + ex.getMessage());
        }
    }

    public void updateCurso(int idCurso, String nombre) {
        String sql = "UPDATE Curso set Nombre = ? WHERE IDCurso = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setInt(2, idCurso);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error updateCurso : " + ex.getMessage());
        }
    }

    public void updateProfesor(int IDDepartamento, int IDUsuario) {
        String sql = "UPDATE Profesor set IDDepartamento = ? WHERE IDUsuario = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, IDDepartamento);
            ps.setInt(2, IDUsuario);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error updateProfesor : " + ex.getMessage());
        }
    }

    public void updateProfesorCurso(int IDUsuario, int IDCurso) {
        String sql = "UPDATE Profesor_Curso set IDCurso = ? WHERE IDUsuario = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, IDCurso);
            ps.setInt(2, IDUsuario);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error updateProfesorCurso : " + ex.getMessage());
        }
    }

    public void updateAlumno(int IDUsuario, int IDCurso) {
        String sql = "UPDATE Alumno set IDCurso = ? WHERE IDUsuario = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, IDCurso);
            ps.setInt(2, IDUsuario);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error updateAlumno : " + ex.getMessage());
        }
    }

    public void updateAdministrador(int IDUsuario, String telefono, String tipo) {
        String sql = "UPDATE Administrador set Telefono = ?, tipo = ? WHERE IDUsuario = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, telefono);
            ps.setString(2, tipo);
            ps.setInt(3, IDUsuario);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error updateAdministrador : " + ex.getMessage());
        }
    }

    public void updateBook(Book book, String ISBN) {
        String sql = "UPDATE Libro set ISBN = ?, Titulo = ?, Autor = ?, Genero = ?, Tematica = ?, Edad = ?, Ejemplar = ?, Editorial = ? WHERE ISBN = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, book.getISBN());
            ps.setString(2, book.getTitulo());
            ps.setString(3, book.getAutor());
            ps.setString(4, book.getGenero());
            ps.setString(5, book.getTematica());
            ps.setInt(6, book.getEdad());
            ps.setInt(7, book.getEjemplar());
            ps.setString(8, book.getEditorial());
            ps.setString(9, ISBN);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error updateAdministrador : " + ex.getMessage());
        }
    }

    public void deleteLecture(int idLectura) {
        String sql = "DELETE FROM Lectura WHERE IDLectura = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idLectura);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error eliminar lectura: " + ex.getMessage());
        }
    }

    public void deleteBook(String isbn) {
        String sql = "DELETE FROM Libro WHERE ISBN = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, isbn);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error eliminar lectura: " + ex.getMessage());
        }
    }

    public void deleteUser(int IDUsuario) {
        String sql = "DELETE FROM Usuario WHERE IDUsuario = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, IDUsuario);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error en delete recomendacion: " + ex.getMessage());
        }
    }

    public void deleteRecomendation(int IDRecomendation) {
        String sql = "DELETE FROM Recomendacion WHERE IDRecomendacion=?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, IDRecomendation);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error en delete recomendacion: " + ex.getMessage());
        }
    }

    public void deleteCurso(int IDCurso) {
        String sql = "DELETE FROM Curso WHERE IDCurso = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, IDCurso);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error en delete recomendacion: " + ex.getMessage());
        }
    }

    public String seleccionarTipoSegunIDUsuario(int idUsuario) {
        String sql = "SELECT Tipo FROM Administrador WHERE IDUsuario = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ResultSet datos = ps.executeQuery();
            if (datos.next()) {
                return datos.getString("Tipo");
            }
        } catch (SQLException ex) {
            System.out.println("Error es seleccionar tipo segun IDUsuario: " + ex.getMessage());
        }
        return null;
    }

    public boolean esAlumno(int idUsuario) {
        String sql = "SELECT IDUsuario FROM Alumno WHERE IDUsuario = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ResultSet datos = ps.executeQuery();
            if (datos.next()) {
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("Error es alumno: " + ex.getMessage());
        }
        return false;
    }

    public boolean esProfesor(int idUsuario) {
        String sql = "SELECT IDUsuario FROM Profesor WHERE IDUsuario = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ResultSet datos = ps.executeQuery();
            if (datos.next()) {
                return true;
            }
        } catch (SQLException ex) {
            System.out.println("Error es profe: " + ex.getMessage());
        }
        return false;
    }

    public ArrayList<String[]> getAlumnosByCurse(int IDCurso) {
        String sql = "SELECT a.IDUsuario, u.Nombre, u.Apellidos FROM Alumno a JOIN Usuario u ON a.IDUsuario = u.IDUsuario JOIN Curso c ON a.IDCurso = c.IDCurso WHERE a.IDCurso = ?";
        ArrayList<String[]> lista = new ArrayList<>();
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, IDCurso);
            ResultSet datos = ps.executeQuery();
            while (datos.next()) {
                String[] alumno = new String[3];
                alumno[0] = String.valueOf(datos.getInt("IDUsuario"));
                alumno[1] = datos.getString("Nombre");
                alumno[2] = datos.getString("Apellidos");
                lista.add(alumno);
            }
        } catch (SQLException ex) {
            System.out.println("Error getAlumnosByCurse: " + ex.getMessage());
        }
        return lista;
    }

    public String getNombreCursoStudent(int IDUsuario) {
        String sql = "SELECT c.Nombre FROM Curso c JOIN Alumno a ON c.IDCurso = a.IDCurso WHERE a.IDUsuario = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, IDUsuario);
            ResultSet datos = ps.executeQuery();
            if (datos.next()) {
                return datos.getString("Nombre");
            }
        } catch (SQLException ex) {
            System.out.println("Error en getNombreCursoStudent: " + ex.getMessage());
        }
        return "Curso no asignado";
    }

    public int countCompletAllLectureISBN(String isbn) {
        String sql = "SELECT COUNT(*) FROM Lectura WHERE ISBN = ? AND Estado = 'completado'";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, isbn);
            ResultSet datos = ps.executeQuery();
            if (datos.next()) {
                return datos.getInt(1);
            }
        } catch (SQLException ex) {
            System.out.println("Error en countStatePendingUser: " + ex.getMessage());
        }
        return 0;
    }

    public String getTipoNameWithIDUser(int IDUsuario) {
        String sql = "SELECT Tipo FROM Administrador WHERE IDUsuario = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, IDUsuario);
            ResultSet datos = ps.executeQuery();
            if (datos.next()) {
                return datos.getString("Tipo");
            }
        } catch (SQLException ex) {
            System.out.println("Error en countStatePendingUser: " + ex.getMessage());
        }
        return "null";
    }

    public int countStateCompleteUser(int IDUsuario) {
        String sql = "SELECT COUNT(*) FROM Lectura WHERE IDUsuario = ? AND Estado = 'completado'";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, IDUsuario);
            ResultSet datos = ps.executeQuery();
            if (datos.next()) {
                return datos.getInt(1);
            }
        } catch (SQLException ex) {
            System.out.println("Error en countStatePendingUser: " + ex.getMessage());
        }
        return 0;
    }

    public int countStatePendingUser(int IDUsuario) {
        String sql = "SELECT COUNT(*) FROM Lectura WHERE IDUsuario = ? AND Estado = 'pendiente'";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, IDUsuario);
            ResultSet datos = ps.executeQuery();
            if (datos.next()) {
                return datos.getInt(1);
            }
        } catch (SQLException ex) {
            System.out.println("Error en countStatePendingUser: " + ex.getMessage());
        }
        return 0;
    }

    public int countStateWithOutReadingUser(int IDUsuario) {
        String sql = "SELECT COUNT(*) FROM Lectura WHERE IDUsuario = ? AND Estado = 'leyendo'";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, IDUsuario);
            ResultSet datos = ps.executeQuery();
            if (datos.next()) {
                return datos.getInt(1);
            }
        } catch (SQLException ex) {
            System.out.println("Error en countStatePendingUser: " + ex.getMessage());
        }
        return 0;
    }

    public int getIDCurseByName(String nombre) {
        String sql = "SELECT IDCurso FROM Curso WHERE Nombre = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ResultSet datos = ps.executeQuery();
            if (datos.next()) {
                return datos.getInt("IDCurso");
            }
        } catch (SQLException ex) {
            System.out.println("Error en getIDCurseByName " + ex.getMessage());
        }
        return -1;
    }

    public int getIDDepartamentoByName(String nombreDepartamento) {
        String sql = "SELECT IDDepartamento FROM Departamento WHERE Nombre = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, nombreDepartamento);
            ResultSet datos = ps.executeQuery();
            if (datos.next()) {
                return datos.getInt("IDDepartamento");
            }
        } catch (SQLException ex) {
            System.out.println("Error en getIDDepartamentoByName: " + ex.getMessage());
        }
        return -1;
    }

    public ArrayList<Curse> listCurse() {
        String sql = "SELECT * FROM Curso";
        ArrayList<Curse> lista = new ArrayList<>();
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ResultSet datos = ps.executeQuery();
            while (datos.next()) {
                lista.add(new Curse(
                        datos.getInt("IDCurso"),
                        datos.getString("Nombre")));
            }
        } catch (SQLException ex) {
            System.getLogger(OperationBD.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return lista;
    }

    public ArrayList<Lecture> getLecturaByISBN(String ISBN) {
        String sql = "SELECT * FROM Lectura WHERE ISBN = ?";
        ArrayList<Lecture> lista = new ArrayList<>();
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, ISBN);
            ResultSet datos = ps.executeQuery();
            while (datos.next()) {
                lista.add(new Lecture(
                        datos.getInt("IDLectura"),
                        datos.getString("Estado"),
                        datos.getString("Fecha_Incio"),
                        datos.getString("Fecha_Fin"),
                        datos.getInt("Paginas_Leidas"),
                        datos.getDouble("Valoracion"),
                        datos.getString("Comentario"),
                        datos.getInt("IDUsuario"),
                        datos.getString("ISBN"),
                        datos.getInt("IDProfesor")));
            }
        } catch (SQLException ex) {
            System.out.println("Error en getLecturaByISBN " + ex.getMessage());
        }
        return lista;
    }

    public int getCurseFromAlumno(int idAlumno) {
        String sql = "SELECT IDCurso FROM Alumno WHERE IDUsuario = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, idAlumno);
            ResultSet datos = ps.executeQuery();
            if (datos.next()) {
                return datos.getInt("IDCurso");
            }
        } catch (SQLException ex) {
            System.out.println("Error en getCurseFromAlumno: " + ex.getMessage());
        }
        return -1;
    }

    public int getListIDUsuario() {
        String sql = "SELECT IDUsuario FROM Usuario";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ResultSet datos = ps.executeQuery();
            while (datos.next()) {
                return datos.getInt("IDUsuario");
            }
        } catch (SQLException ex) {
            System.out.println("Error en getIDUsuarioFromIDLectura: " + ex.getMessage());
        }
        return -1;
    }

    public ArrayList<Object[]> getLibrosCompletadosPorAlumno(int IDAlumno) {
        String sql = "SELECT l.titulo, l.Autor, le.Estado, le.valoracion, le.Fecha_Incio, le.Fecha_Fin FROM Lectura le JOIN Libro l ON le.ISBN = l.ISBN WHERE le.IDUsuario = ?";
        ArrayList<Object[]> lista = new ArrayList<>();
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, IDAlumno);
            ResultSet datos = ps.executeQuery();
            while (datos.next()) {
                Object[] fila = new Object[6];
                fila[0] = datos.getString("Titulo");
                fila[1] = datos.getString("Autor");
                fila[2] = datos.getString("Estado");
                fila[3] = datos.getInt("Valoracion");
                fila[4] = datos.getString("Fecha_Incio");
                fila[5] = datos.getString("Fecha_Fin");
                lista.add(fila);

            }
        } catch (SQLException ex) {
            System.out.println("Error en getLibrosCompletadosPorAlumno: " + ex.getMessage());
        }
        return lista;

    }

    public ArrayList<Object[]> getLibrosConValoracionesWithCurse(int idCurso) {
        ArrayList<Object[]> resultado = new ArrayList<>();
        ArrayList<Book> libros = listaTodosLibro();
        for (Book libro : libros) {
            ArrayList<Lecture> lecturas = getLecturaByISBN(libro.getISBN());
            int sumaValoraciones = 0;
            int contador = 0;
            for (Lecture lectura : lecturas) {
                double valoracion = lectura.getValoracion();
                if (valoracion > 0) {
                    if (idCurso != -1) {
                        int cursoAlumno = getCurseFromAlumno(lectura.getIDUsuario());
                        if (cursoAlumno != idCurso) {
                            continue;
                        }
                    }
                    sumaValoraciones = (int) (sumaValoraciones + valoracion);
                    contador++;
                }
            }
            double media = 0;
            if (contador > 0) {
                media = (double) sumaValoraciones / contador;
                Object[] filaData = new Object[4]; //ISBN, media y autor de los libros 
                filaData[0] = libro.getTitulo();
                filaData[1] = libro.getAutor();
                filaData[2] = media;
                resultado.add(filaData);
            }

        }
        return resultado;
    }

}
