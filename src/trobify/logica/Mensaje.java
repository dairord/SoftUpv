/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify.logica;

/**
 *
 * @author jagon
 */
public class Mensaje {
    private int id;
    private String cuerpo;
    
    public Mensaje(int id, String cuerpo){
        this.id = id;
        this.cuerpo = cuerpo;
    }
    
    public Mensaje(String cuerpo){
        this.cuerpo = cuerpo;
    }
    
    public void setId(int id){
        this.id = id;
    }
    
    public int getId(){
        return this.id;
    }
    
    public void setCuerpo(String cuerpo){
        this.cuerpo = cuerpo;
    }
    
    public String getCuerpo(){
        return this.cuerpo;
    }
}
