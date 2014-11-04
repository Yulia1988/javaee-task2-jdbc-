package com.academysmart.repositorty;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.academysmart.exception.IncorrectEmailException;
import com.academysmart.exception.ServletException;
import com.academysmart.repository.EmployeeRepositorySingleton;

public class EmployeeRepositorySingletonTest {

	@BeforeClass
	public static void beforeClass()throws Exception {
		EmployeeRepositorySingleton.getRepository().addEmployee("Иван",
			"Иванов", "ivanov@mail.ru");
	}

	@Test
	public void testGetRepositoryReturnOneInstance() {
		Assert.assertSame( EmployeeRepositorySingleton.getRepository(),
				EmployeeRepositorySingleton.getRepository());
	}
	
	
	

	@Test(expected=com.academysmart.exception.IncorrectEmailException.class)
	public void testAddEmployeWithIncorrectEmail() throws ServletException, SQLException  {
	
				EmployeeRepositorySingleton.getRepository().addEmployee("Иван",
				"Иванов", "ivanov@mail.ru");
	
				EmployeeRepositorySingleton.getRepository().addEmployee("Иван",
						"Иванов", "ivanov@mail.ru");
	}

}
