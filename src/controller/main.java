/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import model.modelData;

import view.formLogin;

/**
 *
 * @author Userr
 */
public class main {
     public static void main(String[] args) {
        modelData model = new modelData();
       formLogin log = new formLogin();
        new controllerLogin(model,log);
       
        log.setVisible(true); // Show the JFrame
        log.pack();
        log.setLocationRelativeTo(null);
    }
}
