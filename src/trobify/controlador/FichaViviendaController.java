/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify.controlador;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
//import trobify.Conectar;
import static trobify.controlador.BuscadorController.location;
import trobify.fachada.FachadaBD;
import trobify.logica.Favoritos;
import trobify.logica.Notificacion;
//import trobify.logica.ConectorServiciosBD;
//import trobify.logica.ConectorViviendaBD;
import trobify.logica.Servicios;
import trobify.logica.Vivienda;

/**
 * FXML Controller class
 *
 * @author jagon
 */
public class FichaViviendaController implements Initializable {

    @FXML
    private Button volver;
    @FXML
    private Text descripcion;
    @FXML
    private ListView<?> serviciosCerca;
    @FXML
    private HBox imageList;
    @FXML
    private VBox recomendados;

    private static Stage s;
    private static String username;
    @FXML
    private Button addFavoritos;
    @FXML
    private HBox valoracionGrupo;
    @FXML
    private Text textoValoracion;
    @FXML
    private Button editarValoracion;
    @FXML
    private TextField valorValoracion;
    /**
     * Initializes the controller class.
     */

    private static String id;
    Boolean estaEnFav;
    ArrayList<String> listaFotos;
    ArrayList<String> listaRecomendados;
    ArrayList<String> listaServicios;
    int valoracion;
    int precioBase;
    private static String deDondeViene;
    private static String aDondeVa;
    private Vivienda ciudad;
    private URL googleMaps;
    private WebEngine engine;
    public static String location;

    @FXML
    private Text precioVivienda;
    @FXML
    private WebView mapa;
    @FXML
    private HBox esPropietario;
    @FXML
    private TextField ofertaField;
    @FXML
    private HBox ofertaBox;
    public static String activo;
    @FXML
    private Button desOactBoton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (username == null) {
            addFavoritos.setVisible(false);
            ofertaBox.setVisible(false);
        }

        //solo deja editar y eliminar vivienda si es el propietario
        esPropietario.setVisible(FachadaBD.esPropietario(id, username));
        //Crear una conexion

        //Dando valor a mano de la id de vivienda AQUI SE DEBERA
        //PASAR EL ID DE LA VIVIENDA DESDE LA VENTANA ANTERIOR
        this.precioBase = FachadaBD.consultarPrecio(id);

        //Mostrar botones de valoraciones de favortitos o no
        this.estaEnFav = FachadaBD.estaEnFavoritos(id, username);
        if (estaEnFav) {
            valoracion = FachadaBD.getValoracion(id, username);
        }
        mostrarBotones();

        //Crear Array con la lista de fotos de la galeria
        this.listaFotos = new ArrayList();
        listaFotos = FachadaBD.crearListaFotos(id);

        //Crear Array con la lista de fotos de la galeria
        this.listaRecomendados = new ArrayList();
        listaRecomendados = FachadaBD.crearListaRecomendados(id);

        //Mostrar el precio de la vivienda    
        precioVivienda.setText("Precio: " + this.precioBase + "€");
        //Boton de volver atras        
        //volver.setOnAction(e -> scenePropia.setScene(scenaPrevia));      

