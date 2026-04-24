/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.addData;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import model.Administrador;
import model.Curse;
import model.OperationBD;
import model.Student;
import model.Teacher;
import model.User;
import view.addData.AddEditDataUserAdminJDialog;

/**
 *
 * @author Nerea
 */
public class EditDataUserAdminController {

    private AddEditDataUserAdminJDialog view;
    private OperationBD modelOperations;
    private User usuario = new User();
    private String rol;
    private int IDUsuario;

    public EditDataUserAdminController(AddEditDataUserAdminJDialog view, OperationBD modelOperations, int IDUsuario) {
        this.view = view;
        this.modelOperations = modelOperations;
        this.IDUsuario = IDUsuario;
        this.view.setVisibleRolJComboBox(Boolean.FALSE);
        this.view.setVisibleRolJLabel(Boolean.FALSE);
        cargarCursoJComboBox();
        cargarDepartamentoJComboBox();
        cargarDatos();
        this.view.pack();
        this.view.addSaveActionListener(this.getSaveActionListener());
    }

    public ActionListener getSaveActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = view.getNameUserJTextField();
                String apellidos = view.getSecondLastNameUserJTextField();
                String correo = view.getEmailJTextField();
                String nuevaPassword = view.getPasswordJTextField();

                usuario.setNombre(nombre);
                usuario.setApellidos(apellidos);
                usuario.setCorreo(correo);
                String passwordEncript = "";
                if (!nuevaPassword.isEmpty()) {
                    passwordEncript = usuario.encriptPasword(nuevaPassword);
                    usuario.setContrasenha(passwordEncript);
                }

                modelOperations.updateUsuario(usuario);

                String rol = "";
                if (modelOperations.esProfesor(usuario.getIDUsuario())) {
                    rol = "Profesor";
                    String departamento = view.getDepartamentoJComboBox();
                    int idDepartamento = modelOperations.getIDDepartamentoByName(departamento);

                    String curso = view.getCurseJComboBox();
                    int idCurso = modelOperations.getIDCurseByName(curso);

                    modelOperations.updateProfesor(idDepartamento, IDUsuario);
                    modelOperations.updateProfesorCurso(IDUsuario, idCurso);
                    JOptionPane.showMessageDialog(view, "Profesor actualizaco con éxito", "Actualización completada", JOptionPane.INFORMATION_MESSAGE);

                } else if (modelOperations.esAlumno(usuario.getIDUsuario())) {
                    rol = "Alumno";
                    String nombreCurso = view.getCurseJComboBox();
                    int idCurso = modelOperations.getIDCurseByName(nombreCurso);

                    modelOperations.updateAlumno(IDUsuario, idCurso);
                    JOptionPane.showMessageDialog(view, "Alumno actualizaco con éxito", "Actualización completada", JOptionPane.INFORMATION_MESSAGE);
                } else if (modelOperations.esAdministrador(usuario.getIDUsuario())) {
                    rol = modelOperations.seleccionarTipoSegunIDUsuario(usuario.getIDUsuario());
                    String telefono = view.getTelefonoJTextField();
                    String tipo = view.getRolJComboBox();
                    if (tipo.equals("superAdmin") || tipo.equals("Gestor") || tipo.equals("Reparador")) {
                        modelOperations.updateAdministrador(IDUsuario, telefono, tipo);
                    } else {
                        JOptionPane.showMessageDialog(view, "El rol de administrador debe ser: \n - SuperAdmin \n - Reparadpr \n - Gestor", "Rol administrador", JOptionPane.ERROR_MESSAGE);
                    }
                }

                view.dispose();
            }
        };
        return al;
    }

    private void cargarDatos() {
        ArrayList<User> listaUsuario = modelOperations.listUsuario();
        for (User user : listaUsuario) {
            if (user.getIDUsuario() == IDUsuario) {
                usuario = user;
            }
        }
        if (usuario != null) {
            view.setNameUserJTextField(usuario.getNombre());
            view.setSecondLastNameUserJTextField(usuario.getApellidos());
            view.setEmailJTextField(usuario.getCorreo());

            String rol = "";
            if (modelOperations.esAdministrador(usuario.getIDUsuario())) {
                rol = modelOperations.seleccionarTipoSegunIDUsuario(usuario.getIDUsuario());
            } else if (modelOperations.esAlumno(usuario.getIDUsuario())) {
                rol = "Alumno";
            } else if (modelOperations.esProfesor(usuario.getIDUsuario())) {
                rol = "Profesor";
            }
            view.setRolJComboBox(rol);

            if (rol.equals("Profesor")) {
                Teacher teacher = modelOperations.cogerDatosProfesor(usuario.getIDUsuario());
                if (teacher != null) {
                    String departamento = modelOperations.cogerNombresDepartamentoConID(teacher.getIDDepartamento());
                    view.setDepartamentoJComboBox(departamento);

                    view.setVisibleTelefonoJLabel(Boolean.FALSE);
                    view.setVisibleTelefonoJTextField(Boolean.FALSE);
                }
                ArrayList<Curse> listaCurso = modelOperations.listCursoOfIDProfesor(usuario.getIDUsuario());
                if (!listaCurso.isEmpty()) {
                    view.setCurseJComboBox(listaCurso.get(0).getNombre());
                }
            } else if (rol.equals("Alumno")) {
                Student alumno = modelOperations.cogerDatosAlumno(usuario.getIDUsuario());
                if (alumno != null) {
                    String curso = modelOperations.getNombreCursoStudent(usuario.getIDUsuario());
                    view.setCurseJComboBox(curso);

                    view.setVisibleTelefonoJLabel(Boolean.FALSE);
                    view.setVisibleTelefonoJTextField(Boolean.FALSE);
                    view.setVisibleDepartamentoJComboBox(Boolean.FALSE);
                    view.setVisibleDepartamentoJLabel(Boolean.FALSE);
                }
            } else if (rol.equals("superAdmin") || rol.equals("gestor") || rol.equals("reparador")) {
                Administrador admin = modelOperations.cogerDatosAdmin(usuario.getIDUsuario());
                if (admin != null) {
                    view.setTelefonoJTextField(admin.getTelefono());

                    view.setVisibleRolJComboBox(Boolean.TRUE);
                    view.setVisibleRolJLabel(Boolean.TRUE);

                    view.clearRolJComboBox();
                    view.addItemsrolJComboBox("superAdmin");
                    view.addItemsrolJComboBox("gestor");
                    view.addItemsrolJComboBox("reparador");

                    view.setVisibleDepartamentoJComboBox(Boolean.FALSE);
                    view.setVisibleDepartamentoJLabel(Boolean.FALSE);
                    view.setVisibleCurseJComboBox(Boolean.FALSE);
                    view.setVisibleCursoJLabel(Boolean.FALSE);
                }
            }
        }

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

}
