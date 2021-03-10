/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify.controlador;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author jagon
 */
public class FavoritosController implements Initializable {

    @FXML
    private Label nombreUsuario;
    @FXML
    private ChoiceBox<String> elegirOrden;
    @FXML
    private VBox listaViviendas;

    private static Stage s;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //Mostrar nombre de usuario
        nombreUsuario.setText("Patricio");
        
        // Seleccion del orden de busqueda
        ArrayList orden = new ArrayList();
        orden.add("Relevancia");
        orden.add("Precio más bajo");
        orden.add("Precio más alto");
        
        ObservableList<String> ordenar = FXCollections.observableList(orden);
        elegirOrden.setItems(ordenar);
        
        elegirOrden.getSelectionModel().selectFirst();
        
        //Lista de viviendas        
        for (int i = 0; i < 5; ++i) {
        this.listaViviendas.getChildren().add(crearMiniatura());
        }
        
    }    
    
    //Generador de miniauras
    private javafx.scene.layout.HBox crearMiniatura(){
        
        javafx.scene.layout.HBox miniatura = new javafx.scene.layout.HBox();
        
        miniatura.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        
        Image image1 = new Image("/trobify/views/_FI.jpg");
        javafx.scene.image.ImageView foto = new javafx.scene.image.ImageView(image1);
        foto.setFitWidth(200);
        foto.setFitHeight(150);
                
        javafx.scene.layout.VBox datos = new javafx.scene.layout.VBox(10);
        datos.setPadding(new Insets(20,30,30,15));
        
        javafx.scene.control.Label calle = new javafx.scene.control.Label("Calle: " + "Nombre de la calle");
        javafx.scene.control.Label precio = new javafx.scene.control.Label("Precio: " + "Precui de la vivienda" + "/mes");
        javafx.scene.control.Label valoracion = new javafx.scene.control.Label("Valoracion: " + "Valoracion de la vivienda");
        datos.getChildren().addAll(calle,precio,valoracion);
        
        miniatura.getChildren().addAll(foto, datos);        
        return miniatura;
    }
     public static void pasarStage(Stage m){
         s = m;
     }
}
