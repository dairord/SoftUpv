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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import trobify.Conectar;

/**
 * FXML Controller class
 *
 * @author davido747
 */
public class RegistrarViviendaController implements Initializable {

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
    private ComboBox<String> ComprarAlquilar;
    @FXML
    private ComboBox<String> TipoVivienda;
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
    
    Conectar con;
    private static Stage st;
    private static String username;
    
    @FXML
    private Button registrarBoton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        con = new Conectar();
        rellenoComboBox();
        
        //El botón registrar no está disponible hasta rellenar los campos obligatorios
        final BooleanBinding sePuedeBuscar = Bindings.isEmpty(calleField.textProperty())
               .or(Bindings.isEmpty(numeroField.textProperty()))
                 .or(Bindings.isEmpty(pisoField.textProperty()))
                 .or(Bindings.isEmpty(CiudadField.textProperty()))
                 .or(Bindings.isEmpty(codigoField.textProperty()))
                 .or(Bindings.isEmpty(precioField.textProperty()))
                 .or(Bindings.isEmpty(habitacionesField.textProperty()))
                 .or(Bindings.isEmpty(bañosField.textProperty()));
        
        registrarBoton.visibleProperty().bind(sePuedeBuscar);
        
        //Los field numericos no aceptan otra cosa que no numeros sean
        codigoField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
            String newValue) {
                if (!newValue.matches("\\d*")) {
                codigoField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        
        habitacionesField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
            String newValue) {
                if (!newValue.matches("\\d*")) {
                habitacionesField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        
        bañosField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
            String newValue) {
                if (!newValue.matches("\\d*")) {
                bañosField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        
        precioField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
            String newValue) {
                if (!newValue.matches("\\d*")) {
                precioField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        
        pisoField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, 
            String newValue) {
                if (!newValue.matches("\\d*")) {
                pisoField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        
        
    }    

    @FXML
    private void atras(ActionEvent event) {
    }

    @FXML
    private void añadirFoto(ActionEvent event) {
    }

    @FXML
    private void registrar(ActionEvent event) {
    }

    private void rellenoComboBox() {
        
        ArrayList<String> tiposViviendas = new ArrayList<String>();
        tiposViviendas.add("Piso");
        tiposViviendas.add("Casa");
        ObservableList<String> viv = FXCollections.observableList(tiposViviendas);
        TipoVivienda.setItems(viv);
        
        ArrayList <String> queHacer = new ArrayList <String> ();
        queHacer.add("Comprar");
        queHacer.add("Alquilar");
        ObservableList<String> caoc = FXCollections.observableList(queHacer);
        ComprarAlquilar.setItems(caoc);
    }
    
    public static void pasarUsuario(String usuario) {
        username = usuario;
    }
    
    public static void pasarStage(Stage m) {
        st = m;
    }
    
}
