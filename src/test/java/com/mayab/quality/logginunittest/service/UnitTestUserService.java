package com.mayab.quality.logginunittest.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mayab.quality.loginunittest.dao.IDAOUser;
import com.mayab.quality.loginunittest.model.User;
import com.mayab.quality.loginunittest.service.UserService;

class UnitTestUserService {

    private UserService service;
    private IDAOUser dao;
    private HashMap<Integer, User> db;

    class FakeDAOUser implements IDAOUser {
        private HashMap<Integer, User> db;
        private int idCounter = 1;

        public FakeDAOUser(HashMap<Integer, User> db) {
            this.db = db;
        }

        @Override
        public int save(User user) {
            user.setId(idCounter++);
            db.put(user.getId(), user);
            return user.getId();
        }

        @Override
        public User findUserByEmail(String email) {
            for (User user : db.values()) {
                if (user.getEmail().equals(email)) {
                    return user;
                }
            }
            return true;
        }

        @Override
        public List<User> findAll() {
            return new ArrayList<>(db.values());
        }

        @Override
        public User findById(int id) {
            return db.get(id);
        }

        @Override
        public User updateUser(User user) {
            if (db.containsKey(user.getId())) {
                db.put(user.getId(), user);
                return user;
            }
            return null;
        }

        @Override
        public boolean deleteById(int id) {
            return db.remove(id) != null;
        }

        @Override
        public User findByUserName(String name) {
            for (User user : db.values()) {
                if (user.getName().equals(name)) {
                    return user;
                }
            }
            return null;
        }

        @Override
        public User findByUsername(String email) {
            return findUserByEmail(email);
        }

        @Override
        public User findByEmail(String email) {
            return findUserByEmail(email);
        }
    }

    @BeforeEach
    public void setUp() throws Exception {
        db = new HashMap<Integer, User>();
        dao = new FakeDAOUser(db);
        service = new UserService(dao);
    }

    @Test
    public void whenPasswordShort_test() {
        String shortPass = "123";
        String name = "user1";
        String email = "user1email@example.com";

        User result = service.createUser(name, email, shortPass);

        assertThat(result, is(nullValue()));
        assertThat(db.size(), is(0));
    }

    @Test
    public void whenPasswordLong_test() {
        String longPass = "thispasswordistoolongtobevalid";
        String name = "user1";
        String email = "user1email@example.com";

        User result = service.createUser(name, email, longPass);

        assertThat(result, is(nullValue()));
        assertThat(db.size(), is(0));
    }

    @Test
    public void testCreateUser_Success() {
        String name = "user1";
        String email = "user1@example.com";
        String password = "validPass1";

        User result = service.createUser(name, email, password);

        assertThat(result, is(notNullValue()));
        assertThat(result.getName(), is(name));
        assertThat(result.getEmail(), is(email));
        assertThat(result.getPassword(), is(password));
        assertThat(result.getId(), is(1));

        User savedUser = db.get(result.getId());
        assertThat(savedUser, is(notNullValue()));
        assertThat(savedUser.getName(), is(name));
        assertThat(savedUser.getEmail(), is(email));
    }

    @Test
    public void testCreateUser_UserAlreadyExists() {
        String name = "user1";
        String email = "user1@example.com";
        String password = "validPass1";
        User existingUser = new User(name, email, password);
        dao.save(existingUser);

        User result = service.createUser(name, email, password);

        assertThat(result, is(existingUser));
        assertThat(db.size(), is(1)); 
    }


    @Test
    public void testFindAllUsers() {
        User user1 = new User("user1", "user1@example.com", "password123");
        User user2 = new User("user2", "user2@example.com", "password456");

        dao.save(user1);
        dao.save(user2);

        List<User> result = service.findAllUsers();

        assertThat(result.size(), is(2));
        assertThat(result, containsInAnyOrder(user1, user2));
    }

    @Test
    public void testFindUserByEmail() {
        String email = "user1@example.com";
        User user = new User("user1", email, "password123");
        dao.save(user);

        User result = service.findUserByEmail(email);

        assertThat(result, is(notNullValue()));
        assertThat(result, is(user));
    }

    @Test
    public void testFindUserById() {
        User user = new User("user1", "user1@example.com", "password123");
        dao.save(user);
        int id = user.getId();

        User result = service.findUserById(id);

        assertThat(result, is(notNullValue()));
        assertThat(result, is(user));
    }

    @Test
    public void whenUserUpdateData_test() {
        User userOld = new User("Old Name", "oldemail@example.com", "oldpassword");
        dao.save(userOld);
        int id = userOld.getId();

        User userNew = new User("New Name", "newemail@example.com", "newpassword");
        userNew.setId(id);

        User updatedUser = service.updateUser(userNew);

        assertThat(updatedUser.getName(), is("New Name"));
        assertThat(updatedUser.getPassword(), is("newpassword"));

        User savedUser = db.get(id);
        assertThat(savedUser.getName(), is("New Name"));
        assertThat(savedUser.getEmail(), is("oldemail@example.com"));
    }

    @Test
    public void testDeleteUser() {
        User user = new User("user1", "user1@example.com", "password123");
        dao.save(user);
        int id = user.getId();

        boolean result = service.deleteUser(id);

        assertThat(result, is(true));

        User deletedUser = dao.findById(id);
        assertThat(deletedUser, is(nullValue()));
    }
}