        //Generador de fotos de la galeria       
        for (int i = 0; i < listaFotos.size(); i++) {
            try {
                this.imageList.getChildren().add(galeria(listaFotos.get(i)));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FavoritosController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //Mostrar la descripcion de la vivienda
        this.descripcion.setText(FachadaBD.getVivienda(id).getDescripcion());

        //Servicios cerca de la vivienda
        this.listaServicios = new ArrayList();
        consultarServicios(id);

        //Viviendas recomendadas        
        for (int i = 0; i < listaRecomendados.size(); i++) {
            Button botonRecomendacion = new Button();
            botonRecomendacion.setPadding(new Insets(0, 0, 0, 0));
            botonRecomendacion.setId(FachadaBD.consultarIdVivienda(listaRecomendados.get(i)));

            configurarBoton(botonRecomendacion);
            try {
                ImageView fotoBoton = galeria(listaRecomendados.get(i));
                botonRecomendacion.setGraphic(fotoBoton);
                recomendados.getChildren().add(botonRecomendacion);
                //System.out.println(botonRecomendacion);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FavoritosController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //Solamente pueden ponerse valorenumericos en el textfield
        valorValoracion.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                    String newValue) {
                if (!newValue.matches("\\d*")) {
                    valorValoracion.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        //Inicialización del WebView para que se muestre GoogleMaps
        System.setProperty("java.net.useSystemProxies", "true");
        final URL googleMaps = getClass().getResource("GeoPrueba.html");
        final WebEngine engine = mapa.getEngine();
        engine.load(googleMaps.toExternalForm());

        //para saber de donde viene y volver atrás correctamente
        if (deDondeViene.equals("favoritos")) {
            aDondeVa = "/trobify/views/Favoritos.fxml";
        }
        if (deDondeViene.equals("gestionVivienda")) {
            aDondeVa = "/trobify/views/GestionViviendas.fxml";
        } else {
            aDondeVa = "/trobify/views/Buscador.fxml";
        }

        geo();
        
        actiOdesacti();
    }

    private void geo() {
        ciudad = FachadaBD.getVivienda(id);
        geolocalizacion();
        listarGeoPunto(ciudad);
    }

    private void geolocalizacion() {
        //Inicialización del WebView para que se muestre GoogleMaps
        location = ciudad.getCalle();
        System.setProperty("java.net.useSystemProxies", "true");
        googleMaps = getClass().getResource("GeoPrueba.html");
        engine = mapa.getEngine();
        engine.load(googleMaps.toExternalForm());
        engine.getLoadWorker().stateProperty().addListener(
                new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                // System.out.println("oldValue: " + oldValue);
                // System.out.println("newValue: " + newValue);

                if (newValue != Worker.State.SUCCEEDED) {
                    return;
                }
                // System.out.println("Succeeded!");

                JSObject jsObject = (JSObject) engine.executeScript("window");
                jsObject.call("geocode", location);

            }
        });
    } //fin geolocalizacion

    private void listarGeoPunto(Vivienda res) {
        //Provisional
        // res = new Vivienda();
        //res.setCalle("Calle Arzobispo Mayoral");
        //res.setId("vivienda1");
        // res.setDescripcion("Vivienda que se encuentra en la calle Arzobispo Mayoral");
        /////////////////////////////////////////////////////////////////////

        String punto = res.getCalle() + " " + res.getCodigo_postal();

        String desc = res.getCalle();

        engine.getLoadWorker().stateProperty().addListener(
                new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                // System.out.println("oldValue: " + oldValue);
                // System.out.println("newValue: " + newValue);

                if (newValue != Worker.State.SUCCEEDED) {
                    return;
                }
                //System.out.println(id);

                JSObject jsObject = (JSObject) engine.executeScript("window");
                jsObject.call("mark", punto, null, desc);

            }
        });
    }

