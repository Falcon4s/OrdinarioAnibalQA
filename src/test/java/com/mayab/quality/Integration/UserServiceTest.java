package com.mayab.quality.Integration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.dbunit.Assertion;
import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.not;

import com.mayab.quality.integrationtest.dao.IDAOUser;
import com.mayab.quality.integrationtest.dao.UserMysqlDAO;
import com.mayab.quality.integrationtest.model.User;
import com.mayab.quality.integrationtest.service.UserService;

class UserServiceTest extends DBTestCase{
	
	UserMysqlDAO daoMySql;
	
	public UserServiceTest() {
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS,"com.mysql.cj.jdbc.Driver");
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL,"jdbc:mysql://localhost:3306/calidadSoftware2024");
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME,"root");
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD,"123456");	
	
	}
	
	private IDAOUser dao;
	private UserService service;
	
	@BeforeEach
    void setup() throws Exception {
        dao = new UserMysqlDAO();
        service = new UserService(dao);

        IDatabaseConnection connection = getConnection();
        try {
            DatabaseOperation.TRUNCATE_TABLE.execute(connection, getDataSet());
            DatabaseOperation.CLEAN_INSERT.execute(connection, getDataSet());
        } catch (Exception e) {
            fail("Error in setup: " + e.getMessage());
        } finally {
            connection.close();
        }
    }

    protected IDataSet getDataSet() throws Exception {
        return new FlatXmlDataSetBuilder().build(new FileInputStream("src/resources/initDB.xml"));
    }

    @Test
    void happyPathCreateUser() {
        String username = "user1";
        String email = "user1@ejemplo.com";
        String password = "123457832";

        User expected = new User(username, email, password);
        User result = service.createUser(username, email, password);

        assertThat(result.getName(), is(expected.getName()));
        assertThat(result.getEmail(), is(expected.getEmail()));
        assertThat(result.getPassword(), is(expected.getPassword()));
    }

    @Test
    void whenEmailAlreadyExists() {
    	
    	User user = new User("user", "user1@ejemplo.com","newPassword123");
    	dao.save(user);

        User result = service.createUser("user23", "user1@ejemplo.com","new4rerwtrwe");

        assertThat(result.getName(), is(user.getName()));
    }

    @Test
    void whenPasswordShort() {
        String username = "user3";
        String email = "user3@ejemplo.com";
        String password = "1234"; 

        User result = service.createUser(username, email, password);
        User expected = null; 

        assertThat(result, is(expected));
    }

    @Test
    void whenPasswordLong() {
        String username = "user4";
        String email = "user4@ejemplo.com";
        String password = "thisIsALongPasswordThatShouldFail"; 

        User result = service.createUser(username, email, password);
        User expected = null; 

        assertThat(result, is(expected));
    }

    @Test
    void whenUpdateUser() {
        String username = "user1";
        String email = "user1@ejemplo.com";
        String password = "newPassword123";

        User createdUser = service.createUser(username, email, "123457832");
        createdUser.setName("updatedName");
        createdUser.setPassword(password);

        User updatedUser = service.updateUser(createdUser);

        assertThat(updatedUser.getName(), is("updatedName"));
        assertThat(updatedUser.getPassword(), is(password));
    }

    @Test
    void whenDeleteUser() {
        String username = "user2";
        String email = "user2@ejemplo.com";
        String password = "123457832";

        User user = service.createUser(username, email, password);
        boolean result = service.deleteUser(user.getId());

        assertThat(result, is(true)); 
    }

    @Test
    void whenFindAllUsers() {
        service.createUser("user1", "user1@ejemplo.com", "password123");
        service.createUser("user2", "user2@ejemplo.com", "password123");

        List<User> users = service.findAllUsers();

        assertThat(users.size(), is(2)); 
    }

    @Test
    void whenFindUserByEmail() {
        User user = new User("usuario", "usuario@ejemplo", "123455677");
        dao.save(user);

        User expected = service.findUserByEmail("usuario@ejemplo");

        assertThat(user.getEmail(), is(expected.getEmail())); 
    }

    @Test
    void whenFindUserById() {
        User createdUser = service.createUser("user1", "user1@ejemplo.com", "password123");

        User user = service.findUserById(createdUser.getId());

        assertThat(user.getId(), is(createdUser.getId()));
    }

}
