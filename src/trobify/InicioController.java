/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author gabrielacorbalan
 */
public class InicioController implements Initializable {

    @FXML
    private Button comprarBoton;
    @FXML
    private Button alquilarBoton;
    @FXML
    private Button compartirBoton;
    @FXML
    private ComboBox<String> tipo;
    @FXML
    private TextField ciudadText;
    @FXML
    private Button iniciaBoton;
    @FXML
    private Hyperlink registrarse;
    @FXML
    private Button buscarBoton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void comprar(ActionEvent event) {
    }

    @FXML
    private void alquilar(ActionEvent event) {
    }

    @FXML
    private void compartir(ActionEvent event) {
    }

    @FXML
    private void inicia(ActionEvent event) {
    }

    @FXML
    private void buscar(ActionEvent event) {
    }
    
}
