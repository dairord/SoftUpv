/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify.views;

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
import javafx.scene.text.Text;

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

    /**
     * Initializes the controller class.
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {     
        
        // Boton de volver atras        
            //volver.setOnAction(e -> scenePropia.setScene(scenaPrevia));
        
        // Descripcion de la vivienda
        descripcion.setText("Redpiso Primado Reig les ofrece esta vivienda en alquiler, "
                + "DISPONIBLE A PARTIR DE MARZO. La vivienda se encuentra en una primera planta "
                + "sin ascensor y consta de 3 habitaciones, dos de ellas dobles, salón comedor, cocina "
                + "independiente con galería, baño completo. El piso cuenta con buena luminosidad y ventilación ya que es completamente exterior.\n" 
                + "La finca en la que se encuentra la vivienda cuenta con una zona de jardin privado.\n" 
                + "LOCALIZACIÓN VIVIENDA: A 150 metros de la parada del tranvía, cercana a paradas de autobús, a 200 "
                + "metros del PARQUE DE VIVEROS y muy próxima al Real Club de Tenis Valencia y La Hípica. Ven a visitar esta vivienda, "
                + "¡estaremos encantados de conocerte!");
        
        // Servicios cerca de la vivienda        
        ArrayList serviciosCerca = new ArrayList();
        serviciosCerca.add("Supermercado");
        serviciosCerca.add("Transporte publico");
        serviciosCerca.add("Farmacia");
        serviciosCerca.add("Estanco");
        serviciosCerca.add("Gimnasio");
        
        ObservableList servicios = FXCollections.observableList(serviciosCerca);     
        this.serviciosCerca.setItems(servicios);
        
        // Imagenes mostradas de la vivienda
        Image image1 = new Image("/trobify/views/_FI.jpg");
        javafx.scene.image.ImageView foto1 = new javafx.scene.image.ImageView(image1);
        foto1.setFitWidth(200);
        foto1.setFitHeight(150);
        Image image2 = new Image("/trobify/views/_FI.jpg");
        javafx.scene.image.ImageView foto2 = new javafx.scene.image.ImageView(image2);
        foto2.setFitWidth(200);
        foto2.setFitHeight(150);
        Image image3 = new Image("/trobify/views/_FI.jpg");
        javafx.scene.image.ImageView foto3 = new javafx.scene.image.ImageView(image3);
        foto3.setFitWidth(200);
        foto3.setFitHeight(150);
        
        imageList.getChildren().addAll(foto1, foto2, foto3);
    }    
}
