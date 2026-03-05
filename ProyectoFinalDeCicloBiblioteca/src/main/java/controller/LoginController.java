/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import javax.swing.ImageIcon;
import view.login.LoginJFrame;

/**
 *
 * @author nerea.salgado
 */
public class LoginController {

    private LoginJFrame view;

    public LoginController(LoginJFrame view) {
        this.view = view;
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

}
