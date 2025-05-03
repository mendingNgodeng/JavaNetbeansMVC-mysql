/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.comboBoxDao;
import helper.comboBoxHelper;
import helper.koneksi;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import model.modelData;
import view.Dashboard;
import view.formReceipt;
import java.math.BigDecimal;

public class receiptController implements BaseController{
    private static receiptController instance;
     private modelData model;
    private formReceipt view;
    private dashboardController dController;
    
    private boolean isAdding = false;
     private boolean isUpdating= false;
     private int selectedId = -1;
     
 public static synchronized receiptController getInstance(modelData model, formReceipt view) {
        if (instance == null) {
            instance = new receiptController(model, view);
        }
        return instance;
    }
    public receiptController(modelData model, formReceipt view) {
        this.model = model;
        this.view = view;
        populatePatientComboBox();
        populateMedComboBox();
        System.out.println("Controller initialized!");
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
        view.getService().setEnabled(enable);
        view.getPatient().setEnabled(enable);
        view.getMed().setEnabled(enable);
        view.getMedQ().setEnabled(enable);
        view.getSprice().setEnabled(enable);
        view.getStatus().setEnabled(enable);
        view.getButtonUpdate().setEnabled(!enable);
        view.getButtonDelete().setEnabled(!enable);
        view.getTable().setEnabled(!enable); // Disable table
    }
      
      private void cancelAddMode() {
        // Reset DA stateEEEEEEEEEEEEEEEEEEEEE
        isAdding = false;
       view.getPatient().setSelectedItem(0);
       view.getService().setText("");
       view.getMed().setSelectedItem(0);
       view.getStatus().setSelectedItem(0);
       view.getSprice().setText("");
       view.getMedQ().setText("");
        view.getButtonAdd().setText("Add");
         view.getButtonAdd().setEnabled(true);
         view.getButtonCancel().setEnabled(false);
        enableAddMode(false);
    }
    public void displayData() {
    String query = "SELECT r.id_receipt,m.id_medicine,m.stok, r.payment_date, r.payment_status, " +
                 "p.name_patient, m.name_medicine, m.price AS medicine_price, " +
                 "r.medicine_quantity, r.service_name, r.service_price, " +
                 "(COALESCE(r.medicine_quantity * m.price, 0) + COALESCE(r.service_price, 0)) "
            + "AS total_amount " +
                 "FROM receipts r " +
                 "LEFT JOIN patients p ON r.id_patient = p.id_patient " +
                 "LEFT JOIN medicines m ON r.id_medicine = m.id_medicine";
    
    String[] columnNames = {"id_receipt" ,"id_medicine","name_patient", "name_medicine", 
        "medicine_quantity","medicine_price","service_name"
            ,"service_price","payment_date","total_amount","payment_status","stok"};

    var dataList = model.getAllData(query, columnNames);
    DefaultTableModel tableModel = (DefaultTableModel) view.getTable().getModel();

    tableModel.setRowCount(0); // Clear existing rows
    int no = 1;

    for (Object[] rowData : dataList) {
        tableModel.addRow(new Object[]{
            rowData[0],      // ID column
            rowData[1], 
            no++,     
            rowData[2],      
            rowData[3],       
            rowData[4],
            rowData[5],
            rowData[6],
            rowData[7],
            rowData[8],
            rowData[9],
            rowData[10],
            rowData[11],
        });
    }    }
    

