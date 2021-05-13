/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify.conectores;

import trobify.Conectar;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import trobify.controlador.InicioController;
import trobify.logica.Fotografia;

/**
 *
 * @author davido747
 */
public class ConectorFotosBD {
    
    public static void añadirFotografia (Fotografia f) {
        String sql = "INSERT INTO fotografia (id, id_vivienda) VALUES ('"+f.getId()+"', '"+f.getId_vivienda()+"')";
        Plantilla.consultaVoid(sql);
    }
    
    public static void añadirConjuntoFotos (ArrayList <Fotografia> fotos) {
        for(Fotografia f: fotos) {
            añadirFotografia(f);
        }
    }
    
      public static Fotografia fotografia(String id) {
       String sql = "SELECT * FROM fotografia WHERE id_vivienda = '" + id + "'";
          try {
            ResultSet rsl = Plantilla.soloConsulta(sql);
            if (rsl.first()) {
                Fotografia foto = new Fotografia(rsl.getNString("id"), rsl.getNString("id_vivienda"));
                return foto;
            }

        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
      
      public static String consultarFoto(String id) {
        Fotografia foto = fotografia(id);
     if (foto.getId() != null) {
            return foto.getId();
        } else {
            return "C:\\Users\\gabri\\Desktop\\gabri\\SoftUpv\\src\\trobify\\images\\foto0.jpeg";
        }
    }
}
