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
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
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
import trobify.Conectar;

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
    private static ResultSet viviendas;
    ArrayList<String> viviendasList;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //si no ha iniciado sesión
        nombreUsuario.setText("usuario");
        //Crear una conexion
        con = new Conectar();
        //boton buscar desactivado si no están todos los filtros
        final BooleanBinding sePuedeBuscar = Bindings.isEmpty(precioMin.textProperty())
                .or(Bindings.isEmpty(precioMax.textProperty()))
                .or(Bindings.isEmpty(ciudad.textProperty()))
                .or(Bindings.isEmpty(numBaños.textProperty()))
                .or(Bindings.isEmpty(numHabitaciones.textProperty())) // .or(Bindings.)
                ;

        buscarBoton.disableProperty().bind(sePuedeBuscar);
        botonGuardarFiltros.disableProperty().bind(sePuedeBuscar);

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

        // variación filtros
        /*  ArrayList <String> varia = new ArrayList <String> ();
     varia.add("0%");
     varia.add("10%");
     varia.add("30%");
     varia.add("50%");
     ObservableList<String> variaFiltros = FXCollections.observableList(varia);
     variacionFiltros.setItems(variaFiltros);
     variacionFiltros.getSelectionModel().selectFirst();*/
        // variación fecha
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
        //poner directamente el nombre de la ciudad buscada y tipo vivienda
        ciudad.setText(ciu);
        tipoVivienda.getSelectionModel().select(tip);

        viviendasList = new ArrayList();
        buscarOrdenadas();
        ordenarLista();

        //Inicialización del WebView para que se muestre GoogleMaps
        System.setProperty("java.net.useSystemProxies", "true");
        final URL googleMaps = getClass().getResource("GeoPrueba.html");
        final WebEngine engine = mapa.getEngine();
        engine.load(googleMaps.toExternalForm());
    } //fin initialice

    @FXML
    private void guardarFiltros(ActionEvent event) throws IOException {
        if (comprobarNumeros()) {
            errorText.setText("");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/trobify/views/MantenerFiltros.fxml"));
            MantenerFiltrosController.pasarFiltrosBuscar(ciudad.getText(), tipoVivienda.getSelectionModel().selectedItemProperty().getValue(), alqOVen,
                    precioMin.getText(), precioMax.getText(), numBaños.getText(), numHabitaciones.getText(), fechaEntrada.getValue(), fechaSalida.getValue());
            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            MantenerFiltrosController.pasarStage(stage);

            stage.setScene(scene);
            stage.setTitle("Trobify");
            stage.show();
            event.consume();
        } else {
            errorText.setText("Debes introducir números validos");
        }
    }

    @FXML
    private void notificar(ActionEvent event) {
    }

    @FXML
    private void buscar(ActionEvent event) {
        if (comprobarNumeros()) {
            errorText.setText("");
            buscarOrdenadas();
            listaViviendas.getChildren().clear();
            ordenarLista();
        } else {
            errorText.setText("Debes introducir números validos");
        }
    }

    @FXML
    private void Inicio(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/trobify/views/Inicio.fxml"));
        s.close();
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        BuscadorController.pasarStage(stage);
        stage.setScene(scene);
        stage.setTitle("Trobify");
        stage.show();
        event.consume();
    }

    public static void pasarStage(Stage m) {
        s = m;
    }

    public static void pasarFiltrosInicio(String c, String t, int queBuscas) {
        ciu = c;
        tip = t;
        alqOVen = queBuscas;
    }

    private void buscarOrdenadas() {
        if (precioMin.getText().equals("") || precioMax.getText().equals("") || numHabitaciones.getText().equals("")
                || numBaños.getText().equals("")) {
            ordenSinFinltrosConsulta();
        } else {
            ordenarConsulta();
        }

    }

    @FXML
    private void ordenar(ActionEvent event) {

        if (precioMin.getText().equals("") || precioMax.getText().equals("") || numHabitaciones.getText().equals("")
                || numBaños.getText().equals("")) {
            ordenSinFinltrosConsulta();
            listaViviendas.getChildren().clear();
            ordenarLista();
        } else {
            if (comprobarNumeros()) {
                errorText.setText("");
                ordenarConsulta();
                listaViviendas.getChildren().clear();
                ordenarLista();
            } else {
                errorText.setText("Debes introducir números validos");

            }
        }
    }

    private void ordenarConsulta() {
        viviendasList.clear();
        String comoOrdenar;
        if (ordenarPor.getSelectionModel().selectedItemProperty().getValue().equals("Relevancia")) {
            comoOrdenar = "id";
        } else if (ordenarPor.getSelectionModel().selectedItemProperty().getValue().equals("Precio más bajo")) {
            comoOrdenar = "precio ASC";
        } else {
            comoOrdenar = "precio DESC";
        }

        ResultSet rs2;
        int tipo;
        if (tipoVivienda.getSelectionModel().selectedItemProperty().getValue().equals("Piso")) {
            tipo = 1;
        } else if (tipoVivienda.getSelectionModel().selectedItemProperty().getValue().equals("Casa")) {
            tipo = 2;
        } else {
            tipo = 3;
        }

        Statement s;
        if (tipo != 3) {
            try {
                s = con.getConnection().createStatement();
                rs2 = s.executeQuery("select * from vivienda where ciudad = '" + ciudad.getText() + "' and tipo = " + tipo + " and ventaAlquiler = " + alqOVen
                        + " and precio > " + Integer.valueOf(precioMin.getText()) + " and precio < " + Integer.valueOf(precioMax.getText()) + " and baños = "
                        + Integer.valueOf(numBaños.getText())
                        + " and habitaciones = " + Integer.valueOf(numHabitaciones.getText())
                        + " order by " + comoOrdenar); //fin consulta
                if (rs2.first()) {
                    System.out.println(rs2.getString("id"));
                    rs2.beforeFirst();
                    while (rs2.next()) {
                        viviendasList.add(rs2.getString("id"));
                    }
                    /* listaViviendas.getChildren().clear();
                ordenarLista();*/
                }

            } catch (SQLException ex) {
                Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
            }//fin catch

        } //fin if tipo!=3
        else { //misma consulta pero sin mirar el tipo
            try {
                s = con.getConnection().createStatement();
                rs2 = s.executeQuery("select * from vivienda where ciudad = '" + ciudad.getText() + "' and ventaAlquiler = " + alqOVen
                        + " and precio > " + Integer.valueOf(precioMin.getText()) + " and precio < " + Integer.valueOf(precioMax.getText()) + " and baños = "
                        + Integer.valueOf(numBaños.getText())
                        + " and habitaciones = " + Integer.valueOf(numHabitaciones.getText())
                        + " order by " + comoOrdenar
                ); //fin consulta
                if (rs2.first()) {
                    System.out.println(rs2.getString("id"));
                    rs2.beforeFirst();
                    while (rs2.next()) {
                        viviendasList.add(rs2.getString("id"));
                    }
                    /* listaViviendas.getChildren().clear();
                ordenarLista();*/
                }

            } catch (SQLException ex) {
                Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
            }//fin catch
        }
    }

    private void ordenSinFinltrosConsulta() {
        viviendasList.clear();
        String comoOrdenar;
        if (ordenarPor.getSelectionModel().selectedItemProperty().getValue().equals("Relevancia")) {
            comoOrdenar = "id";
        } else if (ordenarPor.getSelectionModel().selectedItemProperty().getValue().equals("Precio más bajo")) {
            comoOrdenar = "precio ASC";
        } else {
            comoOrdenar = "precio DESC";
        }

        ResultSet rs3;
        int tipo;
        if (tipoVivienda.getSelectionModel().selectedItemProperty().getValue().equals("Piso")) {
            tipo = 1;
        } else if (tipoVivienda.getSelectionModel().selectedItemProperty().getValue().equals("Casa")) {
            tipo = 2;
        } else {
            tipo = 3;
        }

        Statement s;
        if (tipo != 3) {

            try {

                s = con.getConnection().createStatement();
                rs3 = s.executeQuery("select * from vivienda where ciudad = '" + ciudad.getText() + "' and tipo = " + tipo + " and ventaAlquiler = " + alqOVen
                        + " order by " + comoOrdenar);
                if (rs3.first()) {
                    rs3.beforeFirst();
                    while (rs3.next()) {
                        viviendasList.add(rs3.getString("id"));

                    }
                    System.out.println(viviendasList.get(0));
                    /* listaViviendas.getChildren().clear();
                ordenarLista();*/
                }

            } catch (SQLException ex) {
                Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } //fin if tip!=3
        else {
            try {
                s = con.getConnection().createStatement();
                rs3 = s.executeQuery("select * from vivienda where ciudad = '" + ciudad.getText() + "' and ventaAlquiler = " + alqOVen
                        + " order by " + comoOrdenar);
                if (rs3.first()) {
                    rs3.beforeFirst();
                    while (rs3.next()) {
                        viviendasList.add(rs3.getString("id"));

                    }
                    System.out.println(viviendasList.get(0));
                    /* listaViviendas.getChildren().clear();
                ordenarLista();*/
                }

            } catch (SQLException ex) {
                Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private boolean comprobarNumeros() {
        if (isNumeric(precioMin.getText())
                && isNumeric(precioMax.getText())
                && isNumeric(numBaños.getText())
                && isNumeric(numHabitaciones.getText())) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isNumeric(String cadena) {
        try {
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    //generador de miniaturas
    private javafx.scene.layout.HBox crearMiniatura(String rutaFoto, String nombreCalle, int precioVivienda) throws FileNotFoundException {

        javafx.scene.layout.HBox miniatura = new javafx.scene.layout.HBox();

        miniatura.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        Image image1 = new Image(new FileInputStream(rutaFoto));
        javafx.scene.image.ImageView foto = new javafx.scene.image.ImageView(image1);
        foto.setFitWidth(200);
        foto.setFitHeight(150);

        javafx.scene.layout.VBox datos = new javafx.scene.layout.VBox(10);
        datos.setPadding(new Insets(20, 30, 30, 15));

        javafx.scene.control.Label calle = new javafx.scene.control.Label("Calle: " + nombreCalle);
        javafx.scene.control.Label precio = new javafx.scene.control.Label("Precio: " + precioVivienda + "/mes");

        datos.getChildren().addAll(calle, precio);

        miniatura.getChildren().addAll(foto, datos);
        return miniatura;
    }

    //Consultar la primera foto de la vivienda pasando como atributo un id
    public String consultarFoto(String id) {

        try {
            Statement stm = con.getConnection().createStatement();
            ResultSet rsl = stm.executeQuery("SELECT id FROM fotografia WHERE id_vivienda = '" + id + "'");
            if (rsl.first()) {
                return rsl.getNString(1);
            }

        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "C:\\Users\\migue\\Desktop\\softupv\\SoftUpv\\src\\trobify\\images\\noImage.jpeg";
        //"F:\\PSW\\SoftUpv\\src\\trobify\\images\\foto0.jpg"
    }

    //Consultar la direccion de la vivienda pasando como atributo un id    
    public String consultarDireccion(String id) {

        try {
            Statement stm = con.getConnection().createStatement();
            ResultSet rsl = stm.executeQuery("SELECT direccion FROM vivienda WHERE id = '" + id + "'");
            if (rsl.first()) {
                return rsl.getNString(1);
            }

        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "No disponible";
    }

    //Consultar el precio de la vivienda pasando como atributo un id    
    public int consultarPrecio(String id) {

        try {
            Statement stm = con.getConnection().createStatement();
            ResultSet rsl = stm.executeQuery("SELECT precio FROM vivienda WHERE id = '" + id + "'");
            if (rsl.first()) {
                return rsl.getInt(1);
            }

        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    //Lista de viviendas
    private void ordenarLista() {
        for (int i = 0; i < viviendasList.size(); ++i) {

            try {
                String foto = consultarFoto(viviendasList.get(i));
                String calle = consultarDireccion(viviendasList.get(i));
                int precio = consultarPrecio(viviendasList.get(i));
                this.listaViviendas.getChildren().add(crearMiniatura(foto, calle, precio));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FavoritosController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void IniciarSesion(ActionEvent event) {
    }

    @FXML
    private void Registrarse(ActionEvent event) {
    }

}// fin clase
