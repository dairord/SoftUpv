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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import trobify.logica.ConectorServiciosBD;
import trobify.logica.ConectorViviendaBD;
import trobify.logica.Servicios;
import trobify.logica.Vivienda;

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
    @FXML
    private Button registrarBoton;

   private static Stage st;
    private static String username;
    private static String id;
    Vivienda vivi;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
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
       
        autorellenoDatos();
        mostrarServicios();
    }    
    private void autorellenoDatos(){
        vivi = ConectorViviendaBD.vivienda(id);
        calleField.setText(vivi.getCalle());
        numeroField.setText(vivi.getPuerta());
        pisoField.setText(vivi.getPiso()+ " ");
        CiudadField.setText(vivi.getCiudad());
        codigoField.setText(vivi.getCodigo_postal()+ "");
        precioField.setText(vivi.getPrecio()+ "");
        habitacionesField.setText(vivi.getHabitaciones() + "");
        bañosField.setText(vivi.getBaños()+ " ");
        pisoOCasa();
        comprarOVender();
        descripcionField.setText(vivi.getDescripcion() + "");
        
    }
    private void pisoOCasa(){
        if(vivi.getTipo()==1) TipoVivienda.getSelectionModel().select("Piso");
        else TipoVivienda.getSelectionModel().select("Casa");
    }
    
    private void comprarOVender(){
        if(vivi.getVentaAlquiler()==1) ComprarAlquilar.getSelectionModel().select("Comprar");
        else ComprarAlquilar.getSelectionModel().select("Alquilar");
    }
    private void mostrarServicios(){
         Servicios servi = ConectorServiciosBD.servicios(id);
       System.out.println("gabriela "+ servi.getBanco());
        if(servi.getSupermercado()==1) botonSupermercado.selectedProperty().set(true);
        if(servi.getTransporte_publico()==1) BotonTransportePublico.selectedProperty().set(true);
        if(servi.getBanco()==1) botonBanco.selectedProperty().set(true);
        if(servi.getCentro_comercial()==1) botonCentroComercial.selectedProperty().set(true);
        if(servi.getGimnasio()==1) botonGimnasio.selectedProperty().set(true);
        if(servi.getFarmacia()==1) botonFarmacia.selectedProperty().set(true);
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
     
    
    

    @FXML
    private void atras(ActionEvent event) throws IOException {
        FichaViviendaController.pasarUsuario(username);
        FichaViviendaController.pasarIdVivienda(id);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/trobify/views/FichaVivienda.fxml"));
        st.close();
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        FichaViviendaController.pasarStage(stage);
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

