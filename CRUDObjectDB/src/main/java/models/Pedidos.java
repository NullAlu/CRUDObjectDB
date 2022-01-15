/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.sql.Date;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author marco
 */
@Entity
public class Pedidos implements Serializable {

    @Id
    @GeneratedValue
    private Long id;


    private Date fecha;
    private double precio;
    private String pendiente;
    private String recogido;
    
    @ManyToOne
    @JoinColumn(name="producto_id")
    private Productos producto;

    
    public Pedidos(Long id, Date fecha, double precio, String pendiente, String recogido) {
        this.id = id;
        this.fecha = fecha;
        this.precio = precio;
        this.pendiente = pendiente;
        this.recogido = recogido;
    }

    public Pedidos() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Productos getProducto() {
        return producto;
    }

    public void setProducto(Productos producto) {
        this.producto = producto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String isPendiente() {
        return pendiente;
    }

    public void setPendiente(String pendiente) {
        this.pendiente = pendiente;
    }

    public String isRecogido() {
        return recogido;
    }

    public void setRecogido(String recogido) {
        this.recogido = recogido;
    }

    @Override
    public String toString() {
        return "Pedido{" + "id=" + id + ", fecha=" + fecha + ", precio=" + precio + ", pendiente=" + pendiente + ", recogido=" + recogido + '}';
    }

}
