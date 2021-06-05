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
import trobify.logica.Agente;

/**
 *
 * @author gabri
 */
public class ConectorAgenciaBD extends Conector{
   public static Conexion con = Conexion.crearConexion();
   
    public static boolean contraseñaDeAgenciaCorrecta(String codigo, String contra){
         String sql = "select codigo from agencia where codigo = '"
             + codigo + " ' and contraseña = '" + contra + " '";
         return consultaBoolean(sql, con);
    }
    
   public static void guardarAgente(Agente a){
      String sql = "INSERT INTO `agente`(`username`, `agencia`) VALUES ('"
                        + a.getUsername() +"','" + a.getAgencia() + "')";
       consultaVoid(sql, con);
   }
   
   public static String getAgenciaDelAgente(String id_usuario){
       try {
            Statement stm = con.getConnection().createStatement();
            ResultSet rsl = stm.executeQuery("select agencia from agente where username = '" + id_usuario + " '");
            if (rsl.first()) {
                return rsl.getString("agencia");
            }

        } catch (SQLException ex) {
            return "";
        }

        return "";
   }
}
