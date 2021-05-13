/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify.controlador;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import static jdk.nashorn.internal.objects.NativeArray.forEach;
import trobify.fachada.FachadaBD;
import trobify.logica.Fotografia;
import trobify.logica.Servicios;
import trobify.logica.Vivienda;

/**
 * FXML Controller class
 *
 * @author davido747
 */
public class RegistrarViviendaController implements Initializable {

    @FXML
    private TextField calleField;
    @FXML
    private TextField numeroField;
    @FXML
    private TextField pisoField;
    @FXML
    private TextField CiudadField;
    @FXML
    private TextField codigoField;
    @FXML
    private TextField precioField;
    @FXML
    private TextField habitacionesField;
    @FXML
    private TextField bañosField;
    @FXML
    private ComboBox<String> ComprarAlquilar;
    @FXML
    private ComboBox<String> TipoVivienda;
    @FXML
    private TextArea descripcionField;
    @FXML
    private HBox listaDeFotos;
    @FXML
    private RadioButton botonSupermercado;
    @FXML
    private RadioButton BotonTransportePublico;
    @FXML
    private RadioButton botonEstanco;
    @FXML
    private RadioButton botonGimnasio;
    @FXML
    private RadioButton botonCentroComercial;
    @FXML
    private RadioButton botonBanco;
    @FXML
    private RadioButton botonFarmacia;

  
    private static Stage st;
    private static String username;
    private static int alquilerOVenta;
    private static int tipo;
    private static int supermercado = 0;
    private static int transportePublico = 0;
    private static int estanco = 0;
    private static int gimnasio = 0;
    private static int centroComercial = 0;
    private static int banco = 0;
    private static int farmacia = 0;

    private static ArrayList<String> FotosSource = new ArrayList<String>();
    private static ArrayList<Fotografia> fotos = new ArrayList<Fotografia>();

    @FXML
    private Button registrarBoton;
    private static String vieneDe;
    private String direccion;
    @FXML
    private Button registrarYPublicarBoton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       rellenoComboBox();

        //El botón registrar no está disponible hasta rellenar los campos obligatorios
        final BooleanBinding sePuedeBuscar = Bindings.isEmpty(calleField.textProperty())
                .or(Bindings.isEmpty(numeroField.textProperty()))
                .or(Bindings.isEmpty(pisoField.textProperty()))
                .or(Bindings.isEmpty(CiudadField.textProperty()))
                .or(Bindings.isEmpty(codigoField.textProperty()))
                .or(Bindings.isEmpty(precioField.textProperty()))
                .or(Bindings.isEmpty(habitacionesField.textProperty()))
                .or(Bindings.isEmpty(bañosField.textProperty()))
                .or(Bindings.isNull(TipoVivienda.getSelectionModel().selectedItemProperty()))
                .or(Bindings.isNull(ComprarAlquilar.getSelectionModel().selectedItemProperty()));

        registrarBoton.disableProperty().bind(sePuedeBuscar);
        registrarYPublicarBoton.disableProperty().bind(sePuedeBuscar);

