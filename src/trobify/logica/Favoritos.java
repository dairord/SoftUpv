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
import java.util.logging.Level;
import java.util.logging.Logger;
import trobify.Conectar;
import trobify.controlador.InicioController;

/**
 *
 * @author gabri
 */
public class Favoritos {
    String id, id_cliente;
    int valoracion;
     private static Conectar con = new Conectar();

    public Favoritos(String id, String id_cliente, int valoracion) {
        this.id = id;
        this.id_cliente = id_cliente;
        this.valoracion = valoracion;
    }

    public String getId() {
        return id;
    }

    public String getId_cliente() {
        return id_cliente;
    }

    public int getValoracion() {
        return valoracion;
    }
    
    
    
    
}