    public boolean insertData(){
        
            comboBoxHelper patient = (comboBoxHelper) view.getPatient().getSelectedItem();
            comboBoxHelper med = (comboBoxHelper) view.getMed().getSelectedItem();         
            String service = view.getService().getText();
            String status = (String) view.getStatus().getSelectedItem();
            
        int MedQ;  
        try {
        MedQ = Integer.parseInt(view.getMedQ().getText());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(view, "Medicine quantity must be a valid number!");
        return false;
    } 
        BigDecimal sp;
    try {
        sp = new BigDecimal(view.getSprice().getText());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(view, "Service price must be a valid number!");
        return false;
    }
         
    if (patient == null || med == null) {
        JOptionPane.showMessageDialog(view, "Please select both a doctor and a patient!");
        return false;
    }
    int patientId = Integer.parseInt(patient.getId());
    int medId = Integer.parseInt(med.getId());
     
     int stokMed =currentStok(medId);
      if (MedQ >  stokMed) {
        JOptionPane.showMessageDialog(view, "Insufficient stock! Only " + stokMed + " items available.");
        return false;
    }
    
            Object[] values = {medId, patientId, MedQ, service,sp,status};
        
            if(MedQ <= 0 || service.isEmpty() || sp.compareTo(BigDecimal.ZERO) <= 0){
            JOptionPane.showMessageDialog(view, "Any input Field CANNOT BE EMPTY!");
            return false;
            }
                
                Object[] stok = {MedQ,medId};
                
            boolean Tstok = model.updateData("UPDATE medicines set stok = stok - ? "
                    + "WHERE id_medicine = ?", stok);
            
            boolean success = model.insertData("INSERT INTO "
                    + "receipts (id_medicine,id_patient,medicine_quantity,"
                    + "service_name,service_price,payment_status) VALUES (?, ?,?,?,?,?)",values);
           
            if (success && Tstok) {
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
            String patient = (String) view.getTable().getValueAt(selectedRow, 3);
            String med = (String) view.getTable().getValueAt(selectedRow, 4);
//            Combo box Med
             for (int i = 0; i < view.getMed().getItemCount(); i++) {
            comboBoxHelper item = (comboBoxHelper) view.getMed().getItemAt(i);
            if (item.toString().equals(med)) { // Match the name
                view.getMed().setSelectedItem(item);
                break;
            }
        }
        // Combo box patient 
        for (int i = 0; i < view.getPatient().getItemCount(); i++) {
            comboBoxHelper item = (comboBoxHelper) view.getPatient().getItemAt(i);
            if (item.toString().equals(patient)) { // Match the name
                view.getPatient().setSelectedItem(item);
                break;
            }
        }
            int medq = (int)view.getTable().getValueAt(selectedRow, 5);
            String service = (String) view.getTable().getValueAt(selectedRow, 7);
            BigDecimal sPrice = (BigDecimal) view.getTable().getValueAt(selectedRow, 8);
            String status = (String) view.getTable().getValueAt(selectedRow, 11);
            view.getMedQ().setText(String.valueOf(medq));
            view.getService().setText(service);
            view.getSprice().setText(String.valueOf(sPrice));
            view.getStatus().setSelectedItem(status);
            
            isUpdating = true;
            
            view.getButtonUpdate().setText("Save");
            enableUpdateMode(true);
        } else {
          
            updateData();
            cancelUpdateMode();
        }
      }
      
