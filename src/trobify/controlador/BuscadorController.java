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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableObjectValue;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import trobify.Conectar;
import trobify.logica.ConectorViviendaBD;
import trobify.logica.Vivienda;

/**
 * FXML Controller class
 *
 * @author gabrielacorbalan
 */
public class BuscadorController implements Initializable {

    @FXML
    private Label nombreUsuario;
    @FXML
    private DatePicker fechaEntrada;
    @FXML
    private DatePicker fechaSalida;
    @FXML
    private ComboBox<String> ordenarPor;
    @FXML
    private TextField ciudad;
    @FXML
    private ComboBox<String> variacionFecha;
    @FXML
    private ComboBox<String> tipoVivienda;
    @FXML
    private TextField precioMin;
    @FXML
    private TextField precioMax;
    @FXML
    private TextField numBaños;

    @FXML
    private TextField numHabitaciones;

    private static Stage s;
    private static String ciu;
    private static String tip;
    private static int alqOVen;
    ArrayList<String> viviendasList;
    private static String usuario;
    //conexion
    Conectar con;
    @FXML
    private Label entradaText;
    @FXML
    private Label salidaText;
    @FXML
    private Button buscarBoton;
    @FXML
    private Button botonGuardarFiltros;
    @FXML
    private Label errorText;
    @FXML
    private VBox listaViviendas;
    @FXML
    private Button IniciarSesionBoton;
    @FXML
    private Hyperlink RegistrarseBoton;
    @FXML
    private WebView mapa;

    //para la consulta
    private String tipo;
    private String habita;
    private String baños;
    private String pMin;
    private String pMax;
    private String comoOrdenar;

    //atributos geolocalización
    private URL googleMaps;
    private WebEngine engine;
    public static String location;

    //cosas del usuario
    private static boolean estaIniciado;
    private static String username;
    @FXML
    private Button favoritos;
    @FXML
    private Button mensajes;
    @FXML
    private Button notificaciones;
    @FXML
    private Label agente;
    @FXML
    private Button registrarV;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Crear una conexion
        con = new Conectar();
        //lista con las viviendas a mostrar
        viviendasList = new ArrayList();

