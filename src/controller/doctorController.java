/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import model.modelData;
import view.Dashboard;
import view.formDoctor;

public class doctorController implements BaseController{
    private static doctorController instance;
     private modelData model;
    private formDoctor view;
    private dashboardController dController;
    
    private boolean isAdding = false;
     private boolean isUpdating= false;
     private int selectedId = -1;
     
 public static synchronized doctorController getInstance(modelData model, formDoctor view) {
        if (instance == null) {
            instance = new doctorController(model, view);
        }
        return instance;
    }
 
    public doctorController(modelData model, formDoctor view) {
        this.model = model;
        this.view = view;
        
        System.out.println("adminController initialized!");
        
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
        view.getSp().setEnabled(enable);
        view.getPhone().setEnabled(enable);
        view.getEmail().setEnabled(enable);
        view.getButtonUpdate().setEnabled(!enable);
        view.getButtonDelete().setEnabled(!enable);
        view.getTable().setEnabled(!enable); // Disable table
    }
      
      private void cancelAddMode() {
        // Reset DA stateEEEEEEEEEEEEEEEEEEEEE
        isAdding = false;
       view.getNama().setText("");
       view.getSp().setText(null);
       view.getPhone().setText("");
       view.getEmail().setText("");
        view.getButtonAdd().setText("Add");
         view.getButtonAdd().setEnabled(true);
         view.getButtonCancel().setEnabled(false);
        enableAddMode(false);
    }

    public void displayData() {
    String query = "SELECT * FROM doctors";
    String[] columnNames = {"id_doctor", "name_doctor", "specialization", "phone","email"};

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
            rowData[4]
            
        });
    }
    }
    public boolean isUsernameExist(String username){     
        String query = "SELECT * FROM doctos WHERE username = ?";
        return model.unique(username, "username", query);
    }
    
    public boolean isEmailExist(String email){
        String query = "SELECT * FROM doctors WHERE email = ?";
        return model.unique(email, "email", query);
        
    }
    public boolean insertData(){
            String Nama = view.getNama().getText();
            String sp = view.getSp().getText();
            String phone = view.getPhone().getText();
            String email = view.getEmail().getText();

            Object[] values = {Nama, sp, phone, email};
            if(Nama.isEmpty() || sp.isEmpty() || email.isEmpty() || phone.isEmpty()){
            JOptionPane.showMessageDialog(view, "Any input Field CANNOT BE EMPTY!");
            return false;
            }
                if (isEmailExist(email)) {
        JOptionPane.showMessageDialog(view, "this Email already exists!");
        return false;
    }
            boolean success = model.insertData("INSERT INTO doctors (name_doctor, specialization, "
                    + "phone,email) VALUES (?, ?,?,?)",values);
            if (success) {
                JOptionPane.showMessageDialog(view, "Data inserted successfully!");
                displayData(); // Refresh the table after insertion
            } else {
                JOptionPane.showMessageDialog(view, "Failed insert!");
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
            String Nama = (String) view.getTable().getValueAt(selectedRow, 2);

            String sp = (String) view.getTable().getValueAt(selectedRow, 3);
            String phone = (String) view.getTable().getValueAt(selectedRow, 4);
            String email = (String) view.getTable().getValueAt(selectedRow, 5);
            
            view.getNama().setText(Nama);
             view.getSp().setText(sp);
            view.getPhone().setText(phone);
            view.getEmail().setText(email);   
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
        
              String oldEmail = (String) view.getTable().getValueAt(selectedRow, 5);
          
          String Nama = view.getNama().getText();
          String sp = view.getSp().getText();

            String phone = view.getPhone().getText();
            String email = view.getEmail().getText();
               Object[] values = {Nama, sp, phone, email, selectedId};
               
        if(Nama.isEmpty() || sp.isEmpty() || email.isEmpty() || phone.isEmpty()){
            JOptionPane.showMessageDialog(view, "Any input Field CANNOT BE EMPTY!");
            return false;
            }
//        validation
          if(!oldEmail.equals(email)){
                if (isEmailExist(email)) {
        JOptionPane.showMessageDialog(view, "this Email already exists!");
        return false;
    }}
        boolean success = model.updateData("UPDATE doctors SET name_doctor= ?, specialization = ?, "
                + "phone = ?,email = ? WHERE id_doctor= ?",values);
        if (success) {
            JOptionPane.showMessageDialog(view, "Data updated successfully!");
            displayData(); // Refresh table
            cancelUpdateMode(); // Reset state
        } else {
            JOptionPane.showMessageDialog(view, "Failed to update Data.");
            return false;
        }
        return true;
    }
           private void cancelUpdateMode() {
        // Reset the state
        isUpdating = false;
        view.getButtonUpdate().setText("Update");
        enableUpdateMode(isUpdating);
        
          view.getNama().setText("");
             view.getSp().setText("");
            view.getPhone().setText("");
            view.getEmail().setText("");
    }

    private void enableUpdateMode(boolean enable) {
        view.getButtonUpdate().setEnabled(true);
        view.getButtonCancel().setEnabled(true);
        view.getButtonAdd().setEnabled(!enable);
        view.getButtonDelete().setEnabled(!enable);
        view.getTable().setEnabled(!enable); // Disable table in update mode
        view.getNama().setEnabled(enable);
           view.getSp().setEnabled(enable);
           view.getPhone().setEnabled(enable);
          view.getEmail().setEnabled(enable);
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
        "Are you sure you want to delete this Data with Name: "+nama+"",
        "Delete Confirmation",
        JOptionPane.YES_NO_OPTION
    );

    if (confirm == JOptionPane.YES_OPTION) {
        boolean success = model.deleteData("DELETE FROM doctors WHERE id_doctor = ?",id);
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
