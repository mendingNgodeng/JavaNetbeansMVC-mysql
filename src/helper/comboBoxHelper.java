/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package helper;

/**
 *
 * @author Userr
 */
public class comboBoxHelper {
      private final String id;
    private final String name;

    public comboBoxHelper(String id, String name) {
        this.id = id;
        this.name = name;
    }

    
    public String toString() {
        return name; // This will be displayed in the comboBox
    }

    public String getId() {
        return id;
    }
    
    
}
