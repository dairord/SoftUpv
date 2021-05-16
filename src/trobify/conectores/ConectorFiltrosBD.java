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
import trobify.Conectar;
import trobify.controlador.InicioController;
import trobify.logica.Filtros;

/**
 *
 * @author gabri
 */
public class ConectorFiltrosBD {
    public static Conectar con = Conectar.conexion();
    
   
    public static void insertarFiltrosSinFecha(Filtros f){
            try{
            Statement s1 = con.getConnection().createStatement();
            s1.executeUpdate("INSERT INTO filtros(id, ciudad, tipo, p_min, p_max, habitaciones, baños, ventaAlquiler) "
                    + "VALUES('"+ f.getId() + "', '"+ f.getCiudad() +"', '"+ f.getTipo() +"', '"+ f.getP_min() +"'"
                    + ", '"+ f.getP_max() +"', '"+ f.getHabitaciones() +"',"
                    + " '"+ f.getBaños() +"', '"+ f.getVentaAlquiler() +"')");
            //System.out.println("filtros guardados con exito");
        }catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }//fin catch )
    }
    
    public static void insertarFiltrosConFechas(Filtros f) {
        Statement s1;
        try{
            s1 = con.getConnection().createStatement();
            s1.executeUpdate("INSERT INTO filtros(id, fecha_entrada, fecha_salida, ciudad, tipo, p_min, p_max, habitaciones, baños, ventaAlquiler) "
                    + "VALUES('"+ f.getId() +"', '"+ f.getFecha_entrada() +"', '"+ f.getFecha_salida() +"'"
                    + ", '"+ f.getCiudad() +"', '"+ f.getTipo() +"', '"+ f.getP_min() +"', '"+ f.getP_max() +"'"
                    + ", '"+ f.getHabitaciones() +"',"
                    + " '"+ f.getBaños() +"', '"+ f.getVentaAlquiler() +"')");
            //System.out.println("filtros guardados con exito");
        }catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }//fin catch 
     }
     
     public static void borrarFiltrosAnteriores(Filtros f) {
        Statement s2;
        try{
            s2 = con.getConnection().createStatement();
            s2.executeUpdate("DELETE from filtros WHERE id = '"+ f.getId() +"'");
            //System.out.println("Los filtros anteriores se borraron correctamente");
        }catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }//fin catch 
     }
     
     public static boolean comprobarFiltros(Filtros f) {
        ResultSet rs;
        Statement st;
        try{st = con.getConnection().createStatement();
            rs = st.executeQuery("SELECT id FROM filtros WHERE id = '"+ f.getId() +"'");
            if(rs.first()) {return true;}
            else {return false;}
        }catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }//fin catch 
        return false;
     }
}