        sesionIniciada();
        botonDesactivado();
        seleccionFechas();
        rellenoComboBox();
        autoRellenoFiltros();
        try {
            comprobaciones();
        } catch (SQLException ex) {
            Logger.getLogger(BuscadorController.class.getName()).log(Level.SEVERE, null, ex);
        }
        geolocalizacion();
        ArrayList<Vivienda> listaCiudad = ConectorViviendaBD.getViviendasPorCiudadActivas(ciudad.getText(), alqOVen);
        for (int i = 0; i < listaCiudad.size(); i++) {
            listarGeoPunto(listaCiudad.get(i), i);
        }
    } //fin initialice

    private void sesionIniciada() {
     //   System.out.println(username);
        //compobar si ha iniciado sesión
        if (estaIniciado) {
            nombreUsuario.setText(username);
            IniciarSesionBoton.setVisible(false);
            RegistrarseBoton.setVisible(false);
        } else {
            nombreUsuario.setText("usuario");
            favoritos.setVisible(false);
            mensajes.setVisible(false);
            notificaciones.setVisible(false);
            botonGuardarFiltros.setVisible(false);
            registrarV.setVisible(false);
        }

    } //fin sesion iniciada

    private void botonDesactivado() {
        //boton buscar desactivado si no están todos los filtros
        final BooleanBinding sePuedeBuscar = Bindings.isEmpty(precioMin.textProperty())
                .or(Bindings.isEmpty(precioMax.textProperty()))
                .or(Bindings.isEmpty(ciudad.textProperty()))
                .or(Bindings.isEmpty(numBaños.textProperty()))
                .or(Bindings.isEmpty(numHabitaciones.textProperty())) // .or(Bindings.)
                ;
        botonGuardarFiltros.disableProperty().bind(sePuedeBuscar);
    } //fin boton desactivado

    private void seleccionFechas() {
        //no poder seleccionar fechas anteriores a hoy
        fechaEntrada.setDayCellFactory((DatePicker picker) -> {
            return new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    LocalDate today = LocalDate.now();
                    setDisable(empty || date.compareTo(today) < 0);
                }
            };
        });

        fechaSalida.setDayCellFactory((DatePicker picker) -> {
            return new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    LocalDate today = fechaEntrada.getValue();
                    setDisable(empty || date.compareTo(today) < 0);
                }
            };
        });

        //no se puede introducir fecha de salida hasta que no esté la de entrada
        final BooleanBinding sePuedePonerFecha = Bindings.isNull(fechaEntrada.valueProperty());
        fechaSalida.disableProperty().bind(sePuedePonerFecha);

        //si es comprar no salen las fechas
        if (alqOVen == 1) {
            entradaText.setVisible(false);
            salidaText.setVisible(false);
            fechaEntrada.setVisible(false);
            fechaSalida.setVisible(false);
            variacionFecha.setVisible(false);
        }
    }//fin seleccion fechas

    private void rellenoComboBox() {
        //Tipo Vivienda
        ArrayList<String> tiposViviendas = new ArrayList<String>();
        tiposViviendas.add("Piso");
        tiposViviendas.add("Casa");
        tiposViviendas.add("Indiferente");
        ObservableList<String> viv = FXCollections.observableList(tiposViviendas);
        tipoVivienda.setItems(viv);

        //ordenar por
        ArrayList<String> orden = new ArrayList<String>();
        orden.add("Relevancia");
        orden.add("Precio más bajo");
        orden.add("Precio más alto");
        ObservableList<String> ordenar = FXCollections.observableList(orden);
        ordenarPor.setItems(ordenar);
        ordenarPor.getSelectionModel().selectFirst();

        //variacion fechas
        if (alqOVen != 1) {
            ArrayList<String> varf = new ArrayList<String>();
            varf.add("Fechas exactas");
            varf.add("± 1 dia");
            varf.add("± 3 dias");
            varf.add("± 7 dias");
            ObservableList<String> variaFecha = FXCollections.observableList(varf);
            variacionFecha.setItems(variaFecha);
            variacionFecha.getSelectionModel().selectFirst();
        }
    } //fin rellenoComboBox

    private void autoRellenoFiltros() {
        //poner directamente el nombre de la ciudad buscada y tipo vivienda
        ciudad.setText(ciu);
        tipoVivienda.getSelectionModel().select(tip);

    } //fin autocompletar filtros

    private void geolocalizacion() {
        //Inicialización del WebView para que se muestre GoogleMaps
        location = ciudad.getText();
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

    //Pasándole una vivienda como parámetro es suficiente para listar el punto en el mapa, poner el número de su id (si fuese vivivienda1 pues 1), y mostrár la desc en el mapa
    //La primera parte del código es provisional hasta que el resto funcione correctamente
    private void listarGeoPunto(Vivienda res, int idd) {
        //Provisional
       // res = new Vivienda();
        //res.setCalle("Calle Arzobispo Mayoral");
        //res.setId("vivienda1");
       // res.setDescripcion("Vivienda que se encuentra en la calle Arzobispo Mayoral");
        /////////////////////////////////////////////////////////////////////
        
        String punto = res.getCalle() +" " +res.getCodigo_postal();
        String id = String.valueOf(idd+1);
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
                System.out.println(id);

                JSObject jsObject = (JSObject) engine.executeScript("window");
                jsObject.call("mark", punto, id, desc);

            }
        });
    }

    @FXML
    private void guardarFiltros(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/trobify/views/MantenerFiltros.fxml"));
        MantenerFiltrosController.pasarFiltrosBuscar(ciudad.getText(), tipoVivienda.getSelectionModel().selectedItemProperty().getValue(), alqOVen,
                precioMin.getText(), precioMax.getText(), numBaños.getText(), numHabitaciones.getText(), fechaEntrada.getValue(), fechaSalida.getValue(), usuario);
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        MantenerFiltrosController.pasarStage(stage);

        stage.setScene(scene);
        stage.setTitle("Trobify");
        stage.show();
        event.consume();
    }

    @FXML
    private void notificar(ActionEvent event) {
       /* ArrayList<Vivienda> viviendasFav = new ArrayList<Vivienda>();
        ArrayList<String> res = new ArrayList<String>();
        
        ///////////////////////////////////////////////////////////////////
        Vivienda prueba = new Vivienda();
        prueba.setCalle("Calle de pruebas");
        prueba.setActivo(1);
        viviendasFav.add(prueba);
        //////////////////////////////////////////////////////////////////
        
        for(int i = 0; i < viviendasFav.size(); i++){
          //  System.out.println("Caca");
            if (viviendasFav.get(i).getActivo() == 1){
            //    System.out.println("Cacota");
                res.add("La vivienda de la " +viviendasFav.get(i).getCalle() +" ya no se encuentra disponible.");
            }
        }
        NotificacionesController.pasarNotis(res);*/
    }

    @FXML
    private void buscar(ActionEvent event) throws SQLException {
        geolocalizacion();
        comprobaciones();
    }

    @FXML
    private void Inicio(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/trobify/views/Inicio.fxml"));
        InicioController.pasarUsuario(estaIniciado, username);
        s.close();
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        InicioController.pasarStage(stage);
        stage.setScene(scene);
        stage.setTitle("Trobify");
        stage.show();
        event.consume();
    }

    public static void pasarUsuario(boolean iniciado, String usuario) {
        estaIniciado = iniciado;
        username = usuario;
    }

    public static void pasarStage(Stage m) {
        s = m;

    }

    public static void pasarFiltrosInicio(String c, String t, int queBuscas) {
        ciu = c;
        tip = t;
        alqOVen = queBuscas;
    }

    public static void pasarLocalizacion(String local) {
        location = local;
    }

    @FXML
    private void ordenar(ActionEvent event) throws SQLException {
        comprobaciones();
    }

    private void comprobaciones() throws SQLException {
        viviendasList.clear();
        listaViviendas.getChildren().clear();
        //comprobación tipos
        if (tipoVivienda.getSelectionModel().selectedItemProperty().getValue().equals("Piso")) {
            tipo = " and tipo = 1";
        } else if (tipoVivienda.getSelectionModel().selectedItemProperty().getValue().equals("Casa")) {
            tipo = " and tipo = 2";
        } else {
            tipo = "";
        }

        //comprobación precio minimo
        if (!precioMin.getText().equals("")) {
            pMin = " and precio > " + Integer.valueOf(precioMin.getText());
        } else {
            pMin = "";
        }
        //cpmprobacion precio maximo
        if (!precioMax.getText().equals("")) {
            pMax = " and precio < " + Integer.valueOf(precioMax.getText());
        } else {
            pMax = "";
        }

        //comprobacion numero baños
        if (!numBaños.getText().equals("")) {
            baños = " and baños = " + Integer.valueOf(numBaños.getText());
        } else {
            baños = "";
        }

        //compprobacion numero de habitaciones
        if (!numHabitaciones.getText().equals("")) {
            habita = " and habitaciones = " + Integer.valueOf(numHabitaciones.getText());
        } else {
            habita = "";
        }

        //comprobar orden
        if (ordenarPor.getSelectionModel().selectedItemProperty().getValue().equals("Relevancia")) {
            comoOrdenar = "id";
        } else if (ordenarPor.getSelectionModel().selectedItemProperty().getValue().equals("Precio más bajo")) {
            comoOrdenar = "precio ASC";
        } else {
            comoOrdenar = "precio DESC";
        }
        consulta();

    }//fin comprobaciones

    //metodo nuevo de sql
    private void consulta() throws SQLException {
        viviendasList = ConectorViviendaBD.consultaBuscador(ciu, alqOVen, tipo, pMin, pMax, baños, habita, comoOrdenar);
        ordenarLista();
    }//fin consulta

    //generador de miniaturas
    private javafx.scene.layout.HBox crearMiniatura(String id, String rutaFoto, String nombreCalle, int precioVivienda, int alquilada) throws FileNotFoundException {

        javafx.scene.layout.HBox miniatura = new javafx.scene.layout.HBox();

        Button botonRedireccion = new Button();
        botonRedireccion.setPadding(new Insets(0, 0, 0, 0));
        botonRedireccion.setId(id);
        botonRedireccion.setOnAction(e -> {
          //  System.out.println(id);
            FichaViviendaController.pasarIdVivienda(botonRedireccion.getId());
            FichaViviendaController.deDondeViene("buscador");
            FichaViviendaController.pasarUsuario(username);
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/trobify/views/FichaVivienda.fxml"));
            s.close();
            Stage stage = new Stage();
            Scene scene;
            try {
                scene = new Scene(fxmlLoader.load());
                FichaViviendaController.pasarStage(stage);
                stage.setScene(scene);
                stage.setTitle("Trobify");
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(BuscadorController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        miniatura.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        Image image1 = new Image(new FileInputStream(rutaFoto));
        javafx.scene.image.ImageView foto = new javafx.scene.image.ImageView(image1);
        foto.setFitWidth(200);
        foto.setFitHeight(150);

        botonRedireccion.setGraphic(foto);

        javafx.scene.layout.VBox datos = new javafx.scene.layout.VBox(10);
        datos.setPadding(new Insets(20, 30, 30, 15));

        javafx.scene.control.Label calle = new javafx.scene.control.Label("Calle: " + nombreCalle);
        javafx.scene.control.Label precio = new javafx.scene.control.Label("Precio: Consulta con el propietario");
        if(alquilada == 1) precio.setText("Precio: " + precioVivienda + "€ en total");
        if(alquilada == 2) precio.setText("Precio: " + precioVivienda + "€ /mes");
        
        datos.getChildren().addAll(calle, precio);

        miniatura.getChildren().addAll(botonRedireccion, datos);
        return miniatura;
    }

    //Lista de viviendas
    private void ordenarLista() {
        for (int i = 0; i < viviendasList.size(); ++i) {
            try {
                String id = viviendasList.get(i);
                String foto = ConectorViviendaBD.consultarFoto(viviendasList.get(i));
                String calle = ConectorViviendaBD.consultarDireccion(viviendasList.get(i));
                int precio = ConectorViviendaBD.consultarPrecio(viviendasList.get(i));
                int alquilada = ConectorViviendaBD.consultarAlquiler(viviendasList.get(i));
                this.listaViviendas.getChildren().add(crearMiniatura(id, foto, calle, precio, alquilada));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FavoritosController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void IniciarSesion(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/trobify/views/IniciarSesion.fxml"));
        IniciarSesionController.deDondeViene("buscador");
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
    private void Registrarse(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/trobify/views/RegistrarUsuario.fxml"));
        RegistrarUsuarioController.deDondeViene("buscador");
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
    private void favBoton(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/trobify/views/Favoritos.fxml"));
        FavoritosController.pasarUsuario(username);
        s.close();
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        FavoritosController.pasarStage(stage);
        stage.setScene(scene);
        stage.setTitle("Favoritos");
        stage.show();
        event.consume();
    }

    @FXML
    private void notifica(ActionEvent event) throws IOException {
        //notificar(null);
        ArrayList<Vivienda> viviendasFav = ConectorViviendaBD.getFavoritosUsuario(username);
        
        ArrayList<String> res = new ArrayList<String>();
        
       /* ///////////////////////////////////////////////////////////////////
        Vivienda prueba = new Vivienda();
        prueba.setCalle("Calle de pruebas");
        prueba.setActivo(1);
        viviendasFav.add(prueba);
        //////////////////////////////////////////////////////////////////*/
        
        for(int i = 0; i < viviendasFav.size(); i++){
            
            if (viviendasFav.get(i).getActivo() == 1){
                
                res.add("La vivienda de la " +viviendasFav.get(i).getCalle() +" ya no se encuentra disponible.");
            }
        }
        NotificacionesController.pasarNotis(res);
        
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
    }

    @FXML
    private void RegistrarViv(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/trobify/views/RegistrarVivienda.fxml"));
        RegistrarViviendaController.pasarUsuario(username);
        s.close();
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        RegistrarViviendaController.pasarStage(stage);
        stage.setScene(scene);
        stage.setTitle("Registrar vivienda");
        stage.show();
        event.consume();
    }

}// fin clase
