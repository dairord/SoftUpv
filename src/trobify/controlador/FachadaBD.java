/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify.controlador;

import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import trobify.logica.ConectorFotosBD;
import trobify.logica.ConectorViviendaBD;
import trobify.logica.Vivienda;

/**
 *
 * @author gabri
 */
public class FachadaBD {
    /* 
     String foto = ConectorFotosBD.consultarFoto(viviendasList.get(i));
                String calle = ConectorViviendaBD.consultarDireccion(viviendasList.get(i));
                int precio = ConectorViviendaBD.consultarPrecio(viviendasList.get(i));
                int alquilada = ConectorViviendaBD.consultarAlquiler(viviendasList.get(i));
    */
    /*public static String consultarFoto(String id){
        return ConectorFotosBD.consultarFoto(id);
    }
    
    public static String consultarDireccion(String id){
        return ConectorViviendaBD.consultarDireccion(id);
    }
    
    public static int consultarPrecio(String id){
        return ConectorViviendaBD.consultarPrecio(id);
    }
    */
    
   public static Vivienda pasarVivienda(String id){
       Vivienda vivi = ConectorViviendaBD.getVivienda(id);
       return vivi;
   }
    
   
}