      private int currentStok(int id){
       int stok = 0;
    String query = "SELECT stok FROM medicines WHERE id_medicine = "+id;
    String[] stokId = {"stok"};  // Convert id to String as an array for query
    List<Object[]> data = model.getAllData(query, stokId);  // Get result from the model
    
    // Extract the stock value from the result list
    if (!data.isEmpty()) {
        // The stock value is the first element in the array returned by the query
        stok = (int) data.get(0)[0];
    }
    return stok;
      }
      
      
  
//      UPDATE DATA
          private boolean updateData() {
            int selectedRow = view.getTable().getSelectedRow();
            int id = Integer.parseInt(view.getTable().getValueAt(selectedRow, 0).toString());
//            old stok
        int quan = Integer.parseInt(view.getTable().getValueAt(selectedRow, 5).toString()); 
        int idstok = Integer.parseInt(view.getTable().getValueAt(selectedRow, 1).toString()); 
//       for stok compariosn to quantity
            comboBoxHelper patient = (comboBoxHelper) view.getPatient().getSelectedItem();
            comboBoxHelper med = (comboBoxHelper) view.getMed().getSelectedItem();
//            has to be number
            int MedQ;
    try {
        MedQ = Integer.parseInt(view.getMedQ().getText());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(view, "Medicine quantity must be a valid number!");
        return false;
    }
            String service = view.getService().getText();
            String status = (String) view.getStatus().getSelectedItem();
             BigDecimal sp;
    try {
        sp = new BigDecimal(view.getSprice().getText());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(view, "Service price must be a valid number!");
        return false;
    }
    if (patient == null || med == null) {
        JOptionPane.showMessageDialog(view, "Please select both a doctor and a patient!");
        return false;
    }
 
    // for the love of god please dont error
    int patientId = Integer.parseInt(patient.getId());
    int medId = Integer.parseInt(med.getId());   
    //    Validate STOK
    int stok =currentStok(medId);
      if (MedQ >  stok) {
        JOptionPane.showMessageDialog(view, "Insufficient stock! Only " + stok + " items available.");
        return false;
    }
            Object[] values = {medId, patientId, MedQ, service, sp, status, id};
            
            Object[] oldStok = {quan,idstok};
            
            Object[] newStok = {MedQ,medId};
        
            if(MedQ <= 0 || service.isEmpty() || sp.compareTo(BigDecimal.ZERO) <= 0){
            JOptionPane.showMessageDialog(view, "Any input Field CANNOT BE EMPTY!");
            return false;
            }
              int confirm = JOptionPane.showConfirmDialog(
            view,
            "This action will revert the previous receipt and put the stok back ",
            "Continue?",
            JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
//            revert the old stok
                boolean revert = model.updateData(
                "UPDATE medicines set stok = stok + ? WHERE id_medicine = ?",oldStok);
//             use the new stok
                boolean takeStok = model.updateData(
                "UPDATE medicines set stok = stok - ? WHERE id_medicine = ?",newStok);
//              update run
             boolean success = model.updateData(
            "UPDATE receipts SET id_medicine = ?, id_patient = ?, "
                    + "medicine_quantity = ?, service_name = ?, service_price = ?, "
                    + "payment_status = ? WHERE id_receipt = ?",
            values
        );
            if (success && revert && takeStok) {
                JOptionPane.showMessageDialog(view, "Data Updated successfully.");
                displayData(); // Refresh the table
            } else {
                JOptionPane.showMessageDialog(view, "Failed to Update the data.");
            }
        }
        return true;
    }
           private void cancelUpdateMode() {
        // Reset the state
        isUpdating = false;
        view.getButtonUpdate().setText("Update");
        enableUpdateMode(isUpdating);
         view.getMedQ().setText("");
         view.getPatient().setSelectedItem(0);
       view.getService().setText("");
       view.getMed().setSelectedItem(0);
       view.getStatus().setSelectedItem(0);
       view.getSprice().setText("");     
    }

    private void enableUpdateMode(boolean enable) {
        view.getButtonUpdate().setEnabled(true);
        view.getButtonCancel().setEnabled(true);
        view.getButtonAdd().setEnabled(!enable);
        view.getButtonDelete().setEnabled(!enable);
        view.getTable().setEnabled(!enable); // Disable table in update mode
        
         view.getStatus().setEnabled(enable);
         view.getService().setEnabled(enable);
        view.getPatient().setEnabled(enable);
        view.getMed().setEnabled(enable);
        view.getMedQ().setEnabled(enable);
        view.getSprice().setEnabled(enable);
    }
    
    
    private void deleteData() {
    // Get the selected row
    int selectedRow = view.getTable().getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(view, "Please select any row to delete.");
        return;
    }
    try {
        // Retrieve values from the selected row
        int id = Integer.parseInt(view.getTable().getValueAt(selectedRow, 0).toString()); // Assuming ID is in column 0
        String nama = view.getTable().getValueAt(selectedRow, 3).toString(); // Assuming Name is in column 2
        int stok = Integer.parseInt(view.getTable().getValueAt(selectedRow, 5).toString()); // Assuming Stock is in column 5
        int idstok = Integer.parseInt(view.getTable().getValueAt(selectedRow, 1).toString()); // Assuming Medicine ID is in column 1
        System.out.println("stok: " + stok);
        System.out.println("id med: " + idstok);
        // Confirm the deletion
        int confirm = JOptionPane.showConfirmDialog(
            view,
            "Are you sure you want to revert this receipt by patient's name ?: " + nama,
            "Delete Confirmation",
            JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            // Revert stock and delete receipt
            boolean revertStok = model.updateData(
                "UPDATE medicines SET stok = stok + ? WHERE id_medicine = ?",new Object[]{stok, idstok});
            boolean success = model.deleteData(
                "DELETE FROM receipts WHERE id_receipt = ?",id);
            if (revertStok && success) {
                JOptionPane.showMessageDialog(view, "Data deleted successfully.");
                displayData(); // Refresh the table
            } else {
                JOptionPane.showMessageDialog(view, "Failed to delete the data.");
            }}
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(view, "Invalid data format in the selected row.");
        e.printStackTrace();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(view, "An error occurred: " + e.getMessage());
        e.printStackTrace();
    }
}

