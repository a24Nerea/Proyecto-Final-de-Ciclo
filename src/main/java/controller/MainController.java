/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import controller.admin.AdminsController;
import controller.login.LoginController;
import controller.teacher.TeacherController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import view.MainJFrame;
import view.login.LoginJDialog;

/**
 *
 * @author nerea.salgado
 */
public class MainController {

    private MainJFrame view;

    public MainController(MainJFrame view) {
        this.view = view;
        this.view.addStudentJButtonActionListener(this.getStudentActionListener());
        this.view.addAdminJButtonActionListener(this.getAdminActionListener());
        this.view.addTeacherJButtonActionListener(this.getTeacherActionListener());
    }

    public void changeImage() {
        java.net.URL url = getClass().getResource("/logoIES.png");
        System.out.println("URL encontrada: " + url);
        if (url == null) {
            System.out.println("No se encontró la imagen");
            return;
        }
        ImageIcon image = new ImageIcon(url);
        view.getImagetoLabel(image);
    }

    public ActionListener getTeacherActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginJDialog ljd = new LoginJDialog(true, view);
                LoginController lc = new LoginController(ljd, view, "Profesor");
                lc.changeImage();
                ljd.setVisible(true);
            }
        };
        return al;
    }

    public ActionListener getAdminActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginJDialog ljd = new LoginJDialog(true, view);
                LoginController lc = new LoginController(ljd, view, "Admin");
                lc.changeImage();
                ljd.setVisible(true);
            }
        };
        return al;
    }

    public ActionListener getStudentActionListener() {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginJDialog ljd = new LoginJDialog(true, view);
                LoginController lc = new LoginController(ljd, view, "Alumno");
                lc.changeImage();
                ljd.setVisible(true);
            }
        };
        return al;
    }

}
