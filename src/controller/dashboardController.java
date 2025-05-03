/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.loginDao;
import model.modelData;
import view.Dashboard;

import view.formLogin;
import view.formPatient;
import view.formAdmin;
import view.formDoctor;
import view.formApp;
import view.formMed;
import view.formReceipt;

public class dashboardController {
     private Dashboard dashboard;
     private static dashboardController instance;
//     all of controller
     
      private patientController pcontroller;
      private adminController acontroller;
      private controllerLogin lcontroller;
      private doctorController dcontroller;
      private appmController apcontroller;
      private medController mcontroller;
      private receiptController rcontroller;
      
//      form
      private formPatient patientForm;
      private formAdmin admin;
      private formLogin loginForm;
      private formDoctor doctor;
      private formApp appm;
      private formMed med;
      private formReceipt rec;
//DAO
      private loginDao log;
    public dashboardController(Dashboard dashboard) {
        this.dashboard = dashboard;
        
        // Tampilkan data login di view
        loadUserData();
        
//        for loop to remove one actionListener
//    to patient button
              for (var listener : dashboard.getButtonPatient().getActionListeners()) {
        dashboard.getButtonPatient().removeActionListener(listener);
}
        dashboard.getButtonPatient().addActionListener(e -> toPatient());
        
//        toadmind button
for (var listener : dashboard.getButtonAdmin().getActionListeners()) {
        dashboard.getButtonAdmin().removeActionListener(listener);
}
        dashboard.getButtonAdmin().addActionListener(e -> toAdmin());
        
//        to doctor button
           for (var listener : dashboard.getButtonDoctor().getActionListeners()) {
        dashboard.getButtonDoctor().removeActionListener(listener);
}
        dashboard.getButtonDoctor().addActionListener(e -> toDoctor());
        
//        to appointment button
           for (var listener : dashboard.getButtonAppm().getActionListeners()) {
        dashboard.getButtonAppm().removeActionListener(listener);
}
        dashboard.getButtonAppm().addActionListener(e -> toAppm());
        
//        to med button
        for (var listener : dashboard.getButtonMed().getActionListeners()) {
        dashboard.getButtonMed().removeActionListener(listener);
}
        dashboard.getButtonMed().addActionListener(e -> toMed());
        
        //        to receipt button
        for (var listener : dashboard.getButtonRec().getActionListeners()) {
        dashboard.getButtonRec().removeActionListener(listener);
}
        dashboard.getButtonRec().addActionListener(e -> toRec());


        // Tambahkan aksi untuk tombol logout
          for (var listener : dashboard.getButtonLogout().getActionListeners()) {
        dashboard.getButtonLogout().removeActionListener(listener);
}
        dashboard.getButtonLogout().addActionListener(e -> logout());
    }
    private void loadUserData() {
        // Ambil data dari loginDao
        loginDao logD = loginDao.getInstance();
        String username = logD.getUsername();
        String level = logD.getLevel();
        
//        disable button for other level
          if ("admin".equalsIgnoreCase(level)) {
        dashboard.getButtonAdmin().setVisible(true); 
        dashboard.getButtonAdmin().setEnabled(true); 
    } else {
//        dashboard.getButtonAdmin().setVisible(false); // Hide button for staff or other levels
        dashboard.getButtonAdmin().setEnabled(false); // Disable button for staff
        dashboard.getButtonMed().setEnabled(false);
        dashboard.getButtonDoctor().setEnabled(false);
    }
        // Tampilkan di view
        dashboard.getUsername().setText(username);
        dashboard.getLevel().setText(level);
    }
    
    private void toPatient(){     
      // Dispose of the dashboard first to avoid overlap WHY IS THIS NOT WOOOK!!!
    dashboard.dispose();
    // Check if patientForm or patientController is null to avoid reinitialization
    if (patientForm == null) {
        System.out.println("Initializing patient form...");
        patientForm = new formPatient();
        modelData model = new modelData();
        pcontroller = new patientController(model, patientForm);
    } else {
        System.out.println("Reusing existing patient form...");
    }
        patientForm.setVisible(true);
        patientForm.pack();
        patientForm.setLocationRelativeTo(null);
    }
    
    private void toAdmin()
    {
     dashboard.dispose();
    // Check if patientForm or patientController is null to avoid reinitialization
    if (admin == null) {
        System.out.println("Initializing admin form...");
        admin = new formAdmin();
        modelData model = new modelData();
        acontroller = new adminController(model, admin);
    } else {
        System.out.println("Reusing existing admin form...");
    }
        admin.setVisible(true);
        admin.pack();
        admin.setLocationRelativeTo(null);
    }
    
     private void toDoctor()
    {
     dashboard.dispose();
    // Check if patientForm or patientController is null to avoid reinitialization
    if (doctor == null) {
        System.out.println("Initializing form...");
        doctor = new formDoctor();
        modelData model = new modelData();
        dcontroller = new doctorController(model, doctor);
    } else {
        System.out.println("Reusing existing doctor form...");
    }
        doctor.setVisible(true);
        doctor.pack();
        doctor.setLocationRelativeTo(null);
    }
     
       private void toAppm()
    {
     dashboard.dispose();
    // Check if patientForm or patientController is null to avoid reinitialization
    if (appm == null) {
        System.out.println("Initializing form...");
        appm = new formApp();
        modelData model = new modelData();
        apcontroller = new appmController(model, appm);
    } else {
        System.out.println("Reusing existing doctor form...");
    }
        appm.setVisible(true);
        appm.pack();
        appm.setLocationRelativeTo(null);
    }
       
       private void toMed()
    {
     dashboard.dispose();
    // Check if patientForm or patientController is null to avoid reinitialization
    if (med == null) {
        System.out.println("Initializing form...");
        med = new formMed();
        modelData model = new modelData();
        mcontroller = new medController(model, med);
    } else {
        System.out.println("Reusing existing doctor form...");
    }
        med.setVisible(true);
        med.pack();
        med.setLocationRelativeTo(null);
    }
       
       
          private void toRec()
    {
     dashboard.dispose();
    // Check if patientForm or patientController is null to avoid reinitialization
    if (rec == null) {
        System.out.println("Initializing form...");
        rec = new formReceipt();
        modelData model = new modelData();
        rcontroller = new receiptController(model, rec);
    } else {
        System.out.println("Reusing existing doctor form...");
    }
        rec.setVisible(true);
        rec.pack();
        rec.setLocationRelativeTo(null);
    }

    private void logout() {
    // Clear login data
    loginDao logD = loginDao.getInstance();
    logD.setUsername(null);
    logD.setLevel(null);
    logD.reset();
    // Close the dashboard
    dashboard.dispose();
   if (loginForm == null) {
            loginForm = new formLogin();
            modelData model = new modelData();
            lcontroller = new controllerLogin(model, loginForm);
        }
    // Show login form
    loginForm.setVisible(true);
    loginForm.pack();
    loginForm.setLocationRelativeTo(null);
         }
}
