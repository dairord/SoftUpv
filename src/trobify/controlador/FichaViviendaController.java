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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import trobify.Conectar;

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
    String id;
    Boolean estaEnFav;
    ArrayList<String> listaFotos;
    ArrayList<String> listaRecomendados;
    ArrayList<String> listaServicios;
    int valoracion;
    int precioBase;
    @FXML
    private Text precioVivienda;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //Crear una conexion
        con = new Conectar();
        
        //Dando valor a mano de la id de vivienda AQUI SE DEBERA
        //PASAR EL ID DE LA VIVIENDA DESDE LA VENTANA ANTERIOR
        this.id = "vivienda3";
        this.precioBase = consultarPrecio(id);
        
        //Mostrar botones de valoraciones de favortitos o no
        this.estaEnFav = estaEnFavoritos(this.id);
        estaValorado();
        mostrarBotones(); 
        
        //Crear Array con la lista de fotos de la galeria
        this.listaFotos = new ArrayList();
        crearListaFotos(this.id);
        
        //Crear Array con la lista de fotos de la galeria
        this.listaRecomendados = new ArrayList();
        crearListaRecomendados(this.id);
                       
        //Mostrar el precio de la vivienda    
        precioVivienda.setText("Precio: " +this.precioBase + "€");
        //Boton de volver atras        
            //volver.setOnAction(e -> scenePropia.setScene(scenaPrevia));      
                
        //Generador de fotos de la galeria       
        for(int i = 0; i < listaFotos.size(); i++){
            try{
                this.imageList.getChildren().add(galeria(listaFotos.get(i)));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FavoritosController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        //Mostrar la descripcion de la vivienda
        this.descripcion.setText(consultarDescripcion(this.id));
        
        //Servicios cerca de la vivienda
        this.listaServicios = new ArrayList();
        consultarServicios(id);
        
        //Viviendas recomendadas        
        for (int i = 0; i < listaRecomendados.size(); i++){
            Button botonRecomendacion = new Button();
            botonRecomendacion.setPadding(new Insets(0, 0, 0, 0));
            botonRecomendacion.setId("botonRecomendacion" +i);
            try{
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
        
    }    
    
    //Generador de listaFotos
    private void crearListaFotos(String id){
        try {
            Statement stm = con.getConnection().createStatement();
            ResultSet rsl = stm.executeQuery ("SELECT id FROM fotografia WHERE id_vivienda = '" + id + "'");
            if(rsl.first()){
                rsl.beforeFirst();
                while (rsl.next()) {
                    this.listaFotos.add(rsl.getString("id"));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Generador de listaFotos
    private void crearListaRecomendados(String id){        
        
        int precioAlto = this.precioBase + 150;
        int precioBajo = this.precioBase - 150;
        int i = 0;
        try {
            Statement stm = con.getConnection().createStatement();
            ResultSet rsl = stm.executeQuery ("SELECT f.id FROM fotografia f WHERE f.id_vivienda IN (SELECT v.id FROM vivienda v WHERE v.id NOT LIKE '" + this.id + "' AND v.precio BETWEEN '" + precioBajo + "' AND '" + precioAlto + "')");
            if(rsl.first()){
                rsl.beforeFirst();
                while(rsl.next() && i < 3){
                    this.listaRecomendados.add(rsl.getString(1));
                    i++;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    //Metodo para obtener y generar una foto a partir de una direccion
    private javafx.scene.image.ImageView galeria(String source) throws FileNotFoundException{  
        Image image = new Image(new FileInputStream(source));
        javafx.scene.image.ImageView fotoGaleria = new javafx.scene.image.ImageView(image);
        fotoGaleria.setFitWidth(200);
        fotoGaleria.setFitHeight(150);
        
        return fotoGaleria;
    }
    
    //Metodo para saber el precio de la vivienda
    public int consultarPrecio(String id){
        
        try {
            Statement stm = con.getConnection().createStatement();
            ResultSet rsl = stm.executeQuery("SELECT precio FROM vivienda WHERE id = '" + id + "'");
            if (rsl.first()) return rsl.getInt(1);
            
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    //Metodo para obtener la descripcion de la vivienda
    public String consultarDescripcion(String id){
        
        String texto = "No lo dejes escapar.";
        try {
            Statement stm = con.getConnection().createStatement();
            ResultSet rsl = stm.executeQuery("SELECT descripcion FROM vivienda WHERE id = '" + this.id + "'");
            if (rsl.first() && rsl.getNString(1) != null) texto = rsl.getNString(1);
            
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return texto;
    }
    
        //Metodo para obtener la descripcion de la vivienda
    public void consultarServicios(String id){
        
        try {
            Statement stm = con.getConnection().createStatement();
            ResultSet rsl = stm.executeQuery("SELECT supermercado, transporteP, banco, estanco, centroC, gimnasio, farmacia FROM servicios WHERE id = '" + this.id + "'");
            rsl.first();
            for(int i = 0; i < 8 ; i++){
                try{
                    if(rsl.getObject(i) != null) this.listaServicios.add(rsl.getString(i));
                } catch (SQLException ex) {}                
            }                
                        
            ObservableList servicios = FXCollections.observableList(listaServicios);     
            this.serviciosCerca.setItems(servicios);       
            
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    //Comprobar si esta en favoritos
    private Boolean estaEnFavoritos(String idVivienda) {
        
        this.estaEnFav = false;
        
        try {
            Statement stm = con.getConnection().createStatement();
            ResultSet rsl = stm.executeQuery ("SELECT id FROM favoritos WHERE id LIKE '" + this.id + "'");
            if(rsl.first()){
                this.estaEnFav = true;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return this.estaEnFav;
    }
    
    //Muestra botones de favoritos
    public void mostrarBotones(){
        
        if(this.estaEnFav){
            valoracionGrupo.setVisible(true);
            addFavoritos.setText("Quitar de favoritos");
            logicaBotonesValoracion();
            
        } else {
            valoracionGrupo.setVisible(false);
            addFavoritos.setText("Añadir a favoritos");
        }    
    }
    
    private void logicaBotonesValoracion(){
        if (valoracion == -1 || valoracion == 0){
            textoValoracion.setText("Valoracion: ");
            valorValoracion.clear();
            valorValoracion.setVisible(true);
            editarValoracion.setText("Añadir");
        }
        else{
            textoValoracion.setText("Valoracion: " + this.valoracion);
            valorValoracion.setVisible(false);
            editarValoracion.setText("Editar");
        }
    }
    
    private int estaValorado(){
        
        try {
            Statement stm = con.getConnection().createStatement();
            ResultSet rsl = stm.executeQuery("SELECT valoracion FROM favoritos WHERE id = '" + this.id + "'");
            if (rsl.first()) this.valoracion = rsl.getInt(1);
            System.out.println(valoracion);
            
        } catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return this.valoracion;
    }
    
    @FXML
    private void addFav(ActionEvent event) throws IOException {
        
        if(!estaEnFav) {
            try {
                Statement stm = con.getConnection().createStatement();
                stm.executeUpdate("INSERT INTO `favoritos`(`id`, `id_cliente`) VALUES ('" + this.id + "','" + 1 + "')");
                this.estaEnFav = true;
                mostrarBotones();
            } catch (SQLException ex) {
                Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else{
            try {
                Statement stm = con.getConnection().createStatement();
                stm.executeUpdate("DELETE FROM favoritos WHERE id = '" + this.id + "'");
                this.estaEnFav = false;
                this.valoracion = -1;
                mostrarBotones();
            } catch (SQLException ex) {
                Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        logicaBotonesValoracion();
    }

    @FXML
    private void editarValoracion(ActionEvent event) {     
        if(this.valoracion == -1 || this.valoracion == 0/*AÑADIR*/){
            this.valoracion = Integer.parseInt(valorValoracion.getText());
            try {
                Statement stm = con.getConnection().createStatement();
                stm.executeUpdate("UPDATE `favoritos` SET `valoracion`='" + this.valoracion + "' WHERE id = '" + this.id + "'");
                
            } catch (SQLException ex) {
                Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else{
            this.valoracion = -1;
        }
        logicaBotonesValoracion();
    }
}