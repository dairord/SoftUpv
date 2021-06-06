/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import trobify.controlador.InicioController;



/**
 *
 * @author dairo
 */
public class Trobify extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
         FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/trobify/views/Inicio.fxml"));
    //descomentamos esto cuando alguna clse vaya a esta    
    //s.close();
        stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        InicioController.pasarStage(stage);
        stage.setScene(scene);
        stage.setTitle("Trobify");
        stage.show();
      
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        launch(args);
        
     
    }
   
}
