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
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author jagon
 */
public class FichaViviendaController implements Initializable {

    @FXML
    private Button volver;
    @FXML
    private Text descripcion;
    @FXML
    private ListView<?> serviciosCerca;
    @FXML
    private HBox imageList;
    @FXML
    private VBox recomendados;

    private static Stage s;
    /**
     * Initializes the controller class.
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {     
        
        //Boton de volver atras        
            //volver.setOnAction(e -> scenePropia.setScene(scenaPrevia));
        
        //Descripcion de la vivienda
        descripcion.setText("Redpiso Primado Reig les ofrece esta vivienda en alquiler, "
                + "DISPONIBLE A PARTIR DE MARZO. La vivienda se encuentra en una primera planta "
                + "sin ascensor y consta de 3 habitaciones, dos de ellas dobles, salón comedor, cocina "
                + "independiente con galería, baño completo. El piso cuenta con buena luminosidad y ventilación ya que es completamente exterior.\n" 
                + "La finca en la que se encuentra la vivienda cuenta con una zona de jardin privado.\n" 
                + "LOCALIZACIÓN VIVIENDA: A 150 metros de la parada del tranvía, cercana a paradas de autobús, a 200 "
                + "metros del PARQUE DE VIVEROS y muy próxima al Real Club de Tenis Valencia y La Hípica. Ven a visitar esta vivienda, "
                + "¡estaremos encantados de conocerte!");
        
        //Servicios cerca de la vivienda        
        ArrayList serviciosCerca = new ArrayList();
        serviciosCerca.add("Supermercado");
        serviciosCerca.add("Transporte publico");
        serviciosCerca.add("Farmacia");
        serviciosCerca.add("Estanco");
        serviciosCerca.add("Gimnasio");
        
        ObservableList servicios = FXCollections.observableList(serviciosCerca);     
        this.serviciosCerca.setItems(servicios);
        
        //Imagenes mostradas de la vivienda        
        for (int i = 0; i < 3; i++){            
            imageList.getChildren().add(galeria("/trobify/views/_FI.jpg"));        
        }
        
        //Viviendas recomendadas
        for (int i = 0; i < 3; i++){
            recomendados.getChildren().add(galeria("/trobify/views/_FI.jpg"));        
        }
        
    }    
    
    //Generador de la galeria de fotos
    private javafx.scene.image.ImageView galeria(String source){
                
        Image image = new Image(source);
        javafx.scene.image.ImageView fotoGaleria = new javafx.scene.image.ImageView(image);
        fotoGaleria.setFitWidth(200);
        fotoGaleria.setFitHeight(150);
        
        return fotoGaleria;
    
    }
     public static void pasarStage(Stage m){
         s = m;
     }
}
