/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify;

import trobify.conectores.Conexion;

/**
 *
 * @author gabri
 */
public class PruebasUnitarias {

    public static void main(String[] args) {
        System.out.println(probarSingleton());

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
}
