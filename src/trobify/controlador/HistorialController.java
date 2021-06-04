/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify.controlador;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import trobify.fachada.FachadaBD;
import trobify.logica.Vivienda;

/**
 * FXML Controller class
 *
 * @author gabri
 */

public class HistorialController extends GeneradorMiniaturas implements Initializable {

    @FXML
    private Label nombreUsuario;
    @FXML
    private VBox listaViviendas;
     private static Stage st;
    private static String username;
    private static String vieneDe;
    private String direccion;
    private ArrayList<String> historialViviendas;
 

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nombreUsuario.setText(username);
        direccion();
        historialViviendas = new ArrayList();
        consulta();
        
    }    

     private void direccion(){
        if(vieneDe.equals("inicio"))
           direccion = "/trobify/views/Inicio.fxml";
        
        if(vieneDe.equals("buscador"))
            direccion = "/trobify/views/Buscador.fxml";
       else { //otros sitios de donde pueda venir
           }
    }
     
      private javafx.scene.layout.HBox crearMiniatura(String id, String rutaFoto, String nombreCalle, int precioVivienda) throws FileNotFoundException{
       
        javafx.scene.layout.HBox miniatura = crearMiniaturas(id, rutaFoto, nombreCalle, precioVivienda, username, "nada", 0, 1);  
     
       return miniatura;

    }
        //Lista de viviendas
    private void ordenarLista(){
        for (int i = 0; i < historialViviendas.size(); ++i) {
           String idBoton = historialViviendas.get(i);
           Vivienda vivi = FachadaBD.pasarVivienda(idBoton);
            try {
                String foto = FachadaBD.consultarFoto(idBoton);
                String calle = vivi.getCalle();
                int precio = vivi.getPrecio();
                //activo = "Publicada";
                //if(vivi.getActivo() == 1) activo = "Despublicada";
                this.listaViviendas.getChildren().add(crearMiniatura(idBoton, foto, calle, precio));
                
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FavoritosController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void consulta(){
    historialViviendas.clear();
      listaViviendas.getChildren().clear();
        historialViviendas = FachadaBD.historialDelUsusario(username);
               System.out.println(historialViviendas);
        ordenarLista();
    }
     
    @FXML
    private void InicioBoton(ActionEvent event) throws IOException {
         FXMLLoader fxmlLoader = new FXMLLoader();
         fxmlLoader.setLocation(getClass().getResource(direccion));
         st.close();
            Stage stage = new Stage();
            Scene scene = new Scene (fxmlLoader.load());
            InicioController.pasarStage(stage);
            BuscadorController.pasarStage(stage);
            FichaViviendaController.pasarStage(stage);
            //añadir todos los controller a los que podria ir
            stage.setScene(scene);
            stage.setTitle("Trobify");
            stage.show();
            event.consume();
    }
    
    public static void pasarStage(Stage stage){
        st = stage;
    }
    
    public static void pasarUsuario(String u){
        username = u;
    }
    
    public static void deDondeViene (String donde){
         vieneDe = donde;
    }

    @Override
    public javafx.scene.layout.VBox crearDatos(String nombreCalle, int precioVivienda, String activo, int valoracion, 
                                                                            javafx.scene.layout.VBox datos, int alquilada) {
        javafx.scene.control.Label calle = new javafx.scene.control.Label("Calle: " + nombreCalle);
        javafx.scene.control.Label precio = new javafx.scene.control.Label("Precio: " + precioVivienda + " €");
        
        datos.getChildren().addAll(calle,precio);
        return datos;
    }

    @Override
    public  javafx.scene.layout.VBox crearBoton(String id) {
         return null;
    }

    @Override
    public javafx.scene.layout.HBox añadirAMiniatura(javafx.scene.layout.HBox miniatura, Button botonRedireccion, javafx.scene.layout.VBox datos, javafx.scene.layout.VBox boton) {
       miniatura.getChildren().addAll(botonRedireccion, datos);        
       return miniatura;
    }

    @Override
    public void cambiarPantalla() {
        FichaViviendaController.deDondeViene("historial");
        st.close();
    }
         
}

