/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify.logica;

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
public class conectorBD {
    public static void añadirUsuario(Usuario u){
        Conectar con = new Conectar();
        try {
         Statement stm = con.getConnection().createStatement();
                stm.executeUpdate("INSERT INTO `usuario`(`id`, `dni`, `password`, `nombre`, `apellidos`, `email`) VALUES ('"
                        + u.getId() + "','" + u.getDni() + "','" + u.getPassword() + "','" + u.getNombre() + "','" + 
                        u.getApellidos() + "','" + u.getEmail() + "')");   
               
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
    } //fin añadir usuario
    
    
}
