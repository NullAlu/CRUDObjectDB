package com.mycompany.gestionapp;

import Datos.HibernateUtil;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modelos.Empresa;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class ProfesorEmpresaController implements Initializable {

    @FXML
    private Label labelNombreProfesor;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtResponsable;
    @FXML
    private TextArea txtObservaciones;
    @FXML
    private Button btnAgregar;
    @FXML
    private TableView<Empresa> tablaEmpresas;
    @FXML
    private TableColumn<Empresa, Long> colId;
    @FXML
    private TableColumn<Empresa, String> colNombre;
    @FXML
    private TableColumn<Empresa, String> colTelefono;
    @FXML
    private TableColumn<Empresa, String> colObservaciones;
    @FXML
    private TableColumn<Empresa, String> colEmail;
    @FXML
    private TableColumn<Empresa, String> colResponsable;
    @FXML
    private ImageView imgAlumno;
    @FXML
    private VBox imgActividades;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Se le añade al Label el texto recogido de la variable globlar WhoIs de iniciar sesión
        labelNombreProfesor.setText(LoginController.whoIs);
        listarEmpresas();
        detectarClickMouse();
    }

    //Listamos las empresas en la tabla
    public void listarEmpresas() {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        ObservableList<Empresa> empresasObservable = FXCollections.observableArrayList();
        tablaEmpresas.setItems(empresasObservable);
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colResponsable.setCellValueFactory(new PropertyValueFactory<>("responsable"));
        colObservaciones.setCellValueFactory(new PropertyValueFactory<>("observaciones"));
        Query q = s.createQuery("FROM Empresa");
        ArrayList<Empresa> empresas = (ArrayList<Empresa>) q.list();
        empresasObservable.addAll(empresas);
        t.commit();
    }

    public void insertarEmpresa() {
        //Creamos a la empresa que le añadiremos las variables reogidas de los textField
        Empresa empresa = new Empresa();

        //Método que lista las empresas de la tabla "Empresa"
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();

        //Creamos las variables que le asignaremos de la empresa mediante Setters
        String nombre = txtNombre.getText();
        String email = txtEmail.getText();
        String telefono = txtTelefono.getText();
        String responsable = txtResponsable.getText();
        String observaciones = txtObservaciones.getText();

        //Agregar argumentos del alumno con los Setters
        empresa.setNombre(nombre);
        empresa.setEmail(email);
        empresa.setTelefono(Integer.parseInt(telefono));
        empresa.setResponsable(responsable);
        empresa.setObservaciones(observaciones);
        //Insertamos a la empresa en la BD con el metodo Save de la sesión
        s.save(empresa);
        //Cerramos la transacción
        t.commit();

    }

    //Elimina a la empresa usando el metodo Session.delete
    public static void eliminarEmpresa(Empresa empresa) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        Empresa EmpresaEliminar = s.get(Empresa.class, empresa.getId());
        s.delete(EmpresaEliminar);
        t.commit();
    }

    //metodo que detecta que empresa esta seleccionado
    public void detectarSeleccionEliminar() {
        // check the table's selected item and get selected item
        if (tablaEmpresas.getSelectionModel().getSelectedItem() != null) {
            Empresa selectedEmpresa = tablaEmpresas.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setTitle("Eliminar Empresa");
            alert.setContentText("¿Deseas borrar a " + selectedEmpresa.getNombre() + "?"
            );
            ButtonType siButton = new ButtonType("Si", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(siButton, noButton);
            Optional<ButtonType> resultado = alert.showAndWait();
            if (resultado.get() == siButton) {
                eliminarEmpresa(selectedEmpresa);

            } else if (resultado.get() == noButton) {
                //No pasa nada
            }

        }
    }

    //Este metodo ha sido creado para escuchar el ratos y si hace 2click se ejecuta el metodo...
    //Detectar selección para ejecurar la eliminación de la empresa
    public void detectarClickMouse() {
        //Detecta si el raton ha hecho click mas de 1 vez y llama al metodo  detectarSeleccionEliminar()
        tablaEmpresas.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() > 1) {
                detectarSeleccionEliminar();
                listarEmpresas();
            }
        });
    }

    @FXML
    private void logOutProfesor(MouseEvent event) {
        try {
            //Cambia de vista al hacer clic en la imagen de LOG-OUT
            App.setRoot("loginVista");
        } catch (IOException ex) {
            System.out.println("Error al acceder al documento loginVista.fxml " + ex);;
        }
    }

    @FXML
    private void agregarEmpresa(MouseEvent event) {
        insertarEmpresa();
        listarEmpresas();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Informacion");
        alert.setContentText("Has agregado la empresa satisfactoriamente");
        ButtonType siButton = new ButtonType("Perfecto", ButtonBar.ButtonData.YES);
        alert.getButtonTypes().setAll(siButton);
        Optional<ButtonType> resultado = alert.showAndWait();
    }

    @FXML
    private void irAdministracionAlumnos(MouseEvent event) {
        try {
            //Cambia de vista al hacer clic en la imagen de LOG-OUT
            App.setRoot("profesorAlumnoVista");
        } catch (IOException ex) {
            System.out.println("Error al acceder al documento loginVista.fxml " + ex);;
        }
    }

    @FXML
    private void irActividades(MouseEvent event) {
        try {
            //Cambia de vista al hacer clic en la imagen de LOG-OUT
            Stage stage = (Stage) txtEmail.getScene().getWindow();
            App.setRoot("profesorActividadesControlVista");
            stage.close(); //Cierro la ventana
            stage.setWidth(777);
            stage.setHeight(530);     //Le pongo un tamaño correcto para mostrar
            stage.show();           //Muestro la ventana después de ajustarla
        } catch (IOException ex) {
            System.out.println("Error al acceder al documento loginVista.fxml " + ex);;
        }
    }

}
