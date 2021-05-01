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
import javafx.geometry.Pos;
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
import trobify.logica.ConectorViviendaBD;

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
    private static String username;
    
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
        orden();
                               
        //Mostrar nombre de usuario
        nombreUsuario.setText(username);
             
        
    }    
    
    
    //Generador de miniauras
    private javafx.scene.layout.HBox crearMiniatura(String id, String rutaFoto, String nombreCalle, int precioVivienda, int valoracionVivienda) throws FileNotFoundException{
        
        javafx.scene.layout.HBox miniatura = new javafx.scene.layout.HBox();  
        
        miniatura.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        
        Button botonRedireccion = new Button();
        botonRedireccion.setPadding(new Insets(0,0,0,0));
        botonRedireccion.setId(id);
        botonRedireccion.setOnAction( e-> {
            FichaViviendaController.pasarIdVivienda(botonRedireccion.getId());
            FichaViviendaController.deDondeViene("favoritos");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/trobify/views/FichaVivienda.fxml"));
            s.close();
            Stage stage = new Stage();
            Scene scene;
            try {
               FichaViviendaController.pasarUsuario(username);
                scene = new Scene(fxmlLoader.load());
                FichaViviendaController.pasarStage(stage);
                stage.setScene(scene);
                stage.setTitle("Trobify");
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(FavoritosController.class.getName()).log(Level.SEVERE, null, ex);
            }            
        });
                
        Image image1 = new Image(new FileInputStream(rutaFoto));
        javafx.scene.image.ImageView foto = new javafx.scene.image.ImageView(image1);
        foto.setFitWidth(200);
        foto.setFitHeight(150);
        
        botonRedireccion.setGraphic(foto);
                
        javafx.scene.layout.VBox datos = new javafx.scene.layout.VBox(10);
        datos.setAlignment(Pos.CENTER_LEFT);
        datos.setPadding(new Insets(20,30,30,15));
        
        javafx.scene.control.Label calle = new javafx.scene.control.Label("Calle: " + nombreCalle);
        javafx.scene.control.Label precio = new javafx.scene.control.Label("Precio: " + precioVivienda + "/mes");
        javafx.scene.control.Label valoracion = new javafx.scene.control.Label("Valoracion: " + valoracionVivienda);
        if(valoracionVivienda == -1) valoracion.setVisible(false);
        
        datos.getChildren().addAll(calle,precio,valoracion);
        
        javafx.scene.layout.VBox eliminarFav = new javafx.scene.layout.VBox(10);
        eliminarFav.setAlignment(Pos.CENTER);
        eliminarFav.setPadding(new Insets(20,20,20,25));
        
        Button botonEliminar = new Button();
        botonEliminar.setText("Eliminar de favoritos");
        botonEliminar.setId(id);
        botonEliminar.setOnAction(e -> {
         
            ConectorViviendaBD.eliminarDeFavoritos(botonEliminar.getId(), username);
            ordenCambiado(null);
        });
        
        eliminarFav.getChildren().add(botonEliminar);
        
        miniatura.getChildren().addAll(botonRedireccion, datos, eliminarFav);        
        return miniatura;
    }
    
    //Lista de viviendas
    private void ordenarLista(){
        for (int i = 0; i < favList.size(); ++i) {
            
            String idBoton = favList.get(i);
            
            try {
                String foto = ConectorViviendaBD.consultarFoto(favList.get(i));
                String calle = ConectorViviendaBD.consultarDireccion(favList.get(i));
                int precio = ConectorViviendaBD.consultarPrecio(favList.get(i));
                int valoracion = ConectorViviendaBD.consultarValoracion(favList.get(i), username);
                this.listaViviendas.getChildren().add(crearMiniatura(idBoton, foto, calle, precio, valoracion));
                
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FavoritosController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void ordenCambiado(ActionEvent event) {
        orden();
    }
   
    private void orden(){
    favList.clear();
        String orden = "";
        switch (elegirOrdenPor.getSelectionModel().selectedItemProperty().getValue()) {
            case "Relevancia":                
                listaViviendas.getChildren().clear();
                orden = "";
                favList = ConectorViviendaBD.ordenarFavoritos(username, orden);
                ordenarLista();
                break;
            case "Precio más bajo":
                listaViviendas.getChildren().clear();
                orden = "ORDER BY precio ASC";
                favList = ConectorViviendaBD.ordenarFavoritos(username, orden);
                ordenarLista();
                break;
            case "Precio más alto":
                listaViviendas.getChildren().clear();
                orden = "ORDER BY precio DESC";
                favList = ConectorViviendaBD.ordenarFavoritos(username, orden);
                ordenarLista();
                break;
            case "Valoracion":
                listaViviendas.getChildren().clear();
                orden = "ORDER BY valoracion DESC";
                favList = ConectorViviendaBD.ordenarFavoritos(username, orden);
                ordenarLista();
                break;
            default:
                break;
        }
    }
    
    @FXML
    private void InicioBoton(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/trobify/views/Buscador.fxml"));
        s.close();
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        BuscadorController.pasarStage(stage);
        stage.setScene(scene);
        stage.setTitle("Trobify");
        stage.show();
        event.consume();
    }
   
    public static void pasarStage(Stage m){
         s = m;
     }
    public static void pasarUsuario(String usu){
        username = usu;
    }
}
