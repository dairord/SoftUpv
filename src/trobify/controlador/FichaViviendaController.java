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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
import javafx.scene.control.Button;
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
import trobify.Conectar;
import trobify.logica.ConectorViviendaBD;
import trobify.logica.Favoritos;
import trobify.logica.Servicios;
import trobify.logica.Vivienda;
import netscape.javascript.JSObject;

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
    Conectar con;
    private static String id;
    Boolean estaEnFav;
    ArrayList<String> listaFotos;
    ArrayList<String> listaRecomendados;
    ArrayList<String> listaServicios;
    int valoracion;
    int precioBase;
    private static String deDondeViene;
    private static String aDondeVa;

    private URL googleMaps;
    private WebEngine engine;
    public static String location;
    public Vivienda res;

    @FXML
    private Text precioVivienda;
    @FXML
    private WebView mapa;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //Crear una conexion
        con = new Conectar();

        //Dando valor a mano de la id de vivienda AQUI SE DEBERA
        //PASAR EL ID DE LA VIVIENDA DESDE LA VENTANA ANTERIOR
        this.precioBase = ConectorViviendaBD.consultarPrecio(id);

        //Mostrar botones de valoraciones de favortitos o no
        this.estaEnFav = ConectorViviendaBD.estaEnFavoritos(id, username);
        if (estaEnFav) {
            valoracion = ConectorViviendaBD.consultarValoracion(id, username);
        }
        mostrarBotones();

        //Crear Array con la lista de fotos de la galeria
        this.listaFotos = new ArrayList();
        listaFotos = ConectorViviendaBD.crearListaFotos(id);

        //Crear Array con la lista de fotos de la galeria
        this.listaRecomendados = new ArrayList();
        listaRecomendados = ConectorViviendaBD.crearListaRecomendados(id, precioBase);

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
        this.descripcion.setText(ConectorViviendaBD.consultarDescripcion(id));

        //Servicios cerca de la vivienda
        this.listaServicios = new ArrayList();
        consultarServicios(id);

        //Viviendas recomendadas        
        for (int i = 0; i < listaRecomendados.size(); i++) {
            Button botonRecomendacion = new Button();
            botonRecomendacion.setPadding(new Insets(0, 0, 0, 0));
            botonRecomendacion.setId(ConectorViviendaBD.consultarIdVivienda(listaRecomendados.get(i)));
            System.out.println("caca" + botonRecomendacion.getId());
            configurarBoton(botonRecomendacion);
            try {
                ImageView fotoBoton = galeria(listaRecomendados.get(i));
                botonRecomendacion.setGraphic(fotoBoton);
                recomendados.getChildren().add(botonRecomendacion);
                System.out.println(botonRecomendacion);
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
        geolocalización();

        //para saber de donde viene y volver atrás correctamente
        if (deDondeViene.equals("favoritos")) {
            aDondeVa = "/trobify/views/Favoritos.fxml";
        } else {
            aDondeVa = "/trobify/views/Buscador.fxml";
        }
    }

    private void geolocalización() {
        //Provisional
        res = new Vivienda();
        res.setCalle("Calle Arzobispo Mayoral");
        res.setId("vivienda1");
        res.setDescripcion("Vivienda que se encuentra en la calle Arzobispo Mayoral");
        /////////////////////////////////////////////////////////////////////

        String location = res.getCalle();
        System.setProperty("java.net.useSystemProxies", "true");
        googleMaps = getClass().getResource("GeoPrueba.html");
        engine = mapa.getEngine();
        engine.load(googleMaps.toExternalForm());
        engine.getLoadWorker().stateProperty().addListener(
                new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                System.out.println("oldValue: " + oldValue);
                System.out.println("newValue: " + newValue);

                if (newValue != Worker.State.SUCCEEDED) {
                    return;
                }
                System.out.println("Succeeded!");

                JSObject jsObject = (JSObject) engine.executeScript("window");
                jsObject.call("geocode", location);

            }
        });
        String punto = res.getCalle();
        String id = res.getId().substring(8);
        String desc = res.getDescripcion();

        engine.getLoadWorker().stateProperty().addListener(
                new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                System.out.println("oldValue: " + oldValue);
                System.out.println("newValue: " + newValue);

                if (newValue != Worker.State.SUCCEEDED) {
                    return;
                }
                System.out.println("Succeeded!");

                JSObject jsObject = (JSObject) engine.executeScript("window");
                jsObject.call("mark", punto, id, desc);

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
        Servicios servi = ConectorViviendaBD.consultarServicios(id);
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
            ConectorViviendaBD.añadirFavoritos(nuevo);
            this.estaEnFav = true;
            mostrarBotones();
        } else {
            ConectorViviendaBD.eliminarDeFavoritos(id, username);
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
            ConectorViviendaBD.editarValoracion(valoracion, id, username);
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
}
