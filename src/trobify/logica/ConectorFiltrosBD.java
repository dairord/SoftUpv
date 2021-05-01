/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify.logica;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import trobify.Conectar;
import trobify.controlador.InicioController;

/**
 *
 * @author gabri
 */
public class ConectorFiltrosBD {
    private static Conectar con = new Conectar();
   
    public static void insrtarFiltrosSinFecha(String username, String ciu, int tipo, String precioMin, String precioMax, String habitaciones, String baños, int alqOVen){
            try{
            Statement s1 = con.getConnection().createStatement();
            s1.executeUpdate("INSERT INTO filtros(id, ciudad, tipo, p_min, p_max, habitaciones, baños, ventaAlquiler) "
                    + "VALUES('"+ username + "', '"+ ciu +"', '"+ tipo +"', '"+ precioMin +"', '"+ precioMax +"', '"+ habitaciones +"',"
                    + " '"+ baños +"', '"+ alqOVen +"')");
            System.out.println("filtros guardados con exito");
        }catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }//fin catch )
}
}
