/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import trobify.fachada.FachadaBD;
import trobify.logica.Notificacion;
import trobify.logica.Vivienda;

/**
 * FXML Controller class
 *
 * @author gabri
 */
public class NotificacionesController implements Initializable {

    @FXML
    private ListView<String> lista;
    private static Stage st;
    private static String username;

    ArrayList<String> favList;

    private static ObservableList listaNotis;
    // private static ArrayList<Notificacion> notificaciones;
    @FXML
    private Button rechazarButton;
    @FXML
    private Button aceptarButton;
    @FXML
    private Button borrarButton;

    private ArrayList<Notificacion> notificaciones;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        init();
// favList = new ArrayList();
    }

    public void init() {
        listaNotis = FXCollections.observableList(new ArrayList<String>());
        notificaciones = FachadaBD.getNotificacionPorUsuario(username);
        gestorNotis(notificaciones);
        lista.setItems(listaNotis);
    }

    private void gestorNotis(ArrayList<Notificacion> notifi) {
        for (int i = 0; i < notifi.size(); i++) {
            Vivienda viv = FachadaBD.getVivienda(notifi.get(i).getId_vivienda());
            Notificacion noti = notifi.get(i);
            String res;
            switch (noti.getTipo()) {

                case 0:
                    if (noti.getEstado() == 0) {
                        res = "La vivienda de la " + viv.getCalle() + " ya no se encuentra disponible.";
                        listaNotis.add(res);
                    } else {
                        res = "La vivienda de la " + viv.getCalle() + " vuelve a encontrarse disponible.";
                        listaNotis.add(res);
                    }
                    break;

                case 1:

                    switch (noti.getEstado()) {

                        case 0:
                            int resto = Integer.parseInt(noti.getDesc()) - viv.getPrecio();
                            res = "Has recibido una contraoferta de " + noti.getDesc() + "€ (" + resto + ") para tu vivienda de la " + viv.getCalle();
                            listaNotis.add(res);
                            break;

                        case 1:
                            res = "La contraoferta de " + noti.getDesc() + "€ en la vivienda de la " + viv.getCalle() + " ha sido aprobada por el propietario, ponte en contacto para proceder con el pago";
                            listaNotis.add(res);
                            break;

                        case 2:
                            res = "La contraoferta de " + noti.getDesc() + "€ en la vivienda de la " + viv.getCalle() + " ha sido rechazada por el propietario";
                            listaNotis.add(res);
                            break;
                    }
                    break;

                case 2:

                    res = "Nueva vivienda disponible en " + viv.getCiudad();
                    listaNotis.add(res);
                    break;
            }
        }
    }

    @FXML
    private void atras(ActionEvent event) throws IOException {
        st.close();
    }

    public static void pasarStage(Stage m) {
        st = m;
    }

    public static void pasarNotis(ArrayList<String> noti) {
        listaNotis = FXCollections.observableList(noti);

    }

    public boolean consulta() {
        return FachadaBD.getNotificacionesCliente(username);
    }

    public static void pasarUsuario(String u) {
        username = u;
    }

    private void lista() {
        for (int i = 0; i < favList.size(); ++i) {
            consulta();
        }
    }

    @FXML
    private void rechazarOferta(ActionEvent event) {
        int index = lista.getSelectionModel().getSelectedIndex();
        Notificacion noti = notificaciones.get(index);
        Notificacion resp = new Notificacion(noti.getId_vivienda(), username, noti.getId_usuario(), noti.getDesc(), new Date(System.currentTimeMillis()), 2, 1);
        FachadaBD.añadirNotificacionNoID(resp);

        borrarNoti(null);

    }

    @FXML
    private void aceptarOferta(ActionEvent event) {
        int index = lista.getSelectionModel().getSelectedIndex();
        Notificacion noti = notificaciones.get(index);
        Notificacion resp = new Notificacion(noti.getId_vivienda(), username, noti.getId_usuario(), noti.getDesc(), new Date(System.currentTimeMillis()), 1, 1);
        FachadaBD.añadirNotificacionNoID(resp);

        borrarNoti(null);

    }

    @FXML
    private void borrarNoti(ActionEvent event) {
        int index = lista.getSelectionModel().getSelectedIndex();
        Notificacion noti = notificaciones.get(index);
        FachadaBD.borrarNotificacion(noti);

        notificaciones = FachadaBD.getNotificacionPorUsuario(username);
        gestorNotis(notificaciones);
        listaNotis.clear();
        lista.setItems(listaNotis);
        init();
    }

    @FXML
    private void selectorNoti(MouseEvent event) {
        String selected = lista.getSelectionModel().getSelectedItem();
        try {
            switch (selected.charAt(0)) {

                case 'L':
                    aceptarButton.setDisable(true);
                    rechazarButton.setDisable(true);
                    borrarButton.setDisable(false);
                    break;

                case 'H':
                    aceptarButton.setDisable(false);
                    rechazarButton.setDisable(false);
                    borrarButton.setDisable(true);
                    break;

                case 'N':
                    aceptarButton.setDisable(true);
                    rechazarButton.setDisable(true);
                    borrarButton.setDisable(false);
                    break;

                default:
                    aceptarButton.setDisable(true);
                    rechazarButton.setDisable(true);
                    borrarButton.setDisable(true);
                    break;
            }
        } catch (Exception e) {
            aceptarButton.setDisable(true);
            rechazarButton.setDisable(true);
            borrarButton.setDisable(true);
        }
    }

}
