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
import trobify.Conectar;

/**
 * FXML Controller class
 *
 * @author Usuario
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
    Conectar con;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      //sale el nombre del usuario
        usuario.setText(username);
        
     //conexion bd
      con = new Conectar();
        
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
      Statement s;
        try {
            s = con.getConnection().createStatement();
             ResultSet rs = s.executeQuery ("select codigo from agencia where codigo = '"
             + codigo.getText() + " ' and contraseña = '" + contraseña.getText() + " '");
            if (rs.first())   {
                System.out.println("funciona");
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    private void guardarAgente(){
       System.out.println(username);
        try {
         Statement stm = con.getConnection().createStatement();
                stm.executeUpdate("INSERT INTO `agente`(`username`, `agencia`) VALUES ('"
                        + username +"','" + codigo.getText() + "')");   
         
         } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
     }//fin if
    
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
