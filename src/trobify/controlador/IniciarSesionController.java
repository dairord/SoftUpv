/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import trobify.fachada.FachadaBD;

/**
 * FXML Controller class
 *
 * @author gabrielacorbalan
 */
public class IniciarSesionController implements Initializable {

    @FXML
    private Label mensajeError;
    @FXML
    private TextField nombre;
    @FXML
    private PasswordField contraseña;
    @FXML
    private Button aceptarBoton;

    //Conexion con;
    private static Stage st;
    private String nom;
    private String pas;
    private static String vieneDe;
    private String direccion;

    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //boton iniciar apagado si no hay datos
        final BooleanBinding sePuedeBuscar = Bindings.isEmpty(nombre.textProperty())
               .or(Bindings.isEmpty(contraseña.textProperty()));
                ;
          aceptarBoton.disableProperty().bind(sePuedeBuscar);
       //con = new Conexion();
       
       if(vieneDe.equals("buscador")){ 
           direccion = "/trobify/views/Buscador.fxml";
            }
       else {direccion = "/trobify/views/Inicio.fxml";
            }
    }    

    @FXML
    private void atras(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(direccion));
        st.close();
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        InicioController.pasarStage(stage);
        BuscadorController.pasarStage(stage);
        stage.setScene(scene);
        stage.setTitle("Trobify");
        stage.show();
        event.consume();
    }

    @FXML
    private void aceptar(ActionEvent event) throws IOException {
      if(consulta()){
        InicioController.pasarUsuario(true, nom);
        BuscadorController.pasarUsuario(true, nom);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(direccion));
        st.close();
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        InicioController.pasarStage(stage);
        BuscadorController.pasarStage(stage);
        stage.setScene(scene);
        stage.setTitle("Trobify");
        stage.show();
        event.consume();
      }
    }
   
    public static void pasarStage(Stage m){
         st = m;
        
     }
    public static void deDondeViene (String donde){
         vieneDe = donde;
    }
    
    public boolean consulta(){
         nom = nombre.getText();
         pas = contraseña.getText();
       Statement s;
        if(FachadaBD.comprobarContraseñaDelUsuarioCorrecta(nom, pas)){
            return true;
        } else {
            mensajeError.setText("Nombre de usuario o contraseña incorrecto");
            return false;
        }
    }
    
    
}
