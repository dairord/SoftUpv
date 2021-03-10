/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify.controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author gabrielacorbalan
 */
public class BuscadorController implements Initializable {

    @FXML
    private Label nombreUsuario;
    @FXML
    private DatePicker fechaEntrada;
    @FXML
    private DatePicker fechaSalida;
    @FXML
    private ComboBox<String> ordenarPor;
    @FXML
    private TextField ciudad;
    @FXML
    private ComboBox<String> variacionFecha;
    @FXML
    private ComboBox<String> tipoVivienda;
    @FXML
    private TextField precioMin;
    @FXML
    private TextField precioMax;
    @FXML
    private TextField numBaños;
    @FXML
    private ComboBox<String> variacionFiltros;
    @FXML
    private TextField numHabitaciones;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       //Tipo Vivienda
        ArrayList <String> tiposViviendas = new ArrayList <String> ();
     tiposViviendas.add("Piso");
     tiposViviendas.add("Casa");
    ObservableList<String> viviendas = FXCollections.observableList(tiposViviendas);
    tipoVivienda.setItems(viviendas);
    
    //ordenar por
      ArrayList <String> orden = new ArrayList <String> ();
     orden.add("Relevancia");
     orden.add("Precio más bajo");
     orden.add("Precio más alto");
    ObservableList<String> ordenar = FXCollections.observableList(orden);
    ordenarPor.setItems(ordenar);
    ordenarPor.getSelectionModel().selectFirst();
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

    @FXML
    private void Inicio(ActionEvent event) throws IOException {
         FXMLLoader loader = new FXMLLoader(getClass().getResource("/trobify/views/Inicio.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
      //  scene.getStylesheets().add(this.getClass().getResource("/resources/anadir.css").toExternalForm());
        Stage stage = new Stage();
       

        stage.setScene(scene);
        stage.setTitle("Trobify");

        stage.showAndWait();
    }
    
}
