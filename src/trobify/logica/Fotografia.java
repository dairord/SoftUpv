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
public class Fotografia {
    String id, id_vivienda;

    public Fotografia(String id, String id_vivienda) {
        this.id = id;
        this.id_vivienda = id_vivienda;
    }

    public String getId() {
        return id;
    }

    public String getId_vivienda() {
        return id_vivienda;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setId_vivienda(String id_vivienda) {
        this.id_vivienda = id_vivienda;
    }
    
    
}
