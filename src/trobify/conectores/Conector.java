/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify.conectores;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import trobify.controlador.InicioController;

/**
 *
 * @author gabri
 */
abstract class Conector {
  
   
    
    final public static boolean consultaBoolean(String sql, Conexion con ){
        try {
            Statement stm = con.getConnection().createStatement();
            ResultSet rs = stm.executeQuery(sql);
            if (rs.first()) {
                return true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }
    
    final public static void consultaVoid(String sql, Conexion con){
         try {
            Statement stm = con.getConnection().createStatement();
            stm.executeUpdate(sql);

        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
  //  final public static E objetoAPartirDeId(){
  //     return null;
  //  }
    
  
}//fin clase