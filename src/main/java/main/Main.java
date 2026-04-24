/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package main;

import controller.MainController;
import java.sql.Connection;
import model.OperationBD;
import org.mindrot.jbcrypt.BCrypt;
import view.MainJFrame;

/**
 *
 * @author nerea.salgado
 */
public class Main {

    public static void main(String[] args) {

        MainJFrame view = new MainJFrame();
        view.setVisible(true);
        MainController mc = new MainController(view);
        mc.changeImage();
    }
}
