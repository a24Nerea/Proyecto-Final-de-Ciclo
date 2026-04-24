/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.login;

import controller.admin.AdminsController;
import controller.addData.AddDataUserAdminController;
import controller.student.StudentController;
import controller.teacher.TeacherController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import model.Administrador;
import model.Student;
import model.OperationBD;
import model.Teacher;
import model.User;
import org.mindrot.jbcrypt.BCrypt;
import view.MainJFrame;
import view.superAdmin.AdminsDialog;
import view.addData.AddEditDataUserAdminJDialog;
import view.login.LoginJDialog;
import view.student.StudentJDialog;
import view.teacher.TeacherJDialog;

/**
 *
 * @author nerea.salgado
 */
public class LoginController {

    private LoginJDialog view;
    private MainJFrame viewMain;
    private OperationBD modelOperations;
    private String rol;
    private boolean sesionActivada;
    private User userActual;

    public LoginController(LoginJDialog view, MainJFrame viewMain, String rol) {
        this.view = view;
        this.viewMain = viewMain;
        this.rol = rol;
        this.modelOperations = new OperationBD();
        cargarDatosLogInEjemplo();
        this.view.setVisibleUserLoginJPanel(false);
        this.view.addForgetPasswordActionListener(this.getForgetPasswordActionListener());
        this.view.addCancelButtonActionListener(this.getCancelJButton());
        this.view.addLoginJButtonActionListener(this.getLoginJButton());
        this.view.addLogOutJButtonActionListener(this.getLogOutJButton());
        this.view.addWindwosListener(this.getWindowsListener());
        this.view.addBackToLectureJButton(this.getBackToLectureJButton());
        this.view.pack();
    }

    public void cargarDatosLogInEjemplo() {
        if (rol.equals("Alumno")) {
            view.setUserEmailLoginJTextField("pablo.alumno@email.com");
            view.setPasswordJField("1234");
        } else if (rol.equals("Profesor")) {
            view.setUserEmailLoginJTextField("carlos.profesor@email.com");
            view.setPasswordJField("1234");
        } else if (rol.equals("Admin")) {
            view.setUserEmailLoginJTextField("nerea.admin@email.com");
            view.setPasswordJField("1234");
        }
    }

    public void changeImage() {
        java.net.URL url = getClass().getResource("/login.png");
        System.out.println("URL encontrada: " + url);
        if (url == null) {
            System.out.println("No se encontró la imagen");
            return;
        }
        ImageIcon image = new ImageIcon(url);
        view.getImagetoLabel(image);
    }

    public void initComponentsLogIn() {
        view.setVisibleUserLoginJPanel(true);
        view.setVisibleBackToLectureJButton(true);
        view.setVisibleLoginJButton(false);
        view.setVisibleEmailJTextField(false);
        view.setVisibleLabelEmail(false);
        view.setVisiblePasswordJTextField(false);
        view.setVisibleLabelPassword(false);
        view.setVisibleCancelJbutton(false);
        view.setVisibleForgetPasswordJLabel(false);
    }

    public void initComponentsLogOut() {
        view.setVisibleBackToLectureJButton(false);
        view.setVisibleUserLoginJPanel(false);
        view.setVisibleLoginJButton(true);
        view.setVisibleEmailJTextField(true);
        view.setVisibleLabelEmail(true);
        view.setVisiblePasswordJTextField(true);
        view.setVisibleLabelPassword(true);
        view.setVisibleCancelJbutton(true);
        view.setVisibleForgetPasswordJLabel(true);
    }

    public ActionListener getLoginJButton() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modelOperations.abrirConexion();
                System.out.println("Login");
                String emailWriter = view.getEmailJTextField();
                String passwordWriter = view.getPasswordJPassword();

