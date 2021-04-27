/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify.controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import trobify.Conectar;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class RegistrarUsuarioController implements Initializable {

    @FXML
    private CheckBox agenteCheck;
    @FXML
    private Button registrarmeButton;
    @FXML
    private Button foto;

    Conectar con;
    private static Stage s;
    private String nom;
    private String pas;
    private static String vieneDe;
    private String direccion;
    private String dondeVa;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
        if(vieneDe.equals("buscador")){ 
           direccion = "/trobify/views/Buscador.fxml";
            dondeVa = "BuscadorController";}
       else {direccion = "/trobify/views/Inicio.fxml";
            dondeVa = "InicioController";}
    
    }    

   
    @FXML
    private void Registrarme(ActionEvent event) {
    }

    @FXML
    private void atras(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(direccion));
        s.close();
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        InicioController.pasarStage(stage);
        stage.setScene(scene);
        stage.setTitle("Trobify");
        stage.show();
        event.consume();
    }
    
     public static void pasarStage(Stage m){
         s = m;
        
     }
    public static void deDondeViene (String donde){
         vieneDe = donde;
    }

    @FXML
    private void tajeta(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/trobify/views/Tarjeta.fxml"));
        s.close();
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        TarjetaController.pasarStage(stage);
        stage.setScene(scene);
        stage.setTitle("Trobify");
        stage.show();
        event.consume();
    }
}
