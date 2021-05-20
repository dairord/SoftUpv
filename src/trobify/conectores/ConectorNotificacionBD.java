/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify.conectores;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import trobify.controlador.InicioController;
import trobify.logica.Notificacion;

/**
 *
 * @author dairo
 */
public class ConectorNotificacionBD extends Conector{

    public static Notificacion getNotificacion(int id) {
        try {
            Statement stm = con.getConnection().createStatement();
            ResultSet rsl = stm.executeQuery("select * from notificaciones where id = '" + id + " '");
            if (rsl.first()) {
                return new Notificacion(rsl.getInt("id"), rsl.getString("id_vivi"),
                        rsl.getString("id_usuario"), rsl.getString("id_usuario_destino"), rsl.getString("descripción"), rsl.getDate("last_mod"),
                        rsl.getInt("estado"), rsl.getInt("tipo"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new Notificacion();
    }

    public static ArrayList<Notificacion> getNotificacionPorVivienda(String id_vivienda) {
        ArrayList<Notificacion> res = new ArrayList<Notificacion>();

        try {
            Statement stm = con.getConnection().createStatement();
            ResultSet rsl = stm.executeQuery("select * from notificaciones where id_vivi = '" + id_vivienda + " '");
            if (rsl.isBeforeFirst()) {
                while (rsl.next()) {
                    res.add(new Notificacion(rsl.getInt("id"), rsl.getString("id_vivi"),
                            rsl.getString("id_usuario"), rsl.getString("id_usuario_destino"), rsl.getString("descripción"), rsl.getDate("last_mod"),
                            rsl.getInt("estado"), rsl.getInt("tipo")));
                }

            }

        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;
    }

    public static ArrayList<Notificacion> getNotificacionPorUsuario(String id_usuario_dest) {
        ArrayList<Notificacion> res = new ArrayList<Notificacion>();

        try {
            Statement stm = con.getConnection().createStatement();
            ResultSet rsl = stm.executeQuery("select * from notificaciones where id_usuario_destino = '" + id_usuario_dest + " '");
            if (rsl.isBeforeFirst()) {
                while (rsl.next()) {
                    res.add(new Notificacion(rsl.getInt("id"), rsl.getString("id_vivi"),
                            rsl.getString("id_usuario"), rsl.getString("id_usuario_destino"), rsl.getString("descripción"), rsl.getDate("last_mod"),
                            rsl.getInt("estado"), rsl.getInt("tipo")));
                }

            }

        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;
    }

    public static void añadirNotificacion(Notificacion n) {
      String sql = "INSERT INTO `notificaciones`(`id`, `id_vivi`, `id_usuario`, `descripción`, `last_mod`, `estado`, `tipo`) VALUES ('"
                    + n.getId() + "','" + n.getId_vivienda() + "','" + n.getId_usuario() + "','" + n.getDesc() + "','"
                    + n.getLast_mod() + "','" + n.getEstado() + "','" + n.getTipo() + "')";
      consultaVoid(sql);
    }
    
    public static void añadirNotificacionNoID(Notificacion n) {
       String sql = "INSERT INTO `notificaciones`(`id_vivi`, `id_usuario`, `descripción`, `last_mod`, `estado`, `tipo`, `id_usuario_destino`) VALUES ('"
                     + n.getId_vivienda() + "','" + n.getId_usuario() + "','" + n.getDesc() + "','"
                    + n.getLast_mod() + "','" + n.getEstado() + "','" + n.getTipo() + "','" +n.getId_usuario_dest() + "')";
       consultaVoid(sql);
    }

    public static void borrarNotificacion(Notificacion n) {
        String sql = "DELETE FROM notificaciones WHERE id = '" + n.getId() + "'";
        consultaVoid(sql);
    }
    
    public static void borrarNotificacionesEnMasa(String id_vivienda, int tipo) {
        String sql = "DELETE FROM notificaciones WHERE id_vivi = '" + id_vivienda + "' AND tipo = '" +tipo +"'";
        consultaVoid(sql);
    }

}