                if (emailWriter.isEmpty() || passwordWriter.isEmpty()) {
                    JOptionPane.showMessageDialog(viewMain, "Completa los campos", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                userActual = modelOperations.cogerDatosUsuarioPorEmail(emailWriter);
                System.out.println("=== DEPURACIÓN ===");
                System.out.println("Usuario: " + userActual.getNombre());
                System.out.println("Hash en BD: " + userActual.getContrasenha());
                System.out.println("Longitud hash: " + userActual.getContrasenha().length());
                System.out.println("Contraseña introducida: " + passwordWriter);

// Probar BCrypt directamente
                boolean resultado = BCrypt.checkpw(passwordWriter, userActual.getContrasenha());
                System.out.println("Resultado BCrypt: " + resultado);

                if (userActual != null) {
                    if (userActual.verifyPassword(passwordWriter)) {
                        if (rol.equals("Admin") && modelOperations.esAdministrador(userActual.getIDUsuario())) {
                            sesionActivada = true;
                            String rol = modelOperations.seleccionarTipoSegunIDUsuario(userActual.getIDUsuario());
                            if (rol.equals("superAdmin")) {
                                JOptionPane.showMessageDialog(viewMain, "Inicio realizado", "Inicio de Sesion", JOptionPane.INFORMATION_MESSAGE);
                                view.setVisibleUserLoginJPanel(true);
                                view.setUserNameLoginJLabel("Inicio sesión (activada): " + userActual.getNombre());
                                view.setUserEmailLoginJLabel("Correo: " + userActual.getCorreo());
                                view.setUserRolLoginJLabel("Rol: " + rol);
                                initComponentsLogIn();

                                Administrador adminGestor = modelOperations.cogerDatosAdmin(userActual.getIDUsuario());
                                String alumnoName = userActual.getNombre();
                                modelOperations.updateFechaUltimoInicioAdmin(userActual.getIDUsuario());
                                iniciarVentanaAdmin(adminGestor, alumnoName);
                            } else if (rol.equals("reparador")) {
                                JOptionPane.showMessageDialog(viewMain, "Inicio realizado", "Inicio de Sesion", JOptionPane.INFORMATION_MESSAGE);
                                view.setVisibleUserLoginJPanel(true);
                                view.setUserNameLoginJLabel("Inicio sesión (activada): " + userActual.getNombre());
                                view.setUserEmailLoginJLabel("Correo: " + userActual.getCorreo());
                                view.setUserRolLoginJLabel("Rol: " + rol);
                                initComponentsLogIn();
                                Administrador adminGestor = modelOperations.cogerDatosAdmin(userActual.getIDUsuario());
                                String alumnoName = userActual.getNombre();
                                modelOperations.updateFechaUltimoInicioAdmin(userActual.getIDUsuario());
                                iniciarVentanaAdmin(adminGestor, alumnoName);
                            } else if (rol.equals("gestor")) {
                                JOptionPane.showMessageDialog(viewMain, "Inicio realizado", "Inicio de Sesion", JOptionPane.INFORMATION_MESSAGE);
                                view.setVisibleUserLoginJPanel(true);
                                view.setUserNameLoginJLabel("Inicio sesión (activada): " + userActual.getNombre());
                                view.setUserEmailLoginJLabel("Correo: " + userActual.getCorreo());
                                view.setUserRolLoginJLabel("Rol: " + rol);
                                initComponentsLogIn();
                                Administrador adminGestor = modelOperations.cogerDatosAdmin(userActual.getIDUsuario());
                                String alumnoName = userActual.getNombre();
                                modelOperations.updateFechaUltimoInicioAdmin(userActual.getIDUsuario());
                                iniciarVentanaAdmin(adminGestor, alumnoName);
                            } else {
                                JOptionPane.showMessageDialog(viewMain, "No tienes ningun rol de administrador asigando \n Puede ser: \n - superAdmin \n - gestor \n -reparador", "Rol incorrecto", JOptionPane.ERROR_MESSAGE);
                            }
                        } else if (rol.equals("Profesor") && modelOperations.esProfesor(userActual.getIDUsuario())) {
                            sesionActivada = true;
                            JOptionPane.showMessageDialog(viewMain, "Inicio realizado", "Inicio de Sesion", JOptionPane.INFORMATION_MESSAGE);
                            view.setVisibleUserLoginJPanel(true);
                            view.setUserNameLoginJLabel("Inicio sesión (activada): " + userActual.getNombre());
                            view.setUserEmailLoginJLabel("Correo: " + userActual.getCorreo());
                            view.setUserRolLoginJLabel("Rol: " + rol);
                            initComponentsLogIn();
                            TeacherJDialog tjd = new TeacherJDialog(viewMain, true);
                            Teacher profesor = modelOperations.cogerDatosProfesor(userActual.getIDUsuario());
                            TeacherController thc = new TeacherController(tjd, modelOperations, profesor);
                            tjd.setVisible(true);
                        } else if (rol.equals("Alumno") && modelOperations.esAlumno(userActual.getIDUsuario())) {
                            sesionActivada = true;
                            JOptionPane.showMessageDialog(viewMain, "Inicio realizado", "Inicio de Sesion", JOptionPane.INFORMATION_MESSAGE);
                            view.setVisibleUserLoginJPanel(true);
                            view.setUserNameLoginJLabel("Inicio sesión (activada): " + userActual.getNombre());
                            view.setUserEmailLoginJLabel("Correo: " + userActual.getCorreo());
                            view.setUserRolLoginJLabel("Rol: " + rol);
                            initComponentsLogIn();

                            Student alumno = modelOperations.cogerDatosAlumno(userActual.getIDUsuario());
                            String alumnoName = userActual.getNombre();
                            iniciarVentanaAlumno(alumno, alumnoName);

                        } else {
                            String rolUsuario = "";
                            if (modelOperations.esAdministrador(userActual.getIDUsuario())) {
                                String tipoAdmin = modelOperations.seleccionarTipoSegunIDUsuario(userActual.getIDUsuario());
                                if (rolUsuario.equals("")) {
                                    rolUsuario = "Administrador (" + tipoAdmin + ")";
                                } else {
                                    rolUsuario = rolUsuario + " Administrador (" + tipoAdmin + ")";
                                }
                            }

                            if (modelOperations.esProfesor(userActual.getIDUsuario())) {
                                if (rolUsuario.equals("")) {
                                    rolUsuario = "Profesor";
                                } else {
                                    rolUsuario = rolUsuario + " y Profesor";
                                }
                            }

                            if (modelOperations.esAlumno(userActual.getIDUsuario())) {
                                if (rolUsuario.equals("")) {
                                    rolUsuario = "Alumno";
                                } else {
                                    rolUsuario = rolUsuario + " y Alumno";
                                }
                            }

                            if (rolUsuario.equals("")) {
                                JOptionPane.showMessageDialog(viewMain, "Tu rol no ha sido asigando (usuario)", "Rol incorrecto", JOptionPane.ERROR_MESSAGE);
                            }
                            JOptionPane.showMessageDialog(viewMain, "Has pulsado el botón de: " + rol + "\n" + "Tus roles asignados son: " + rolUsuario, "Roles del usuario", JOptionPane.INFORMATION_MESSAGE);
                        }

                    } else {
                        JOptionPane.showMessageDialog(viewMain, "Contraseña incorrecta", "Error en contraseña", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(viewMain, "El usuario introducido no está en la BD", "Usuario no existe", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        return al;
    }

    public ActionListener getBackToLectureJButton() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (rol.equals("Alumno")) {
                    Student alumno = modelOperations.cogerDatosAlumno(userActual.getIDUsuario());
                    String alumnoName = userActual.getNombre();
                    iniciarVentanaAlumno(alumno, alumnoName);
                } else if (rol.equals("Profesor")) {
                    Teacher profesor = modelOperations.cogerDatosProfesor(userActual.getIDUsuario());
                    String nombreProfesor = userActual.getNombre();
                    iniciarVentanaProfesor(profesor, nombreProfesor);
                } else if (rol.equals("Admin")) {
                    Administrador admin = modelOperations.cogerDatosAdmin(userActual.getIDUsuario());
                    String nombreAdmin = userActual.getNombre();
                    iniciarVentanaAdmin(admin, nombreAdmin);
                }
            }
        };
        return al;
    }

    public void iniciarVentanaProfesor(Teacher profesor, String nombreProfesor) {
        TeacherJDialog tjd = new TeacherJDialog(viewMain, true);
        TeacherController thc = new TeacherController(tjd, modelOperations, profesor);
        tjd.setVisible(true);
    }

    public void iniciarVentanaAdmin(Administrador admin, String nombreAdmin) {
        AdminsDialog admjd = new AdminsDialog(viewMain, true);
        AdminsController admc = new AdminsController(admjd, modelOperations, admin, nombreAdmin);
        admjd.setVisible(true);
    }

    public void iniciarVentanaAlumno(Student alumno, String alumnoName) {
        StudentJDialog std = new StudentJDialog(viewMain, true);
        StudentController stc = new StudentController(std, alumno, alumnoName, modelOperations);
        std.setVisible(true);
    }

    public WindowListener getWindowsListener() {
        WindowListener windowListener = new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
                if (sesionActivada) {
                    int opcion = JOptionPane.showConfirmDialog(viewMain, "¿Seguro que quieres salir? \n Tienes la sesión iniciada al cerrar tendrás que volver a iniciar sesión", "Cerrar ventana", JOptionPane.YES_NO_OPTION);
                    if (opcion == JOptionPane.YES_OPTION) {
                        view.dispose();
                    }
                } else {
                    view.dispose();
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        };
        return windowListener;
    }

    public ActionListener getLogOutJButton() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sesionActivada = false;
                initComponentsLogOut();
                view.clearFields();
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

    public MouseListener getForgetPasswordActionListener() {
        MouseListener moul = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(view, "Ponte en contacto con el administrador del sistema\n para poder restablecer la contraseña", "Contactar con administrador", JOptionPane.INFORMATION_MESSAGE);
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        };
        return moul;
    }

}
