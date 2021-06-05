/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import trobify.conectores.Conexion;
import trobify.logica.Agente;
import trobify.fachada.FachadaBD;

/**
 * FXML Controller class
 *
 * @author gabri
 */
public class RegistroAgenteController implements Initializable {

    private static Stage st;
    private static boolean estaIniciado;
    private static String username;
    private static String direccion;
    @FXML
    private Label usuario;
    @FXML
    private TextField codigo;
    @FXML
    private PasswordField contraseña;
    @FXML
    private Label errorText;
    @FXML
    private Button confirmarBoton;
    Conexion con;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      //sale el nombre del usuario
        usuario.setText(username);

      
      //boton desactivado
       final BooleanBinding bb = Bindings.isEmpty(codigo.textProperty())
               .or(Bindings.isEmpty(contraseña.textProperty()))  ;
         confirmarBoton.disableProperty().bind(bb);
       
    }    
    
     public static void pasarStage(Stage m){
         st = m;
        
     }
     public static void pasarDatos (boolean iniciado, String usuario, String dir){
     estaIniciado = iniciado;
     username = usuario;
     direccion = dir;
        
    }

    
    @FXML
    private void cancelar(ActionEvent event) throws IOException {
        InicioController.pasarUsuario(true, username);
        BuscadorController.pasarUsuario(true, username);
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
    private void confirmar(ActionEvent event) throws IOException {
        if(contraseñaCorrecta()){
           guardarAgente();
           volver();
        }
        else 
            errorText.setText("Codigo o contraseña incorrectos.");
    }
    
    private boolean contraseñaCorrecta(){
        if(FachadaBD.contraseñaCorrecta(codigo.getText(), contraseña.getText())) return true;
        else return false;
    }
    
    private void guardarAgente(){
       Agente nuevo = new Agente (username, codigo.getText());
        FachadaBD.guardarAgente(nuevo);
     }//fin guardar agete
    
    private void volver() throws IOException{
        InicioController.pasarUsuario(true, username);
        BuscadorController.pasarUsuario(true, username);
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
    }
}
