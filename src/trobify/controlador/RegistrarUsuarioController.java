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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import trobify.Conectar;
import trobify.logica.Usuario;
import trobify.logica.ConectorUsuarioBD;

/**
 * FXML Controller class
 *
 * @author Usuario
 */
public class RegistrarUsuarioController implements Initializable {

    @FXML
    private CheckBox agenteCheck;
    @FXML
    private Button foto;

    Conectar con;
    private static Stage st;
    private static String vieneDe;
    private String direccion;
    
    @FXML
    private TextField username;
    @FXML
    private Label usuIncorrecto;
    @FXML
    private PasswordField contraseña;
    @FXML
    private PasswordField repetirContraseña;
    @FXML
    private Label contraIncorrecta;
    @FXML
    private TextField nombre;
    @FXML
    private TextField apellidos;
    @FXML
    private TextField dni;
    @FXML
    private Button registrarmeBoton;
    @FXML
    private Label errorText;
    @FXML
    private TextField email;
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      con = new Conectar();
    //ruta para volver atras
        if(vieneDe.equals("buscador")){ 
           direccion = "/trobify/views/Buscador.fxml";
            }
       else {direccion = "/trobify/views/Inicio.fxml";
           }
    
    //boton registrar desactivado hasta que haya datos
         final BooleanBinding sePuedeBuscar = Bindings.isEmpty(username.textProperty())
               .or(Bindings.isEmpty(contraseña.textProperty()))
                 .or(Bindings.isEmpty(repetirContraseña.textProperty()))
                 .or(Bindings.isEmpty(nombre.textProperty()))
                 .or(Bindings.isEmpty(apellidos.textProperty()))
                 .or(Bindings.isEmpty(dni.textProperty()))
                 .or(Bindings.isEmpty(email.textProperty()))
                 
                 ;
         registrarmeBoton.disableProperty().bind(sePuedeBuscar);
     
        
    }    

   private boolean errorContraseña(){
       if(!contraseña.getText().equals(repetirContraseña.getText())) {
           repetirContraseña.setText("");
           contraIncorrecta.setText(" *");
           errorText.setText("Debes introducir la misma contraseña.");
           return false;
       }
       return true;
   }
   
   private boolean usuarioNoRepetido(){
      if(!ConectorUsuarioBD.usuarioRepetido(username.getText())){
           usuIncorrecto.setText(" *");
           errorText.setText("Nombre de usuario ya en uso.");
           return false;
       }
      return true;
   }
    
    @FXML
    private void Registrarme(ActionEvent event) throws IOException  {
       if(usuarioNoRepetido() && errorContraseña()){
           Usuario nuevo = new Usuario(username.getText(), dni.getText(), contraseña.getText(),
                nombre.getText(), apellidos.getText(), email.getText(), null);
           ConectorUsuarioBD.añadirUsuario(nuevo);
           if(agenteCheck.isSelected()) esAgente();
           else estaRegistrado();
     }//fin if
       
    } // fin metodo
    
    //volver al inicio o buscador con la sesion iniciada
    private void estaRegistrado() throws IOException{
        InicioController.pasarUsuario(true, username.getText());
        BuscadorController.pasarUsuario(true, username.getText());
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
    
    //ir a la pagina para registrarse como agente
    private void esAgente() throws IOException{
        RegistroAgenteController.pasarDatos(true, username.getText(), direccion);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/trobify/views/RegistroAgente.fxml"));
        st.close();
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        RegistroAgenteController.pasarStage(stage);
        stage.setScene(scene);
        stage.setTitle("Trobify");
        stage.show();
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
    
     public static void pasarStage(Stage m){
         st = m;  
     }
     
    public static void deDondeViene (String donde){
         vieneDe = donde;
    }

    @FXML
    private void tajeta(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/trobify/views/Tarjeta.fxml"));
        st.close();
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        TarjetaController.pasarStage(stage);
        stage.setScene(scene);
        stage.setTitle("Trobify");
        stage.show();
        event.consume();
    }
}