//    OLD
//    private void deleteData() {
//    // Get the selected row
//    int selectedRow = view.getTable().getSelectedRow();
//    if (selectedRow == -1) {
//        JOptionPane.showMessageDialog(view, "Please select any row to delete.");
//        return;
//    }
//
//    // Get the ID of the selected student
//    int id = (int) view.getTable().getValueAt(selectedRow, 0); // Assuming ID is in column 0
//    String nama = (String) view.getTable().getValueAt(selectedRow, 2);
////    int stok = Integer.parseInt(view.getTable().getValueAt(selectedRow, 5));
////int idstok = Integer.parseInt(view.getTable().getValueAt(selectedRow, 1));
// int stok = (int)view.getTable().getValueAt(selectedRow, 5);
//int idstok = (int) view.getTable().getValueAt(selectedRow, 1);
//    System.out.println("stok:" + stok);
//    System.out.println("id med:" + idstok);
//
//    
////    int stokInt = Integer.parseInt(stok);
////    int idInt = Integer.parseInt(idstok);
//    
//    
//
//    // Confirm the deletion
//    int confirm = JOptionPane.showConfirmDialog(
//        view,
//        "Are you sure you want to revert this receipt by patient's name ?: "+nama+"",
//        "Delete Confirmation",
//        JOptionPane.YES_NO_OPTION
//    );
//
//    if (confirm == JOptionPane.YES_OPTION) {
//        boolean revertStok = model.updateData("UPDATE medicines set stok = stok + ? WHERE id_medicine = ?", new Object[]{stok,idstok});
//        boolean success = model.deleteData("DELETE FROM receipts WHERE id_receipt = ?",id);
//        if (success && revertStok) {
//            JOptionPane.showMessageDialog(view, "Data deleted successfully.");
//            displayData(); // Refresh the table
//        } else {
//            JOptionPane.showMessageDialog(view, "Failed to delete the data.");
//        }
//    }
//}
   //end old 
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
    private koneksi k;
     private void populatePatientComboBox() {
         comboBoxDao dao = new comboBoxDao(k);
        List<Object[]> data_patient = dao.getAllData(
        "SELECT name_patient, id_patient FROM patients",
        new String[] { "name_patient", "id_patient" }
    );
    view.getPatient().removeAllItems(); // Clear existing items in the combo box
    // Iterate over the retrieved data and format it
    for (Object[] row : data_patient) {
        String name = (String) row[0];
        String id = String.valueOf(row[1]);
        view.getPatient().addItem(new comboBoxHelper(id,name));
    }
     }
     private void populateMedComboBox() {
         comboBoxDao dao = new comboBoxDao(k);
        List<Object[]> data_patient = dao.getAllData(
        "SELECT name_medicine, id_medicine FROM medicines",
        new String[] { "name_medicine", "id_medicine" }
    );
    view.getMed().removeAllItems(); // Clear existing items in the combo box
    // Iterate over the retrieved data and format it
    for (Object[] row : data_patient) {
        String name = (String) row[0];
        String id = String.valueOf(row[1]);
        view.getMed().addItem(new comboBoxHelper(id,name));
    }
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
        view.dispose();}}
