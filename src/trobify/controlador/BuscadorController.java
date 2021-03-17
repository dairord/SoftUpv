/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
import trobify.Conectar;

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
    
    private static Stage s;
    private static String ciu;
    private static String tip;
    
    //base de datos
    private static Connection conn;
    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String user = "root";
    private static final String password = "";
    private static final String url = "jdbc:mysql://localhost3306/soft_db";
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       //Tipo Vivienda
        ArrayList <String> tiposViviendas = new ArrayList <String> ();
     tiposViviendas.add("Piso");
     tiposViviendas.add("Casa");
     tiposViviendas.add("Indiferente");
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
    
    // variación filtros
    ArrayList <String> varia = new ArrayList <String> ();
     varia.add("0%");
     varia.add("10%");
     varia.add("30%");
     varia.add("50%");
     ObservableList<String> variaFiltros = FXCollections.observableList(varia);
     variacionFiltros.setItems(variaFiltros);
     variacionFiltros.getSelectionModel().selectFirst();
    
     // variación fecha
      ArrayList <String> varf = new ArrayList <String> ();
     varf.add("Fechas exactas");
     varf.add("± 1 dia");
     varf.add("± 3 dias");
     varf.add("± 7 dias");
     ObservableList<String> variaFecha = FXCollections.observableList(varf);
     variacionFecha.setItems(variaFecha);
     variacionFecha.getSelectionModel().selectFirst();
     
     //poner directamente el nombre de la ciudad buscada y tipo vivienda
     ciudad.setText(ciu);
     tipoVivienda.getSelectionModel().select(tip);
     
    Conectar con = new Conectar();
    
    }    
    
    @FXML
    private void guardarFiltros(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
         fxmlLoader.setLocation(getClass().getResource("/trobify/views/MantenerFiltros.fxml"));
            Stage stage = new Stage();
             Scene scene = new Scene (fxmlLoader.load());
             MantenerFiltrosController.pasarStage(stage);
         
             stage.setScene(scene);
             stage.setTitle("Trobify");
             stage.show();
             event.consume();
    }

    @FXML
    private void notificar(ActionEvent event) {
    }

    @FXML
    private void buscar(ActionEvent event) {
    }

    @FXML
    private void Inicio(ActionEvent event) throws IOException {
          FXMLLoader fxmlLoader = new FXMLLoader();
         fxmlLoader.setLocation(getClass().getResource("/trobify/views/Inicio.fxml"));
            s.close();
            Stage stage = new Stage();
             Scene scene = new Scene (fxmlLoader.load());
             BuscadorController.pasarStage(stage);
             stage.setScene(scene);
             stage.setTitle("Trobify");
             stage.show();
             event.consume();
    }
   // metodo para conseguir que al pasar de unas ventanas a otras se cierre la anterior. 
    // solo hay que añadir el metodo pasarStage y crear la variable private static Stage s en cada clase
    // y cada vez que cambias de ventana añadir s.close(); y pasarle el stage al controler de la  ventana a la que vas
    public static void pasarStage(Stage m){
         s = m;
     }
    
     public static void pasarFiltrosInicio(String c, String t){
         ciu = c;
         tip = t;
     }
}
