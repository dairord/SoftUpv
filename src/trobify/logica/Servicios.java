/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify.logica;

import java.util.logging.Logger;

/**
 *
 * @author gabri
 */
public class Servicios {
    String id;
    int supermercado, transporte_publico, banco, estanco, centro_comercial, gimnasio,farmacia;

    public Servicios(String id, int supermercado, int transporte_publico, int banco, int estanco, int centro_comercial, int gimnasio, int farmacia) {
        this.id = id;
        this.supermercado = supermercado;
        this.transporte_publico = transporte_publico;
        this.banco = banco;
        this.estanco = estanco;
        this.centro_comercial = centro_comercial;
        this.gimnasio = gimnasio;
        this.farmacia = farmacia;
    }

    public String getId() {
        return id;
    }

    public int getSupermercado() {
        return supermercado;
    }

    public int getTransporte_publico() {
        return transporte_publico;
    }

    public int getBanco() {
        return banco;
    }

    public int getEstanco() {
        return estanco;
    }

    public int getCentro_comercial() {
        return centro_comercial;
    }

    public int getGimnasio() {
        return gimnasio;
    }

    public int getFarmacia() {
        return farmacia;
    }

    

  
   
}