        //Los field numericos no aceptan otra cosa que no numeros sean
        codigoField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                    String newValue) {
                if (!newValue.matches("\\d*")) {
                    codigoField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        habitacionesField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                    String newValue) {
                if (!newValue.matches("\\d*")) {
                    habitacionesField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        bañosField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                    String newValue) {
                if (!newValue.matches("\\d*")) {
                    bañosField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        precioField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                    String newValue) {
                if (!newValue.matches("\\d*")) {
                    precioField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        pisoField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                    String newValue) {
                if (!newValue.matches("\\d*")) {
                    pisoField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        direccion();
    }

     private void direccion(){
        if(vieneDe.equals("inicio"))
           direccion = "/trobify/views/Inicio.fxml";
        
        if(vieneDe.equals("buscador"))
            direccion = "/trobify/views/Buscador.fxml";
       else { //otros sitios de donde pueda venir
           }
    }
    @FXML
    private void atras(ActionEvent event) throws IOException {
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

     public static void deDondeViene (String donde){
         vieneDe = donde;
    }
     
    @FXML
    private void añadirFoto(ActionEvent event) throws IOException {
        FileChooser imageChooser = new FileChooser();
        imageChooser.setTitle("Seleccionar Imagen");

        imageChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Todas las Imágenes", "*.jpg", "*.png", "*.ico", "*.PNG", "*.JPG"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );

        File imgFile = imageChooser.showOpenDialog(null);
        String origen = imgFile.getCanonicalPath();

        String cadena = origen;
        String[] parts = cadena.split("\\\\");
        String direccion = parts[parts.length - 1];

        //String destino = "F:\\PSW\\SoftUpv\\src\\trobify\\images\\" + direccion;
        String destino = "src\\trobify\\images\\" + direccion;
        Path origenPath = FileSystems.getDefault().getPath(origen);
        Path destinoPath = FileSystems.getDefault().getPath(destino);

        try {
            Files.move(origenPath, destinoPath);
        } catch (IOException e) {
            System.err.println(e);
        }

        String idFotoBD = "src\\\\trobify\\\\images\\\\" + direccion;
        FotosSource.add(idFotoBD);
        mostrarFotos();
    }

    @FXML
    private void registrar(ActionEvent event) throws IOException {
        int numero = FachadaBD.numeroViviendas() + 1;
        String id = "vivienda" + numero;
        int precio = Integer.parseInt(precioField.getText());
        int baños = Integer.parseInt(bañosField.getText());
        int habitaciones = Integer.parseInt(habitacionesField.getText());
        int piso = Integer.parseInt(habitacionesField.getText());
        int codigo = Integer.parseInt(codigoField.getText());

        if (ComprarAlquilar.getSelectionModel().selectedItemProperty().getValue().equals("Vender")) {
            alquilerOVenta = 1;
        } else {
            alquilerOVenta = 2;
        }

        if (TipoVivienda.getSelectionModel().selectedItemProperty().getValue().equals("Casa")) {
            tipo = 2;
        } else {
            tipo = 1;
        }

        if (botonSupermercado.isSelected()) {
            supermercado = 1;
        }
        if (BotonTransportePublico.isSelected()) {
            transportePublico = 1;
        }
        if (botonEstanco.isSelected()) {
            estanco = 1;
        }
        if (botonGimnasio.isSelected()) {
            gimnasio = 1;
        }
        if (botonCentroComercial.isSelected()) {
            centroComercial = 1;
        }
        if (botonFarmacia.isSelected()) {
            farmacia = 1;
        }
        if (botonBanco.isSelected()) {
            banco = 1;
        }

        //codigo para pasar las fotos añadidas al array
        //direccion gabriela C:\\\\Users\\\\gabi\\\\Desktop\\\\gabri\\\\SoftUpv\\\\src\\\\trobify\\\\images\\\\foto0.jpeg
        if (FotosSource.isEmpty()) {
            FotosSource.add("src\\\\trobify\\\\images\\\\foto0.jpeg");
        }

        Vivienda vivi = new Vivienda(id, calleField.getText(), CiudadField.getText(), alquilerOVenta, "ninguna", precio, username, tipo, baños, habitaciones,
                descripcionField.getText(), piso, numeroField.getText(), codigo, 0);
        Servicios servi = new Servicios(id, supermercado, transportePublico, banco, estanco, centroComercial, gimnasio, farmacia);

        for (String f : FotosSource) {
            fotos.add(new Fotografia(f, id));
        }

      Alert alerta1 = new Alert(Alert.AlertType.CONFIRMATION);
        alerta1.setHeaderText("¿Seguro que desea registrar esta vivienda?");
        Optional<ButtonType> ok = alerta1.showAndWait();
        if (ok.isPresent() && ok.get().equals(ButtonType.OK)) {
            FachadaBD.registrarVivienda(vivi, servi, fotos);

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setHeaderText("Vivienda registrada correctamente.");
            Optional<ButtonType> vale = alerta.showAndWait();
            if (vale.isPresent() && vale.get().equals(ButtonType.OK)) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource(direccion));
                st.close();
                Stage stage = new Stage();
                Scene scene = new Scene(fxmlLoader.load());
                BuscadorController.pasarStage(stage);
                InicioController.pasarStage(stage);
                stage.setScene(scene);
                stage.setTitle("Trobify");
                stage.show();
                event.consume();

            }
            alerta.close();
        }
        alerta1.close();
    
    }

    private void rellenoComboBox() {

        ArrayList<String> tiposViviendas = new ArrayList<String>();
        tiposViviendas.add("Piso");
        tiposViviendas.add("Casa");
        ObservableList<String> viv = FXCollections.observableList(tiposViviendas);
        TipoVivienda.setItems(viv);

        ArrayList<String> queHacer = new ArrayList<String>();
        queHacer.add("Vender");
        queHacer.add("Alquilar");
        ObservableList<String> caoc = FXCollections.observableList(queHacer);
        ComprarAlquilar.setItems(caoc);
    }

    public static void pasarUsuario(String usuario) {
        username = usuario;
    }

    public static void pasarStage(Stage m) {
        st = m;
    }

    private javafx.scene.layout.VBox crearFotos(String rutaFoto) throws FileNotFoundException {

        javafx.scene.layout.VBox fotosConBoton = new javafx.scene.layout.VBox();
        fotosConBoton.setSpacing(5);
        fotosConBoton.setAlignment(Pos.TOP_CENTER);

        Image image1 = new Image(new FileInputStream(rutaFoto));
        javafx.scene.image.ImageView foto = new javafx.scene.image.ImageView(image1);
        foto.setFitWidth(175);
        foto.setFitHeight(125);

        Button botonEliminar = new Button("Eliminar");
        botonEliminar.setId(rutaFoto);
        botonEliminar.setOnAction(e -> {
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
            alerta.setHeaderText("¿Seguro que quieres eliminar esta fotografía?");
            Optional<ButtonType> ok = alerta.showAndWait();
            if (ok.isPresent() && ok.get().equals(ButtonType.OK)) {
                FotosSource.remove(botonEliminar.getId());
                mostrarFotos();
            }
            alerta.close();

        });

        fotosConBoton.getChildren().addAll(foto, botonEliminar);
        return fotosConBoton;
    }

    private void mostrarFotos() {
        listaDeFotos.getChildren().clear();
        listaDeFotos.setSpacing(5);

        for (int i = 0; i < FotosSource.size(); ++i) {
            try {
                listaDeFotos.getChildren().add(crearFotos(FotosSource.get(i)));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FavoritosController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void registrarYPublicar(ActionEvent event) throws IOException {
        int numero = FachadaBD.numeroViviendas() + 1;
        String id = "vivienda" + numero;
        int precio = Integer.parseInt(precioField.getText());
        int baños = Integer.parseInt(bañosField.getText());
        int habitaciones = Integer.parseInt(habitacionesField.getText());
        int piso = Integer.parseInt(habitacionesField.getText());
        int codigo = Integer.parseInt(codigoField.getText());

        if (ComprarAlquilar.getSelectionModel().selectedItemProperty().getValue().equals("Vender")) {
            alquilerOVenta = 1;
        } else {
            alquilerOVenta = 2;
        }

        if (TipoVivienda.getSelectionModel().selectedItemProperty().getValue().equals("Casa")) {
            tipo = 2;
        } else {
            tipo = 1;
        }

        if (botonSupermercado.isSelected()) {
            supermercado = 1;
        }
        if (BotonTransportePublico.isSelected()) {
            transportePublico = 1;
        }
        if (botonEstanco.isSelected()) {
            estanco = 1;
        }
        if (botonGimnasio.isSelected()) {
            gimnasio = 1;
        }
        if (botonCentroComercial.isSelected()) {
            centroComercial = 1;
        }
        if (botonFarmacia.isSelected()) {
            farmacia = 1;
        }
        if (botonBanco.isSelected()) {
            banco = 1;
        }

        //codigo para pasar las fotos añadidas al array
        //direccion gabriela C:\\\\Users\\\\gabi\\\\Desktop\\\\gabri\\\\SoftUpv\\\\src\\\\trobify\\\\images\\\\foto0.jpeg
        if (FotosSource.isEmpty()) {
            FotosSource.add("src\\\\trobify\\\\images\\\\foto0.jpeg");
        }

        Vivienda vivi = new Vivienda(id, calleField.getText(), CiudadField.getText(), alquilerOVenta, "ninguna", precio, username, tipo, baños, habitaciones,
                descripcionField.getText(), piso, numeroField.getText(), codigo, 1);
        Servicios servi = new Servicios(id, supermercado, transportePublico, banco, estanco, centroComercial, gimnasio, farmacia);

        for (String f : FotosSource) {
            fotos.add(new Fotografia(f, id));
        }

      Alert alerta1 = new Alert(Alert.AlertType.CONFIRMATION);
        alerta1.setHeaderText("¿Seguro que desea registrar y publicar esta vivienda?");
        Optional<ButtonType> ok = alerta1.showAndWait();
        if (ok.isPresent() && ok.get().equals(ButtonType.OK)) {
            FachadaBD.registrarVivienda(vivi, servi, fotos);

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setHeaderText("Vivienda registrada y publicada correctamente.");
            Optional<ButtonType> vale = alerta.showAndWait();
            if (vale.isPresent() && vale.get().equals(ButtonType.OK)) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource(direccion));
                st.close();
                Stage stage = new Stage();
                Scene scene = new Scene(fxmlLoader.load());
                BuscadorController.pasarStage(stage);
                InicioController.pasarStage(stage);
                stage.setScene(scene);
                stage.setTitle("Trobify");
                stage.show();
                event.consume();

            }
            alerta.close();
        }
        alerta1.close();
    
    }
    
   
}
