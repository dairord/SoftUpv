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
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ObservableObjectValue;
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
import javafx.scene.control.DateCell;
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
    private TextField numHabitaciones;
    
    private static Stage s;
    private static String ciu;
    private static String tip;
    private static int alqOVen;
    private static ResultSet viviendas;
    
    //conexion
    Conectar con;
    @FXML
    private Label entradaText;
    @FXML
    private Label salidaText;
    @FXML
    private Button buscarBoton;
    @FXML
    private Button botonGuardarFiltros;
    @FXML
    private Label errorText;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    
     //boton buscar desactivado si no están todos los filtros
     final BooleanBinding sePuedeBuscar = Bindings.isEmpty(precioMin.textProperty())
               .or(Bindings.isEmpty(precioMax.textProperty()))
               .or(Bindings.isEmpty(ciudad.textProperty()))
               .or(Bindings.isEmpty(numBaños.textProperty()))
               .or(Bindings.isEmpty(numHabitaciones.textProperty()))
              // .or(Bindings.)
              ;
          
     buscarBoton.disableProperty().bind(sePuedeBuscar);
     botonGuardarFiltros.disableProperty().bind(sePuedeBuscar);
     
   //no poder seleccionar fechas anteriores a hoy
  fechaEntrada.setDayCellFactory((DatePicker picker) -> {
            return new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    LocalDate today = LocalDate.now();
                    setDisable(empty || date.compareTo(today) < 0 );
                }             
            };         
        });
  
  
  fechaSalida.setDayCellFactory((DatePicker picker) -> {
            return new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    LocalDate today = fechaEntrada.getValue();
                    setDisable(empty || date.compareTo(today) < 0 );
                }             
            };         
        });

     //no se puede introducir fecha de salida hasta que no esté la de entrada
      final BooleanBinding sePuedePonerFecha = Bindings.isNull(fechaEntrada.valueProperty());   
       fechaSalida.disableProperty().bind(sePuedePonerFecha);
     
      
    //si es comprar no salen las fechas
     if(alqOVen == 1){
         entradaText.setVisible(false);
         salidaText.setVisible(false);
         fechaEntrada.setVisible(false);
         fechaSalida.setVisible(false);
         variacionFecha.setVisible(false);
     }
    //Tipo Vivienda
    ArrayList <String> tiposViviendas = new ArrayList <String> ();
    tiposViviendas.add("Piso");
    tiposViviendas.add("Casa");
    tiposViviendas.add("Indiferente");
    ObservableList<String> viv = FXCollections.observableList(tiposViviendas);
    tipoVivienda.setItems(viv);
    
    //ordenar por
     ArrayList <String> orden = new ArrayList <String> ();
     orden.add("Relevancia");
     orden.add("Precio más bajo");
     orden.add("Precio más alto");
     ObservableList<String> ordenar = FXCollections.observableList(orden);
     ordenarPor.setItems(ordenar);
     ordenarPor.getSelectionModel().selectFirst();
    
    // variación filtros
 /*  ArrayList <String> varia = new ArrayList <String> ();
     varia.add("0%");
     varia.add("10%");
     varia.add("30%");
     varia.add("50%");
     ObservableList<String> variaFiltros = FXCollections.observableList(varia);
     variacionFiltros.setItems(variaFiltros);
     variacionFiltros.getSelectionModel().selectFirst();*/
    
     // variación fecha
    if(alqOVen != 1){
     ArrayList <String> varf = new ArrayList <String> ();
     varf.add("Fechas exactas");
     varf.add("± 1 dia");
     varf.add("± 3 dias");
     varf.add("± 7 dias");
     ObservableList<String> variaFecha = FXCollections.observableList(varf);
     variacionFecha.setItems(variaFecha);
     variacionFecha.getSelectionModel().selectFirst();
    } 
     //poner directamente el nombre de la ciudad buscada y tipo vivienda
     ciudad.setText(ciu);
     tipoVivienda.getSelectionModel().select(tip);
    
    //base de datos
    Conectar con = new Conectar();
    //este metodo devuelve la lista con las viviendas que cumplen los primeros filtros
    BuscadorController.viviendas = buscarOrdenadas();
  
    }
    
    @FXML
    private void guardarFiltros(ActionEvent event) throws IOException {
       if(comprobarNumeros()){
           errorText.setText("");
        FXMLLoader fxmlLoader = new FXMLLoader();
         fxmlLoader.setLocation(getClass().getResource("/trobify/views/MantenerFiltros.fxml"));
         MantenerFiltrosController.pasarFiltrosBuscar(ciudad.getText(), tipoVivienda.getSelectionModel().selectedItemProperty().getValue(), alqOVen,
                precioMin.getText(), precioMax.getText(), numBaños.getText(), numHabitaciones.getText(), fechaEntrada.getValue(), fechaSalida.getValue());
            Stage stage = new Stage();
             Scene scene = new Scene (fxmlLoader.load());
             MantenerFiltrosController.pasarStage(stage);
         
             stage.setScene(scene);
             stage.setTitle("Trobify");
             stage.show();
             event.consume();}
       else errorText.setText("Debes introducir números validos");
    }

    @FXML
    private void notificar(ActionEvent event) {
    }

    @FXML
    private void buscar(ActionEvent event) {
       if(comprobarNumeros()){
           errorText.setText("");
        viviendas = buscarOrdenadas();}
       else errorText.setText("Debes introducir números validos");
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
    
public static void pasarFiltrosInicio(String c, String t, int queBuscas){
         ciu = c;
         tip = t;
         alqOVen = queBuscas;
     }
    
 
private ResultSet buscarOrdenadas() {
     ResultSet viviendasOrdenadas;
        if(precioMin.getText().equals("") || precioMax.getText().equals("") || numHabitaciones.getText().equals("")
               || numBaños.getText().equals("")) 
            viviendasOrdenadas = ordenSinFinltrosConsulta();
       else
        viviendasOrdenadas = ordenarConsulta();
        return viviendasOrdenadas;
    }
     
     
    @FXML
private ResultSet ordenar(ActionEvent event) {
     if(comprobarNumeros()){
         errorText.setText("");
         ResultSet viviendasOrdenadas;
        if(precioMin.getText().equals("") || precioMax.getText().equals("") || numHabitaciones.getText().equals("")
               || numBaños.getText().equals("")) 
            viviendasOrdenadas = ordenSinFinltrosConsulta();
       else
        viviendasOrdenadas = ordenarConsulta();
        return viviendasOrdenadas;
    } else{
         errorText.setText("Debes introducir números validos");
         return null;
     } 
}
    
private ResultSet ordenarConsulta(){
       String comoOrdenar;
        if(ordenarPor.getSelectionModel().selectedItemProperty().getValue().equals("Relevancia")) comoOrdenar= "id";
        else 
            if(ordenarPor.getSelectionModel().selectedItemProperty().getValue().equals("Precio más bajo")) comoOrdenar= "precio DESC";
            else comoOrdenar = "precio ASC";
         
     ResultSet rs2;
     int tipo;
    if(tip.equals("Piso")) tipo = 1;
      else if(tip.equals("Casa")) tipo = 2;
      else tipo = 3;
       Conectar con = new Conectar();
       Statement s;
    if(tipo != 3){
    try {
            s = con.getConnection().createStatement();
            rs2 = s.executeQuery ("select * from vivienda where ciudad = '" + ciu + "' and tipo = "+ tipo +" and ventaAlquiler = " + alqOVen +
                     " and precio > " + Integer.valueOf(precioMin.getText()) + " and precio < " + Integer.valueOf(precioMax.getText()) + " and baños = " 
                     + Integer.valueOf(numBaños.getText()) 
                     + " and habitaciones = " + Integer.valueOf(numHabitaciones.getText())
                     + " order by " + comoOrdenar   ); //fin consulta
           if (rs2.first())   {
               System.out.println (rs2.getString("id"));
               return rs2;
        }
            
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }//fin catch
 
 } //fin if tipo!=3
    else{ //misma consulta pero sin mirar el tipo
    try {
            s = con.getConnection().createStatement();
            rs2 = s.executeQuery ("select * from vivienda where ciudad = '" + ciu + "' and ventaAlquiler = " + alqOVen +
                     " and precio > " + Integer.valueOf(precioMin.getText()) + " and precio < " + Integer.valueOf(precioMax.getText()) + " and baños = " 
                     + Integer.valueOf(numBaños.getText()) 
                     + " and habitaciones = " + Integer.valueOf(numHabitaciones.getText())
                     + " order by " + comoOrdenar 
                             ); //fin consulta
           if (rs2.first())   {
               System.out.println (rs2.getString("id"));
               return rs2;
        }
            
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }//fin catch
    }
  
      return null;
    } 
    
private ResultSet ordenSinFinltrosConsulta(){
      String comoOrdenar;
        if(ordenarPor.getSelectionModel().selectedItemProperty().getValue().equals("Relevancia")) comoOrdenar= "id";
        else 
            if(ordenarPor.getSelectionModel().selectedItemProperty().getValue().equals("Precio más bajo")) comoOrdenar= "precio DESC";
            else comoOrdenar = "precio ASC";
         
      ResultSet rs3;
     int tipo;
    if(tip.equals("Piso")) tipo = 1;
      else if(tip.equals("Casa")) tipo = 2;
      else tipo = 3;
       Conectar con = new Conectar();
      Statement s;
         if(tipo != 3){
    
        try {
           
            s = con.getConnection().createStatement();
            rs3 = s.executeQuery ("select * from vivienda where ciudad = '" + ciu + "' and tipo = "+ tipo +" and ventaAlquiler = " + alqOVen 
                + " order by " + comoOrdenar );
            if (rs3.first()) {
                    return rs3;}

        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
      } //fin if tip!=3
         else{
             try {
            s = con.getConnection().createStatement();
            rs3 = s.executeQuery ("select * from vivienda where ciudad = '" + ciu + "' and ventaAlquiler = " + alqOVen
                + " order by " + comoOrdenar);
            if (rs3.first()) {
                    return rs3;}

        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
   
       return null;
    }
    
 private boolean comprobarNumeros(){
     if(isNumeric(precioMin.getText())
             && isNumeric(precioMax.getText())
             && isNumeric(numBaños.getText())
             && isNumeric(numHabitaciones.getText())) return true;
     else return false;
 }
 
 private static boolean isNumeric(String cadena){
	try {
		Integer.parseInt(cadena);
		return true;
	} catch (NumberFormatException nfe){
		return false;
	}
}
}// fin clase
