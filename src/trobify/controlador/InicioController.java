/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify.controlador;


import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javax.swing.JFrame;
import trobify.conectores.Conectar;
import trobify.fachada.FachadaBD;
import trobify.logica.Mensaje;

/**
 * FXML Controller class
 *
 * @author gabrielacorbalan
 */
public class InicioController implements Initializable {

    @FXML
    private Button iniciaBoton;
    @FXML
    private Hyperlink registrarse;

    @FXML
    private TextField ciudadText;
    @FXML
    private Button buscarBoton;
    @FXML
    private ComboBox<String> queBuscas;
    @FXML
    private ComboBox<String> tipo;

    private static Stage s;
    private static int alqOVen;
    private static boolean estaIniciado;
    private static String username;

    //para la busqueda
    private String tipvivi;
    //base de datos

    ResultSet rs;

    @FXML
    private Label mensajeError;
    @FXML
    private Label nombreUsuario;
    @FXML
    private HBox iniciado;
    @FXML
    private Button misViviendasBoton;
    @FXML
    private Label agente;
    @FXML
    private Button registrarV;
    @FXML
    private Button favoritos;
    @FXML
    private Button mensajes;
    @FXML
    private Button notificaciones;
    @FXML
    private ImageView fotoNotificacion;
    @FXML
    private Button reportarErroreresBoton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //si está iniciado sesión
        iniciado.setVisible(false);
        bienvenido();

        //tipos de viviendas gabri
        ArrayList<String> tiposViviendas = new ArrayList<String>();
        tiposViviendas.add("Indiferente");
        tiposViviendas.add("Piso");
        tiposViviendas.add("Casa");

        ObservableList<String> viviendas = FXCollections.observableList(tiposViviendas);
        tipo.setItems(viviendas);

        //comprar alquilar o compartir gabri
        ArrayList<String> queHacer = new ArrayList<String>();
        queHacer.add("Comprar");
        queHacer.add("Alquilar");

        ObservableList<String> caoc = FXCollections.observableList(queHacer);
        queBuscas.setItems(caoc);

        //activar y desactivar boton buscar gabri
        final BooleanBinding sePuedeBuscar = Bindings.isEmpty(ciudadText.textProperty())
                .or(Bindings.isNull(tipo.getSelectionModel().selectedItemProperty()))
                .or(Bindings.isNull(queBuscas.getSelectionModel().selectedItemProperty()));
        buscarBoton.disableProperty().bind(sePuedeBuscar);

    }

    private void hayNotis() {
        //falta un if con un boolean
        try {
            Image image1 = new Image(new FileInputStream("C:\\Users\\gabri\\Desktop\\gabri\\SoftUpv\\src\\trobify\\images\\notiActiva.png"));
            fotoNotificacion.setImage(image1);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BuscadorController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void inicia(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/trobify/views/IniciarSesion.fxml"));
        IniciarSesionController.deDondeViene("inicio");
        s.close();
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        IniciarSesionController.pasarStage(stage);
        stage.setScene(scene);
        stage.setTitle("Trobify");
        stage.show();
        event.consume();

    }

    @FXML
    private void buscar(ActionEvent event) throws IOException {
        if (comprobaciones() && consulta()) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/trobify/views/Buscador.fxml"));
            BuscadorController.pasarUsuario(estaIniciado, username);
            BuscadorController.pasarFiltrosInicio(ciudadText.getText(), tipo.getSelectionModel().selectedItemProperty().getValue(), alqOVen);
            BuscadorController.pasarLocalizacion(ciudadText.getText());
            s.close();
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            BuscadorController.pasarStage(stage);
            stage.setScene(scene);
            stage.setTitle("Buscar vivienda");
            stage.show();
            event.consume();
        } else {
            mensajeError.setText("No existe ninguna vivienda con esas características");
        }
    }

    public static void pasarStage(Stage m) {
        s = m;
    }

    private boolean comprobaciones() {
        //tippS
        if (tipo.getSelectionModel().selectedItemProperty().getValue().equals("Piso")) {
            tipvivi = " and tipo = 1";
        } else if (tipo.getSelectionModel().selectedItemProperty().getValue().equals("Casa")) {
            tipvivi = " and tipo = 2";
        } else {
            tipvivi = "";
        }
        //System.out.println(tipvivi + "kk");
        //alquilar o vender
        if (queBuscas.getSelectionModel().selectedItemProperty().getValue().equals("Comprar")) {
            alqOVen = 1;
        } else {
            alqOVen = 2;
        }
        return true;
    }

    public boolean consulta() {
        String ciu = ciudadText.getText();
        return FachadaBD.consultaInicial(ciu, tipvivi, alqOVen);
    }//fin consulta

    public static void pasarUsuario(boolean iniciado, String usuario) {
        estaIniciado = iniciado;
        username = usuario;

    }

    private void bienvenido() {
        if (estaIniciado) {
            nombreUsuario.setText("Bienvenido " + username);
            iniciaBoton.setVisible(false);
            registrarse.setVisible(false);
            iniciado.setVisible(true);
        }
    }

    @FXML
    private void registrar(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/trobify/views/RegistrarUsuario.fxml"));
        RegistrarUsuarioController.deDondeViene("inicio");
        s.close();
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        RegistrarUsuarioController.pasarStage(stage);
        stage.setScene(scene);
        stage.setTitle("Trobify");
        stage.show();
        event.consume();
    }

    @FXML
    private void misViviendas(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/trobify/views/GestionViviendas.fxml"));
        GestionViviendasController.deDondeViene("inicio");
        GestionViviendasController.pasarUsuario(username);
        s.close();
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        GestionViviendasController.pasarStage(stage);
        stage.setScene(scene);
        stage.setTitle("Trobify");
        stage.show();
        event.consume();
    }

    @FXML
    private void RegistrarViv(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/trobify/views/RegistrarVivienda.fxml"));
        RegistrarViviendaController.pasarUsuario(username);
        RegistrarViviendaController.deDondeViene("inicio");
        s.close();
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        RegistrarViviendaController.pasarStage(stage);
        stage.setScene(scene);
        stage.setTitle("Registrar vivienda");
        stage.show();
        event.consume();
    }

    @FXML
    private void favBoton(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/trobify/views/Favoritos.fxml"));
        FavoritosController.deDondeViene("inicio");
        FavoritosController.pasarUsuario(username);
        s.close();
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        FavoritosController.pasarStage(stage);
        stage.setScene(scene);
        stage.setTitle("Trobify");
        stage.show();
        event.consume();
    }

    @FXML
    private void notifica(ActionEvent event) {
    }

    @FXML
    private void reportarErrores(ActionEvent event) {
        
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Reportar errores");
        dialog.setHeaderText("Indique brevemente los errores o sugerencias que tengas");
        
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            System.out.println(result.get());
            //Aqui hay que conectar con la base de datos
            Mensaje m = new Mensaje(result.get());
            FachadaBD.añadirMensaje(m);
        }
    
    }

    @FXML
    private void historial(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
         fxmlLoader.setLocation(getClass().getResource("/trobify/views/Historial.fxml"));
         HistorialController.pasarUsuario(username);
         HistorialController.deDondeViene("inicio");
         s.close();
            Stage stage = new Stage();
            Scene scene = new Scene (fxmlLoader.load());
            HistorialController.pasarStage(stage);
            
            //añadir todos los controller a los que podria ir
            stage.setScene(scene);
            stage.setTitle("Trobify");
            stage.show();
            event.consume();  
    }
}
