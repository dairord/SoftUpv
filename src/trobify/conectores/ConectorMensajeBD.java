/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify.conectores;

import java.sql.Date;
import trobify.Conectar;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import trobify.controlador.InicioController;
import trobify.logica.Mensaje;

/**
 *
 * @author jagon
 */
public class ConectorMensajeBD {
    
    public static Conectar con = Conectar.conexion();
    
    public static void añadirMensaje(Mensaje m) {
              
        try {
            Statement stm = con.getConnection().createStatement();
            stm.executeUpdate("INSERT INTO `mensajes`(`id`, `cuerpo`) VALUES ('"
                    + m.getId() + "','" + m.getCuerpo() + "')");

        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
