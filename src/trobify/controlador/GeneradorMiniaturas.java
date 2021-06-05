/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify.controlador;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


/**
 *
 * @author gabri
 */
public abstract class GeneradorMiniaturas {    
    
  abstract public javafx.scene.layout.VBox crearDatos(String nombreCalle, int precioVivienda,
          String activo, int valoracion, javafx.scene.layout.VBox datos, int alquilada);
  abstract public javafx.scene.layout.VBox crearBoton(String id);
  abstract public javafx.scene.layout.HBox añadirAMiniatura(javafx.scene.layout.HBox miniatura, Button botonRedireccion, 
          javafx.scene.layout.VBox datos, javafx.scene.layout.VBox boton);
  abstract public void cambiarPantalla();
   
  //metodo plantilla
  final public javafx.scene.layout.HBox crearMiniaturas(String id, String rutaFoto, String nombreCalle, int precioVivienda, String username, String activo, int Valoracion, int alquilada) throws FileNotFoundException{
        
        javafx.scene.layout.HBox miniatura = new javafx.scene.layout.HBox();  
        miniatura.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        
        Button botonRedireccion = new Button();
        botonRedireccion.setPadding(new Insets(0,0,0,0));
        botonRedireccion.setId(id);
        botonRedireccion.setOnAction( e-> {
            FichaViviendaController.pasarIdVivienda(botonRedireccion.getId());
                 FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/trobify/views/FichaVivienda.fxml"));
           
           
            Stage stage = new Stage();
            Scene scene;
            FichaViviendaController.pasarUsuario(username);
            FichaViviendaController.pasarStage(stage);
        try {
            cambiarPantalla();
            scene = new Scene(fxmlLoader.load());
                stage.setScene(scene);
                stage.setTitle("Trobify");
                stage.show();
                } catch (IOException ex) {
            Logger.getLogger(GestionViviendasController.class.getName()).log(Level.SEVERE, null, ex);
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
        
        datos = crearDatos(nombreCalle, precioVivienda, activo, Valoracion , datos, alquilada);
        javafx.scene.layout.VBox boton = crearBoton(id);
        miniatura = añadirAMiniatura(miniatura, botonRedireccion, datos, boton);
      
        
      
      
       return miniatura;

    }
  
  }
