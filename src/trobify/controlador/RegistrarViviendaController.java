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
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
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
import static jdk.nashorn.internal.objects.NativeArray.forEach;
import trobify.Conectar;
import static trobify.logica.ConectorFotosBD.añadirConjuntoFotos;
import static trobify.logica.ConectorServiciosBD.añadirServicios;
import static trobify.logica.ConectorViviendaBD.añadirVivienda;
import static trobify.logica.ConectorViviendaBD.numeroViviendas;
import trobify.logica.Fotografia;
import trobify.logica.Servicios;
import trobify.logica.Vivienda;

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
    private static int alquilerOVenta;
    private static int tipo;
    private static int supermercado = 0;
    private static int transportePublico = 0;
    private static int estanco = 0;
    private static int gimnasio = 0;
    private static int centroComercial = 0;
    private static int banco = 0;
    private static int farmacia = 0;
    
    private static ArrayList <String> FotosSource;
    private static ArrayList <Fotografia> fotos;
    
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
        
        registrarBoton.disableProperty().bind(sePuedeBuscar);
        
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
    private void atras(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/trobify/views/Buscador.fxml"));
        st.close();
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        BuscadorController.pasarStage(stage);
        stage.setScene(scene);
        stage.setTitle("Trobify");
        stage.show();
        event.consume();
    }

    @FXML
    private void añadirFoto(ActionEvent event) {
    }

    @FXML
    private void registrar(ActionEvent event) throws IOException {
        int numero = numeroViviendas() + 1;
        String id = "vivienda" + numero;
        int precio = Integer.parseInt(precioField.getText());
        int baños = Integer.parseInt(bañosField.getText());
        int habitaciones = Integer.parseInt(habitacionesField.getText());
        int piso = Integer.parseInt(habitacionesField.getText());
        int codigo = Integer.parseInt(codigoField.getText());
        
        
        if (ComprarAlquilar.getSelectionModel().selectedItemProperty().getValue().equals("comprar")) {alquilerOVenta = 1;}
        else {alquilerOVenta = 2;}
        
        if (TipoVivienda.getSelectionModel().selectedItemProperty().getValue().equals("casa")) {tipo = 2;}
        else {tipo = 1;}
        
        if(botonSupermercado.isPressed()) {supermercado = 1;}
        if(BotonTransportePublico.isPressed()) {transportePublico = 1;}
        if(botonEstanco.isPressed()) {estanco = 1;}
        if(botonGimnasio.isPressed()) {gimnasio = 1;}
        if(botonCentroComercial.isPressed()) {centroComercial = 1;}
        if(botonFarmacia.isPressed()) {farmacia = 1;}
        if(botonBanco.isPressed()) {banco = 1;}
        
        //codigo para pasar las fotos añadidas al array
        
        if(FotosSource.isEmpty()) {FotosSource.add("C:\\Users\\davido747\\Documents\\Uni\\SoftUpv\\src\\trobify\\images\\foto0.jpeg");}
        
        Vivienda vivi = new Vivienda(id, calleField.getText(), CiudadField.getText(), alquilerOVenta, "ninguna", precio, username, tipo, baños, habitaciones,
                descripcionField.getText(), piso, numeroField.getText(), codigo, 0);
        Servicios servi = new Servicios(id, supermercado, transportePublico, banco, estanco, centroComercial, gimnasio, farmacia);
        
        for (String f : FotosSource) {
            fotos.add( new Fotografia(f, id));
        }
        
        añadirVivienda(vivi);
        añadirServicios(servi);
        añadirConjuntoFotos(fotos);
        
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/trobify/views/Buscador.fxml"));
        st.close();
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        BuscadorController.pasarStage(stage);
        stage.setScene(scene);
        stage.setTitle("Trobify");
        stage.show();
        event.consume();
        
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
