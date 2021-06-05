/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify.fachada;

import trobify.conectores.ConectorServiciosBD;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import trobify.conectores.ConectorAgenciaBD;
import trobify.conectores.ConectorViviendaBD;
import trobify.logica.Agente;
import trobify.conectores.ConectorFiltrosBD;
import trobify.conectores.ConectorFotosBD;
import trobify.conectores.ConectorNotificacionBD;
import trobify.conectores.ConectorUsuarioBD;
import trobify.conectores.ConectorMensajeBD;
import trobify.logica.Favoritos;
import trobify.logica.Filtros;
import trobify.logica.Fotografia;
import trobify.logica.Historial;
import trobify.logica.IIterator;
import trobify.logica.Notificacion;
import trobify.logica.Servicios;
import trobify.logica.Usuario;
import trobify.logica.Vivienda;
import trobify.logica.Vivienda;
import trobify.logica.Mensaje;

/**
 *
 * @author gabri
 */
public class FachadaBD {

    public static ArrayList<Vivienda> viviendasEnCiudad(String ciu, int alqOVen) {
        return ConectorViviendaBD.getViviendasPorCiudadActivas(ciu, alqOVen);
    }

    public static ArrayList<String> consultaBuscador(String ciu, int alqOVen, String tipo, String pMin, String pMax, String baños, String habita, String comoOrdenar) throws SQLException {
        return ConectorViviendaBD.consultaBuscador(ciu, alqOVen, tipo, pMin, pMax, baños, habita, comoOrdenar);
    }

    public static Vivienda getVivienda(String id) {
        Vivienda viv = ConectorViviendaBD.getVivienda(id);
        viv.setServicios(ConectorServiciosBD.getServiciosPorVivienda(id));
        viv.setFotos(ConectorFotosBD.getListaFotos(id));
        return viv;
    }

    /*public static void añadirFotografia(String idFotoBD, String id) {
        ConectorViviendaBD.añadirFotografia(idFotoBD, id);
    }*/
    public static void actualiarVivienda(Vivienda viviActualizada) {
        ConectorViviendaBD.actualizarVivienda(viviActualizada);
        ConectorServiciosBD.acualizarServicios(viviActualizada.getServiciosPorVivienda());
    }

    public static ArrayList<String> getListaFotosVivienda(String id) {
        return ConectorViviendaBD.getListaFotosVivienda(id);
    }

  
    public static void registrarVivienda(Vivienda v) {
        ConectorViviendaBD.añadirVivienda(v);
        ConectorServiciosBD.añadirServicios(v.getServiciosPorVivienda());
        IIterator it = v.createIterator();
        while (it.hasNext()) {
            ConectorFotosBD.añadirFotografia((Fotografia) it.currentObject());
            it.next();
        }
    }

    public static int numeroViviendasEnBD() {
        return ConectorViviendaBD.numeroViviendasEnBD();
    }

    public static ArrayList<String> getListaFavoritosUsuario(String username, String orden) {
        return ConectorViviendaBD.getListaFavoritosUsuario(username, orden);
    }

    public static boolean hayViviendasEnLaCiudad(String ciu, String tipo, int alqOVen) {
        return ConectorViviendaBD.hayViviendasEnLaCiudad(ciu, tipo, alqOVen);
    }

    public static int getPrecioVivienda(String id) {
        return ConectorViviendaBD.getPrecioVivienda(id);
    }

    public static boolean esPropietario(String id_viv, String propie) {
        if (ConectorViviendaBD.getVivienda(id_viv).getId_propietario().equals(propie)) {
            return true;
        }
        return false;
    }

    public static int getValoracion(String id, String username) {
        return ConectorViviendaBD.getFavorito(id, username).getValoracion();
    }

    public static boolean estaEnFavoritos(String id, String username) {
        Favoritos fav = ConectorViviendaBD.getFavorito(id, username);
        if (fav.getId().equals(fav.getId_cliente())) {
            return false;
        }
        return true;
    }

    public static ArrayList<String> getListaRecomendados(String id) {
        return ConectorViviendaBD.getListaRecomendados(id);
    }

    public static String getIdVivienda(String direccionFoto) {
        return ConectorViviendaBD.getIdVivienda(direccionFoto);
    }

    public static void añadirFavorito(Favoritos fav) {
        ConectorViviendaBD.añadirFavoritos(fav);
    }

    public static void eliminarFavorito(String id, String username) {
        ConectorViviendaBD.eliminarFavorito(id, username);
    }

    public static void editarValoracion(int valoracion, String id, String username) {
        ConectorViviendaBD.editarValoracion(valoracion, id, username);
    }

    public static void desactivarVivienda(String id) {
        ConectorViviendaBD.desactivarVivienda(id);
    }

    public static ArrayList<String> getListaIdViviendasDelUsusario(String username, String orden) {
        return ConectorViviendaBD.getListaIdViviendasDelUsusario(username, orden);
    }

    public static ArrayList<String> getListaIdHistorialDelUsusario(String username) {
        return ConectorViviendaBD.getListaIdHistorialDelUsusario(username);
    }

    public static void activarVivienda(String id) {
        ConectorViviendaBD.activarVivienda(id);
    }

    public static String getActivo(String id) {
        if (ConectorViviendaBD.getActivo(id) == 0) {
            return "activo";
        } else {
            return "desactivo";
        }
    }

