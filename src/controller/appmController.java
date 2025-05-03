/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.comboBoxDao;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import model.modelData;
import view.Dashboard;
import view.formApp;
import helper.koneksi;
import helper.comboBoxHelper;

/**
 *
 * @author Userr
 */
public class appmController implements BaseController{
    private static appmController instance;
     private modelData model;
    private formApp view;
    private dashboardController dController;
    private comboBoxHelper cb;
    private boolean isAdding = false;
     private boolean isUpdating= false;
     private int selectedId = -1;
     
 public static synchronized appmController getInstance(modelData model, formApp view) {
        if (instance == null) {
            instance = new appmController(model, view);
        }
        return instance;
    }
 
    public appmController(modelData model, formApp view) {
        this.model = model;
        this.view = view;
        populateDoctorComboBox();
        populatePatientComboBox();
        System.out.println("appointment Controller initialized!");
        
       for (var listener : view.getButtonBack().getActionListeners()) {
        view.getButtonBack().removeActionListener(listener);
}
          view.getButtonBack().addActionListener(e -> back());

          view.getButtonAdd().addActionListener(e -> toggleAddMode());
          view.getButtonCancel().addActionListener(e -> {
            if(isAdding)
        {
      cancelAddMode();
        }else if(!isAdding && isUpdating){
       cancelUpdateMode();
        }
          });
          
//        view.getInsertButton().addActionListener(e -> insertStudent());
  view.getButtonUpdate().addActionListener(e -> enableUpdate());
  
  view.getButtonDelete().addActionListener(e -> deleteData());
  
        displayData();
    }
    
      private void toggleAddMode() {
        if (!isAdding) {
            // Switch to "Add" mode
            isAdding = true;
            view.getButtonAdd().setText("Save");
            enableAddMode(true);

        } else {
            // Run insertStudent when "Save" is clicked
           insertData();
        }
    }
      
      private void enableAddMode(boolean enable) {
        // Enable/disable components based on add mode
        view.getButtonCancel().setEnabled(true);
        view.getButtonAdd().setEnabled(true); 
        view.getDoctorName().setEnabled(enable);
        view.getPatientName().setEnabled(enable);
        view.getStatus().setEnabled(enable);
        view.getDate().setEnabled(enable);
        view.getNotes().setEnabled(enable);
        view.getButtonUpdate().setEnabled(!enable);
        view.getButtonDelete().setEnabled(!enable);
        view.getTable().setEnabled(!enable); // Disable table
    }
      
      private void cancelAddMode() {
        // Reset DA stateEEEEEEEEEEEEEEEEEEEEE
        isAdding = false;
       view.getDoctorName().setSelectedItem(0);
       view.getPatientName().setSelectedItem(0);
       view.getDate().setDate(null);
       view.getStatus().setSelectedItem(0);
       view.getNotes().setText("");
        view.getButtonAdd().setText("Add");
         view.getButtonAdd().setEnabled(true);
         view.getButtonCancel().setEnabled(false);
        enableAddMode(false);
    }

    public void displayData() {
    String query = "SELECT * FROM appointments LEFT JOIN "
            + "doctors on appointments.id_doctor = doctors.id_doctor"
            + " LEFT JOIN patients on appointments.id_patient = patients.id_patient";
    String[] columnNames = {"id_appointment", "name_patient", "name_doctor", "appointment_date","status","notes"};

    var dataList = model.getAllData(query, columnNames);
    DefaultTableModel tableModel = (DefaultTableModel) view.getTable().getModel();

    tableModel.setRowCount(0); // Clear existing rows
    int no = 1;

    for (Object[] rowData : dataList) {
        tableModel.addRow(new Object[]{
            rowData[0],      // ID column
            no++,            // No column (auto-increment)
            rowData[1],      
            rowData[2],      
            rowData[3],       
            rowData[4],       
            rowData[5],

        });
    }
        

    } 
    public boolean insertData(){
//     comboBoxHelper cb = new comboBoxHelper();
comboBoxHelper DNama = (comboBoxHelper) view.getDoctorName().getSelectedItem();
    comboBoxHelper PNama = (comboBoxHelper) view.getPatientName().getSelectedItem();

    if (DNama == null || PNama == null) {
        JOptionPane.showMessageDialog(view, "Please select both a doctor and a patient!");
        return false;
    }
    // for the love of god please dont error
    int doctorId = Integer.parseInt(DNama.getId());
    int patientId = Integer.parseInt(PNama.getId());
    java.util.Date appDate = view.getDate().getDate();
    var status = view.getStatus().getSelectedItem(); 
    String notes = view.getNotes().getText();

    if (notes.isEmpty() || appDate == null || status == null) {
        JOptionPane.showMessageDialog(view, "All fields must be filled out!");
        return false;
    }
    // Prepare the data to be inserted
    Object[] values = {patientId, doctorId, appDate, status, notes};
    // Attempt to insert data
    boolean success = model.insertData(
        "INSERT INTO appointments (id_patient, id_doctor, appointment_date, status, notes)"
                + " VALUES (?, ?, ?, ?, ?)", 
        values
    );
    if (success) {
        JOptionPane.showMessageDialog(view, "Data inserted successfully!");
        displayData(); // Refresh the table after insertion
    } else {
        JOptionPane.showMessageDialog(view, "Failed to insert data.");
    }
    return success;
   }
    
