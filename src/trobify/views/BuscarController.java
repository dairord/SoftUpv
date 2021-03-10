/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify.views;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author gabrielacorbalan
 */
public class BuscarController implements Initializable {

    @FXML
    private Label nombreUsuario;
    @FXML
    private DatePicker fechaEntrada;
    @FXML
    private DatePicker fechaSalida;
    @FXML
    private ComboBox<?> ordenarPor;
    @FXML
    private TextField ciudad;
    @FXML
    private ComboBox<?> variacionFecha;
    @FXML
    private ComboBox<?> tipoVivienda;
    @FXML
    private ComboBox<?> precioMin;
    @FXML
    private ComboBox<?> precioMax;
    @FXML
    private ComboBox<?> numHabitacoines;
    @FXML
    private ComboBox<?> numBaños;
    @FXML
    private ComboBox<?> variacionFiltros;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void guardarFiltros(ActionEvent event) {
    }

    @FXML
    private void notificar(ActionEvent event) {
    }

    @FXML
    private void buscar(ActionEvent event) {
    }
    
}
