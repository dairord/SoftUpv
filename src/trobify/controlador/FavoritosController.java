/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify.controlador;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import trobify.Conectar;

/**
 * FXML Controller class
 *
 * @author jagon
 */
public class FavoritosController implements Initializable {

    @FXML
    private Label nombreUsuario;
    @FXML
    private VBox listaViviendas;
    @FXML
    private ComboBox<String> elegirOrdenPor;
    private static Stage s;
    /**
     * Initializes the controller class.
     */
    
    Conectar con;
    ArrayList<String> favList;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //Crear una conexion
        con = new Conectar();        
        
        // Seleccion del orden de busqueda
        ArrayList<String> orden = new ArrayList();
        orden.add("Relevancia");
        orden.add("Precio más bajo");
        orden.add("Precio más alto");
        orden.add("Valoracion");
        
        ObservableList<String> ordenar = FXCollections.observableList(orden);
        elegirOrdenPor.setItems(ordenar);
        
        elegirOrdenPor.getSelectionModel().selectFirst();
        
        //Crear Array con la lista de favoritos, por defecto seleccionara LA PRIMERA opcion
        favList = new ArrayList();       
        switch (elegirOrdenPor.getSelectionModel().selectedItemProperty().getValue()) {
            case "Relevancia":
                ordenarPrecio();
                break;
            case "Precio más bajo":
                ordenarPrecioBajo();
                break;
            case "Precio más alto":
                ordenarPrecioAlto();
                break;
            case "Valoracion":
                ordenarValoracion();
                break;
            default:
                break;
        }
                               
        //Mostrar nombre de usuario
        nombreUsuario.setText("Patricio");
        