      private void enableUpdate() {
        if (!isUpdating) {
            // Switch to "Update" mode
            int selectedRow = view.getTable().getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(view, "Please select any row to update.");
                return;
            }
            // Load the selected student into the text fields
            selectedId = (int) view.getTable().getValueAt(selectedRow, 0); // Assuming ID is in column 0
            String nameP = (String) view.getTable().getValueAt(selectedRow, 2);
            String nameD = (String) view.getTable().getValueAt(selectedRow, 3);
            Date db = (Date) view.getTable().getValueAt(selectedRow, 4);

            String status = (String) view.getTable().getValueAt(selectedRow, 5);
            String notes = (String) view.getTable().getValueAt(selectedRow, 6);
//            view.getDoctorName().setSelectedItem(nameP);
//            view.getPatientName().setSelectedItem(nameD);
      // Find and select the corresponding comboBoxHelper object for DoctorName
        for (int i = 0; i < view.getDoctorName().getItemCount(); i++) {
            comboBoxHelper item = (comboBoxHelper) view.getDoctorName().getItemAt(i);
            if (item.toString().equals(nameD)) { // Match the name
                view.getDoctorName().setSelectedItem(item);
                break;
            }
        }
        // Find and select the corresponding comboBoxHelper object for PatientName
        for (int i = 0; i < view.getPatientName().getItemCount(); i++) {
            comboBoxHelper item = (comboBoxHelper) view.getPatientName().getItemAt(i);
            if (item.toString().equals(nameP)) { // Match the name
                view.getPatientName().setSelectedItem(item);
                break;
            }
        }

            view.getDate().setDate(db);
            view.getStatus().setSelectedItem(status);
            view.getNotes().setText(notes);

            isUpdating = true;
                       
            view.getButtonUpdate().setText("Save");
            enableUpdateMode(true);
        } else {
            // Save the updated student
            updateData();
             cancelUpdateMode();
        }
      }
          private boolean updateData() {
          int selectedRow = view.getTable().getSelectedRow();
          selectedId = (int) view.getTable().getValueAt(selectedRow, 0);
    comboBoxHelper DNama = (comboBoxHelper) view.getDoctorName().getSelectedItem();
    comboBoxHelper PNama = (comboBoxHelper) view.getPatientName().getSelectedItem();

    if (DNama == null || PNama == null) {
        JOptionPane.showMessageDialog(view, "Please select both a doctor and a patient!");
        return false;
    }
    // for the love of god please dont error
    int doctorId = Integer.parseInt(DNama.getId());
    int patientId = Integer.parseInt(PNama.getId());
   
    java.util.Date appDate = view.getDate().getDate();
    var status = view.getStatus().getSelectedItem(); 
    String notes = view.getNotes().getText();
               Object[] values = {patientId, doctorId, appDate, status, notes,selectedId};    
        if (notes.isEmpty() || appDate == null) {
            JOptionPane.showMessageDialog(view, "Please fill in all fields.");
            return false;
        }

        boolean success = model.updateData("UPDATE appointments SET"
                + " id_patient = ?, id_doctor = ?, appointment_date = ?,status = ?, notes = ? "
                + "WHERE id_appointment = ?",values);
        if (success) {
            JOptionPane.showMessageDialog(view, "Data updated successfully!");
            displayData(); // Refresh table
            cancelUpdateMode(); // Reset state
        } else {
            JOptionPane.showMessageDialog(view, "Failed to update Data.");
        }
        return success;
    }
           private void cancelUpdateMode() {
        // Reset the state
        isUpdating = false;
        view.getButtonUpdate().setText("Update");
        enableUpdateMode(isUpdating);
           view.getPatientName().setSelectedItem(0);
            view.getDoctorName().setSelectedItem(0);
             view.getDate().setDate(null);
            view.getStatus().setSelectedItem(0);
            view.getNotes().setText("");

    }

    private void enableUpdateMode(boolean enable) {
        view.getButtonUpdate().setEnabled(true);
        view.getButtonCancel().setEnabled(true);
        view.getButtonAdd().setEnabled(!enable);
        view.getButtonDelete().setEnabled(!enable);
        view.getTable().setEnabled(!enable); // Disable table in update mode
          view.getPatientName().setEnabled(enable);
            view.getDoctorName().setEnabled(enable);
             view.getDate().setEnabled(enable);
            view.getStatus().setEnabled(enable);
            view.getNotes().setEnabled(enable);

    } 
    private void deleteData() {
    // Get the selected row
    int selectedRow = view.getTable().getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(view, "Please select any row to delete.");
        return;
    }
    // Get the ID of the selected student
    int id = (int) view.getTable().getValueAt(selectedRow, 0); // Assuming ID is in column 0
    String nama = (String) view.getTable().getValueAt(selectedRow, 2);
    // Confirm the deletion
    int confirm = JOptionPane.showConfirmDialog(
        view,
        "Are you sure you want to delete this data with Name: "+nama+"",
        "Delete Confirmation",
        JOptionPane.YES_NO_OPTION
    );

    if (confirm == JOptionPane.YES_OPTION) {
        boolean success = model.deleteData("DELETE FROM appointments WHERE id_appointment= ?",id);
        if (success) {
            JOptionPane.showMessageDialog(view, "Data deleted successfully.");
            displayData(); // Refresh the table
        } else {
            JOptionPane.showMessageDialog(view, "Failed to delete the data.");
        }
    }
}  
    public void back(){
        System.out.println("Stopping PatientController...");
          // Tutup dashboard
          for (var listener : view.getButtonBack().getActionListeners()) {
                 view.getButtonBack().removeActionListener(listener);
            }     
        view.dispose();
        view.setVisible(false); 
       SwingUtilities.invokeLater(new Runnable() {
    @Override
    public void run() {
        view.setVisible(false);  // Safely hide the form on EDT
    }
}); 
        Dashboard log = new Dashboard();     
        dController = new dashboardController(log);
        // Tampilkan form login
        log.setVisible(true);
        log.pack();
        log.setLocationRelativeTo(null);
    }
    @Override
     public void stopController() {
        System.out.println("Stopping PatientController...");
        // Remove listeners or clean up resources
        view.getButtonBack().removeActionListener(null);
        view.getButtonAdd().removeActionListener(null);
        view.getButtonCancel().removeActionListener(null);
        view.getButtonUpdate().removeActionListener(null);
        view.getButtonDelete().removeActionListener(null);
        view.dispose();
    }
     private koneksi k;
     private void populatePatientComboBox() {
         comboBoxDao dao = new comboBoxDao(k);
        List<Object[]> data_patient = dao.getAllData(
        "SELECT name_patient, id_patient FROM patients",
        new String[] { "name_patient", "id_patient" }
    );

    view.getPatientName().removeAllItems(); // Clear existing items in the combo box

    // Iterate over the retrieved data and format it
    for (Object[] row : data_patient) {
        String name = (String) row[0];
        String id = String.valueOf(row[1]);
        view.getPatientName().addItem(new comboBoxHelper(id,name));
    }
     }
     
      private void populateDoctorComboBox() {
         comboBoxDao dao = new comboBoxDao(k);
        List<Object[]> data_patient = dao.getAllData(
        "SELECT name_doctor, email,id_doctor FROM doctors",
        new String[] { "name_doctor","id_doctor"}
    );

    view.getDoctorName().removeAllItems(); // Clear existing items in the combo box

    // Iterate over the retrieved data and format it
    for (Object[] row : data_patient) {
        String name = (String) row[0];
        
        String id = String.valueOf(row[1]);
        view.getDoctorName().addItem(new comboBoxHelper(id,name));
    }
     }
}

