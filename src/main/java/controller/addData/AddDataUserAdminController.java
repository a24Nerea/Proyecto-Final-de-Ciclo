/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.addData;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import model.Administrador;
import model.OperationBD;
import model.Student;
import model.Teacher;
import model.User;
import view.addData.AddEditDataUserAdminJDialog;

/**
 *
 * @author Nerea
 */
public class AddDataUserAdminController {

    private AddEditDataUserAdminJDialog view;
    private OperationBD modelOperations;
    private User usuario = new User();
    private String rolSeleccionado;

    public AddDataUserAdminController(AddEditDataUserAdminJDialog view, OperationBD modelOperations) {
        this.view = view;
        this.modelOperations = modelOperations;
        cargarDepartamentoJComboBox();
        cargarCursoJComboBox();
        view.setVisibleDepartamentoJComboBox(Boolean.FALSE);
        view.setVisibleDepartamentoJLabel(Boolean.FALSE);
        view.setVisibleCurseJComboBox(Boolean.FALSE);
        view.setVisibleCursoJLabel(Boolean.FALSE);
        view.setVisibleTelefonoJTextField(Boolean.FALSE);
        view.setVisibleTelefonoJLabel(Boolean.FALSE);
        rolSeleccionado = view.getRolJComboBox();
        this.view.addSaveActionListener(this.getSaveJButtonActionListener());
        this.view.addCancelActionListener(this.getCancelJButtonActionListener());
        this.view.addRolJComboBoxActionListener(this.getRolJComboBoxActionListener());
        this.view.pack();
    }

    public void enableVisibleJComboBoxs() {
        /*
        Profesor
        SuperAdmin
        Gestor
        Reparador
        Alumno
         */
        String itemRolComboBox = view.getRolJComboBox();
        System.out.println("Rol seleccionado: " + itemRolComboBox);

        rolSeleccionado = itemRolComboBox;
        if (rolSeleccionado.equalsIgnoreCase("Profesor")) {
            view.setVisibleDepartamentoJComboBox(Boolean.TRUE);
            view.setVisibleDepartamentoJLabel(Boolean.TRUE);

            view.setVisibleTelefonoJTextField(Boolean.FALSE);
            view.setVisibleTelefonoJLabel(Boolean.FALSE);

            view.setVisibleCurseJComboBox(Boolean.TRUE);
            view.setVisibleCursoJLabel(Boolean.TRUE);
        } else if (rolSeleccionado.equalsIgnoreCase("SuperAdmin")) {
            view.setVisibleDepartamentoJComboBox(Boolean.FALSE);
            view.setVisibleDepartamentoJLabel(Boolean.FALSE);

            view.setVisibleTelefonoJTextField(Boolean.TRUE);
            view.setVisibleTelefonoJLabel(Boolean.TRUE);

            view.setVisibleCurseJComboBox(Boolean.FALSE);
            view.setVisibleCursoJLabel(Boolean.FALSE);
        } else if (rolSeleccionado.equalsIgnoreCase("Gestor")) {
            view.setVisibleDepartamentoJComboBox(Boolean.FALSE);
            view.setVisibleDepartamentoJLabel(Boolean.FALSE);

            view.setVisibleTelefonoJTextField(Boolean.TRUE);
            view.setVisibleTelefonoJLabel(Boolean.TRUE);

            view.setVisibleCurseJComboBox(Boolean.FALSE);
            view.setVisibleCursoJLabel(Boolean.FALSE);
        } else if (rolSeleccionado.equalsIgnoreCase("Reparador")) {
            view.setVisibleDepartamentoJComboBox(Boolean.FALSE);
            view.setVisibleDepartamentoJLabel(Boolean.FALSE);

            view.setVisibleTelefonoJTextField(Boolean.TRUE);
            view.setVisibleTelefonoJLabel(Boolean.TRUE);

            view.setVisibleCurseJComboBox(Boolean.FALSE);
            view.setVisibleCursoJLabel(Boolean.FALSE);
        } else if (rolSeleccionado.equalsIgnoreCase("Alumno")) {
            view.setVisibleDepartamentoJComboBox(Boolean.FALSE);
            view.setVisibleDepartamentoJLabel(Boolean.FALSE);

            view.setVisibleTelefonoJTextField(Boolean.FALSE);
            view.setVisibleTelefonoJLabel(Boolean.FALSE);

            view.setVisibleCurseJComboBox(Boolean.TRUE);
            view.setVisibleCursoJLabel(Boolean.TRUE);
        }
        view.pack();
        view.repaint();
    }

