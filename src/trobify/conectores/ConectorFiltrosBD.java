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
    private static Conectar con = new Conectar();
    
   
    public static void insertarFiltrosSinFecha(Filtros f){
        String sql = "INSERT INTO filtros(id, ciudad, tipo, p_min, p_max, habitaciones, baños, ventaAlquiler) "
                    + "VALUES('"+ f.getId() + "', '"+ f.getCiudad() +"', '"+ f.getTipo() +"', '"+ f.getP_min() +"'"
                    + ", '"+ f.getP_max() +"', '"+ f.getHabitaciones() +"',"
                    + " '"+ f.getBaños() +"', '"+ f.getVentaAlquiler() +"')";
        Plantilla.consultaVoid(sql);
    }
    
    public static void insertarFiltrosConFechas(Filtros f) {
        String sql = "INSERT INTO filtros(id, fecha_entrada, fecha_salida, ciudad, tipo, p_min, p_max, habitaciones, baños, ventaAlquiler) "
                    + "VALUES('"+ f.getId() +"', '"+ f.getFecha_entrada() +"', '"+ f.getFecha_salida() +"'"
                    + ", '"+ f.getCiudad() +"', '"+ f.getTipo() +"', '"+ f.getP_min() +"', '"+ f.getP_max() +"'"
                    + ", '"+ f.getHabitaciones() +"',"
                    + " '"+ f.getBaños() +"', '"+ f.getVentaAlquiler() +"')";
        Plantilla.consultaVoid(sql);
     }
     
     public static void borrarFiltrosAnteriores(Filtros f) {
       String sql = "DELETE from filtros WHERE id = '"+ f.getId() +"'";
       Plantilla.consultaVoid(sql);
     }
     
     public static boolean comprobarFiltros(Filtros f) {
       String sql = "SELECT id FROM filtros WHERE id = '"+ f.getId() +"'";
       return Plantilla.consultaBoolean(sql);
     }
}