        ordenarLista();   
    }    
    
    
    //Generador de miniauras
    private javafx.scene.layout.HBox crearMiniatura(String id, String rutaFoto, String nombreCalle, int precioVivienda, int valoracionVivienda) throws FileNotFoundException{
        
        javafx.scene.layout.HBox miniatura = new javafx.scene.layout.HBox();
        
        miniatura.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        
        Button botonRedireccion = new Button();
        botonRedireccion.setPadding(new Insets(0,0,0,0));
        botonRedireccion.setId(id);
        
        Image image1 = new Image(new FileInputStream(rutaFoto));
        javafx.scene.image.ImageView foto = new javafx.scene.image.ImageView(image1);
        foto.setFitWidth(200);
        foto.setFitHeight(150);
        
        botonRedireccion.setGraphic(foto);
                
        javafx.scene.layout.VBox datos = new javafx.scene.layout.VBox(10);
        datos.setPadding(new Insets(20,30,30,15));
        
        javafx.scene.control.Label calle = new javafx.scene.control.Label("Calle: " + nombreCalle);
        javafx.scene.control.Label precio = new javafx.scene.control.Label("Precio: " + precioVivienda + "/mes");
        javafx.scene.control.Label valoracion = new javafx.scene.control.Label("Valoracion: " + valoracionVivienda);
        if(valoracionVivienda == -1) valoracion.setVisible(false);
        
        datos.getChildren().addAll(calle,precio,valoracion);
        
        javafx.scene.layout.VBox eliminarFav = new javafx.scene.layout.VBox(10);
        datos.setPadding(new Insets(20,30,30,15));
        
        
        
        miniatura.getChildren().addAll(botonRedireccion, datos, eliminarFav);        
        return miniatura;
    }
    
    //Consultar la primera foto de la vivienda pasando como atributo un id
    public String consultarFoto(String id){
        
        try {
            Statement stm = con.getConnection().createStatement();
            ResultSet rsl = stm.executeQuery("SELECT id FROM fotografia WHERE id_vivienda = '" + id + "'");
            if (rsl.first()) return rsl.getNString(1);
            
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "F:\\PSW\\SoftUpv\\src\\trobify\\images\\foto0.jpg";
        //"F:\\PSW\\SoftUpv\\src\\trobify\\images\\foto0.jpg"
    }
    
    //Consultar la direccion de la vivienda pasando como atributo un id    
    public String consultarDireccion(String id){
        
        try {
            Statement stm = con.getConnection().createStatement();
            ResultSet rsl = stm.executeQuery("SELECT direccion FROM vivienda WHERE id = '" + id + "'");
            if (rsl.first()) return rsl.getNString(1);
            
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "No disponible";
    }
    
    //Consultar el precio de la vivienda pasando como atributo un id    
    public int consultarPrecio(String id){
        
        try {
            Statement stm = con.getConnection().createStatement();
            ResultSet rsl = stm.executeQuery("SELECT precio FROM vivienda WHERE id = '" + id + "'");
            if (rsl.first()) return rsl.getInt(1);
            
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    //Consultar la valoracion fijada a la vivienda
    public int consultarValoracion(String id){
        
        int aux = -1;
        
        try {
            Statement stm = con.getConnection().createStatement();
            ResultSet rsl = stm.executeQuery("SELECT valoracion FROM favoritos WHERE id LIKE '" + id + "'");
            if (rsl.first()) aux = rsl.getInt(1);
            if(aux == 0) return -1;
            else return aux;
            
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return aux;
    }
     
    private void ordenarPrecio(){
        try {
            Statement stm = con.getConnection().createStatement();
            ResultSet rsl = stm.executeQuery ("SELECT id FROM favoritos");
            if(rsl.first()){
                rsl.beforeFirst();
                while (rsl.next()) {
                    favList.add(rsl.getString("id"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
     
    }
     
    private void ordenarPrecioBajo(){
        try {
            Statement stm = con.getConnection().createStatement();
            ResultSet rsl = stm.executeQuery ("SELECT f.id FROM favoritos f, vivienda v WHERE f.id = v.id ORDER BY precio ASC");
            if(rsl.first()){
                rsl.beforeFirst();
                while (rsl.next()) {
                    favList.add(rsl.getString("id"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
    private void ordenarPrecioAlto(){
        try {
            Statement stm = con.getConnection().createStatement();
            ResultSet rsl = stm.executeQuery ("SELECT f.id FROM favoritos f, vivienda v WHERE f.id = v.id ORDER BY precio DESC");
            if(rsl.first()){
                rsl.beforeFirst();
                while (rsl.next()) {
                    favList.add(rsl.getString("id"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void ordenarValoracion(){
        try {
            Statement stm = con.getConnection().createStatement();
            ResultSet rsl = stm.executeQuery ("SELECT f.id FROM favoritos f, vivienda v WHERE f.id = v.id ORDER BY valoracion DESC");
            if(rsl.first()){
                rsl.beforeFirst();
                while (rsl.next()) {
                    favList.add(rsl.getString("id"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
    //Lista de viviendas
    private void ordenarLista(){
        for (int i = 0; i < favList.size(); ++i) {
            
            String idBoton = favList.get(i);
            
            try {
                String foto = consultarFoto(favList.get(i));
                String calle = consultarDireccion(favList.get(i));
                int precio = consultarPrecio(favList.get(i));
                int valoracion = consultarValoracion(favList.get(i));
                this.listaViviendas.getChildren().add(crearMiniatura(idBoton, foto, calle, precio, valoracion));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FavoritosController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void ordenCambiado(ActionEvent event) {
        
        favList.clear();
        
        switch (elegirOrdenPor.getSelectionModel().selectedItemProperty().getValue()) {
            case "Relevancia":                
                listaViviendas.getChildren().clear();
                ordenarPrecio();
                ordenarLista();
                break;
            case "Precio más bajo":
                listaViviendas.getChildren().clear();
                ordenarPrecioBajo();
                ordenarLista();
                break;
            case "Precio más alto":
                listaViviendas.getChildren().clear();
                ordenarPrecioAlto();
                ordenarLista();
                break;
            case "Valoracion":
                listaViviendas.getChildren().clear();
                ordenarValoracion();
                ordenarLista();
                break;
            default:
                break;
        }
    }

    @FXML
    private void InicioBoton(ActionEvent event) throws IOException {
         FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/trobify/views/Inicio.fxml"));
        s.close();
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        BuscadorController.pasarStage(stage);
        stage.setScene(scene);
        stage.setTitle("Trobify");
        stage.show();
        event.consume();
    }
}
