/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify.controlador;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author gabrielacorbalan
 */
public class InicioController implements Initializable {

    @FXML
    private Button iniciaBoton;
    @FXML
    private Hyperlink registrarse;
   
    @FXML
    private TextField ciudadText;
    @FXML
    private Button buscarBoton;
    @FXML
    private ComboBox<String> queBuscas;
    @FXML
    private ComboBox<String> tipo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       //tipos de viviendas gabri
     ArrayList <String> tiposViviendas = new ArrayList <String> ();
     tiposViviendas.add("Piso");
     tiposViviendas.add("Casa");
    ObservableList<String> viviendas = FXCollections.observableList(tiposViviendas);
    tipo.setItems(viviendas);
    
    //comprar alquilar o compartir gabri
    
     ArrayList <String> queHacer = new ArrayList <String> ();
     queHacer.add("Comprar");
     queHacer.add("Alquilar");
     queHacer.add("Compartir");
   
    ObservableList<String> caoc = FXCollections.observableList(queHacer);
    queBuscas.setItems(caoc);
      
    //activar y desactivar boton buscar gabri
        
    final BooleanBinding sePuedeBuscar = Bindings.isEmpty(ciudadText.textProperty())
               .or(Bindings.isNull(tipo.getSelectionModel().selectedItemProperty()))
               .or(Bindings.isNull(queBuscas.getSelectionModel().selectedItemProperty()))
                ;
          buscarBoton.disableProperty().bind(sePuedeBuscar);

    }
        
      
    @FXML
    private void inicia(ActionEvent event) {
    }

    @FXML
    private void buscar(ActionEvent event) {
    }
    
}
