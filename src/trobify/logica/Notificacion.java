/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify.logica;

import java.util.Date;

/**
 *
 * @author dairo
 */
public class Notificacion {
    private int id;

    
    private String id_vivienda;
    private String id_usuario;
    private String desc;
    private Date last_mod;
    private int estado;
    private int tipo;

    public Notificacion(int id, String id_vivienda, String id_usuario, String desc, Date last_mod, int estado, int tipo) {
        this.id = id;
        this.id_vivienda = id_vivienda;
        this.id_usuario = id_usuario;
        this.desc = desc;
        this.last_mod = last_mod;
        this.estado = estado;
        this.tipo = tipo;
        
        
    }

    public Notificacion(String id_vivienda, String id_usuario, String desc, Date last_mod, int estado, int tipo) {
        this.id_vivienda = id_vivienda;
        this.id_usuario = id_usuario;
        this.desc = desc;
        this.last_mod = last_mod;
        this.estado = estado;
        this.tipo = tipo;
    }
    
    
    
    public Notificacion() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getId_vivienda() {
        return id_vivienda;
    }

    public void setId_vivienda(String id_vivienda) {
        this.id_vivienda = id_vivienda;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getLast_mod() {
        return last_mod;
    }

    public void setLast_mod(Date last_mod) {
        this.last_mod = last_mod;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
    
    
    
}