    public ActionListener getRolJComboBoxActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enableVisibleJComboBoxs();
            }
        };
        return al;
    }

    private void cargarCursoJComboBox() {
        ArrayList<String> lista = modelOperations.cogerNombreCurso();
        for (String cursos : lista) {
            System.out.println("Cursos: " + lista);
            view.addCurseJComboBox(cursos);
        }
    }

    private void cargarDepartamentoJComboBox() {
        ArrayList<String> lista = modelOperations.cogerNombreDepartamento();
        for (String nombres : lista) {
            System.out.println("Nombres: " + nombres);
            view.addDepartamentoJComboBox(nombres);
        }
    }

    public ActionListener getSaveJButtonActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modelOperations.abrirConexion();
                String name = view.getNameUserJTextField();
                String secondLastName = view.getSecondLastNameUserJTextField();
                String email = view.getEmailJTextField();
                String password = view.getPasswordJTextField();

                if (name.isEmpty() || secondLastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(view, "Rellena los campos", "Campos vacíos", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String passwordEncript = usuario.encriptPasword(password);
                User user = new User(0, name, secondLastName, email, passwordEncript);
                System.out.println("Password encripti: " + passwordEncript);

                ArrayList<String> listaCorreos = modelOperations.getListaCorreos();
                for (String correo : listaCorreos) {
                    //System.out.println("LISTA COMPLETA CORREOS: " + listaCorreos);
                    if (email.equals(correo)) {
                        JOptionPane.showMessageDialog(view, "Correo ya introducido en la base de datos", "Correo duplicado", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                int idUser = modelOperations.addUsuarioAndGiveID(user);

                if (idUser > 0) {
                    if (rolSeleccionado.equals("Alumno")) {
                        String cursoNombre = view.getCurseJComboBox();
                        int idCurso = modelOperations.getIDCurseByName(cursoNombre);
                        Student student = new Student(idUser, idCurso);
                        modelOperations.addStudent(student);
                        JOptionPane.showMessageDialog(view, "Estudiante: " + name + " registrado con éxito", "Registro completado", JOptionPane.INFORMATION_MESSAGE);
                    } else if (rolSeleccionado.equals(("Profesor"))) {
                        String nombreDepartamento = view.getDepartamentoJComboBox();
                        int idDepartamento = modelOperations.getIDDepartamentoByName(nombreDepartamento);
                        Teacher teacher = new Teacher(idUser, idDepartamento);
                        System.out.println("=== DEPURACIÓN ===");
                        System.out.println("rolSeleccionado vale: '" + rolSeleccionado + "'");
                        System.out.println("Longitud: " + rolSeleccionado.length());
                        modelOperations.addTeacher(teacher);
                        String cursoSeleccionado = view.getCurseJComboBox();
                        int idCurso = modelOperations.getIDCurseByName(cursoSeleccionado);
                        modelOperations.addProfesor_Curso(idUser, idCurso);
                        JOptionPane.showMessageDialog(view, "Profesor: " + name + " registrado con éxito", "Registro completado", JOptionPane.INFORMATION_MESSAGE);
                    } else if (rolSeleccionado.equalsIgnoreCase("SuperAdmin") || rolSeleccionado.equalsIgnoreCase("Gestor") || rolSeleccionado.equalsIgnoreCase("Reparador")) {
                        //String nombreTipo = view.getRolJComboBox();
                        String telefono = view.getTelefonoJTextField();
                        Administrador admin = new Administrador(idUser, rolSeleccionado, "", telefono);
                        System.out.println("=== DEPURACIÓN ===");
                        System.out.println("rolSeleccionado vale: '" + rolSeleccionado + "'");
                        System.out.println("Longitud: " + rolSeleccionado.length());
                        modelOperations.addAdministrador(admin);
                        JOptionPane.showMessageDialog(view, "Administrador : (" + name + ") registrado con éxito", "Registro completado", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                view.clear();
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
