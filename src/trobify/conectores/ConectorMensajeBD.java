/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify.conectores;

import java.sql.Date;
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
public class ConectorMensajeBD extends Conector{
    
    public static Conexion con = Conexion.crearConexion();
    
   public static void añadirMensaje(Mensaje m) {
      String sql = "INSERT INTO `mensajes`(`id`, `cuerpo`) VALUES ('"
                    + m.getId() + "','" + m.getCuerpo() + "')";
      consultaVoid(sql, con);
    }
    
}
