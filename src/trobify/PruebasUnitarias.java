/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify;

import java.util.ArrayList;
import trobify.conectores.Conexion;
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
        System.out.println(probarSingleton());
        System.out.println(probarIterador());
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
}
