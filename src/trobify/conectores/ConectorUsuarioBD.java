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
import trobify.logica.Usuario;

/**
 *
 * @author davido747
 */
public class ConectorUsuarioBD extends Conector{

     public static void añadirUsuario(Usuario u) {
       String sql = "INSERT INTO `usuario`(`id`, `dni`, `password`, `nombre`, `apellidos`, `email`) VALUES ('"
                    + u.getId() + "','" + u.getDni() + "','" + u.getPassword() + "','" + u.getNombre() + "','"
                    + u.getApellidos() + "','" + u.getEmail() + "')";
         consultaVoid(sql);
    } //fin añadir usuario. 

    public static Usuario getUsuario(String id) {
         try {
            Statement stm = con.getConnection().createStatement();
            ResultSet rsl = stm.executeQuery("select * from usuario where id = '" + id + " '");
            if (rsl.first()) {
                return new Usuario(rsl.getString("id"), rsl.getString("dni"),
                        rsl.getString("password"), rsl.getString("nombre"), rsl.getString("apellidos"),
                        rsl.getString("email"), rsl.getString("foto"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new Usuario();
    }

    public static boolean usuarioCorrecto(String id, String contra) {
        String rs = "select id from usuario where id = '" + id + " ' and password = '" + contra + " '";
        return consultaBoolean(rs);
    }
    
   
}
