/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify.logica;

import java.sql.Date;
import java.time.LocalDate;

/**
 *
 * @author gabri
 */
public class Filtros {
    String id, ciudad;
    LocalDate fecha_entrada, fecha_salida;
    int tipo, p_min, p_max, habitaciones, baños, ventaAlquiler;

    public String getId() {
        return id;
    }

    public String getCiudad() {
        return ciudad;
    }

    public LocalDate getFecha_entrada() {
        return fecha_entrada;
    }

    public LocalDate getFecha_salida() {
        return fecha_salida;
    }

    public int getTipo() {
        return tipo;
    }

    public int getP_min() {
        return p_min;
    }

    public int getP_max() {
        return p_max;
    }

    public int getHabitaciones() {
        return habitaciones;
    }

    public int getBaños() {
        return baños;
    }

    public int getVentaAlquiler() {
        return ventaAlquiler;
    }
    
    

    public Filtros(String id, String ciudad, LocalDate fecha_entrada, LocalDate fecha_salida, int tipo, int p_min, int p_max, int habitaciones, int baños, int ventaAlquiler) {
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
