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
public class Agencia {
    String codigo, nombre, contraseña, web;

    public Agencia(String codigo, String nombre, String contraseña, String web) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.contraseña = contraseña;
        this.web = web;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getContraseña() {
        return contraseña;
    }

    public String getWeb() {
        return web;
    }
    
    
}
