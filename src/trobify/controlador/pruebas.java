/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify.controlador;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;


public class pruebas {
  /*  private void ordenarConsulta() {
        viviendasList.clear();
        
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
                     listaViviendas.getChildren().clear();
                ordenarLista();
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
                     listaViviendas.getChildren().clear();
                ordenarLista();
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
                     listaViviendas.getChildren().clear();
                ordenarLista();
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
                     listaViviendas.getChildren().clear();
                ordenarLista();
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
*/
}
