/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trobify.logica;

import java.util.ArrayList;

/**
 *
 * @author dairo
 */
public class Vivienda implements IContainer{

    public Vivienda() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setVentaAlquiler(int ventaAlquiler) {
        this.ventaAlquiler = ventaAlquiler;
    }

    public void setId_agencia(String id_agencia) {
        this.id_agencia = id_agencia;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public void setId_propietario(String id_propietario) {
        this.id_propietario = id_propietario;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public void setBaños(int baños) {
        this.baños = baños;
    }

    public void setHabitaciones(int habitaciones) {
        this.habitaciones = habitaciones;
    }

    public void setPiso(int piso) {
        this.piso = piso;
    }

    public void setPuerta (String puerta) {
        this.puerta = puerta;
    }

    public void setCodigo_postal(int codigo_postal) {
        this.codigo_postal = codigo_postal;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    public void setServicios(Servicios servicios) {
        this.servicios = servicios;
    }

    public void setFotos(ArrayList<Fotografia> fotos) {
        this.fotos = fotos;
    }
    
    

    public String getId() {
        return id;
    }

    public String getCalle() {
        return calle;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getVentaAlquiler() {
        return ventaAlquiler;
    }

    public String getId_agencia() {
        return id_agencia;
    }

    public int getPrecio() {
        return precio;
    }

    public String getId_propietario() {
        return id_propietario;
    }

    public int getTipo() {
        return tipo;
    }

    public int getBaños() {
        return baños;
    }

    public int getHabitaciones() {
        return habitaciones;
    }

    public int getPiso() {
        return piso;
    }

    public String getPuerta() {
        return puerta;
    }

    public int getCodigo_postal() {
        return codigo_postal;
    }

    public int getActivo() {
        return activo;
    }

    public Servicios getServiciosPorVivienda() {
        return servicios;
    }

    public ArrayList<Fotografia> getFotos() {
        return fotos;
    }
    
    

    private String id, calle, ciudad, descripcion, id_agencia, id_propietario, puerta;
    private int ventaAlquiler, precio, tipo, baños,
            habitaciones, piso, codigo_postal, activo;
    private Servicios servicios;
    private ArrayList<Fotografia> fotos;

    public Vivienda(String id, String calle, String ciudad, String descripcion, String id_agencia, String id_propietario, String puerta, int ventaAlquiler, int precio, int tipo, int baños, int habitaciones, int piso, int codigo_postal, int activo, Servicios servicios, ArrayList<Fotografia> fotos) {
        this.id = id;
        this.calle = calle;
        this.ciudad = ciudad;
        this.descripcion = descripcion;
        this.id_agencia = id_agencia;
        this.id_propietario = id_propietario;
        this.puerta = puerta;
        this.ventaAlquiler = ventaAlquiler;
        this.precio = precio;
        this.tipo = tipo;
        this.baños = baños;
        this.habitaciones = habitaciones;
        this.piso = piso;
        this.codigo_postal = codigo_postal;
        this.activo = activo;
        this.servicios = servicios;
        this.fotos = fotos;
    }
    
    

    public Vivienda(String id, String calle, String ciudad, int ventaAlquiler, String id_agencia, int precio, String id_propietario, int tipo, int baños, int habitaciones, String descripcion, int piso, String puerta, int codigo_postal, int activo) {
        this.id = id;
        this.calle = calle;
        this.ciudad = ciudad;
        this.descripcion = descripcion;
        this.ventaAlquiler = ventaAlquiler;
        this.id_agencia = id_agencia;
        this.precio = precio;
        this.id_propietario = id_propietario;
        this.tipo = tipo;
        this.baños = baños;
        this.habitaciones = habitaciones;
        this.piso = piso;
        this.puerta = puerta;
        this.codigo_postal = codigo_postal;
        this.activo = activo;
    }

    @Override
    public IIterator createIterator() {
        IteratorFotos result = new IteratorFotos();
        return result;
    }

    public class IteratorFotos implements IIterator {
        
        private int position = 0;
        
        @Override
        public boolean hasNext() {
            if (position < fotos.size()) {return true;}
           else {return false;}
        }

        @Override
        public void next() {
            if(this.hasNext()) {
                position++;
            }
        }

        @Override
        public Object currentObject() {
            return fotos.get(position);
        }

        @Override
        public void first() {
            position = 0;
        }
    
    }
}
