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
import trobify.logica.Favoritos;
import trobify.logica.Historial;
import trobify.logica.Vivienda;

/**
 *
 * @author gabri
 */
public class ConectorViviendaBD extends Conector{
    
    public static Conexion con = Conexion.crearConexion();
    
     private static ArrayList<String> consultaLista(String sql){
         ArrayList<String> listaFotos = new ArrayList();
        try {
            Statement stm = con.getConnection().createStatement();
            ResultSet rsl = stm.executeQuery(sql);
            if (rsl.isBeforeFirst()) {
                
                while (rsl.next()) {
                    listaFotos.add(rsl.getString("id"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaFotos;
    }

    public static boolean hayViviendasEnLaCiudad(String ciudad, String tipoVivienda, int alqOVent) {
        String sql = "select * from vivienda where ciudad = '" + ciudad + "'" + tipoVivienda + " and ventaAlquiler = " + alqOVent + " and activo = 0";
        return consultaBoolean(sql, con);
    } //fin consulta inicial 

    public static ArrayList<String> consultaBuscador(String ciudad, int alqOVent, String tipo, String pMin, String pMax, String baños, String habita, String comoOrdenar) throws SQLException {
        ArrayList<String> viviendasList = new ArrayList();
        try {
            Statement stm = con.getConnection().createStatement();
            ResultSet rs = stm.executeQuery("select * from vivienda where ciudad = '" + ciudad
                    + "' and ventaAlquiler = " + alqOVent + tipo + pMin + pMax + baños + habita + " and activo = 0"
                    + " order by " + comoOrdenar);
            if (rs.isBeforeFirst()) {
               // System.out.println(rs.getString("id"));
                
                while (rs.next()) {
                    viviendasList.add(rs.getString("id"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return viviendasList;
    }


    public static int getPrecioVivienda(String id) {
        Vivienda vivi = getVivienda(id);
        return vivi.getPrecio();
    }

    public static void eliminarFavorito(String id, String username) {
        String sql = "DELETE FROM favoritos WHERE id = '" + id + "' and id_cliente = '"
                    + username + "'";
        consultaVoid(sql, con);
    }


    public static ArrayList<String> getListaFavoritosUsuario(String username, String orden) {
        String sql = "SELECT f.id FROM favoritos f, vivienda v WHERE f.id = v.id AND id_cliente = '" + username + "' and activo = 0 " + orden;
        return consultaLista(sql);
    }

     public static ArrayList<String> getListaIdViviendasDelUsusario(String username, String orden) {
        String sql = "SELECT id FROM vivienda WHERE id_propietario = '" + username + "' " + orden;
        return consultaLista(sql);
    }
     
      public static ArrayList<String> getListaIdHistorialDelUsusario(String username) {
        String sql = "SELECT id_vivienda FROM historial WHERE username = '" + username + "'  order By `id` DESC";
          ArrayList<String> lista = new ArrayList();
        try {
            Statement stm = con.getConnection().createStatement();
            ResultSet rsl = stm.executeQuery(sql);
            if (rsl.isBeforeFirst()) {
                
                while (rsl.next()) {
                    lista.add(rsl.getString("id_vivienda"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }
      
    public static ArrayList<String> getListaFotosVivienda(String id) {
       String sql = "SELECT id FROM fotografia WHERE id_vivienda = '" + id + "'";
       return consultaLista(sql);
    }

    public static ArrayList<String> getListaRecomendados(String id) {
        ArrayList<String> listaRecomendados = new ArrayList();
        int precioBase = getPrecioVivienda(id);
        double porcentaje = precioBase * 0.15;
        double precioAlto = precioBase + porcentaje;
        double precioBajo = precioBase - porcentaje;
        int i = 0;
        try {
            Statement stm = con.getConnection().createStatement();
            ResultSet rsl = stm.executeQuery("SELECT DISTINCT f.id_vivienda FROM fotografia f WHERE f.id_vivienda IN (SELECT v.id FROM vivienda v WHERE v.id NOT LIKE '" + id + "' AND v.precio BETWEEN '" + precioBajo + "' AND '" + precioAlto + "')");
            if (rsl.first()) {
                rsl.beforeFirst();
                String foto;
                while (rsl.next() && i < 3) {
                    foto = ConectorFotosBD.consultarFotoViviendaPorId(rsl.getNString("id_vivienda"));
                    listaRecomendados.add(foto);
                    i++;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaRecomendados;
    }

    public static void editarValoracion(int valoracion, String id, String username) {
       String sql = "UPDATE `favoritos` SET `valoracion`='" + valoracion + "' WHERE id = '" + id + "' AND id_cliente = '" + username + "'";
       consultaVoid(sql, con);
    }

    public static String getIdVivienda(String direccionFoto) {
        String cadena = direccionFoto;
        String[] parts = cadena.split("\\\\");
        String direccion = parts[parts.length - 1];
        //System.out.println(direccion);
        String idVivienda = "";
        try {
            Statement stm = con.getConnection().createStatement();
            ResultSet rsl = stm.executeQuery("SELECT id_vivienda FROM fotografia WHERE id LIKE '%" + direccion + "'");
            //System.out.println("prueba " + direccion);
            if (rsl.first()) {
                idVivienda = rsl.getString("id_vivienda");
                //System.out.println("prueba " + idVivienda);
            }
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return idVivienda;
    }

    public static void añadirFavoritos(Favoritos nuevo) {
        String sql = "INSERT INTO `favoritos`(`id`, `id_cliente`) VALUES ('" + nuevo.getId() + "','" + nuevo.getId_cliente() + "')";
        consultaVoid(sql, con);
    }


    public static void desactivarVivienda(String id) {
        Vivienda vivi = getVivienda(id);
        vivi.setActivo(1);
      String sql = "UPDATE `vivienda` SET `activo`='" + vivi.getActivo() + "' WHERE id = '" + vivi.getId() + "'";
      consultaVoid(sql, con);
    }

    public static void añadirVivienda(Vivienda vivi) {
        String sql = "INSERT INTO vivienda (id, calle, ciudad, ventaAlquiler, id_agencia, precio, id_propietario, tipo, "
                    + "baños, habitaciones, descripcion, piso, puerta, codigo_postal, activo) VALUE ('"+vivi.getId()+"', '"+vivi.getCalle()+"'"
                    + ", '"+vivi.getCiudad()+"', '"+vivi.getVentaAlquiler()+"', '"+vivi.getId_agencia()+"', '"+vivi.getPrecio()+"'"
                    + ", '"+vivi.getId_propietario()+"', '"+vivi.getTipo()+"', '"+vivi.getBaños()+"', '"+vivi.getHabitaciones()+"'"
                    + ", '"+vivi.getDescripcion()+"', '"+vivi.getPiso()+"', '"+vivi.getPuerta()+"', '"+vivi.getCodigo_postal()+"','"+ vivi.getActivo() +"')";
        
        consultaVoid(sql, con);
    }

    public static ArrayList<Vivienda> getViviendasPorCiudadActivas(String ciudad, int CompraAlquiler) {
        ArrayList<Vivienda> lista = new ArrayList<Vivienda>();
        try {
            Statement stm = con.getConnection().createStatement();
            ResultSet rsl = stm.executeQuery("SELECT * FROM vivienda WHERE ciudad = '" + ciudad + "' AND activo = 0 AND tipo = " +CompraAlquiler);
            if (rsl.isBeforeFirst()) {
                while (rsl.next()) {
                    Vivienda res = new Vivienda(rsl.getString("id"),
                            rsl.getString("calle"), rsl.getString("ciudad"), rsl.getInt("ventaAlquiler"),
                            rsl.getString("id_agencia"), rsl.getInt("precio"), rsl.getString("id_propietario"),
                            rsl.getInt("tipo"), rsl.getInt("baños"), rsl.getInt("habitaciones"),
                            rsl.getString("descripcion"), rsl.getInt("piso"), rsl.getString("puerta"),
                            rsl.getInt("codigo_postal"), rsl.getInt("activo"));
                    lista.add(res);
                }
                return lista;
            }
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return null;
    }

    public static int numeroViviendasEnBD() {
        int cont = 0;
        try {
            Statement stm = con.getConnection().createStatement();
            ResultSet rsl = stm.executeQuery("SELECT * from vivienda");
            if (rsl.isBeforeFirst()) {
                
                while (rsl.next()) {
                    cont++;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cont;

    }


    
    public static Vivienda getVivienda(String id){
        Vivienda res = new Vivienda();
        try {
            // System.out.println(id_usuario);
            Statement stm = con.getConnection().createStatement();

            ResultSet rsl = stm.executeQuery("SELECT * FROM vivienda WHERE id = '" + id + "'");
            if (rsl.isBeforeFirst()){
                rsl.first();
                res = new Vivienda(rsl.getString("id"),
                                rsl.getString("calle"), rsl.getString("ciudad"), rsl.getInt("ventaAlquiler"),
                                rsl.getString("id_agencia"), rsl.getInt("precio"), rsl.getString("id_propietario"),
                                rsl.getInt("tipo"), rsl.getInt("baños"), rsl.getInt("habitaciones"),
                                rsl.getString("descripcion"), rsl.getInt("piso"), rsl.getString("puerta"),
                                rsl.getInt("codigo_postal"), rsl.getInt("activo"));
                
            }
    }catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }
    
    public static void actualizarVivienda(Vivienda vivi){
       String sql = "UPDATE `vivienda` SET `calle`='"+ vivi.getCalle()+ "',`ciudad`='"+ vivi.getCiudad()+"',`ventaAlquiler`='" +vivi.getVentaAlquiler()+"',`id_agencia`='"+vivi.getId_agencia()+
                    "',`precio`='"+vivi.getPrecio()+"',`id_propietario`='"+vivi.getId_propietario()+"',`tipo`='"+vivi.getTipo()+"',`baños`='"+vivi.getBaños()+"',"
                    + "`habitaciones`='"+vivi.getHabitaciones()+"',`descripcion`='"+ vivi.getDescripcion() +"',`piso`='"+ vivi.getPiso() +"',`puerta`='"+ vivi.getPuerta()+"',"
                    + "`codigo_postal`='"+ vivi.getCodigo_postal() +"',`activo`='"+ vivi.getActivo()+ "' WHERE id = '" + vivi.getId() + "'";
       consultaVoid(sql, con);
    }
    
    public static Favoritos getFavorito(String id, String username){
         //String sql = "SELECT id FROM favoritos WHERE id LIKE '" + id + "' AND id_cliente = '" + username + "'";
         try{
             Statement stm = con.getConnection().createStatement();
             ResultSet rsl = stm.executeQuery("SELECT * FROM favoritos WHERE id LIKE '" + id + "' AND id_cliente = '" + username + "'");
             if(rsl.first()){
                 return new Favoritos(rsl.getString("id"), rsl.getString("id_cliente"), rsl.getInt("valoracion"));
                 
             }
         }catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
         return new Favoritos();
    }
    
     public static void activarVivienda(String id) {
        Vivienda vivi = getVivienda(id);
        vivi.setActivo(0);
      String sql = "UPDATE `vivienda` SET `activo`='" + vivi.getActivo() + "' WHERE id = '" + vivi.getId() + "'";
      consultaVoid(sql, con);
    }
   
     public static int getActivo(String id){
         Vivienda vivi = getVivienda(id);
         return vivi.getActivo();
     }
   
      public static void añadirAHistorial(Historial h) {
        String sql = "INSERT INTO `historial`(`id_vivienda`, `username`) VALUES ('"+ h.getId_vivienda() +"','"+ h.getUsername() +"')";
        consultaVoid(sql, con);
    }
      
      
      public static ArrayList<String> getUsuariosPorViviendaFav(String id_vivienda) {
        // System.out.println("GetStrings");
        ArrayList<String> res = new ArrayList<String>();
        try {
            // System.out.println(id_usuario);
            Statement stm = con.getConnection().createStatement();

            ResultSet rs = stm.executeQuery("SELECT * FROM favoritos WHERE id = '" + id_vivienda + "'");

            if (rs.isBeforeFirst()) {
               
                while (rs.next()) {
                   String nuevo = rs.getString("id_cliente");
                    //System.out.println(nuevo);
                    res.add(nuevo);
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;
    }
      
      
} //fin clase
