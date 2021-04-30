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
public class Usuario {
    String id, dni, password, nombre, apellidos, email, foto;
    public Usuario (String id, String dni, String password, String nombre, String apellidos, String email, String foto){
        this.id = id;
        this.dni = dni;
        this.password = password;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.foto = foto;
    }
    
   public String getId (){
       return id;
   }
   
   public String getDni () {
       return dni;
   }
   
   public String getPassword (){
       return password;
   }
   
   public String getNombre (){
       return nombre;
   }
   
   public String getApellidos () {
       return apellidos;
   }
   
   public String getEmail () {
       return email;
   }
   
   public String getFoto() {
       return foto;
   }
}
