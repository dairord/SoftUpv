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

/**
 *
 * @author davido747
 */
public class ConectorServiciosBD {
    private static Conectar con = new Conectar();
    
    public static void añadirServicios(Servicios servi){
        try {
            Statement stm = con.getConnection().createStatement();
            stm.executeLargeUpdate("INSERT INTO servicios (id, supermercado, transporte_publico, banco, estanco, "
                    + "centro_comercial, gimnasio, farmacia) VALUES ('"+servi.getId()+"', '"+servi.getSupermercado()+"'"
                    + ", '"+servi.getTransporte_publico()+"', '"+servi.getBanco()+"', '"+servi.getEstanco()+"'"
                    + ", '"+servi.getCentro_comercial()+"', '"+servi.getGimnasio()+"', '"+servi.getFarmacia()+"')");
        } catch (SQLException ex) {
            Logger.getLogger(ConectorServiciosBD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
