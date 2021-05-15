/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify.conectores;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import trobify.Conectar;
import trobify.controlador.InicioController;

/**
 *
 * @author gabri
 */
abstract class Conector {
   
  private static Conectar con = new Conectar();
  
  abstract String prepararConsulta();
  abstract void hacerConsulta();
  abstract void utilizarConsulta(ResultSet rs);
  abstract String devolverString();
  abstract int devolverInt();
  abstract ArrayList<String> devolverLista();
  
  public final void consulta(){
      String sql = prepararConsulta();
      ResultSet rs = soloConsulta(sql);
      utilizarConsulta(rs);
      devolverBoolean(rs);
  }


 public static ResultSet soloConsulta (String sql){
         try {
            Statement stm = con.getConnection().createStatement();
           ResultSet rs = stm.executeQuery(sql);
            return rs;
     }  catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
     return null;
}
 
 public static Boolean devolverBoolean(ResultSet rs){
  try {
            if (rs.first()) {
                return true;
            }
 } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
 return false;
 }
 
}//fin clase