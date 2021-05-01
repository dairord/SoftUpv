/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify.logica;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import trobify.Conectar;
import trobify.controlador.InicioController;

/**
 *
 * @author gabri
 */
public class ConectorViviendaBD {
    private static Conectar con = new Conectar();
   
   
   public static boolean consultaInicial(String ciudad, String tipoVivienda, int alqOVent){
            try {
            Statement stm = con.getConnection().createStatement();
            ResultSet rs = stm.executeQuery ("select * from vivienda where ciudad = '" + ciudad + "'" + tipoVivienda + " and ventaAlquiler = " + alqOVent );
            if ( rs.first()) return true;
           
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
      
       return false;
    } //fin consulta inicial 
    
   public static ArrayList<String> consultaBuscador(String ciudad, int alqOVent, String tipo, String pMin, String pMax, String baños, String habita, String comoOrdenar) throws SQLException{
        ArrayList<String> viviendasList = new ArrayList();
        try {
           Statement stm = con.getConnection().createStatement();
            ResultSet rs = stm.executeQuery("select * from vivienda where ciudad = '" + ciudad
                    + "' and ventaAlquiler = " + alqOVent + tipo + pMin + pMax + baños + habita
                    + " order by " + comoOrdenar);
          if (rs.first()) {
                System.out.println(rs.getString("id"));
                rs.beforeFirst();
                while (rs.next()) {
                    viviendasList.add(rs.getString("id"));
                } 
          }      
     } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return viviendasList;
    }      
    
   public static String consultarFoto(String id){
       Fotografia foto = fotografia(id);
       if(foto.getId()!=null) return foto.getId();
       else
        return "C:\\Users\\gabri\\Desktop\\gabri\\SoftUpv\\src\\trobify\\images\\foto0.jpg";
    }
   
   public static Fotografia fotografia(String id){
        try {
            Statement stm = con.getConnection().createStatement();
            ResultSet rsl = stm.executeQuery("SELECT * FROM fotografia WHERE id_vivienda = '" + id + "'");
            if (rsl.first()) {
               Fotografia foto = new Fotografia( rsl.getNString("id"), rsl.getNString("id_vivienda"));
               return foto;
            }

        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
      return null;
   }

   public static String consultarDireccion(String id) {
        Vivienda vivi = vivienda(id);
        if(vivi.getCalle() != null) return vivi.getCalle();
        return "No disponible";
    }
    
   public static int consultarPrecio(String id) {
      Vivienda vivi = vivienda(id);
      return vivi.getPrecio();
     }
     
   public static void eliminarDeFavoritos(String id, String username){
     try {
                Statement stm = con.getConnection().createStatement();
                stm.executeUpdate("DELETE FROM favoritos WHERE id = '" + id + "' and id_cliente = '"
                + username +"'");
                
            } catch (SQLException ex) {
                Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
            }
     }
     
   public static int consultarValoracion(String id, String username){
        int aux = -1;
        try {
            Statement stm = con.getConnection().createStatement();
            ResultSet rsl = stm.executeQuery("SELECT valoracion FROM favoritos WHERE id LIKE '" + id + "' AND id_cliente = '" + username + " '");
            if (rsl.first()) aux = rsl.getInt(1);
            if(aux == 0) return -1;
            else return aux;
            
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return aux;
    }
     
   public static ArrayList<String> ordenarFavoritos(String username, String orden){
         ArrayList<String> favList = new ArrayList();
         try {
            Statement stm = con.getConnection().createStatement();
            ResultSet rsl = stm.executeQuery ("SELECT f.id FROM favoritos f, vivienda v WHERE f.id = v.id AND id_cliente = '"+ username + "' " + orden);
            if(rsl.first()){
                rsl.beforeFirst();
                while (rsl.next()) {
                    favList.add(rsl.getString("id"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
     return favList;
     }
     
   public static boolean estaEnFavoritos(String id, String username){
      try {
            Statement stm = con.getConnection().createStatement();
            ResultSet rsl = stm.executeQuery ("SELECT id FROM favoritos WHERE id LIKE '" + id + "' AND id_cliente = '" + username + "'");
            if(rsl.first()){
               return true;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
     }
     
   public static ArrayList<String> crearListaFotos(String id){
      ArrayList<String> listaFotos = new ArrayList();
          try {
            Statement stm = con.getConnection().createStatement();
            ResultSet rsl = stm.executeQuery ("SELECT id FROM fotografia WHERE id_vivienda = '" + id + "'");
            if(rsl.first()){
                rsl.beforeFirst();
                while (rsl.next()) {
                    listaFotos.add(rsl.getString("id"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
          return listaFotos;
    }
    
   public static ArrayList<String> crearListaRecomendados(String id,int precioBase){        
        ArrayList<String> listaRecomendados = new ArrayList();
        int precioAlto = precioBase + 150;
        int precioBajo = precioBase - 150;
        int i = 0;
        try {
            Statement stm = con.getConnection().createStatement();
            ResultSet rsl = stm.executeQuery ("SELECT f.id FROM fotografia f WHERE f.id_vivienda IN (SELECT v.id FROM vivienda v WHERE v.id NOT LIKE '" + id + "' AND v.precio BETWEEN '" + precioBajo + "' AND '" + precioAlto + "')");
            if(rsl.first()){
                rsl.beforeFirst();
                while(rsl.next() && i < 3){
                    listaRecomendados.add(rsl.getNString("id"));
                    i++;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaRecomendados;
    }
     
   public static void editarValoracion(int valoracion, String id, String username){
       try {
                Statement stm = con.getConnection().createStatement();
                stm.executeUpdate("UPDATE `favoritos` SET `valoracion`='" + valoracion + "' WHERE id = '" + id + "' AND id_cliente = '" + username + "'");
                
            } catch (SQLException ex) {
                Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
  
   private static Vivienda vivienda(String id){
       try {
            Statement stm = con.getConnection().createStatement();
            ResultSet rsl = stm.executeQuery("SELECT * FROM vivienda WHERE id = '" + id + "'");
            if (rsl.first()) {
            Vivienda vivi = new Vivienda(rsl.getNString("id"),rsl.getNString("calle"),rsl.getNString("ciudad"),
                rsl.getInt("ventaAlquiler"), rsl.getNString("id_Agencia"), rsl.getInt("precio"), rsl.getNString("id_propietario"),
                    rsl.getInt("tipo"), rsl.getInt("baños"), rsl.getInt("habitaciones"), rsl.getNString("descripcion"),
                    rsl.getInt("piso"), rsl.getNString("puerta"), rsl.getInt("codigo_postal"), rsl.getInt("activo"));
          return vivi;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
       return null;
    }
   
   public static String consultarDescripcion(String id){
        String texto = "No lo dejes escapar.";
        Vivienda vivi = vivienda(id);
        if ( vivi.getDescripcion() != null) texto = vivi.getDescripcion();
        return texto;
    }
   
   public static String consultarIdVivienda(String direccionFoto){
       String cadena = direccionFoto;
       String[] parts = cadena.split("\\\\");              
       String direccion = parts[parts.length-1];
       System.out.println(direccion);
       String idVivienda = "";
        try {
            Statement stm = con.getConnection().createStatement();
            //esta consulta no va creo
            ResultSet rsl = stm.executeQuery ("SELECT id_vivienda FROM fotografia WHERE id = '%" + direccion + "'");
            if(rsl.first()){
            idVivienda = rsl.getString("id_vivienda");
            System.out.println("prueba "+ idVivienda);
            }
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return idVivienda;
    }
   
   public static void añadirFavoritos(Favoritos nuevo){
        try {
                Statement stm = con.getConnection().createStatement();
                stm.executeUpdate("INSERT INTO `favoritos`(`id`, `id_cliente`) VALUES ('" + nuevo.getId() + "','" + nuevo.getId_cliente() + "')");
            } catch (SQLException ex) {
                Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
            }
   }
   
   public static Servicios consultarServicios (String id){
      try {
            Statement stm = con.getConnection().createStatement();
            ResultSet rsl = stm.executeQuery("SELECT * FROM servicios WHERE id = '" + id + "'");
            if(rsl.first()){
                Servicios servi = new Servicios (id, rsl.getInt("supermercado"), rsl.getInt("transporte_publico"),
                        rsl.getInt("banco"), rsl.getInt("estanco"), rsl.getInt("centro_comercial"), rsl.getInt("gimnasio"),
                        rsl.getInt("farmacia"));
                return servi;
            }    
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
   }
} //fin clase
