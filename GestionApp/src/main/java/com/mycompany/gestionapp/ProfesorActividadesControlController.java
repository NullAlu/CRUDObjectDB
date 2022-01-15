package com.mycompany.gestionapp;

import Datos.HibernateUtil;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import modelos.Actividades;
import modelos.Alumno;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/**
 * FXML Controller class
 *
 * @author Arz
 */
public class ProfesorActividadesControlController implements Initializable {

    @FXML
    private TableColumn<Actividades, Long> colId;
    @FXML
    private TableColumn<Actividades, Alumno> colAlumnoId;
    @FXML
    private TableColumn<Actividades, String> colNombreActividad;
    @FXML
    private TableColumn<Actividades, Integer> colHoras;
    @FXML
    private TableColumn<Actividades, Date> colFecha;
    @FXML
    private TableColumn<Actividades, String> colObservaciones;
    @FXML
    private TableView<Actividades> tablaActividades;
    @FXML
    private ImageView imgEmpresa;
    @FXML
    private ImageView imgAlumno;
    @FXML
    private ImageView imgLogOut;
    @FXML
    private Button btnCSV;
    @FXML
    private TextField txtArchivoNombre;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listarActividades();
        detectarClickMouse();
    }
    

    public void listarActividades() {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        ObservableList<Actividades> actividadesObservable = FXCollections.observableArrayList();
        tablaActividades.setItems(actividadesObservable);
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colAlumnoId.setCellValueFactory(new PropertyValueFactory<>("alumno"));
        colNombreActividad.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colHoras.setCellValueFactory(new PropertyValueFactory<>("horasDia"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colObservaciones.setCellValueFactory(new PropertyValueFactory<>("observaciones"));

        Query q = s.createQuery("FROM Actividades");
        ArrayList<Actividades> actividades = (ArrayList<Actividades>) q.list();
        actividadesObservable.addAll(actividades);
        t.commit();

    }

    @FXML
    private void irAdministracionEmpresas(MouseEvent event) {
        try {
            //Cambia de vista al hacer clic en la imagen de LOG-OUT
            Stage stage = (Stage) tablaActividades.getScene().getWindow();
            App.setRoot("profesorEmpresaVista");
            stage.close(); //Cierro la ventana
            stage.setWidth(1680);
            stage.setHeight(750);     //Le pongo un tamaño correcto para mostrar
            stage.show();           //Muestro la ventana después de ajustarla
        } catch (IOException ex) {
            System.out.println("Error al acceder al documento loginVista.fxml " + ex);;
        }
    }

    @FXML
    private void irAdministracionAlumnos(MouseEvent event) {
        try {
            //Cambia de vista al hacer clic en la imagen de LOG-OUT
            Stage stage = (Stage) tablaActividades.getScene().getWindow();
            App.setRoot("profesorAlumnoVista");
            stage.close(); //Cierro la ventana
            stage.setWidth(1680);
            stage.setHeight(750);     //Le pongo un tamaño correcto para mostrar
            stage.show();           //Muestro la ventana después de ajustarla
        } catch (IOException ex) {
            System.out.println("Error al acceder al documento loginVista.fxml " + ex);;
        }
    }

    @FXML
    private void logOutProfesor(MouseEvent event) {
        try {
            //Cambia de vista al hacer clic en la imagen de LOG-OUT
            Stage stage = (Stage) tablaActividades.getScene().getWindow();
            App.setRoot("loginVista");
            stage.close(); //Cierro la ventana
            stage.setWidth(1680);
            stage.setHeight(750);     //Le pongo un tamaño correcto para mostrar
            stage.show();           //Muestro la ventana después de ajustarla
        } catch (IOException ex) {
            System.out.println("Error al acceder al documento loginVista.fxml " + ex);;
        }
    }
    
      //Elimina a la empresa usando el metodo Session.delete
    public static void eliminarActividad(Actividades actividad) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        Actividades actividadEliminar = s.get(Actividades.class, actividad.getId());
        s.delete(actividadEliminar);
        t.commit();
    }

    //metodo que detecta que empresa esta seleccionado
    public void detectarSeleccionEliminar() {
        // check the table's selected item and get selected item
        if (tablaActividades.getSelectionModel().getSelectedItem() != null) {
            Actividades selectedActividad = tablaActividades.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setTitle("Eliminar control");
            alert.setContentText("¿Deseas borrar el control?");
            ButtonType siButton = new ButtonType("Si", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(siButton, noButton);
            Optional<ButtonType> resultado = alert.showAndWait();
            if (resultado.get() == siButton) {
                eliminarActividad(selectedActividad);

            } else if (resultado.get() == noButton) {
                //No pasa nada
            }

        }
    }
    
    //Este metodo ha sido creado para escuchar el ratos y si hace 2click se ejecuta el metodo...
    //Detectar selección para ejecurar la eliminación de la empresa
    public void detectarClickMouse() {
        //Detecta si el raton ha hecho click mas de 1 vez y llama al metodo  detectarSeleccionEliminar()
        tablaActividades.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() > 1) {
                detectarSeleccionEliminar();
                listarActividades();
            }
        });
    }

    public static void guardarArhivoCSV(ArrayList<Actividades> actividades,String archivo) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(archivo+".csv"));
            CSVPrinter printer = new CSVPrinter(bw, CSVFormat.EXCEL.withHeader("ID", "ALUMNO","NOMBRE","HORAS","FECHA","OBSERVACIONES"));
            for (Actividades a : actividades) {
                 printer.printRecord(a.getId(), a.getAlumno().getNombre(),a.getNombre(),a.getHorasDia(),a.getFecha(),a.getObservaciones());
            }
            printer.flush();
            printer.close();
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    @FXML
    private void guardarCSV(MouseEvent event) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query q = s.createQuery("FROM Actividades");
        ArrayList<Actividades> actividades = (ArrayList<Actividades>) q.list();
        String archivo = txtArchivoNombre.getText();
        guardarArhivoCSV(actividades, archivo);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setTitle("Archivo guardado");
            alert.setContentText("Archivo CSV generado, podras verlo en tu proyecto despues de cerrar la aplicacion");
            ButtonType siButton = new ButtonType("Entendido", ButtonBar.ButtonData.YES);
            alert.getButtonTypes().setAll(siButton);
            Optional<ButtonType> resultado = alert.showAndWait();
             
    }
    
    

}
