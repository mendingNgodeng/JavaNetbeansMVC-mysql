/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author UMUKA
 */
public class loginDao {

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the rp
     */
    public String getLevel() {
        return level;
    }

    /**
     * @param rp the rp to set
     */
    public void setLevel(String level) {
        this.level = level;
    }
    
  public static synchronized loginDao getInstance() {
    if (instance == null) {
        instance = new loginDao();
    }
    return instance;
}
     
      public void reset() {
        username = null;
        level = null;
    }
    
    private static loginDao instance;
    private String username;
    private String password;
    private String level;


}
