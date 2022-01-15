/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.crudobjectdb;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import models.Pedidos;
import models.Productos;

/**
 *
 * @author marco
 */
public class Main {

    private static EntityManagerFactory emf;
    private static EntityManager em;

    public class sqlConstantes {

        public static final String LISTARCARTA = "SELECT pr FROM Productos pr";
        public static final String PRODUCTOID = "SELECT pr FROM Productos pr where pr.id=:id";
        public static final String PEDIDOID = "SELECT pr FROM Pedidos pr where pr.id=:id";
        public static final String LISTARPEDIDOS = "SELECT pr FROM Pedidos pr";
        public static final String LISTARPEDIDOSHOY = "SELECT pr FROM Pedidos pr where pr.pendiente='Obvio' and pr.fecha=:fecha";
       

    }

    public static void main(String[] args) {
        emf = Persistence.createEntityManagerFactory("db.odb");

        int seleccionMenu = 0;
        boolean menuActivo = true;
        Scanner input = new Scanner(System.in);

        while (menuActivo) {
            System.out.println("1- Listar carta disponible");
            System.out.println("2- Listar todos los pedidos");
            System.out.println("3- Listar los pedidos de hoy");
            System.out.println("4- Crear un pedido");
            System.out.println("5- Borrar un pedido");
            System.out.println("6- Marcar pedido como recogido");
            System.out.println("7- Salir");

            seleccionMenu = input.nextInt();
            switch (seleccionMenu) {
                case 1:
                    listarCarta();
                    break;
                case 2:
                    listarPedidos();
                    break;
                case 3:
                    listarPedidosPendientes();
                    break;
                case 4:
                     Scanner id_input = new Scanner(System.in);
                     listarCarta();
                     System.out.println("Inserte el ID del pedido que desea: ");
                     Long id = id_input.nextLong();
                    crearPedido(id);
                    break;
                case 5:
                    Scanner id_input3 = new Scanner(System.in);
                     listarPedidos();
                     System.out.println("Inserte el ID del pedido que desea eliminar: ");
                     Long id3 = id_input3.nextLong();
                    borrarPedido(id3);
                    break;
                case 6:
                     Scanner id_input2 = new Scanner(System.in);
                     listarPedidos();
                     System.out.println("Inserte el ID del pedido para recoger: ");
                     Long id2 = id_input2.nextLong();
                    marcarRecogido(id2);
                    break;
                case 7:
                    menuActivo = false;
                    break;
                default:
                    System.out.println("Elija una opción válida");
            }
        }
        
    }

    public void saveCarta(Pedidos c) {
        
        em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(c);
        em.getTransaction().commit();
        em.close();
    }

    public static void listarCarta() {
        em = emf.createEntityManager();
        TypedQuery<Productos> q = em.createQuery(sqlConstantes.LISTARCARTA, Productos.class);
        var todo = (ArrayList<Productos>) q.getResultList();
        em.close();
        System.out.println("La Carta: ");
        todo.forEach(c -> System.out.println(c));

    }
    
    public static void crearPedido(Long id){
        em = emf.createEntityManager();        
        var pedido = new Pedidos();
        var producto = getProducto(id,em);
       
        pedido.setFecha(fechaActual());
        pedido.setProducto(producto);
        pedido.setPrecio(producto.getPrecio());
        pedido.setPendiente("Obvio");
        pedido.setRecogido("No");
      
        
        System.out.println("Su pedido es: " + producto);
        
        em.getTransaction().begin();
        em.persist(pedido);
        em.getTransaction().commit();
        em.close();
    }
    
    public static void marcarRecogido(Long id2){
        em = emf.createEntityManager();  

        var p = getPedido(id2, em);
        
        em.getTransaction().begin();
        
        p.setPendiente("No");
        p.setRecogido("Ea");
        
       em.getTransaction().commit();
        System.out.println("Pedido recogido");
       em.close();
    }
    
     public static void borrarPedido(Long id3){
        em = emf.createEntityManager();  
        var p = getPedido(id3, em);
        em.getTransaction().begin();
        em.remove(p);
        em.getTransaction().commit();
         System.out.println("Se ha eliminado el pedido");
         em.close();
    }
     
       public static void listarPedidos(){
        em = emf.createEntityManager();
        TypedQuery<Pedidos> q = em.createQuery(sqlConstantes.LISTARPEDIDOS, Pedidos.class);
        var todo = (ArrayList<Pedidos>) q.getResultList();
        em.close();
        System.out.println("Los pedidos pendientes: ");
        todo.forEach(c -> System.out.println(c));
           
           
    }
       
        public static void listarPedidosPendientes(){
        
            var fecha = fechaActual();
        em = emf.createEntityManager();
        TypedQuery<Pedidos> q = em.createQuery(sqlConstantes.LISTARPEDIDOSHOY, Pedidos.class);
        q.setParameter("fecha",fecha);
        var todo = (ArrayList<Pedidos>) q.getResultList();
        em.close();
        System.out.println("Los pedidos pendientes de hoy: ");
         todo.forEach(c -> System.out.println(c));
       
    }
    
    public static Productos getProducto(Long id, EntityManager em){
        TypedQuery<Productos> tq = em.createQuery(sqlConstantes.PRODUCTOID,Productos.class);
        tq.setParameter("id",id);
        var producto = tq.getSingleResult();
        return producto;
        
    }
    
     public static Pedidos getPedido(Long id, EntityManager em) {

        TypedQuery<Pedidos> tq = em.createQuery(sqlConstantes.PEDIDOID, Pedidos.class);
        tq.setParameter("id", id);
        var pedido = tq.getSingleResult();
        return pedido;

    }
    
     public static Date fechaActual() {

        java.util.Date utilDate = new java.util.Date();
        long lnMilisegundos = utilDate.getTime();
        java.sql.Date date = new java.sql.Date(lnMilisegundos);

        return date;
    }

}