    //Funcion para poder moverte entre casas relacionadas a traves de botones en recomendados
    public void configurarBoton(Button boton) {
        boton.setOnAction(e -> {

            FichaViviendaController.pasarIdVivienda(boton.getId());

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/trobify/views/FichaVivienda.fxml"));
            s.close();
            Stage stage = new Stage();
            Scene scene;
            try {
                FavoritosController.pasarUsuario(username);
                scene = new Scene(fxmlLoader.load());
                FichaViviendaController.pasarStage(stage);
                stage.setScene(scene);
                stage.setTitle("Trobify");
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(FavoritosController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    //Metodo para obtener y generar una foto a partir de una direccion
    private javafx.scene.image.ImageView galeria(String source) throws FileNotFoundException {
        Image image = new Image(new FileInputStream(source));
        javafx.scene.image.ImageView fotoGaleria = new javafx.scene.image.ImageView(image);
        fotoGaleria.setFitWidth(200);
        fotoGaleria.setFitHeight(150);

        return fotoGaleria;
    }

    public void consultarServicios(String id) {
        Servicios servi = FachadaBD.getServicios(id);
        if (servi.getBanco() == 1) {
            listaServicios.add("Banco");
        }
        if (servi.getSupermercado() == 1) {
            listaServicios.add("Supermercado");
        }
        if (servi.getTransporte_publico() == 1) {
            listaServicios.add("Transporte publico");
        }
        if (servi.getGimnasio() == 1) {
            listaServicios.add("Gimnasio");
        }
        if (servi.getEstanco() == 1) {
            listaServicios.add("Estanco");
        }
        if (servi.getCentro_comercial() == 1) {
            listaServicios.add("Centro comercial");
        }
        if (servi.getFarmacia() == 1) {
            listaServicios.add("Farmacia");
        }
        ObservableList servicios = FXCollections.observableList(listaServicios);
        this.serviciosCerca.setItems(servicios);
    }

    //Muestra botones de favoritos
    public void mostrarBotones() {

        if (this.estaEnFav) {
            valoracionGrupo.setVisible(true);
            addFavoritos.setText("Quitar de favoritos");
            logicaBotonesValoracion();

        } else {
            valoracionGrupo.setVisible(false);
            addFavoritos.setText("Añadir a favoritos");
        }
    }

    private void logicaBotonesValoracion() {
        if (valoracion == -1 || valoracion == 0) {
            textoValoracion.setText("Valoracion: ");
            valorValoracion.clear();
            valorValoracion.setVisible(true);
            editarValoracion.setText("Añadir");
        } else {
            textoValoracion.setText("Valoracion: " + valoracion);
            valorValoracion.setVisible(false);
            editarValoracion.setText("Editar");
        }
    }

    @FXML
    private void addFav(ActionEvent event) throws IOException {
        if (!estaEnFav) {
            Favoritos nuevo = new Favoritos(id, username, 1);
            FachadaBD.añadirFavorito(nuevo);
            this.estaEnFav = true;
            mostrarBotones();
        } else {
            FachadaBD.eliminarFavorito(id, username);
            this.estaEnFav = false;
            this.valoracion = -1;
            mostrarBotones();

        }
        logicaBotonesValoracion();
    }

    @FXML
    private void editarValoracion(ActionEvent event) {
        if (this.valoracion == -1 || this.valoracion == 0) {
            this.valoracion = Integer.parseInt(valorValoracion.getText());
            FachadaBD.editarValoracion(valoracion, id, username);
        } else {
            this.valoracion = -1;
        }
        logicaBotonesValoracion();

    }

    @FXML
    private void atrasBoton(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(aDondeVa));
        s.close();
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        InicioController.pasarStage(stage);
        FavoritosController.pasarStage(stage);
        GestionViviendasController.pasarStage(stage);
        BuscadorController.pasarStage(stage);
        stage.setScene(scene);
        stage.setTitle("Trobify");
        stage.show();
        event.consume();
    }

    public static void pasarIdVivienda(String i) {
        id = i;
    }

    public static void pasarStage(Stage m) {
        s = m;
    }

    public static void deDondeViene(String donde) {
        deDondeViene = donde;
    }

    public static void pasarUsuario(String us) {
        username = us;
    }

    @FXML
    private void editar(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/trobify/views/EditarVivienda.fxml"));
        EditarViviendaController.pasarDatos(username, id);
        s.close();
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        EditarViviendaController.pasarStage(stage);
        stage.setScene(scene);
        stage.setTitle("Trobify");
        stage.show();
    }

    private void volverAtras() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(aDondeVa));

        s.close();
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        BuscadorController.pasarStage(stage);
        FavoritosController.pasarStage(stage);
        stage.setScene(scene);
        stage.setTitle("Trobify");
        stage.show();

    }

    @FXML
    private void enviarOferta(ActionEvent event) {
        int precio = Integer.parseInt(ofertaField.getText());
        if (precio >= precioBase) {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setHeaderText("Introduzca un valor inferior al precio original");
            Optional<ButtonType> vale = alerta.showAndWait();
            ofertaField.clear();
        } else {
            Date now = new Date(System.currentTimeMillis());
            Notificacion oferta = new Notificacion(id, username, ofertaField.getText(), now, 0, 1);
            FachadaBD.añadirNotificacionNoID(oferta);
        }
    }

    @FXML
    private void desactivarOactivar(ActionEvent event) throws IOException {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        String texto = "activar";
        if (activo.equals("activo")) {
            texto = "desactivar";
        }
        alerta.setHeaderText("Seguro que quieres " + texto + " esta vivienda?");
        Optional<ButtonType> ok = alerta.showAndWait();
        if (ok.isPresent() && ok.get().equals(ButtonType.OK)) {
            if (texto.equals("desactivar")) {
                FachadaBD.desactivarVivienda(id);
            } else {
                FachadaBD.activarVivienda(id);
            }
            listaFotos.clear();
            this.imageList.getChildren().clear();

            initialize(null, null);

        }
        alerta.close();
    }

    private void actiOdesacti() {
        activo = FachadaBD.getActivo(id);
        if (activo.equals("activo")) {
            desOactBoton.setText("Despublicar");
        } else {
            desOactBoton.setText("Publicar");
        }
    }

}
