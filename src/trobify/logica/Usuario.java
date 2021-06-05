/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify.logica;

import java.util.Objects;

/**
 *
 * @author gabri
 */
public class Usuario {

    String id, dni, password, nombre, apellidos, email, foto, preferencia;

    public Usuario(String id, String dni, String password, String nombre, String apellidos, String email, String foto) {
        this.id = id;
        this.dni = dni;
        this.password = password;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.foto = foto;
        this.preferencia = "";
    }
    
    public Usuario(String id, String dni, String password, String nombre, String apellidos, String email, String foto, String preferencia){
        this.id = id;
        this.dni = dni;
        this.password = password;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.foto = foto;
        this.preferencia = preferencia;
    }

    public Usuario() {
        this.id = "";
        this.dni = "";
        this.password = "";
        this.nombre = "";
        this.apellidos = "";
        this.email = "";
        this.foto = "";
        this.preferencia = null;
    }

    public String getId() {
        return id;
    }

    public String getDni() {
        return dni;
    }

    public String getPassword() {
        return password;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getEmail() {
        return email;
    }

    public String getFoto() {
        return foto;
    }

    public String getPreferencia() {
        return preferencia;
    }

    public void setPreferencia(String preferencia) {
        this.preferencia = preferencia;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Usuario other = (Usuario) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.dni, other.dni)) {
            return false;
        }
        if (!Objects.equals(this.password, other.password)) {
            return false;
        }
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.apellidos, other.apellidos)) {
            return false;
        }
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        
        if (!Objects.equals(this.preferencia, other.preferencia)) {
            return false;
        }
        return true;
    }

    
   
}
