/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify.controlador;

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
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import trobify.Conectar;

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

    Conectar con;
    private static Stage st;
    private String[] usuarios;
    private String nom;
    private String pas;
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
       con = new Conectar();
    }    

    @FXML
    private void atras(ActionEvent event) {
        st.close();
    }

    @FXML
    private void aceptar(ActionEvent event) {
      consulta();
       
      
    }
   
    public static void pasarStage(Stage m){
         st = m;
     }
    
    public void consulta(){
    
         nom = nombre.getText();
         pas = contraseña.getText();
         Conectar con = new Conectar();
      
       Statement s;
        try {
            s = con.getConnection().createStatement();
             ResultSet rs = s.executeQuery ("select usuario from usuario where usuario = '"
             + nom + " ' and password = '" + pas + " '");
            if (rs.first())   {
                st.close(); 
                System.out.println (rs.getString("usuario"));
            } else mensajeError.setText("Nombre de usuario o contraseña incorrecto");
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
