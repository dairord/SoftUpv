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
import trobify.controlador.InicioController;
import trobify.logica.Usuario;

/**
 *
 * @author davido747
 */
public class ConectorUsuarioBD extends Conector {
    
    public static Conexion con = Conexion.crearConexion();

    public static void añadirUsuario(Usuario u) {
        String sql = "INSERT INTO `usuario`(`id`, `dni`, `password`, `nombre`, `apellidos`, `email`) VALUES ('"
                + u.getId() + "','" + u.getDni() + "','" + u.getPassword() + "','" + u.getNombre() + "','"
                + u.getApellidos() + "','" + u.getEmail() + "')";
        consultaVoid(sql, con);
    } //fin añadir usuario. 

    public static Usuario getUsuario(String id) {
        try {
            Statement stm = con.getConnection().createStatement();
            ResultSet rsl = stm.executeQuery("select * from usuario where id = '" + id + " '");
            if (rsl.first()) {
                return new Usuario(rsl.getString("id"), rsl.getString("dni"),
                        rsl.getString("password"), rsl.getString("nombre"), rsl.getString("apellidos"),
                        rsl.getString("email"), rsl.getString("foto"), rsl.getString("preferencia"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new Usuario();
    }

    
    public static ArrayList<String> getUsariosPorPreferencia(String preferencia) throws SQLException {
        ArrayList<String> res = new ArrayList<String>();
        try {            
            Statement stm = con.getConnection().createStatement();
            ResultSet rsl = stm.executeQuery("select id from usuario where preferencia = '" + preferencia + "'");
            if (rsl.isBeforeFirst()) {
                while (rsl.next()) {
                    String usuario = rsl.getString("id");
                    res.add(usuario);
                }
            }return res;
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }
    
    public static void actualizarPreferenciaDeUsuario(String preferencia, String id){
        try {            
            Statement stm = con.getConnection().createStatement();
            stm.executeUpdate("UPDATE `usuario` SET `preferencia`='" + preferencia + "' WHERE id = '" + id + "'");            
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
