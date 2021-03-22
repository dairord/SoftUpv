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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import trobify.Conectar;

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

    private static Stage s;
    private static int alqOVen;
   
    
    //base de datos
    Conectar con;
    ResultSet rs;
    
    @FXML
    private Label mensajeError;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      
    
   //tipos de viviendas gabri
     ArrayList <String> tiposViviendas = new ArrayList <String> ();
     tiposViviendas.add("Indiferente");
     tiposViviendas.add("Piso");
     tiposViviendas.add("Casa");
   
    ObservableList<String> viviendas = FXCollections.observableList(tiposViviendas);
    tipo.setItems(viviendas);
    
    //comprar alquilar o compartir gabri
    
     ArrayList <String> queHacer = new ArrayList <String> ();
     queHacer.add("Comprar");
     queHacer.add("Alquilar");
   
    ObservableList<String> caoc = FXCollections.observableList(queHacer);
    queBuscas.setItems(caoc);
      
    //activar y desactivar boton buscar gabri
        
    final BooleanBinding sePuedeBuscar = Bindings.isEmpty(ciudadText.textProperty())
               .or(Bindings.isNull(tipo.getSelectionModel().selectedItemProperty()))
               .or(Bindings.isNull(queBuscas.getSelectionModel().selectedItemProperty()))
                ;
          buscarBoton.disableProperty().bind(sePuedeBuscar);
          
          /* Este es el codigo que hay que copiar para las consultas
       //base de datos
    Conectar con = new Conectar();
      
      //CODIGO PARA LAS CONSULTAs  
       Statement s;
        try {
            s = con.getConnection().createStatement();
             ResultSet rs = s.executeQuery ("select usuario from usuario");
             while (rs.next())
        {
                   System.out.println (rs.getString("usuario"));
        }
            
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        */
     
    }
        
      
    @FXML
    private void inicia(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
         fxmlLoader.setLocation(getClass().getResource("/trobify/views/IniciarSesion.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene (fxmlLoader.load());
             
            IniciarSesionController.pasarStage(stage);
            stage.setScene(scene);
            stage.setTitle("Trobify");
            stage.show();
            event.consume();
    }

   
    @FXML
    private void buscar(ActionEvent event) throws IOException {
     if(consulta()){
        FXMLLoader fxmlLoader = new FXMLLoader();
         fxmlLoader.setLocation(getClass().getResource("/trobify/views/Buscador.fxml"));
         BuscadorController.pasarFiltrosInicio(ciudadText.getText(), tipo.getSelectionModel().selectedItemProperty().getValue(), alqOVen);
         s.close();
            Stage stage = new Stage();
             Scene scene = new Scene (fxmlLoader.load());
             BuscadorController.pasarStage(stage);
             stage.setScene(scene);
             stage.setTitle("Buscar vivienda");
             stage.show();
             event.consume();
         }
     else mensajeError.setText("No existe ninguna vivienda con esas características");
    }
    
    public static void pasarStage(Stage m){
         s = m;
     }
 
 public boolean consulta(){
    String ciu = ciudadText.getText();
    int tip;
      if(tipo.getSelectionModel().selectedItemProperty().getValue().equals("Piso")) tip = 1;
      else if(tipo.getSelectionModel().selectedItemProperty().getValue().equals("Casa")) tip = 2;
      else tip = 3;
      
      if(queBuscas.getSelectionModel().selectedItemProperty().getValue().equals("Comprar")) alqOVen = 1;
      else alqOVen = 2;
      System.out.println(alqOVen);
      Conectar con = new Conectar();
      Statement s;  
    
      if(tip != 3){
     try {
            s = con.getConnection().createStatement();
             ResultSet rs = s.executeQuery ("select * from vivienda where ciudad = '" + ciu + "' and tipo = "+ tip +" and ventaAlquiler = " + alqOVen );
            if ( !rs.first()) return false;
           
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
      } //fin if tip!=3
      else{
        try {
            s = con.getConnection().createStatement();
             ResultSet rs = s.executeQuery ("select * from vivienda where ciudad = '" + ciu + "' and ventaAlquiler = " + alqOVen );
            if ( !rs.first()) return false;
           
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
      }//fin else
          
     
       return true;
      
 }
 
}
