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
import trobify.logica.Servicios;

/**
 *
 * @author davido747
 */
public class ConectorServiciosBD {
   
    public static void añadirServicios(Servicios servi){
      String sql = "INSERT INTO servicios (id, supermercado, transporte_publico, banco, estanco, "
                    + "centro_comercial, gimnasio, farmacia) VALUES ('"+servi.getId()+"', '"+servi.getSupermercado()+"'"
                    + ", '"+servi.getTransporte_publico()+"', '"+servi.getBanco()+"', '"+servi.getEstanco()+"'"
                    + ", '"+servi.getCentro_comercial()+"', '"+servi.getGimnasio()+"', '"+servi.getFarmacia()+"')";
      Plantilla.consultaVoid(sql);
    }
    
    public static Servicios servicios (String id){
        String sql = "SELECT * FROM servicios WHERE id = '" + id + "'";
        try {
            ResultSet rsl = Plantilla.soloConsulta(sql);
            if (rsl.first()) {
                Servicios servicios = new Servicios(rsl.getNString("id"), rsl.getInt("supermercado"),
                rsl.getInt("transporte_publico"), rsl.getInt("banco"), rsl.getInt("estanco"), 
                rsl.getInt("centro_comercial"), rsl.getInt("gimnasio"), rsl.getInt("farmacia"));
                System.out.print("gabriela" + servicios.getBanco());
                
                return servicios;
            }

        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
     public static void acualizarServicios(Servicios serv){
       String sql = "UPDATE `servicios` SET `supermercado`='"+serv.getSupermercado()+
                    "',`transporte_publico`='"+serv.getTransporte_publico()+
                    "',`banco`='"+serv.getBanco()+"',`estanco`='"+serv.getEstanco()+
                    "',`centro_comercial`='"+serv.getCentro_comercial()+"',`gimnasio`='"+serv.getGimnasio()+
                    "',`farmacia`='"+serv.getFarmacia()+"' WHERE `id`='" + serv.getId() +"'";
       Plantilla.consultaVoid(sql);
    }
     
      public static Servicios getServicios(String id) {
        String sql = "SELECT * FROM servicios WHERE id = '" + id + "'";
          try {
            ResultSet rsl = Plantilla.soloConsulta(sql);
            if (rsl.first()) {
                Servicios servi = new Servicios(id, rsl.getInt("supermercado"), rsl.getInt("transporte_publico"),
                        rsl.getInt("banco"), rsl.getInt("estanco"), rsl.getInt("centro_comercial"), rsl.getInt("gimnasio"),
                        rsl.getInt("farmacia"));
                return servi;
            }
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
