/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify.logica;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
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
  
    public static Vivienda pasarVivienda(String id){
       Vivienda vivi = ConectorViviendaBD.getVivienda(id);
       return vivi;
   }
    
   public static String consultarFoto(String id){
       return ConectorFotosBD.consultarFoto(id);
   }
   
   public static ArrayList<Vivienda> favoritosUsuario(String username){
       return ConectorViviendaBD.getFavoritosUsuario(username);
   }
   
   public static ArrayList<Vivienda> viviendasEnCiudad(String ciu, int alqOVen){
        return ConectorViviendaBD.getViviendasPorCiudadActivas(ciu, alqOVen);
   }
   
   public static ArrayList<String> consultaBuscador(String ciu, int alqOVen,String tipo, String pMin, String pMax, String baños, String habita, String comoOrdenar) throws SQLException{
       return ConectorViviendaBD.consultaBuscador(ciu, alqOVen, tipo, pMin, pMax, baños, habita, comoOrdenar);
   }
}
