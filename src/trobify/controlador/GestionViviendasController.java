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
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import trobify.fachada.FachadaBD;
import trobify.logica.Vivienda;

/**
 * FXML Controller class
 *
 * @author gabri
 */
public class GestionViviendasController extends GeneradorMiniaturas implements Initializable {

    @FXML
    private Label nombreUsuario;
    @FXML
    private ComboBox<String> elegirOrdenPor;
    @FXML
    private VBox listaViviendas;
    
    private static Stage st;
    private static String username;
    private static String vieneDe;
    private String direccion;
    private ArrayList<String> misViviendas;
    private String activo;
    private String textoBoton;
 
   
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nombreUsuario.setText(username);
        rellenoComboBox();
        direccion();
        
        misViviendas = new ArrayList();    
      
        orden();
        
       
    }    

    private void direccion(){
        if(vieneDe.equals("inicio"))
           direccion = "/trobify/views/Inicio.fxml";
        
        if(vieneDe.equals("buscador"))
            direccion = "/trobify/views/Buscador.fxml";
       else { //otros sitios de donde pueda venir
           }
    }
    
   
    private void rellenoComboBox(){
     ArrayList<String> orden = new ArrayList();
        orden.add("Relevancia");
        orden.add("Precio más bajo");
        orden.add("Precio más alto");
      
        
        ObservableList<String> ordenar = FXCollections.observableList(orden);
        elegirOrdenPor.setItems(ordenar);
        
        elegirOrdenPor.getSelectionModel().selectFirst();
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
            //añadir todos los controller a los que podria ir
            stage.setScene(scene);
            stage.setTitle("Trobify");
            stage.show();
            event.consume();
    
    }

    //Generador de miniauras
    private javafx.scene.layout.HBox crearMiniatura(String id, String rutaFoto, String nombreCalle, int precioVivienda, String activo) throws FileNotFoundException{
      
        javafx.scene.layout.HBox miniatura = crearMiniaturas(id, rutaFoto, nombreCalle, precioVivienda, username, activo, 0, 1);  

        return miniatura;
    }
    
    //Lista de viviendas
    private void ordenarLista(){
        for (int i = 0; i < misViviendas.size(); ++i) {
           String idBoton = misViviendas.get(i);
           Vivienda vivi = FachadaBD.pasarVivienda(idBoton);
            try {
                String foto = FachadaBD.consultarFoto(idBoton);
                String calle = vivi.getCalle();
                int precio = vivi.getPrecio();
                activo = "Publicada";
                if(vivi.getActivo() == 1) activo = "Despublicada";
                this.listaViviendas.getChildren().add(crearMiniatura(idBoton, foto, calle, precio, activo));
                
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FavoritosController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void alerta(String botonGestionar){
    Alert alerta = new Alert (Alert.AlertType.CONFIRMATION);
    String texto = "publicar";
    Vivienda vivi = FachadaBD.getVivienda(botonGestionar);
    if(vivi.getActivo()==0) texto = "despublicar";
    alerta.setHeaderText("Seguro que quieres "+texto+" esta vivienda?");
    Optional<ButtonType> ok = alerta.showAndWait();
    if(ok.isPresent() && ok.get().equals(ButtonType.OK)) { 
    if(texto.equals("despublicar")){
        FachadaBD.desactivarVivienda(botonGestionar);
        FachadaBD.notificarDesact(botonGestionar);
    }
    
    else{
        FachadaBD.activarVivienda(botonGestionar);
        FachadaBD.notificarActiv(botonGestionar);
    }
    ordenCambiado(null);
    
}   alerta.close();
   }
    
    
    @FXML
    private void ordenCambiado(ActionEvent event) {
        orden();
    }
   
    private void orden(){
    misViviendas.clear();
        String orden = "";
        switch (elegirOrdenPor.getSelectionModel().selectedItemProperty().getValue()) {
            case "Relevancia":                
                orden = "";
                break;
            case "Precio más bajo":
                orden = "ORDER BY precio ASC";
                break;
            case "Precio más alto":
               orden = "ORDER BY precio DESC";
               break;
            case "Valoracion":
              orden = "ORDER BY valoracion DESC";
              break;
            default:
                break;
        }
        listaViviendas.getChildren().clear();
        misViviendas = FachadaBD.viviendasDelUsusario(username, orden);
                ordenarLista();
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
    public VBox crearDatos(String nombreCalle, int precioVivienda, String activo, int valoracion, VBox datos, int alquilada) {
       javafx.scene.control.Label calle = new javafx.scene.control.Label("Calle: " + nombreCalle);
        javafx.scene.control.Label precio = new javafx.scene.control.Label("Precio: " + precioVivienda + "/mes");
        javafx.scene.control.Label estado = new javafx.scene.control.Label("Vivienda " + activo );
         
       datos.getChildren().addAll(calle,precio,estado);
       return datos;
    }

    @Override
    public javafx.scene.layout.VBox crearBoton(String id) {
         
       javafx.scene.layout.VBox eliminarFav = new javafx.scene.layout.VBox(10);
       eliminarFav.setAlignment(Pos.CENTER);
       eliminarFav.setPadding(new Insets(20,20,20,25));
       //boton de activar o desactivar
       if(activo.equals("Publicada")) textoBoton = "Despublicar vivienda";
       else textoBoton = "Publicar vivienda";
      
        Button botonGestionar = new Button();
        botonGestionar.setText(textoBoton);
        botonGestionar.setId(id);
        botonGestionar.setOnAction(e -> {
        alerta(botonGestionar.getId());
       });
       
        
       eliminarFav.getChildren().add(botonGestionar);
       return eliminarFav;
    }

    @Override
    public HBox añadirAMiniatura(HBox miniatura, Button botonRedireccion, VBox datos,  javafx.scene.layout.VBox boton) {
          miniatura.getChildren().addAll(botonRedireccion, datos, boton);   
          return miniatura;
    }

    @Override
    public void cambiarPantalla() {
         FichaViviendaController.deDondeViene("gestionVivienda");
            st.close();
        
    }

  
         
}
