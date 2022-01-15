package com.mycompany.gestionapp;

import Datos.HibernateUtil;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import modelos.Alumno;
import modelos.Profesor;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class LoginController implements Initializable {

    public static String whoIs = null; //Variable global para guardar el usuario
    @FXML
    private Label labelUsuario;
    @FXML
    private TextField txtUsuario;
    @FXML
    private Label labelContraseña;
    @FXML
    private PasswordField txtContraseña;
    @FXML
    private ImageView imgLogo;
    @FXML
    private ImageView imgUsuario;
    @FXML
    private ImageView imgContraseña;
    @FXML
    private Button btnEntrar;
    @FXML
    private ImageView imgSalir;

    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @FXML
    //Este es la imagen de la esquina superior derecha para salir de la APP
    private void btnSalir(MouseEvent event) {
        //A través de la imagen cojo y le asigno a la variable la ventana donde esta situada
        Stage ventana = (Stage) imgSalir.getScene().getWindow();
        //Una vez tenemos la ventana asignada a una variable la cerramos.
        ventana.close();

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void btnEntrar(MouseEvent event) {
        //Creamos una sesión
        Session s = HibernateUtil.getSessionFactory().openSession();
        //Creamos una transacción
        Transaction t = s.beginTransaction();
        //Creamos una query
        Query queryAlumno = s.createQuery("FROM Alumno a WHERE a.nombre =?1 and a.clave =?2");
        //Asignamos parametros a la query
        queryAlumno.setParameter(1, txtUsuario.getText().toLowerCase());
        queryAlumno.setParameter(2, txtContraseña.getText().toLowerCase());
        //Metemos el resultado de la querya en una lista
        ArrayList<Alumno> alumnos = (ArrayList<Alumno>) queryAlumno.list();
        //Comprobamos si los datos obtenidos se encuentran en la base de datos.
        if (!alumnos.isEmpty()) {
            try {
                //Guarda en la variable global whoIs el usuario ingresado.(Para diseños en las vistas)
                whoIs = txtUsuario.getText().toUpperCase();
                //Cambiamos a la vista de usuarios en el caso de que haga login un usuario
                Stage stage = (Stage) txtContraseña.getScene().getWindow();
                App.setRoot("usuarioVista"); //Cambio de vista
                stage.close(); //Cierro la ventana
                stage.setWidth(900); 
                stage.setHeight(400);     //Le pongo un tamaño correcto para mostrar
                stage.show();           //Muestro la ventana después de ajustarla
             
            } catch (IOException ex) {
                System.out.println("Error al intentar acceder a usuarioVista.fxml " + ex);
            }
        } else {
            Query queryProfesor = s.createQuery("FROM Profesor p WHERE p.nombre =?1 and p.clave =?2");
            queryProfesor.setParameter(1, txtUsuario.getText().toLowerCase());
            queryProfesor.setParameter(2, txtContraseña.getText().toLowerCase());
            ArrayList<Profesor> profesores = (ArrayList<Profesor>) queryProfesor.list();
            if (!profesores.isEmpty()) {
                try {
                    whoIs = txtUsuario.getText().toUpperCase();
                    //Cambiamos a la vista de Profesores en el caso de que haga login un profesor
                    App.setRoot("profesorAlumnoVista");
                } catch (IOException ex) {
                    System.out.println("Error al intentar acceder a profesorVista.fxml " + ex);
                }

            } else {
                //En el caso de que no se encuentre nada en las dos tablas, entonces saldra un ALERT
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText(null);
                alert.setTitle("Error al iniciar sesión");
                alert.setContentText("El usuario o la constraseña son erroneos");
                ButtonType valeButton = new ButtonType("Vale", ButtonBar.ButtonData.YES);
                alert.show();
            }
        }

        //Cerramos la transacción
        t.commit();
    }
}
