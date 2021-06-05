/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify.controlador;

import java.io.FileInputStream;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import trobify.fachada.FachadaBD;
import trobify.logica.Vivienda;

/**
 * FXML Controller class
 *
 * @author jagon
 */
public class FavoritosController extends GeneradorMiniaturas implements Initializable {

    @FXML
    private Label nombreUsuario;
    @FXML
    private VBox listaViviendas;
    @FXML
    private ComboBox<String> elegirOrdenPor;
    private static Stage s;
    private static String username;
    private static String vieneDe;
    private String direccion;
    
    /**
     * Initializes the controller class.
     */
    
   
    ArrayList<String> favList;
    @FXML
    private ImageView fotoNotificacion;
    
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
       // Seleccion del orden de busqueda
        ArrayList<String> orden = new ArrayList();
        orden.add("Relevancia");
        orden.add("Precio más bajo");
        orden.add("Precio más alto");
        orden.add("Valoracion");
        
        ObservableList<String> ordenar = FXCollections.observableList(orden);
        elegirOrdenPor.setItems(ordenar);
        
        elegirOrdenPor.getSelectionModel().selectFirst();
        
        //Crear Array con la lista de favoritos, por defecto seleccionara LA PRIMERA opcion
        favList = new ArrayList();       
        orden();
                               
        //Mostrar nombre de usuario
        nombreUsuario.setText(username);
        
