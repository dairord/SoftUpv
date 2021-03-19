/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import trobify.Conectar;

/**
 * FXML Controller class
 *
 * @author gabrielacorbalan
 */

public class MantenerFiltrosController implements Initializable {

    private static Stage s;
    private static String ciu;
    private static String tip;
    private static int alqOVen;
    private static String precioMin;
    private static String precioMax;
    private static String habitaciones;
    private static String baños;
    private static LocalDate fechaEntrada;
    private static LocalDate fechaSalida;
    private static Boolean hayFechas;
 
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    @FXML
    private void siBoton(ActionEvent event) throws IOException {
        Conectar con = new Conectar();
         if(alqOVen == 2 && hayFechas == true) {//Opción alquilar y los datepicker tienen fechas
            if (comprobarFiltros(con)) {
                borrarFiltrosAnteriores(con);
                insertarFiltrosConFechas(con);
            }
            else {insertarFiltrosConFechas(con);}
        }
         else {//Opción comprar o alquilar sin fechas en los datepicker
             if (comprobarFiltros(con)) {
                borrarFiltrosAnteriores(con);
                insertarFiltrosSinFechas(con);
            }
            else {insertarFiltrosSinFechas(con);}
         }
         
        Alert alerta = new Alert (Alert.AlertType.INFORMATION);
        alerta.setHeaderText("Filtros guardados correctamente!");
        Optional<ButtonType> resultado = alerta.showAndWait();
        if(resultado.isPresent() && resultado.get().equals(ButtonType.OK)) {
            s.close();
            alerta.close();
           
    } alerta.close();
    }

    @FXML
    private void noBoton(ActionEvent event) throws IOException {
          s.close();
          
    }
     public static void pasarStage(Stage m){
         s = m;
     }
     
     //Para obtener los filtros desde la pantalla de buscar
     public static void pasarFiltrosBuscar(String c, String t, int queBuscas, String p_Min, String p_Max, String b, String habi, LocalDate f_entr, LocalDate f_sal){
         ciu = c;
         tip = t;
         alqOVen = queBuscas;
         precioMin = p_Min;
         precioMax = p_Max;
         baños = b;
         habitaciones = habi;
         fechaEntrada = f_entr;
         fechaSalida = f_sal;
         if(fechaEntrada == null || fechaSalida == null) {hayFechas = false;}
         else {hayFechas = true;}
     }
     
     private void insertarFiltrosSinFechas(Conectar con) {
        int tipo;
        if(tip.equals("Piso")) tipo = 1;
        else if(tip.equals("Casa")) tipo = 2;
        else tipo = 3;
        Statement s1;
        try{
            s1 = con.getConnection().createStatement();
            s1.executeUpdate("INSERT INTO filtros(id, ciudad, tipo, p_min, p_max, habitaciones, baños, ventaAlquiler) "
                    + "VALUES(1, '"+ ciu +"', '"+ tipo +"', '"+ precioMin +"', '"+ precioMax +"', '"+ habitaciones +"',"
                    + " '"+ baños +"', '"+ alqOVen +"')");
            System.out.println("filtros guardados con exito");
        }catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }//fin catch 
     }
     
     private void insertarFiltrosConFechas(Conectar con) {
        int tipo;
        if(tip.equals("Piso")) tipo = 1;
        else if(tip.equals("Casa")) tipo = 2;
        else tipo = 3;
        Statement s1;
        try{
            s1 = con.getConnection().createStatement();
            s1.executeUpdate("INSERT INTO filtros(id, fecha_entrada, fecha_salida, ciudad, tipo, p_min, p_max, habitaciones, baños, ventaAlquiler) "
                    + "VALUES(1, '"+ fechaEntrada +"', '"+ fechaSalida +"', '"+ ciu +"', '"+ tipo +"', '"+ precioMin +"', '"+ precioMax +"', '"+ habitaciones +"',"
                    + " '"+ baños +"', '"+ alqOVen +"')");
            System.out.println("filtros guardados con exito");
        }catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }//fin catch 
     }
     
     private void borrarFiltrosAnteriores(Conectar con) {
        Statement s2;
        try{
            s2 = con.getConnection().createStatement();
            s2.executeUpdate("DELETE from filtros WHERE id = 1");
            System.out.println("Los filtros anteriores se borraron correctamente");
        }catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }//fin catch 
     }
     
     private boolean comprobarFiltros(Conectar con) {
        ResultSet rs;
        Statement st;
        try{st = con.getConnection().createStatement();
            rs = st.executeQuery("SELECT id FROM filtros WHERE id = 1");
            if(rs.first()) {return true;}
            else {return false;}
        }catch (SQLException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }//fin catch 
        return false;
     }
     
}
