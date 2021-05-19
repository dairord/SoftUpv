/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify.logica;

/**
 *
 * @author gabri
 */
public class Historial {
    int id;
    String id_vivienda, username;

    public Historial(int id, String id_vivienda, String username) {
        this.id = id;
        this.id_vivienda = id_vivienda;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public String getId_vivienda() {
        return id_vivienda;
    }

    public String getUsername() {
        return username;
    }
    
    
}
