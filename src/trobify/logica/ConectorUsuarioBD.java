/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify.logica;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import trobify.Conectar;
import trobify.controlador.InicioController;

/**
 *
 * @author gabri
 */
public class ConectorUsuarioBD {
   private static Conectar con = new Conectar();
    
   public static void añadirUsuario(Usuario u){
        try {
         Statement stm = con.getConnection().createStatement();
                stm.executeUpdate("INSERT INTO `usuario`(`id`, `dni`, `password`, `nombre`, `apellidos`, `email`) VALUES ('"
                        + u.getId() + "','" + u.getDni() + "','" + u.getPassword() + "','" + u.getNombre() + "','" + 
                        u.getApellidos() + "','" + u.getEmail() + "')");   
               
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
    } //fin añadir usuario
    
    public static boolean usuarioRepetido (String id){
        try {
            Statement stm = con.getConnection().createStatement();
             ResultSet rs = stm.executeQuery ("select id from usuario where id = '"
             + id + " '");
            if (rs.first())   {
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
    public static boolean usuarioCorrecto (String id, String contra){
         
        try {
           Statement stm = con.getConnection().createStatement();
             ResultSet rs = stm.executeQuery ("select id from usuario where id = '"
             + id + " ' and password = '" + contra + " '");
            if (rs.first())   {
                return true;
            } 
            
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
