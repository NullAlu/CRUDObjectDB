package com.mycompany.gestionapp;

import Datos.HibernateUtil;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modelos.Actividades;
import modelos.Alumno;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
/**
 * FXML Controller class
 *
 * @author Arz
 */
public class UsuarioController implements Initializable {

    @FXML
    private VBox usuarioVista;
    @FXML
    private Label labelAlumnoId;
    @FXML
    private ImageView imgLogOut;
    @FXML
    private TextField txtActividad;
    @FXML
    private TextField txtTiempo;
    @FXML
    private TextArea txtObservaciones;
    @FXML
    private Button btnEnviar;
    private Label txtAlumnoId;
    @FXML
    private ComboBox<Alumno> comboAlumnoActividad;
    @FXML
    private DatePicker datePickerActividad;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getAlumnoId();
               
    }

    
    @FXML
    private void logOutUsuario(MouseEvent event) {
        try {
            Stage stage = (Stage) txtActividad.getScene().getWindow();
            App.setRoot("loginVista");
            stage.close(); //Cierro la ventana
                stage.setWidth(1680); 
                stage.setHeight(750);     //Le pongo un tamaño correcto para mostrar
                stage.show();           //Muestro la ventana después de ajustarla
        } catch (IOException ex) {
            Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    public void getAlumnoId(){
      String idString = LoginController.whoIs;
      Session s = HibernateUtil.getSessionFactory().openSession();
      Transaction t = s.beginTransaction();
      Query q = s.createQuery("FROM Alumno a WHERE a.nombre =:n ");
      q.setParameter("n", idString);
      ArrayList<Alumno> a = (ArrayList<Alumno>) q.list();
      comboAlumnoActividad.setValue(a.get(0));
      
    }
    
    public void insertarActividad(){
        //Método que lista los alumnos de la tabla "Alumnos"
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        Alumno alumno = comboAlumnoActividad.getValue();
        String nombreActividad = txtActividad.getText();
        String tiempoString = txtTiempo.getText();
        int tiempo = Integer.parseInt(tiempoString);
        //Hay que hacer conversiones para pasar de LocalDate a date y luego pasarlo a SQLDATE
        LocalDate fechaActividad = datePickerActividad.getValue();
        Date date = Date.from(fechaActividad.atStartOfDay(ZoneId.systemDefault()).toInstant());
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        String observaciones = txtObservaciones.getText();
        
        Actividades actividad = new Actividades();
        actividad.setAlumno(alumno);
        actividad.setFecha(date);
        actividad.setHorasDia(tiempo);
        actividad.setNombre(nombreActividad);
        actividad.setObservaciones(observaciones);
        
        s.save(actividad);
        t.commit();
        
        
        
    }

    @FXML
    private void introducirActividad(MouseEvent event) {
        insertarActividad();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setTitle("Informacion");
            alert.setContentText("Has enviado el control de actividades al profesorado");
            ButtonType siButton = new ButtonType("Perfecto", ButtonBar.ButtonData.YES);
            alert.getButtonTypes().setAll(siButton);
            Optional<ButtonType> resultado = alert.showAndWait();
    }
}
