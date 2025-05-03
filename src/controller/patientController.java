/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import model.modelData;
import view.Dashboard;
import view.formPatient;
public class patientController implements BaseController{
    private static patientController instance;
     private modelData model;
    private formPatient view;
    private dashboardController dController;
    
    private boolean isAdding = false;
     private boolean isUpdating= false;
     private int selectedId = -1;
     
 public static synchronized patientController getInstance(modelData model, formPatient view) {
        if (instance == null) {
            instance = new patientController(model, view);
        }
        return instance;
    }
 
    public patientController(modelData model, formPatient view) {
        this.model = model;
        this.view = view;
        
        System.out.println("patientController initialized!");
        
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
        view.getNama().setEnabled(enable);
        view.getGender().setEnabled(enable);
        view.getBd().setEnabled(enable);
        view.getPhone().setEnabled(enable);
        view.getAddress().setEnabled(enable);
        view.getButtonUpdate().setEnabled(!enable);
        view.getButtonDelete().setEnabled(!enable);
        view.getTable().setEnabled(!enable); // Disable table
    }
      private void cancelAddMode() {
        // Reset DA stateEEEEEEEEEEEEEEEEEEEEE
        isAdding = false;
       view.getNama().setText("");
       view.getBd().setDate(null);
       view.getPhone().setText("");
       view.getAddress().setText("");
        view.getButtonAdd().setText("Add");
         view.getButtonAdd().setEnabled(true);
         view.getButtonCancel().setEnabled(false);
        enableAddMode(false);
    }

    public void displayData() {
    String query = "SELECT * FROM patients";
    String[] columnNames = {"id_patient", "name_patient", "gender", "birth_date","phone","address"};

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
            rowData[5]       
        });
    }
    }
    
    public boolean insertData(){
      
            String Nama = view.getNama().getText();
            var gender = (String) view.getGender().getSelectedItem();
            java.util.Date bd = view.getBd().getDate();
            String phone = view.getPhone().getText();
            String address = view.getAddress().getText();

            Object[] values = {Nama, gender, bd, phone, address};
            
            if(Nama.isEmpty() || phone.isEmpty() || address.isEmpty()){
            JOptionPane.showMessageDialog(view, "Any input Field CANNOT BE EMPTY!");
            return false;
            }
            
            boolean success = model.insertData("INSERT INTO patients (name_patient, "
                    + "gender, birth_date,phone,address) VALUES (?, ?,?,?,?)",values);
            if (success) {
                JOptionPane.showMessageDialog(view, "Data inserted successfully!");
                displayData(); // Refresh the table after insertion
            } else {
                JOptionPane.showMessageDialog(view, "Failed to insert data.");
            }
        return false;
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
            String name = (String) view.getTable().getValueAt(selectedRow, 2);
            String gender = (String) view.getTable().getValueAt(selectedRow, 3);
            Date db = (Date) view.getTable().getValueAt(selectedRow, 4);

            String phone = (String) view.getTable().getValueAt(selectedRow, 5);
            String address = (String) view.getTable().getValueAt(selectedRow, 6);
            view.getNama().setText(name);
            view.getGender().setSelectedItem(gender);
            view.getPhone().setText(phone);
             view.getBd().setDate(db);
            view.getAddress().setText(address);
            isUpdating = true;
            view.getButtonUpdate().setText("Save");
            enableUpdateMode(true);
        } else {
            // Save the updated student
            updateData();
             cancelUpdateMode();
        }
      }
          private void updateData() {
              int selectedRow = view.getTable().getSelectedRow();
          selectedId = (int) view.getTable().getValueAt(selectedRow, 0);
          String Nama = view.getNama().getText();
            var gender = (String) view.getGender().getSelectedItem();
            java.util.Date bd = view.getBd().getDate();
            String phone = view.getPhone().getText();
            String address = view.getAddress().getText();
               Object[] values = {Nama, gender, bd, phone, address,selectedId};
               
        if (Nama.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Please fill in all fields.");
            return;
        }

        boolean success = model.updateData("UPDATE patients SET name_patient = ?, "
                + "gender = ?, birth_date = ?,phone = ?, address = ? WHERE id_patient= ?",values);
        if (success) {
            JOptionPane.showMessageDialog(view, "Data updated successfully!");
            displayData(); // Refresh table
            cancelUpdateMode(); // Reset state
        } else {
            JOptionPane.showMessageDialog(view, "Failed to update Data.");
        }
    }
           private void cancelUpdateMode() {
        // Reset the state
        isUpdating = false;
        view.getButtonUpdate().setText("Update");
        enableUpdateMode(isUpdating);
          view.getNama().setText("");
          view.getPhone().setText("");
          view.getBd().setDate(null);
          view.getGender().setSelectedItem("");
          view.getAddress().setText("");
      
    }

    private void enableUpdateMode(boolean enable) {
        view.getButtonUpdate().setEnabled(true);
        view.getButtonCancel().setEnabled(true);
        view.getButtonAdd().setEnabled(!enable);
        view.getButtonDelete().setEnabled(!enable);
        view.getTable().setEnabled(!enable); // Disable table in update mode
        view.getNama().setEnabled(enable);
           view.getPhone().setEnabled(enable);
           view.getBd().setEnabled(enable);
           view.getGender().setEnabled(enable);
          view.getAddress().setEnabled(enable);
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
        "Are you sure you want to delete this student with Name: "+nama+"",
        "Delete Confirmation",
        JOptionPane.YES_NO_OPTION
    );

    if (confirm == JOptionPane.YES_OPTION) {
        boolean success = model.deleteData("DELETE FROM patients WHERE id_patient= ?",id);
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
}
