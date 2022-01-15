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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import modelos.Alumno;
import modelos.Empresa;
import modelos.Profesor;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class ProfesorAlumnoController implements Initializable {

    @FXML
    private Label labelNombreProfesor;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellidos;
    @FXML
    private TextField txtDNI;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtHorasDuales;
    @FXML
    private TextField txtHorasFCT;
    @FXML
    private TextArea txtObservaciones;
    @FXML
    private Button btnAgregar;
    @FXML
    private TableView<Alumno> tablaAlumnos;
    @FXML
    private TableColumn<Alumno, Long> colId;
    @FXML
    private TableColumn<Alumno, String> colDNI;
    @FXML
    private TableColumn<Alumno, String> colNombre;
    @FXML
    private TableColumn<Alumno, String> colApellidos;
    @FXML
    private TableColumn<Alumno, Date> colNacimiento;
    @FXML
    private TableColumn<Alumno, Integer> colTelefono;
    @FXML
    private TableColumn<Alumno, String> colEmail;
    @FXML
    private TableColumn<Alumno, String> colClave;
    @FXML
    private TableColumn<Alumno, Integer> colHorasDual;
    @FXML
    private TableColumn<Alumno, Integer> colHorasFCT;
    @FXML
    private TableColumn<Alumno, Integer> colEmpresa;
    @FXML
    private TableColumn<Alumno, Integer> colProfesor;
    @FXML
    private TableColumn<Alumno, String> colObservaciones;
    @FXML
    private TextField txtClave;
    @FXML
    private DatePicker datePickerNacimiento;
    @FXML
    private ComboBox<Empresa> comboEmpresa;
    @FXML
    private ComboBox<Profesor> comboProfesor;
    @FXML
    private ImageView imgEmpresa;
    @FXML
    private ImageView imgActividades;

    private void switchToPrimary() throws IOException {
        App.setRoot("LoginController");
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Se le añade al Label el texto recogido de la variable globlar WhoIs de iniciar sesión
        labelNombreProfesor.setText(LoginController.whoIs);
        listarAlumnos();
        inicializarComboBoxEmpresas();
        inicializarComboBoxProfesores();
        detectarClickMouse();

    }

    public void listarAlumnos() {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        ObservableList<Alumno> alumnosObservable = FXCollections.observableArrayList();
        tablaAlumnos.setItems(alumnosObservable);
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDNI.setCellValueFactory(new PropertyValueFactory<>("dni"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colNacimiento.setCellValueFactory(new PropertyValueFactory<>("fechaNacimiento"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colClave.setCellValueFactory(new PropertyValueFactory<>("clave"));
        colHorasDual.setCellValueFactory(new PropertyValueFactory<>("horasDual"));
        colHorasFCT.setCellValueFactory(new PropertyValueFactory<>("horasFCT"));
        colEmpresa.setCellValueFactory(new PropertyValueFactory<>("empresa"));
        colProfesor.setCellValueFactory(new PropertyValueFactory<>("profesor"));
        colObservaciones.setCellValueFactory(new PropertyValueFactory<>("observaciones"));
        Query q = s.createQuery("FROM Alumno");
        ArrayList<Alumno> alumnos = (ArrayList<Alumno>) q.list();
        alumnosObservable.addAll(alumnos);
        t.commit();
    }

    //Inicializa la lista de valores que tiene el ComboBOxProfesores
    public void inicializarComboBoxProfesores() {
        //Se crea una Arraylist de profesores y le pasamos como valores el metodo obtenerProfesores()
        ArrayList<Profesor> profesores = obtenerProfesores();
        Profesor p = new Profesor();
        for (Profesor elemento : profesores) {
            //Le añadimos por cada iteración un elemento al comboBox
            comboProfesor.getItems().add(elemento);
        }

    }

    //Inicializa la lista de valores que tiene el ComboBoxEmpresas
    public void inicializarComboBoxEmpresas() {
        //Se crea una Arraylist de Empresas y le pasamos como valores el metodo obtenerEmpresas()
        ArrayList<Empresa> empresas = obtenerEmpresas();
        Empresa e = new Empresa();
        for (Empresa elemento : empresas) {
            //Le añadimos por cada iteración un elemento al comboBox
            comboEmpresa.getItems().add(elemento);
        }

    }

    //Este método Extrae de la base de datos todos los profesores y retorna una lista de profesores
    //Se ha creado para poder asignarselo al Combobox
    public ArrayList<Profesor> obtenerProfesores() {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        Query q = s.createQuery("FROM Profesor");
        ArrayList<Profesor> profesores = (ArrayList<Profesor>) q.list();
        return profesores;
    }

    //Este método Extrae de la base de datos todos las Empresa y retorna una lista de Empresas
    //Se ha creado para poder asignarselo al Combobox
    public ArrayList<Empresa> obtenerEmpresas() {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        Query q = s.createQuery("FROM Empresa");
        ArrayList<Empresa> empresas = (ArrayList<Empresa>) q.list();
        return empresas;
    }

    public void insertarAlumno() {
        //Creamos al alumno que le añadiremos las variables reogidas de los textField
        Alumno alumno = new Alumno();

        //Método que lista los alumnos de la tabla "Alumnos"
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        String dni = txtDNI.getText();
        String nombre = txtNombre.getText();
        String apellidos = txtApellidos.getText();

        //Hay que hacer conversiones para pasar de LocalDate a date y luego pasarlo a SQLDATE
        LocalDate fechaNacimiento = datePickerNacimiento.getValue();
        Date date = Date.from(fechaNacimiento.atStartOfDay(ZoneId.systemDefault()).toInstant());
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        //Creamos las variables que le asignaremos al alumno mediante Setters
        String telefono = txtTelefono.getText();
        String email = txtEmail.getText();
        String clave = txtClave.getText();
        String horasDual = txtHorasDuales.getText();
        String horasFCT = txtHorasFCT.getText();
        String observaciones = txtObservaciones.getText();
        Empresa empresa = comboEmpresa.getValue();
        Profesor profesor = comboProfesor.getValue();

        //Agregar argumentos del alumno con los Setters
        alumno.setDni(dni);
        alumno.setNombre(nombre);
        alumno.setApellidos(apellidos);
        alumno.setFechaNacimiento(sqlDate);
        alumno.setTelefono(Integer.parseInt(telefono));
        alumno.setEmail(email);
        alumno.setClave(clave);
        alumno.setHorasDual(Integer.parseInt(horasDual));
        alumno.setHorasFCT(Integer.parseInt(horasFCT));
        alumno.setObservaciones(observaciones);
        alumno.setEmpresa(empresa);
        alumno.setProfesor(profesor);
        //Insertamos al alumno en la BD con el metodo Save de la sesión
        s.save(alumno);
        //Cerramos la transacción
        t.commit();

    }

    //Elimina al alumno usando el metodo Session.delete
    public static void eliminarAlumno(Alumno alumno) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction t = s.beginTransaction();
        Alumno AlumnoEliminar = s.get(Alumno.class, alumno.getId());
        s.delete(AlumnoEliminar);
        t.commit();
    }

    //metodo que detecta que alumno esta seleccionado
    public void detectarSeleccionEliminar() {
        if (tablaAlumnos.getSelectionModel().getSelectedItem() != null) {
            Alumno selectedPerson = tablaAlumnos.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setTitle("Eliminar Alumno");
            alert.setContentText("¿Deseas borrar a " + selectedPerson.getNombre() + "?"
            );
            ButtonType siButton = new ButtonType("Si", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(siButton, noButton);
            Optional<ButtonType> resultado = alert.showAndWait();
            if (resultado.get() == siButton) {
                eliminarAlumno(selectedPerson);

            } else if (resultado.get() == noButton) {
                //No pasa nada
            }

        }
    }

    //Este metodo ha sido creado para escuchar el ratos y si hace 2click se ejecuta el metodo...
    //Detectar selección para ejecurar la eliminación del alumno
    public void detectarClickMouse() {
        //Detecta si el raton ha hecho click mas de 1 vez y llama al metodo  detectarSeleccionEliminar()
        tablaAlumnos.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() > 1) {
                detectarSeleccionEliminar();
                listarAlumnos();

            }
        });
    }

    @FXML
    //boton que se activa al presionar el boton de agregar alumno
    private void agregarAlumno(MouseEvent event) {
        insertarAlumno();
        listarAlumnos();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText(null);
            alert.setTitle("Informacion");
            alert.setContentText("Has agregado el alumno satisfactoriamente");
            ButtonType siButton = new ButtonType("Perfecto", ButtonBar.ButtonData.YES);
            alert.getButtonTypes().setAll(siButton);
            Optional<ButtonType> resultado = alert.showAndWait();
        
    }

    @FXML
    private void irAdministracionEmpresas(MouseEvent event) {
        try {
            //Cambia de vista al hacer clic en la imagen de LOG-OUT
            App.setRoot("profesorEmpresaVista");
        } catch (IOException ex) {
            System.out.println("Error al acceder al documento profesorEmpresaVista.fxml " + ex);;
        }
    }

    @FXML
    private void irActividades(MouseEvent event) {
        try {
            //Cambia de vista al hacer clic en la imagen de LOG-OUT
            Stage stage = (Stage) txtApellidos.getScene().getWindow();
            App.setRoot("profesorActividadesControlVista");
            stage.close(); //Cierro la ventana
            stage.setWidth(777);
            stage.setHeight(530);     //Le pongo un tamaño correcto para mostrar
            stage.show();           //Muestro la ventana después de ajustarla
        } catch (IOException ex) {
            System.out.println("Error al acceder al documento profesorActividadesControlVista.fxml " + ex);;
        }
    }

}
