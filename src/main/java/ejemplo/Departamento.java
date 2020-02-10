package ejemplo;

import java.util.HashSet;
import java.util.Set;

public class Departamento {

	private int idDepartamento;
	private String nombre;

	private Set<Empleado> empleado = new HashSet<Empleado>(0);

	public Departamento() {
		super();
	}

	public Departamento(int idDepartamento, String nombre) {
		super();
		this.idDepartamento = idDepartamento;
		this.nombre = nombre;
	}

	public int getIdDepartamento() {
		return idDepartamento;
	}

	public void setIdDepartamento(int idDepartamento) {
		this.idDepartamento = idDepartamento;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Set<Empleado> getEmpleado() {
		return empleado;
	}

	public void setEmpleado(Set<Empleado> empleado) {
		this.empleado = empleado;
	}

}
