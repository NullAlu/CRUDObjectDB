package modelos;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "alumnos")
public class Alumno implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "dni")
    private String dni;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "apellidos")
    private String apellidos;
    @Column(name = "telefono")
    private int telefono;
    @Column(name = "email")
    private String email;
    @Column(name = "fecha_nacimiento")
    private Date fechaNacimiento;
    @Column(name = "clave")
    private String clave;
    @Column(name = "observaciones")
    private String observaciones;
    @Column(name = "horas_dual")
    private int horasDual;
    @Column(name = "horas_fct")
    private int horasFCT;
    
    
    //Relacion N:1 (Muchos alumnos tiene asignada una unica empresa)
    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;
    
    //Relacion N:1 (Muchos alumnos tienen asignada un profesor)
    @ManyToOne
    @JoinColumn(name = "profesor_id")
    private Profesor profesor;

    
    public Alumno() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public int getHorasDual() {
        return horasDual;
    }

    public void setHorasDual(int horasDual) {
        this.horasDual = horasDual;
    }

    public int getHorasFCT() {
        return horasFCT;
    }

    public void setHorasFCT(int horasFCT) {
        this.horasFCT = horasFCT;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    @Override
    public String toString() {
        return  nombre;
    }

    

   

   

}
