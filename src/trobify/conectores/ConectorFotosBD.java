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
import trobify.logica.Fotografia;

/**
 *
 * @author davido747
 */
public class ConectorFotosBD extends Conector {
    public static Conexion con = Conexion.crearConexion();
    
   
    public static void añadirFotografia (Fotografia f) {
       String sql = "INSERT INTO fotografia (id, id_vivienda) VALUES ('"+f.getId()+"', '"+f.getId_vivienda()+"')";
        consultaVoid(sql, con);
    }
    
    
      public static Fotografia getFotografia(String id) {
        try {
            Statement stm = con.getConnection().createStatement();
            ResultSet rsl = stm.executeQuery("SELECT * FROM fotografia WHERE id_vivienda = '" + id + "'");
            if (rsl.first()) {
                Fotografia foto = new Fotografia(rsl.getNString("id"), rsl.getNString("id_vivienda"));
                return foto;
            }

        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
      
      public static String consultarFotoViviendaPorId(String id) {
        Fotografia foto = getFotografia(id);
     if (foto.getId() != null) {
            return foto.getId();
        } else {
            return "C:\\Users\\gabri\\Desktop\\gabri\\SoftUpv\\src\\trobify\\images\\foto0.jpeg";
        }
    }
      
    public static ArrayList<Fotografia> getListaFotos(String id){
         ArrayList<Fotografia> listaFotos = new ArrayList();
        try {
            Statement stm = con.getConnection().createStatement();
            ResultSet rsl = stm.executeQuery("SELECT id FROM fotografia WHERE id_vivienda = '" + id + "'");
            if (rsl.isBeforeFirst()) {
                
                while (rsl.next()) {
                    listaFotos.add(new Fotografia(rsl.getString("id"), id));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaFotos;
    }
    
      public static void eliminarFoto(String rutaFoto, String idVivienda){
        String cadena = rutaFoto;
        String[] parts = cadena.split("\\\\");
        String direccion = parts[parts.length - 1];
        //System.out.println(direccion);
        
       String sql = "DELETE FROM fotografia WHERE id LIKE '%" + direccion + "'";
       consultaVoid(sql, con);
    }
      
       public static void añadirFotografia (String idFoto, String idVivienda){
        String sql = "INSERT INTO `fotografia`(`id`, `id_vivienda`) VALUES ('" + idFoto + "','" + idVivienda + "')";
        consultaVoid(sql, con);
    }
   
}
