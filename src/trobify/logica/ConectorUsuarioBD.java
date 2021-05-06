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
 * @author davido747
 */
public class ConectorUsuarioBD {

    private static Conectar con = new Conectar();

    public static boolean consultaBoolean(String sql) {
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

    public static void añadirUsuario(Usuario u) {
        try {
            Statement stm = con.getConnection().createStatement();
            stm.executeUpdate("INSERT INTO `usuario`(`id`, `dni`, `password`, `nombre`, `apellidos`, `email`) VALUES ('"
                    + u.getId() + "','" + u.getDni() + "','" + u.getPassword() + "','" + u.getNombre() + "','"
                    + u.getApellidos() + "','" + u.getEmail() + "')");

        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