        direccion();
        hayNotis();
    }    
   
     private void hayNotis() {
        if (FachadaBD.getNotificacionPorUsuarioDestino(username).size() != 0) {
            //falta un if con un boolean
            try {
                Image image1 = new Image(new FileInputStream("src\\trobify\\images\\notiActiva.png"));
                fotoNotificacion.setImage(image1);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(BuscadorController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
            try {
                Image image1 = new Image(new FileInputStream("src\\trobify\\images\\notificacion.jpg"));
                fotoNotificacion.setImage(image1);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(BuscadorController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    //Generador de miniauras
    private javafx.scene.layout.HBox crearMiniatura(String id, String rutaFoto, String nombreCalle, int precioVivienda, int valoracionVivienda) throws FileNotFoundException{
        
        javafx.scene.layout.HBox miniatura = crearMiniaturas(id, rutaFoto, nombreCalle, precioVivienda, username, "nada", valoracionVivienda, 1);
        return miniatura;
    }
    
    //Lista de viviendas
    private void ordenarLista(){
        for (int i = 0; i < favList.size(); ++i) {
           String idBoton = favList.get(i);
           Vivienda vivi = FachadaBD.getVivienda(idBoton);
            try {
                String foto = FachadaBD.consultarFotoViviendaPorId(idBoton);
                String calle = vivi.getCalle();
                int precio = vivi.getPrecio();
                int valoracion = FachadaBD.getValoracion(favList.get(i), username);
                this.listaViviendas.getChildren().add(crearMiniatura(idBoton, foto, calle, precio, valoracion));
                
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FavoritosController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

     private void direccion(){
        if(vieneDe.equals("inicio"))
           direccion = "/trobify/views/Inicio.fxml";
        if(vieneDe.equals("buscador"))
            direccion = "/trobify/views/Buscador.fxml";
        if(vieneDe.equals("gestionVivienda"))
            direccion = "/trobify/views/GestionViviendas.fxml";
       else { //otros sitios de donde pueda venir
           }
     }
     
    private void alerta(String botonEliminar){
       Alert alerta = new Alert (Alert.AlertType.CONFIRMATION);
        alerta.setHeaderText("Seguro que quieres eliminar?");
        Optional<ButtonType> ok = alerta.showAndWait();
        if(ok.isPresent() && ok.get().equals(ButtonType.OK)) {
             FachadaBD.eliminarFavorito(botonEliminar, username);
            ordenCambiado(null);
            alerta.close();
           
    } alerta.close();
    }
    
    @FXML
    private void ordenCambiado(ActionEvent event) {
        orden();
    }
   
    private void orden(){
    favList.clear();
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
        favList = FachadaBD.getListaFavoritosUsuario(username, orden);
                ordenarLista();
    }
    
    @FXML
    private void InicioBoton(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(direccion));
        GestionViviendasController.deDondeViene("inicio");
         GestionViviendasController.pasarUsuario(username);
        s.close();
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        BuscadorController.pasarStage(stage);
        GestionViviendasController.pasarStage(stage);
        InicioController.pasarStage(stage);
        stage.setScene(scene);
        stage.setTitle("Trobify");
        stage.show();
        event.consume();
    }
   
    public static void pasarStage(Stage m){
         s = m;
     }
    public static void pasarUsuario(String usu){
        username = usu;
    }
    
    public static void deDondeViene (String donde){
         vieneDe = donde;
    }

    @FXML
    private void notificaciones(ActionEvent event) throws IOException {
         //notificar(null);
       // ArrayList<Vivienda> viviendasFav = FachadaBD.favoritosUsuario(username);
        //ArrayList<String> res = new ArrayList<String>();

        /* ///////////////////////////////////////////////////////////////////
        Vivienda prueba = new Vivienda();
        prueba.setCalle("Calle de pruebas");
        prueba.setActivo(1);
        viviendasFav.add(prueba);
        //////////////////////////////////////////////////////////////////*/
       /* for (int i = 0; i < viviendasFav.size(); i++) {

            if (viviendasFav.get(i).getActivo() == 1) {

                res.add("La vivienda de la " + viviendasFav.get(i).getCalle() + " ya no se encuentra disponible.");
            }
        }*/
        //NotificacionesController.pasarNotis(res);

        NotificacionesController.pasarUsuario(username);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/trobify/views/Notificaciones.fxml"));
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        NotificacionesController.pasarStage(stage);
        stage.setScene(scene);
        stage.setTitle("Trobify");
        stage.show();
        event.consume();
        
        try {
                Image image1 = new Image(new FileInputStream("src\\trobify\\images\\notificacion.jpg"));
                fotoNotificacion.setImage(image1);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(BuscadorController.class.getName()).log(Level.SEVERE, null, ex);
            }
        
    }

    @Override
    public VBox crearDatos(String nombreCalle, int precioVivienda, String activo, int valoracionVivienda, VBox datos, int alquilada) {
       javafx.scene.control.Label calle = new javafx.scene.control.Label("Calle: " + nombreCalle);
        javafx.scene.control.Label precio = new javafx.scene.control.Label("Precio: " + precioVivienda + " €");
        javafx.scene.control.Label valoracion = new javafx.scene.control.Label("Valoracion: " + valoracionVivienda);
        if(valoracionVivienda == -1) valoracion.setVisible(false);
        
        datos.getChildren().addAll(calle,precio,valoracion);
        return datos;
    }

    @Override
    public VBox crearBoton(String id) {
        javafx.scene.layout.VBox eliminarFav = new javafx.scene.layout.VBox(10);
        eliminarFav.setAlignment(Pos.CENTER);
        eliminarFav.setPadding(new Insets(20,20,20,25));
        
        Button botonEliminar = new Button();
        botonEliminar.setText("Eliminar de favoritos");
        botonEliminar.setId(id);
        botonEliminar.setOnAction(e -> {
          alerta(botonEliminar.getId());
        });
        
        eliminarFav.getChildren().add(botonEliminar);
        return eliminarFav;
        
    }

    @Override
    public HBox añadirAMiniatura(HBox miniatura, Button botonRedireccion, VBox datos, VBox boton) {
      miniatura.getChildren().addAll(botonRedireccion, datos, boton);        
        return miniatura;
    }

    @Override
    public void cambiarPantalla() {
         FichaViviendaController.deDondeViene("favoritos");
            s.close();
           
    }
}
