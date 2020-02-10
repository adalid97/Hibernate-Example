package ejemplo;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class Main {

	private static SessionFactory factory;
	private static ServiceRegistry serviceRegistry;

	public static void main(String[] args) {

		Configuration config = new Configuration();
		config.configure();
		config.addAnnotatedClass(Empleado.class);

		serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
		factory = config.buildSessionFactory(serviceRegistry);

		Main main = new Main();

		Scanner s = new Scanner(System.in);

		int opcion;
		do {
			System.out.println("\n\n\t========================");
			System.out.println("\t|||| MENÚ PRINCIPAL ||||");
			System.out.println("\t========================\n");

			System.out.println("EMPLEADOS.");
			System.out.println("\t1. Listar empleados");
			System.out.println("\t2. Insertar empleados.");
			System.out.println("\t3. Borrar empleados.");
			System.out.println("\t4. Actualizar empleados.");

			System.out.println("\nDEPARTAMENTOS.");
			System.out.println("\t5. Listar departamentos");
			System.out.println("\t6. Insertar departamentos.");
			System.out.println("\t7. Borrar departamentos.");
			System.out.println("\t8. Actualizar departamentos.");

			System.out.println("\n\t9.Mostrar empleados de un departamentos por idDepartamento: ");

			System.out.println("\n\t0.Salir del programa");

			System.out.print("\nEscribe el número de la opción (0-8): ");

			opcion = Integer.parseInt(s.nextLine());

			switch (opcion) {
			case 0:
				System.out.println("FIN DEL PROGRAMA.");
				break;
			case 1:
				List<Empleado> empleados = main.listEmpleado();
				System.out.println("Total empleados: " + empleados.size());
				for (Empleado e : empleados) {
					System.out.println("Id empleado: " + e.getIdEmpleado());
					System.out.println("\tNombre empleado: " + e.getNombre());
					System.out.println("\tApellidos empleado: " + e.getApellidos());
					System.out.println("\tID dto: " + e.getDepartamento().getIdDepartamento() + "\n");
				}
				break;
			case 2:
				Scanner s2 = new Scanner(System.in);
				System.out.print("Nombre Empleado: ");
				String nombre = s2.nextLine();
				System.out.print("Apellidos Empleado: ");
				String apellidos = s2.nextLine();
				System.out.print("Id Departamento: ");
				int idDto = Integer.parseInt(s2.next());

				insertEmpleado(nombre, apellidos, idDto);

				break;
			case 3:
				Scanner s3 = new Scanner(System.in);
				System.out.print("ID Empleado a borrar: ");
				int id = Integer.parseInt(s3.next());
				eliminarEmpleado(id);
				break;
			case 4:
				Scanner s4 = new Scanner(System.in);
				System.out.print("Id Empleado a cambiar: ");
				int idEmp = Integer.parseInt(s4.next());
				System.out.print("Nombre Empleado: ");
				String nombreE = s4.next();
				System.out.print("Apellidos Empleado: ");
				String apellidosE = s4.next();
				System.out.print("Id Departamento: ");
				int idDtoE = Integer.parseInt(s4.next());
				actualizarEmpleado(idEmp, nombreE, apellidosE, idDtoE);
				break;
			case 5:
				List<Departamento> departamentos = main.listDepartamento();
				System.out.println("Total Departamentos: " + departamentos.size());
				for (Departamento d : departamentos) {
					System.out.println("Id dto: " + d.getIdDepartamento());
					System.out.println("\tNombre dto: " + d.getNombre() + "\n");
				}
				break;
			case 6:
				Scanner s5 = new Scanner(System.in);
				System.out.print("Nombre Departamento: ");
				String nombreD = s5.nextLine();
				insertDepartamento(nombreD);
				break;
			case 7:
				Scanner s6 = new Scanner(System.in);
				System.out.print("ID Departamento a borrar: ");
				int idD = Integer.parseInt(s6.next());
				eliminarDepartamento(idD);
				break;
			case 8:
				Scanner s7 = new Scanner(System.in);
				System.out.print("Id Departamento a cambiar: ");
				int idDto2 = Integer.parseInt(s7.next());
				System.out.print("Nombre Departamento: ");
				String nombreDto = s7.next();
				actualizarDepartamento(idDto2, nombreDto);
				break;
			case 9:

				List<Empleado> em = main.listEmpleadosDeUnDepartamento();
				String resultado = "";
				for (Empleado d : em) {
					resultado += "Nombre empleado: " + d.getNombre() + "\n";
				}
				if (resultado != null) {
					JOptionPane.showMessageDialog(null, resultado);
				}

				break;
			default:
				System.out.println("OPCIÓN NO VÁLIDA");
				break;
			}

		} while (opcion != 0);

	}

	// INSERTAR

	private static boolean insertEmpleado(String nombre, String apellidos, int idDto) {
		boolean resultado = false;
		Session session = factory.openSession();
		Transaction tx = null;

		Departamento d = new Departamento(idDto, null);

		Empleado e = new Empleado(1, nombre, apellidos, d);

		try {
			tx = session.beginTransaction();
			session.save(e);
			tx.commit();
			resultado = false;
		} catch (HibernateException ex) {
			if (tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
		} finally {
			session.close();
		}
		return resultado;
	}

	private static boolean insertDepartamento(String nombre) {
		boolean resultado = false;
		Session session = factory.openSession();
		Transaction tx = null;

		Departamento d = new Departamento(0, nombre);
		try {
			tx = session.beginTransaction();
			session.save(d);
			tx.commit();
			resultado = false;
		} catch (HibernateException ex) {
			if (tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
		} finally {
			session.close();
		}
		return resultado;
	}

	// SELECT
	private List<Empleado> listEmpleado() {

		Session sesn = factory.openSession();
		Transaction tx = null;
		List<Empleado> empleados = new ArrayList();

		try {
			tx = sesn.beginTransaction();
			empleados = sesn.createQuery("FROM Empleado").list();
			tx.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			sesn.close();
		}
		return empleados;
	}

	private List<Empleado> listEmpleadoPorID() {

		Session sesn = factory.openSession();
		Transaction tx = null;
		List<Empleado> empleados = new ArrayList();

		try {
			tx = sesn.beginTransaction();
			empleados = sesn.createQuery("FROM Empleado WHERE nombre").list();
			tx.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			sesn.close();
		}
		return empleados;
	}

	private static List<Empleado> listEmpleadosDeUnDepartamento() {

		Session sesn = factory.openSession();
		Transaction tx = null;
		List<Empleado> empleados = new ArrayList();

		Main main = new Main();
		List<Departamento> departamentos = main.listDepartamento();
		for (Departamento d : departamentos) {
			System.out.println("Id dto: " + d.getIdDepartamento());
			System.out.println("\tNombre dto: " + d.getNombre() + "\n");
		}

		Scanner s8 = new Scanner(System.in);
		System.out.print("Id Departamento: ");
		int idDep = Integer.parseInt(s8.next());

		try {
			tx = sesn.beginTransaction();
			empleados = sesn.createQuery("FROM Empleado WHERE idDepartamento = " + idDep).list();
			tx.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			sesn.close();
		}
		return empleados;
	}

	private List<Departamento> listDepartamento() {

		Session sesn = factory.openSession();
		Transaction tx = null;
		List<Departamento> departamentos = new ArrayList();

		try {
			tx = sesn.beginTransaction();
			departamentos = sesn.createQuery("FROM Departamento").list();
			tx.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
		} finally {
			sesn.close();
		}
		return departamentos;
	}

	// UPDATE
	private static boolean actualizarEmpleado(int idEmp, String nombre, String apellidos, int idDto) {
		boolean resultado = false;
		Session session = factory.openSession();
		Transaction tx = null;

		Departamento d = new Departamento(idDto, null);

		Empleado u = new Empleado(idEmp, nombre, apellidos, d);
		try {
			tx = session.beginTransaction();
			session.update(u);
			tx.commit();
			resultado = true;
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return resultado;
	}

	private static boolean actualizarDepartamento(int id, String nombre) {
		boolean resultado = false;
		Session session = factory.openSession();
		Transaction tx = null;

		Departamento d = new Departamento(id, nombre);

		try {
			tx = session.beginTransaction();
			session.update(d);
			tx.commit();
			resultado = true;
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return resultado;
	}

	// DELETE
	private static boolean eliminarEmpleado(int id) {
		boolean resultado = false;
		Session session = factory.openSession();
		Transaction tx = null;

		Empleado e = new Empleado(id, null, null, null);

		try {
			tx = session.beginTransaction();
			session.delete(e);
			tx.commit();
			resultado = true;
		} catch (HibernateException ex) {
			if (tx != null) {
				tx.rollback();
			}
			ex.printStackTrace();
		} finally {
			session.close();
		}
		return resultado;
	}

	private static boolean eliminarDepartamento(int id) {
		boolean resultado = false;
		Session session = factory.openSession();
		Transaction tx = null;

		Departamento e = new Departamento(id, null);

		try {
			tx = session.beginTransaction();
			session.delete(e);
			tx.commit();
			resultado = true;
		} catch (HibernateException ex) {
			if (tx != null) {
				tx.rollback();
			}
			System.out.println("No se puede borrar este departamento, ya que contiene empleado/s");
		} finally {
			session.close();
		}
		return resultado;
	}
}
