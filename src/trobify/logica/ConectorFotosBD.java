/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify.logica;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import trobify.Conectar;

/**
 *
 * @author davido747
 */
public class ConectorFotosBD {
    private static Conectar con = new Conectar();
    
    public static void añadirFotografia (Fotografia f) {
        try {
            Statement stm = con.getConnection().createStatement();
            stm.executeUpdate("INSERT INTO fotografia (id, id_vivienda) VALUES ('"+f.getId()+"', '"+f.getId_vivienda()+"')");
        } catch (SQLException ex) {
            Logger.getLogger(ConectorFotosBD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void añadirConjuntoFotos (ArrayList <Fotografia> fotos) {
        for(Fotografia f: fotos) {
            añadirFotografia(f);
        }
    }
}
