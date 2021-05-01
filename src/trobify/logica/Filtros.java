/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify.logica;

import java.sql.Date;

/**
 *
 * @author gabri
 */
public class Filtros {
    String id, ciudad;
    Date fecha_entrada, fecha_salida;
    int tipo, p_min, p_max, habitaciones, baños, ventaAlquiler;

    public Filtros(String id, String ciudad, Date fecha_entrada, Date fecha_salida, int tipo, int p_min, int p_max, int habitaciones, int baños, int ventaAlquiler) {
        this.id = id;
        this.ciudad = ciudad;
        this.fecha_entrada = fecha_entrada;
        this.fecha_salida = fecha_salida;
        this.tipo = tipo;
        this.p_min = p_min;
        this.p_max = p_max;
        this.habitaciones = habitaciones;
        this.baños = baños;
        this.ventaAlquiler = ventaAlquiler;
    }
    
    
}
