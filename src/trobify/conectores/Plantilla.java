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
public class Plantilla {
    
    private static Conectar con = new Conectar();
    
    public static boolean consultaBoolean(String sql){
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
    } //fin consulta boolean
    
     public static void consultaVoid(String sql){
         try {
            Statement stm = con.getConnection().createStatement();
            stm.executeUpdate(sql);

        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
    } //fin consulta void
     
     public static ArrayList<String> consultaLista(String sql){
         ArrayList<String> lista = new ArrayList();
        try {
            Statement stm = con.getConnection().createStatement();
            ResultSet rsl = stm.executeQuery(sql);
            if (rsl.isBeforeFirst()) {
                
                while (rsl.next()) {
                    lista.add(rsl.getString("id"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
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
}
