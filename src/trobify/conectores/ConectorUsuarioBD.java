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
import java.util.ArrayList;
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
        Plantilla.consultaVoid(sql);
    } //fin añadir usuario. 

    public static Usuario getUsuario(String id) {
         String sql = "select * from usuario where id = '" + id + " '";
        try {
            ResultSet rsl = Plantilla.soloConsulta(sql);
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
        return Plantilla.consultaBoolean(rs);
    }

    @Override
    String prepararConsulta() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    void hacerConsulta() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    void utilizarConsulta(ResultSet rs) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    String devolverString() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    int devolverInt() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    ArrayList<String> devolverLista() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
