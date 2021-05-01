/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import trobify.Conectar;

/**
 * FXML Controller class
 *
 * @author gabri
 */
public class NotificacionesController implements Initializable {

    @FXML
    private ListView<String> lista;
    private static Stage st;
    private static String username;
    Conectar con;
    private String[] viviendas;
    ArrayList<String> favList;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       con = new Conectar();
       lista();
       
       favList = new ArrayList(); 
    }    

    @FXML
    private void atras(ActionEvent event) throws IOException {
        st.close();
    }
    
     public static void pasarStage(Stage m){
         st = m;  
     }
     
     public boolean consulta(){
         Statement s;
        try {
            s = con.getConnection().createStatement();
             ResultSet rs = s.executeQuery ("SELECT * FROM `favoritos` WHERE `id_cliente` = '" + username + "' AND `id` IN (SELECT `id` FROM `vivienda` WHERE `activo` = 1)");
            if (rs.first())   {
               rs.beforeFirst();
                while (rs.next()) {
                   System.out.println("hay algo");
                  //  favList.add("hola");
               }
             
               return true;
            } else{ 
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
         return false;
     }
     
    public static void pasarUsuario(String u){
        username = u;
    }
    
    private void lista(){
         for (int i = 0; i < favList.size(); ++i) {
             consulta();
         }
    }
}
