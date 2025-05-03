/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import java.math.BigDecimal;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import model.modelData;
import view.Dashboard;
import view.formMed;

public class medController implements BaseController{
    private static medController instance;
     private modelData model;
    private formMed view;
    private dashboardController dController;
    
    private boolean isAdding = false;
     private boolean isUpdating= false;
     private int selectedId = -1;
     
 public static synchronized medController getInstance(modelData model, formMed view) {
        if (instance == null) {
            instance = new medController(model, view);
        }
        return instance;
    }
 
    public medController(modelData model, formMed view) {
        this.model = model;
        this.view = view;
        
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
        view.getMed().setEnabled(enable);
        view.getStok().setEnabled(enable);
        view.getDesc().setEnabled(enable);
        view.getPrice().setEnabled(enable);
        view.getButtonUpdate().setEnabled(!enable);
        view.getButtonDelete().setEnabled(!enable);
        view.getTable().setEnabled(!enable); // Disable table
    }
      
      private void cancelAddMode() {
        // Reset DA stateEEEEEEEEEEEEEEEEEEEEE
        isAdding = false;
       view.getMed().setText("");
       view.getStok().setText(null);
       view.getDesc().setText("");
       view.getPrice().setText("");
        view.getButtonAdd().setText("Add");
         view.getButtonAdd().setEnabled(true);
         view.getButtonCancel().setEnabled(false);
        enableAddMode(false);
    }

    public void displayData() {
    String query = "SELECT * FROM medicines";
    String[] columnNames = {"id_medicine", "name_medicine", "stok", "description","price"};

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
    public boolean isMedExist(String med){
        String query = "SELECT * FROM medicines WHERE name_medicine = ?";
        return model.unique(med, "name_medicine", query);
        
    }
    public boolean insertData(){
            String med = view.getMed().getText();
            String stok = view.getStok().getText();
            String desc = view.getDesc().getText();
            String price = view.getPrice().getText();     
            int stokValue;
    try {
        stokValue = Integer.parseInt(stok);
        if (stokValue < 0) {
            JOptionPane.showMessageDialog(view, "Stok must be a positive number!");
            return false;
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(view, "Stok must be a valid number!");
        return false;
    }
    // Validate price as a number
    double priceValue;
    try {
        priceValue = Double.parseDouble(price);
        if (priceValue < 0) {
            JOptionPane.showMessageDialog(view, "Price must be a positive number!");
            return false;
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(view, "Price must be a valid number!");
        return false;
    }
            Object[] values = {med, stokValue, desc, priceValue};       
            if(med.isEmpty() || stok.isEmpty() || desc.isEmpty() || price.isEmpty()){
            JOptionPane.showMessageDialog(view, "Any input Field CANNOT BE EMPTY!");
            return false;
            }    
         if (isMedExist(med)) {
        JOptionPane.showMessageDialog(view, "this Medicine already exists!");
        return false;
    }
            boolean success = model.insertData("INSERT INTO medicines (name_medicine, "
                    + "stok, description,price) VALUES (?, ?,?,?)",values);
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
            String med = (String) view.getTable().getValueAt(selectedRow, 2);

            int stok = (int) view.getTable().getValueAt(selectedRow, 3);
            String stoks = Integer.toString(stok);
            String desc = (String) view.getTable().getValueAt(selectedRow, 4);
            BigDecimal price = (BigDecimal) view.getTable().getValueAt(selectedRow, 5);
            String prices = price.toPlainString();
            
            view.getMed().setText(med);
             view.getStok().setText(stoks);
            view.getDesc().setText(desc);
            view.getPrice().setText(prices);

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
              String oldMed = (String) view.getTable().getValueAt(selectedRow, 2);
        
          String med = view.getMed().getText();
          String stok = view.getStok().getText();

            String desc = view.getDesc().getText();
            String price = view.getPrice().getText();
            
            int stokValue;
    try {
        stokValue = Integer.parseInt(stok);
        if (stokValue < 0) {
            JOptionPane.showMessageDialog(view, "Stok must be a positive number!");
            return false;
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(view, "Stok must be a valid number!");
        return false;
    }
    // Validate price as a number
    double priceValue;
    try {
        priceValue = Double.parseDouble(price);
        if (priceValue < 0) {
            JOptionPane.showMessageDialog(view, "Price must be a positive number!");
            return false;
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(view, "Price must be a valid number!");
        return false;
    }
               Object[] values = {med, stokValue, desc, priceValue, selectedId};
               
        if(med.isEmpty() || stok.isEmpty() || desc.isEmpty() || price.isEmpty()){
            JOptionPane.showMessageDialog(view, "Any input Field CANNOT BE EMPTY!");
            return false;
            }
//        validation
        if(!oldMed.equalsIgnoreCase(med)){
           if (isMedExist(med)) {
        JOptionPane.showMessageDialog(view, "this Medicine already exists!");
        return false;
            }}
 
        boolean success = model.updateData("UPDATE medicines SET name_medicine= ?, "
                + "stok= ?, description = ?,price = ? WHERE id_medicine= ?",values);
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
        
          view.getMed().setText("");
             view.getStok().setText("");
            view.getDesc().setText("");
            view.getPrice().setText("");

      
    }
    private void enableUpdateMode(boolean enable) {
        view.getButtonUpdate().setEnabled(true);
        view.getButtonCancel().setEnabled(true);
        view.getButtonAdd().setEnabled(!enable);
        view.getButtonDelete().setEnabled(!enable);
        view.getTable().setEnabled(!enable); // Disable table in update mode
        view.getMed().setEnabled(enable);
           view.getStok().setEnabled(enable);
           view.getDesc().setEnabled(enable);
          view.getPrice().setEnabled(enable);
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
        "Are you sure you want to delete this Medicine with Name: "+nama+"",
        "Delete Confirmation",
        JOptionPane.YES_NO_OPTION
    );
    if (confirm == JOptionPane.YES_OPTION) {
        boolean success = model.deleteData("DELETE FROM medicines WHERE id_medicine= ?",id);
        if (success) {
            JOptionPane.showMessageDialog(view, "Data deleted successfully.");
            displayData(); // Refresh the table
        } else {
            JOptionPane.showMessageDialog(view, "Failed to delete the data.");
        }}}
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
