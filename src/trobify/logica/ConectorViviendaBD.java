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
    
    private static boolean consultaBoolean(String sql){
        try {
            Statement stm = con.getConnection().createStatement();
            ResultSet rs = stm.executeQuery(sql);
            if (rs.first()) {
                return true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }
    
    private static void consultaVoid(String sql){
         try {
            Statement stm = con.getConnection().createStatement();
            stm.executeUpdate(sql);

        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
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

    public static boolean consultaInicial(String ciudad, String tipoVivienda, int alqOVent) {
        String sql = "select * from vivienda where ciudad = '" + ciudad + "'" + tipoVivienda + " and ventaAlquiler = " + alqOVent + " and activo = 0";
        return consultaBoolean(sql);
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

   
    
    public static void eliminarFoto(String rutaFoto, String idVivienda){
        String cadena = rutaFoto;
        String[] parts = cadena.split("\\\\");
        String direccion = parts[parts.length - 1];
        System.out.println(direccion);
        
       String sql = "DELETE FROM fotografia WHERE id LIKE '%" + direccion + "'";
       consultaVoid(sql);
    }
    
    public static void añadirFotografia (String idFoto, String idVivienda){
        String sql = "INSERT INTO `fotografia`(`id`, `id_vivienda`) VALUES ('" + idFoto + "','" + idVivienda + "')";
        consultaVoid(sql);
    }
   

  
    public static String consultarDireccion(String id) {
        Vivienda vivi = vivienda(id);
        if (vivi.getCalle() != null) {
            return vivi.getCalle();
        }
        return "No disponible";
    }

    public static int consultarPrecio(String id) {
        Vivienda vivi = vivienda(id);
        return vivi.getPrecio();
    }
    
    public static int consultarAlquiler(String id) {
        Vivienda vivi = vivienda(id);
        return vivi.getVentaAlquiler();
    }

    public static void eliminarDeFavoritos(String id, String username) {
        String sql = "DELETE FROM favoritos WHERE id = '" + id + "' and id_cliente = '"
                    + username + "'";
        consultaVoid(sql);
    }

    public static int consultarValoracion(String id, String username) {
        int aux = -1;
        try {
            Statement stm = con.getConnection().createStatement();
            ResultSet rsl = stm.executeQuery("SELECT valoracion FROM favoritos WHERE id LIKE '" + id + "' AND id_cliente = '" + username + " '");
            if (rsl.first()) {
                aux = rsl.getInt(1);
            }
            if (aux == 0) {
                return -1;
            } else {
                return aux;
            }

        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return aux;
    }

    public static ArrayList<String> ordenarFavoritos(String username, String orden) {
        String sql = "SELECT f.id FROM favoritos f, vivienda v WHERE f.id = v.id AND id_cliente = '" + username + "' and activo = 0 " + orden;
        return consultaLista(sql);
    }

    public static boolean estaEnFavoritos(String id, String username) {
       String sql = "SELECT id FROM favoritos WHERE id LIKE '" + id + "' AND id_cliente = '" + username + "'";
       return consultaBoolean(sql);
    }

    public static ArrayList<String> crearListaFotos(String id) {
       String sql = "SELECT id FROM fotografia WHERE id_vivienda = '" + id + "'";
       return consultaLista(sql);
    }

    public static ArrayList<String> crearListaRecomendados(String id) {
        ArrayList<String> listaRecomendados = new ArrayList();
        int precioBase = consultarPrecio(id);
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
                    foto = ConectorFotosBD.consultarFoto(rsl.getNString("id_vivienda"));
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
       consultaVoid(sql);
    }

    public static Vivienda vivienda(String id) {
        try {
            Statement stm = con.getConnection().createStatement();
            ResultSet rsl = stm.executeQuery("SELECT * FROM vivienda WHERE id = '" + id + "'");
            if (rsl.first()) {
                Vivienda vivi = new Vivienda(rsl.getNString("id"), rsl.getNString("calle"), rsl.getNString("ciudad"),
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

    public static String consultarDescripcion(String id) {
        String texto = "No lo dejes escapar.";
        Vivienda vivi = vivienda(id);
        if (vivi.getDescripcion() != null) {
            texto = vivi.getDescripcion();
        }
        return texto;
    }

    public static String consultarIdVivienda(String direccionFoto) {
        String cadena = direccionFoto;
        String[] parts = cadena.split("\\\\");
        String direccion = parts[parts.length - 1];
        System.out.println(direccion);
        String idVivienda = "";
        try {
            Statement stm = con.getConnection().createStatement();
            ResultSet rsl = stm.executeQuery("SELECT id_vivienda FROM fotografia WHERE id LIKE '%" + direccion + "'");
            System.out.println("prueba " + direccion);
            if (rsl.first()) {
                idVivienda = rsl.getString("id_vivienda");
                System.out.println("prueba " + idVivienda);
            }
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return idVivienda;
    }

    public static void añadirFavoritos(Favoritos nuevo) {
        String sql = "INSERT INTO `favoritos`(`id`, `id_cliente`) VALUES ('" + nuevo.getId() + "','" + nuevo.getId_cliente() + "')";
        consultaVoid(sql);
    }

  

    public static boolean esPropietario(String id, String username) {
        String sql ="SELECT * FROM vivienda WHERE id = '" + id + "' and id_propietario = '" + username + "'";
        return consultaBoolean(sql);
    }

    public static void desactivarVivienda(String id) {
        Vivienda vivi = vivienda(id);
        vivi.setActivo(1);
      String sql = "UPDATE `vivienda` SET `activo`='" + vivi.getActivo() + "' WHERE id = '" + vivi.getId() + "'";
      consultaVoid(sql);
    }

    public static void añadirVivienda(Vivienda vivi) {
        String sql = "INSERT INTO vivienda (id, calle, ciudad, ventaAlquiler, id_agencia, precio, id_propietario, tipo, "
                    + "baños, habitaciones, descripcion, piso, puerta, codigo_postal, activo) VALUE ('"+vivi.getId()+"', '"+vivi.getCalle()+"'"
                    + ", '"+vivi.getCiudad()+"', '"+vivi.getVentaAlquiler()+"', '"+vivi.getId_agencia()+"', '"+vivi.getPrecio()+"'"
                    + ", '"+vivi.getId_propietario()+"', '"+vivi.getTipo()+"', '"+vivi.getBaños()+"', '"+vivi.getHabitaciones()+"'"
                    + ", '"+vivi.getDescripcion()+"', '"+vivi.getPiso()+"', '"+vivi.getPuerta()+"', '"+vivi.getCodigo_postal()+"','"+ vivi.getActivo() +"')";
        
        consultaVoid(sql);
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

    public static int numeroViviendas() {
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

    public static ArrayList<Vivienda> getFavoritosUsuario(String id_usuario) {

        ArrayList<String> lista_String = getStringsFavoritos(id_usuario);
        ArrayList<Vivienda> lista = new ArrayList<Vivienda>();

        for (int i = 0; i < lista_String.size(); i++) {
            System.out.println(lista_String.size());
            try {
                String id_viv = lista_String.get(i);
                Statement stm = con.getConnection().createStatement();
                ResultSet rsl = stm.executeQuery("SELECT * FROM vivienda WHERE id = '" + id_viv + "'");
                if (rsl.isBeforeFirst()) {
                    
                    while (rsl.next()) {
                      Vivienda  res = new Vivienda(rsl.getString("id"),
                                rsl.getString("calle"), rsl.getString("ciudad"), rsl.getInt("ventaAlquiler"),
                                rsl.getString("id_agencia"), rsl.getInt("precio"), rsl.getString("id_propietario"),
                                rsl.getInt("tipo"), rsl.getInt("baños"), rsl.getInt("habitaciones"),
                                rsl.getString("descripcion"), rsl.getInt("piso"), rsl.getString("puerta"),
                                rsl.getInt("codigo_postal"), rsl.getInt("activo"));
                        lista.add(res);
                    }

                    
                }
            } catch (SQLException ex) {
                Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
                return lista;
            }
        }

        return lista;

    }

    public static ArrayList<String> getStringsFavoritos(String id_usuario) {
        // System.out.println("GetStrings");
        ArrayList<String> res = new ArrayList<String>();
        try {
            // System.out.println(id_usuario);
            Statement stm = con.getConnection().createStatement();

            ResultSet rs = stm.executeQuery("SELECT * FROM favoritos WHERE id_cliente = '" + id_usuario + "'");

            if (rs.isBeforeFirst()) {
               
                while (rs.next()) {
                   String nuevo = rs.getString("id");
                    //System.out.println(nuevo);
                    res.add(nuevo);
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;
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
       consultaVoid(sql);
    }
    
   
} //fin clase
