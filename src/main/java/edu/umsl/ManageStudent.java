/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.umsl;

import java.util.Iterator;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
//import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
//import org.hibernate.service.ServiceRegistry;

public class ManageStudent {
	private static SessionFactory factory;

	public static void main(String[] args) {
		try {
			factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex);
		}
		ManageStudent ME = new ManageStudent();

		/* Add few employee records in database */
		Integer stuID1 = ME.addStudent("John", "Doe", 999999999, 100000);
		Integer stuID2 = ME.addStudent("Daisy", "Das", 282222222, 22000);
		// Integer empID3 = ME.addEmployee("John", "Paul", 10000);

		/* List down all the employees */
		ME.listStudents();

		/* Update employee's records */
		ME.updateStudent(stuID1, 999999999);

		/* Delete an employee from the database */
		ME.deleteStudent(stuID2);

		/* List down new list of the employees */
		ME.listStudents();
	}

	/* Method to CREATE an employee in the database */
	public Integer addStudent(String fname, String lname, int ssn, int salary) {
		Session session = factory.openSession();
		Transaction tx = null;
		Integer studentID = null;
		try {
			tx = session.beginTransaction();
			Student student = new Student(fname, lname, ssn, salary);
			studentID = (Integer) session.save(student);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return studentID;
	}

	/* Method to READ all the employees */
	public void listStudents() {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			@SuppressWarnings("unchecked")
			Query q = session.createSQLQuery("select * from Student where first_name = 'John'");

			List l = q.list();

			List<Student> students = session.createQuery("FROM Student").list();

			for (Iterator<Student> iterator = students.iterator(); iterator.hasNext();) {
				Student student = iterator.next();
				System.out.print("First Name: " + student.getFirstName());
				System.out.print("Last Name: " + student.getLastName());
				System.out.println("SSN: " + student.getSSN());
                                System.out.println("Salary: " + student.getSalary());
			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	/* Method to UPDATE salary for an employee */
	public void updateStudent(Integer StudentID, int ssn) {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Student student = (Student) session.get(Student.class, StudentID);
			student.setSSN(ssn);
			session.update(student);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	/* Method to DELETE an employee from the records */
	public void deleteStudent(Integer StudentID) {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Student student = (Student) session.get(Student.class, StudentID);
			session.delete(student);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
}

