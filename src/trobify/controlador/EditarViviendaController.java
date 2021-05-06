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
import java.sql.Array;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import trobify.logica.FachadaBD;
import trobify.logica.Servicios;
import trobify.logica.Vivienda;

/**
 * FXML Controller class
 *
 * @author gabri
 */
public class EditarViviendaController implements Initializable {

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
    @FXML
    private Button registrarBoton;

    private static Stage st;
    private static String username;
    private static String id;
    Vivienda vivi;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        soloNumeros();
        autorellenoDatos();
        mostrarServicios();
        mostrarFotos();
        rellenoComboBox();
    }    
    
    private void soloNumeros(){
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
    }
    
    private void autorellenoDatos(){
        vivi = FachadaBD.getVivienda(id);
        calleField.setText(vivi.getCalle());
        numeroField.setText(vivi.getPuerta());
        pisoField.setText(vivi.getPiso()+ " ");
        CiudadField.setText(vivi.getCiudad());
        codigoField.setText(vivi.getCodigo_postal()+ "");
        precioField.setText(vivi.getPrecio()+ "");
        habitacionesField.setText(vivi.getHabitaciones() + "");
        bañosField.setText(vivi.getBaños()+ " ");
        pisoOCasa();
        comprarOVender();
        descripcionField.setText(vivi.getDescripcion() + "");
        
    }
    private void pisoOCasa(){
        if(vivi.getTipo()==1) TipoVivienda.getSelectionModel().select("Piso");
        else TipoVivienda.getSelectionModel().select("Casa");
    }
    
    private void comprarOVender(){
        if(vivi.getVentaAlquiler()==1) ComprarAlquilar.getSelectionModel().select("Vender");
        else ComprarAlquilar.getSelectionModel().select("Alquilar");
    }
    private void mostrarServicios(){
        Servicios servi = FachadaBD.getServicios(id);
        if(servi.getSupermercado()==1) botonSupermercado.selectedProperty().set(true);
        if(servi.getTransporte_publico()==1) BotonTransportePublico.selectedProperty().set(true);
        if(servi.getBanco()==1) botonBanco.selectedProperty().set(true);
        if(servi.getCentro_comercial()==1) botonCentroComercial.selectedProperty().set(true);
        if(servi.getGimnasio()==1) botonGimnasio.selectedProperty().set(true);
        if(servi.getFarmacia()==1) botonFarmacia.selectedProperty().set(true);
        if(servi.getEstanco()==1) botonEstanco.selectedProperty().set(true);
    }
    
    private void rellenoComboBox() {
        ArrayList<String> tiposViviendas = new ArrayList<String>();
        tiposViviendas.add("Piso");
        tiposViviendas.add("Casa");
        ObservableList<String> viv = FXCollections.observableList(tiposViviendas);
        TipoVivienda.setItems(viv);
        
        ArrayList <String> queHacer = new ArrayList <String> ();
        queHacer.add("Vender");
        queHacer.add("Alquilar");
        ObservableList<String> caoc = FXCollections.observableList(queHacer);
        ComprarAlquilar.setItems(caoc);
    }
     
    
    

    @FXML
    private void atras(ActionEvent event) throws IOException {
        FichaViviendaController.pasarUsuario(username);
        FichaViviendaController.pasarIdVivienda(id);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/trobify/views/FichaVivienda.fxml"));
        st.close();
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        FichaViviendaController.pasarStage(stage);
        stage.setScene(scene);
        stage.setTitle("Trobify");
        stage.show();
        event.consume();
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
        
        FachadaBD.añadirFotografia(idFotoBD, id);
        mostrarFotos();
    }
    
    private Vivienda viviendaActualizada(){
        int compOalq = 0;
        if(ComprarAlquilar.getSelectionModel().getSelectedItem().equals("Vender")) compOalq = 1;
        if(ComprarAlquilar.getSelectionModel().getSelectedItem().equals("Alquilar")) compOalq = 2;

      int tipo = 0;
        if(TipoVivienda.getSelectionModel().getSelectedItem().equals("Piso")) tipo = 1;
        if(TipoVivienda.getSelectionModel().getSelectedItem().equals("Casa")) tipo = 2;

        Vivienda actualizada = new Vivienda(id, calleField.getText(), CiudadField.getText(),
                compOalq, "ninguna", Integer.parseInt(precioField.getText()), username, tipo,
                Integer.parseInt(bañosField.getText()),Integer.parseInt(habitacionesField.getText()),
                descripcionField.getText(), Integer.parseInt(pisoField.getText()),
                numeroField.getText(), Integer.parseInt(codigoField.getText()), 0 );
        return actualizada;
    }
    
    private Servicios serviciosActualizados(){
        int supermercado = 0;
        int transportePublico = 0;
        int estanco = 0;
        int gimnasio = 0;
        int centroComercial = 0;
        int banco = 0;
        int farmacia = 0;
    
        if(botonSupermercado.isSelected()) {supermercado = 1;}
        if(BotonTransportePublico.isSelected()) {transportePublico = 1;}
        if(botonEstanco.isSelected()) {estanco = 1;}
        if(botonGimnasio.isSelected()) {gimnasio = 1;}
        if(botonCentroComercial.isSelected()) {centroComercial = 1;}
        if(botonFarmacia.isSelected()) {farmacia = 1;}
        if(botonBanco.isSelected()) {banco = 1;}
        System.out.print(supermercado);
        Servicios actualizado = new Servicios(id, supermercado, transportePublico, banco, estanco, centroComercial, gimnasio, farmacia);
    
         return actualizado;
    }      
    
    @FXML
    private void registrar(ActionEvent event) throws IOException {
         Alert alerta1 = new Alert (Alert.AlertType.CONFIRMATION);
        alerta1.setHeaderText("Seguro que quieres actul¡alizar la vivienda?");
        Optional<ButtonType> aceptar = alerta1.showAndWait();
        if(aceptar.isPresent() && aceptar.get().equals(ButtonType.OK)) {
                Vivienda viviActualizada = viviendaActualizada();
                Servicios servActualizado = serviciosActualizados();
                FachadaBD.actualiarVivienda(viviActualizada, servActualizado);
            
                Alert alerta2 = new Alert (Alert.AlertType.INFORMATION);
                    alerta2.setHeaderText("Vivienda actualizada correctamente");
                    Optional<ButtonType> ok = alerta2.showAndWait();
                    if(ok.isPresent() && ok.get().equals(ButtonType.OK)) {
            volverAtras();
    } alerta2.close();
            
    } alerta1.close();
        
    }
    
    public void volverAtras() throws IOException{
        FichaViviendaController.pasarUsuario(username);
        FichaViviendaController.pasarIdVivienda(id);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/trobify/views/FichaVivienda.fxml"));
        st.close();
        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        FichaViviendaController.pasarStage(stage);
        stage.setScene(scene);
        stage.setTitle("Trobify");
        stage.show();
    }
    
    public static void pasarStage(Stage s){
        st = s;
    }
    
    public static void pasarDatos (String u, String vivi){
        username = u;
        id = vivi;
    }
    
    private void mostrarFotos(){
        listaDeFotos.getChildren().clear();
        ArrayList<String> fotos = FachadaBD.listaFotos(id);
        listaDeFotos.setSpacing(5);
        
        for (int i = 0; i < fotos.size(); ++i) {
        try{
            listaDeFotos.getChildren().add(crearFotos(fotos.get(i)));
        } catch (FileNotFoundException ex) {
                Logger.getLogger(FavoritosController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private javafx.scene.layout.VBox crearFotos(String rutaFoto) throws FileNotFoundException{
        
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
            Alert alerta = new Alert (Alert.AlertType.CONFIRMATION);
            alerta.setHeaderText("¿Seguro que quieres eliminar esta fotografía?");
            Optional<ButtonType> ok = alerta.showAndWait();
            if(ok.isPresent() && ok.get().equals(ButtonType.OK)) {
                FachadaBD.eliminarFoto(rutaFoto, id);
                mostrarFotos();
            }
            alerta.close();
            
        });
        
        fotosConBoton.getChildren().addAll(foto, botonEliminar);        
        return fotosConBoton;
    }
}

