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
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author gabri
 */
public class EditarViviendaController implements Initializable {

    @FXML
    private TextField calleField;
    @FXML
    private TextField numeroField;
    @FXML
    private TextField pisoField;
    @FXML
    private TextField CiudadField;
    @FXML
    private TextField codigoField;
    @FXML
    private TextField precioField;
    @FXML
    private TextField habitacionesField;
    @FXML
    private TextField bañosField;
    @FXML
    private ComboBox<?> ComprarAlquilar;
    @FXML
    private ComboBox<?> TipoVivienda;
    @FXML
    private TextArea descripcionField;
    @FXML
    private HBox listaDeFotos;
    @FXML
    private RadioButton botonSupermercado;
    @FXML
    private RadioButton BotonTransportePublico;
    @FXML
    private RadioButton botonEstanco;
    @FXML
    private RadioButton botonGimnasio;
    @FXML
    private RadioButton botonCentroComercial;
    @FXML
    private RadioButton botonBanco;
    @FXML
    private RadioButton botonFarmacia;
    @FXML
    private Button registrarBoton;

    private static String vieneDe;
    private static Stage st;
    private static String username;
    private static String id;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        
    }    

    @FXML
    private void atras(ActionEvent event) throws IOException {
        FichaViviendaController.pasarUsuario(username);
        FichaViviendaController.pasarIdVivienda(id);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/trobify/views/FichaVivienda.fxml"));
        st.close();
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        FichaViviendaController.pasarStage(st);
        stage.setScene(scene);
        stage.setTitle("Trobify");
        stage.show();
        event.consume();
    }

    @FXML
    private void añadirFoto(ActionEvent event) {
    }

    @FXML
    private void registrar(ActionEvent event) {
    }
    
    public static void pasarStage(Stage s){
        st = s;
    }
    
    public static void pasarDatos (String u, String vivi){
        username = u;
        id = vivi;
    }
}

