/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify;

import trobify.conectores.Conectar;

/**
 *
 * @author gabri
 */
public class PruebasUnitarias {

    public static void main(String[] args) {
        System.out.println(probarSingleton());

    }

    public static boolean probarSingleton() {
        Conectar con1, con2;

        con1 = Conectar.crearConexion();

        con2 = Conectar.crearConexion();

        if (con1.equals(con2)) {
            return true;
        }

        return false;
    }
}
