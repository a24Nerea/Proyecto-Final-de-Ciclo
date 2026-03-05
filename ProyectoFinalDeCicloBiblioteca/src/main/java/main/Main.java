/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package main;

import controller.LoginController;
import view.login.LoginJFrame;

/**
 *
 * @author nerea.salgado
 */
public class Main {

    public static void main(String[] args) {
        LoginJFrame view = new LoginJFrame();
        view.setVisible(true);
        LoginController mc = new LoginController(view);
        mc.changeImage();
        
    }
}
