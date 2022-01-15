module com.mycompany.gestionapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;
    requires org.hibernate.orm.core;
    requires java.persistence;
    requires java.naming;
    requires commons.csv;

    opens com.mycompany.gestionapp to javafx.fxml,org.hibernate.orm.core,java.sql,commons.csv;
    opens modelos;
    exports com.mycompany.gestionapp;
    
}
