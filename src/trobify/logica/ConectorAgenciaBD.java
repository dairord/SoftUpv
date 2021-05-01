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
public class ConectorAgenciaBD {
    private static Conectar con = new Conectar();
   
    public static boolean contraseñaCorrecta(String codigo, String contra){
         try {
           Statement stm = con.getConnection().createStatement();
             ResultSet rs = stm.executeQuery ("select codigo from agencia where codigo = '"
             + codigo + " ' and contraseña = '" + contra + " '");
            if (rs.first())   {
                System.out.println("funciona");
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
   public static void guardarAgente(Agente a){
       try {
         Statement stm = con.getConnection().createStatement();
                stm.executeUpdate("INSERT INTO `agente`(`username`, `agencia`) VALUES ('"
                        + a.getUsername() +"','" + a.getAgencia() + "')");   
         
         } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
   }
}
