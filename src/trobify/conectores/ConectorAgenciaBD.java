/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify.conectores;

import trobify.Conectar;
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
public class ConectorAgenciaBD {
    private static Conectar con = new Conectar();
   
    public static boolean contraseñaCorrecta(String codigo, String contra){
        String sql = "select codigo from agencia where codigo = '"
             + codigo + " ' and contraseña = '" + contra + " '";
        return Plantilla.consultaBoolean(sql);
    }
    
   public static void guardarAgente(Agente a){
      String sql = "INSERT INTO `agente`(`username`, `agencia`) VALUES ('"
                        + a.getUsername() +"','" + a.getAgencia() + "')";
      Plantilla.consultaVoid(sql);
              
   }
}
