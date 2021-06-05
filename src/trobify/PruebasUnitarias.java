/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify;

import java.util.ArrayList;
import trobify.conectores.Conexion;
import trobify.fachada.FachadaBD;
import static trobify.fachada.FachadaBD.getVivienda;
import trobify.logica.Fotografia;
import trobify.logica.IIterator;
import trobify.logica.Vivienda;

/**
 *
 * @author gabri
 */
public class PruebasUnitarias {

    public static void main(String[] args) {
        System.out.println("Prueba del Singleton: " +probarSingleton());
        System.out.println("Prueba del Iterador: " +probarIterador());
        System.out.println("Prueba de la Consulta a Vivienda: " +probarVivienda());
    }

    public static boolean probarSingleton() {
        Conexion con1, con2;

        con1 = Conexion.crearConexion();

        con2 = Conexion.crearConexion();

        if (con1.equals(con2)) {
            return true;
        }

        return false;
    }
    
    public static boolean probarIterador() {
        boolean result = true;
        int i = 0;
        Vivienda prueba = getVivienda("vivienda1");
        ArrayList<Fotografia> fotos = prueba.getFotos();
        IIterator it = prueba.createIterator();
        while(i < fotos.size() && it.hasNext()){
            if(!fotos.get(i).equals((Fotografia)it.currentObject())) {
                return false;
            }
            i++;
            it.next();
        }
        return result;
    }
    
    public static boolean probarVivienda(){
        Vivienda comp = new Vivienda("vivienda1", "Calle Arzobispo Mayoral", "Valencia", 1, "5", 100000, "admin", 1, 2, 2, null, 2, "2", 46002, 0);
        Vivienda res = FachadaBD.getVivienda("vivienda1");
        if (comp.equals(res)){
            return true;
        }
        
        return false;
    }
}