    public static void añadirViviendaHistorial(Historial h) {
        ConectorViviendaBD.añadirAHistorial(h);
    }

    //cosas de usuario
    public static Boolean isUsernameRepetido(String username) {
        if (username.equals(ConectorUsuarioBD.getUsuario(username).getId())) {
            return true;
        }
        return false;
    }

    public static boolean comprobarContraseñaDelUsuarioCorrecta(String nom, String pas) {
        Usuario u = ConectorUsuarioBD.getUsuario(nom);
        return pas.equals(u.getPassword());
    }

    public static void añadirUsuario(Usuario user) {
        ConectorUsuarioBD.añadirUsuario(user);
    }

    public static ArrayList<String> listaUsuariosPorPreferencia(String preferencia) throws SQLException {
        return ConectorUsuarioBD.getUsariosPorPreferencia(preferencia);
    }

    public static String getPreferenciaDeUsuario(String id) {
        Usuario u = ConectorUsuarioBD.getUsuario(id);
        return u.getPreferencia();
    }

    public static void updatePreferenciaDeUsuario(String preferencia, String id) {
        ConectorUsuarioBD.actualizarPreferenciaDeUsuario(preferencia, id);
    }

    //cosas de fotos
    public static String consultarFotoViviendaPorIdViviendaPorId(String id) {
        return ConectorFotosBD.consultarFotoViviendaPorId(id);
    }

    public static void añadirFotografia(String idFotoBD, String id) {
        ConectorFotosBD.añadirFotografia(idFotoBD, id);
    }
    
      public static void eliminarFoto(String rutaFoto, String id) {
        ConectorFotosBD.eliminarFoto(rutaFoto, id);
    }

    //cosas de servicios
    public static Servicios getServiciosPorViviendaPorVivienda(String id) {
        return ConectorServiciosBD.getServiciosPorVivienda(id);
    }

    //cosas de filtros
    public static void guardarFiltros(Filtros f, boolean hayFechas) {
        if (f.getVentaAlquiler() == 2 && hayFechas == true) {//Opción alquilar y los datepicker tienen fechas
            if (ConectorFiltrosBD.comprobarFiltros(f)) {
                ConectorFiltrosBD.borrarFiltrosAnteriores(f);
                ConectorFiltrosBD.insertarFiltrosConFechas(f);
            } else {
                ConectorFiltrosBD.insertarFiltrosConFechas(f);
            }
        } else {//Opción comprar o alquilar sin fechas en los datepicker
            if (ConectorFiltrosBD.comprobarFiltros(f)) {
                ConectorFiltrosBD.borrarFiltrosAnteriores(f);
                ConectorFiltrosBD.insertarFiltrosSinFecha(f);
            } else {
                ConectorFiltrosBD.insertarFiltrosSinFecha(f);
            }
        }
    }

    //cosas agencia
    public static boolean contraseñaDeAgenciaCorrecta(String codigo, String contraseña) {
        return ConectorAgenciaBD.contraseñaDeAgenciaCorrecta(codigo, contraseña);
    }

    public static String getAgenciaDelAgente(String id_usuario) {
        return ConectorAgenciaBD.getAgenciaDelAgente(id_usuario);
    }

    public static void guardarAgente(Agente a) {
        ConectorAgenciaBD.guardarAgente(a);
    }

    //cosas de notificaciones
    public static ArrayList<Notificacion> getNotificacionPorUsuarioDestinoDestino(String id_usuario_dest) {
        return ConectorNotificacionBD.getNotificacionPorUsuarioDestino(id_usuario_dest);
    }

    public static ArrayList<Notificacion> getNotificacionPorVivienda(String id_vivienda) {
        return ConectorNotificacionBD.getNotificacionPorVivienda(id_vivienda);
    }

    public static void añadirNotificacion(Notificacion n) {
        ConectorNotificacionBD.añadirNotificacionNoID(n);
    }

    public static void borrarNotificacion(Notificacion n) {
        ConectorNotificacionBD.borrarNotificacion(n);
    }

    //cosas de mensajes
    public static void añadirMensaje(Mensaje m) {
        ConectorMensajeBD.añadirMensaje(m);
    }

    public static void notificarDesactivacionVivienda(String id_vivienda) {
        Notificacion res = new Notificacion(id_vivienda, null, null, null, new Date(System.currentTimeMillis()), 0, 0);
        ArrayList<String> users = ConectorViviendaBD.getUsuariosPorViviendaFav(id_vivienda);
        ConectorNotificacionBD.borrarNotificacionesEnMasa(id_vivienda, 0);
        for (int i = 0; i < users.size(); i++) {
            res.setId_usuario_dest(users.get(i));
            añadirNotificacion(res);
        }
    }

    public static void notificarActivacionVivienda(String id_vivienda) {
        Notificacion res = new Notificacion(id_vivienda, null, null, null, new Date(System.currentTimeMillis()), 1, 0);
        ArrayList<String> users = ConectorViviendaBD.getUsuariosPorViviendaFav(id_vivienda);

        ConectorNotificacionBD.borrarNotificacionesEnMasa(id_vivienda, 0);
        for (int i = 0; i < users.size(); i++) {
            res.setId_usuario_dest(users.get(i));
            añadirNotificacion(res);
        }
    }
}
