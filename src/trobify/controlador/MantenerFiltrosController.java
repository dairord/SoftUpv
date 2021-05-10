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
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import trobify.Conectar;
import trobify.fachada.FachadaBD;
import trobify.logica.Filtros;

/**
 * FXML Controller class
 *
 * @author gabrielacorbalan
 */

public class MantenerFiltrosController implements Initializable {

    private static Stage s;
    private static Boolean hayFechas;
    private static Filtros f;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
              
    }    

    @FXML
    private void siBoton(ActionEvent event) throws IOException {
            FachadaBD.guardarFiltros(f, hayFechas);
         
        Alert alerta = new Alert (Alert.AlertType.INFORMATION);
        alerta.setHeaderText("Filtros guardados correctamente!");
        Optional<ButtonType> resultado = alerta.showAndWait();
        if(resultado.isPresent() && resultado.get().equals(ButtonType.OK)) {
            s.close();
            alerta.close();
           
    } alerta.close();
    }

    @FXML
    private void noBoton(ActionEvent event) throws IOException {
          s.close();
          
    }
     public static void pasarStage(Stage m){
         s = m;
     }
     
     //Para obtener los filtros desde la pantalla de buscar
     public static void pasarFiltrosBuscar(Filtros filtro){
         f = filtro;
         if(f.getFecha_entrada() == null || f.getFecha_salida() == null) {hayFechas = false;}
         else {hayFechas = true;}
     }
     
     
     
     
}
