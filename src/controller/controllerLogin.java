/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import dao.loginDao;
import model.modelData;
import javax.swing.JOptionPane;
import view.formLogin;
import view.Dashboard;

public class controllerLogin {
    private modelData model;
    private formLogin login;
    private String nama;
    private String level;
     public controllerLogin(modelData model, formLogin login) {
        this.model = model;
        this.login = login;
        System.out.println("Login initiated");
        login.getButton().addActionListener(e -> validation());
     
     }
     
     public boolean validation(){
         String username = login.getUsername().getText();
         String password = login.getPassword().getText();
         String query = "SELECT * FROM admins WHERE username = ? AND pass = ?";

//         String role = null;
           if(username.isEmpty() || password.isEmpty()){
            JOptionPane.showMessageDialog(login, "Any input Field CANNOT BE EMPTY!");
            return false;
            }
          
          String role = model.validateUser(username, password,query );
        if (role != null) {
            String message = "Welcome " + username + "!";
            JOptionPane.showMessageDialog(login, message);
            loginDao log = loginDao.getInstance();
            log.setUsername(username);
            log.setLevel(role);
            // Open forms based on role
            if ("Admin".equals(role)) {
                System.out.println("Admin view loaded.");
                Dashboard dashboard = new Dashboard();
                dashboard.setVisible(true); // Show the JFrame
                dashboard.pack();
                dashboard.setLocationRelativeTo(null);
                dashboard.getUsername().setText(log.getUsername() + "!");
            dashboard.getLevel().setText(log.getLevel());
            } else {
                nama = username;
                System.out.println("User view loaded.");
                Dashboard dashboard = new Dashboard();
                dashboard.setVisible(true); // Show the JFrame
                dashboard.pack();
                dashboard.setLocationRelativeTo(null);
                dashboard.getUsername().setText(log.getUsername() + "!");
            }
            login.dispose();
            
        } else {
            JOptionPane.showMessageDialog(login, "Invalid username or password!");
        }
        return false;
     }

}

    

