/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify.fachada;

import trobify.conectores.ConectorServiciosBD;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import trobify.conectores.ConectorAgenciaBD;
import trobify.conectores.ConectorFotosBD;
import trobify.conectores.ConectorViviendaBD;
import trobify.logica.Agente;
import trobify.conectores.ConectorFiltrosBD;
import trobify.conectores.ConectorFotosBD;
import trobify.conectores.ConectorUsuarioBD;
import trobify.logica.Favoritos;
import trobify.logica.Filtros;
import trobify.logica.Fotografia;
import trobify.logica.Servicios;
import trobify.logica.Usuario;
import trobify.logica.Vivienda;
import trobify.logica.Vivienda;

/**
 *
 * @author gabri
 */
public class FachadaBD {

    public static Vivienda pasarVivienda(String id) {
        Vivienda vivi = ConectorViviendaBD.getVivienda(id);
        return vivi;
    }

    public static String consultarFoto(String id) {
        return ConectorFotosBD.consultarFoto(id);
    }

    public static ArrayList<Vivienda> favoritosUsuario(String username) {
        return ConectorViviendaBD.getFavoritosUsuario(username);
    }

    public static ArrayList<Vivienda> viviendasEnCiudad(String ciu, int alqOVen) {
        return ConectorViviendaBD.getViviendasPorCiudadActivas(ciu, alqOVen);
    }

    public static ArrayList<String> consultaBuscador(String ciu, int alqOVen, String tipo, String pMin, String pMax, String baños, String habita, String comoOrdenar) throws SQLException {
        return ConectorViviendaBD.consultaBuscador(ciu, alqOVen, tipo, pMin, pMax, baños, habita, comoOrdenar);
    }

    public static Vivienda getVivienda(String id) {
        return ConectorViviendaBD.getVivienda(id);
    }

    public static Servicios getServicios(String id) {
        return ConectorServiciosBD.getServicios(id);
    }

    public static void añadirFotografia(String idFotoBD, String id) {
        ConectorViviendaBD.añadirFotografia(idFotoBD, id);
    }

    public static void actualiarVivienda(Vivienda viviActualizada, Servicios servActualizado) {
        ConectorViviendaBD.actualizarVivienda(viviActualizada);
        ConectorServiciosBD.acualizarServicios(servActualizado);
    }

    public static ArrayList<String> crearListaFotos(String id) {
        return ConectorViviendaBD.crearListaFotos(id);
    }

    public static void eliminarFoto(String rutaFoto, String id) {
        ConectorViviendaBD.eliminarFoto(rutaFoto, id);
    }

    public static void registrarVivienda(Vivienda v, Servicios s, ArrayList<Fotografia> fotos) {
        ConectorViviendaBD.añadirVivienda(v);
        ConectorServiciosBD.añadirServicios(s);
        ConectorFotosBD.añadirConjuntoFotos(fotos);
    }

    public static int numeroViviendas() {
        return ConectorViviendaBD.numeroViviendas();
    }

    public static Boolean isUsernameRepetido(String username) {
        if (username.equals(ConectorUsuarioBD.getUsuario(username).getId())) {
            return true;
        }
        return false;
    }
//////Sobra
   /* public static int consultarValoracion(String id, String username) {
        return ConectorViviendaBD.consultarValoracion(id, username);
    }*/

    public static void eliminarDeFavoritos(String botonElinimar, String username) {
        ConectorViviendaBD.eliminarDeFavoritos(botonElinimar, username);
    }

    public static ArrayList<String> ordenarFavoritos(String username, String orden) {
        return ConectorViviendaBD.ordenarFavoritos(username, orden);
    }

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

    public static boolean consultaInicial(String ciu, String tipo, int alqOVen) {
        return ConectorViviendaBD.consultaInicial(ciu, tipo, alqOVen);
    }

    public static boolean contraseñaCorrecta(String codigo, String contraseña) {
        return ConectorAgenciaBD.contraseñaCorrecta(codigo, contraseña);
    }

    public static void guardarAgente(Agente a) {
        ConectorAgenciaBD.guardarAgente(a);
    }

    public static boolean usuarioCorrecto(String nom, String pas) {
        Usuario u = ConectorUsuarioBD.getUsuario(nom);
        return pas.equals(u.getPassword());
    }

    public static int consultarPrecio(String id) {
        return ConectorViviendaBD.consultarPrecio(id);
    }

    public static void añadirUsuario(Usuario user) {
        ConectorUsuarioBD.añadirUsuario(user);
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

    public static ArrayList<String> crearListaRecomendados(String id) {
        return ConectorViviendaBD.crearListaRecomendados(id);
    }

    public static String consultarIdVivienda(String direccionFoto) {
        return ConectorViviendaBD.consultarIdVivienda(direccionFoto);
    }

    public static void añadirFavorito(Favoritos fav) {
        ConectorViviendaBD.añadirFavoritos(fav);
    }

    public static void eliminarFavorito(String id, String username) {
        ConectorViviendaBD.eliminarDeFavoritos(id, username);
    }

    public static void editarValoracion(int valoracion, String id, String username) {
        ConectorViviendaBD.editarValoracion(valoracion, id, username);
    }

    public static void desactivarVivienda(String id) {
        ConectorViviendaBD.desactivarVivienda(id);
    }
    
    public static ArrayList<String> viviendasDelUsusario(String username, String orden) {
        return ConectorViviendaBD.viviendasDelUsuario(username, orden);
    }
    public static void activarVivienda(String id){
        ConectorViviendaBD.activarVivienda(id);
    }
   

}
