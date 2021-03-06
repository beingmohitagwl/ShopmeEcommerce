package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {

	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateNewUserWithOneRole() {
		Role roleAdmin = entityManager.find(Role.class, 1);
		User userMohit = new User("beingmohitagwl@gmail.com", "qwerty123", "Mohit", "Agarwal");
		userMohit.addRole(roleAdmin);
		
		User savedUser = repo.save(userMohit);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateNewUserWithTwoRole() {
		User userTwinkle = new User("twinkle.agarwal2297@gmail.com", "qwerty123", "Twinkle", "Agarwal");
		userTwinkle.addRole(entityManager.find(Role.class, 3));
		userTwinkle.addRole(entityManager.find(Role.class, 5));
		
		User savedUser = repo.save(userTwinkle);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllUsers() {
		Iterable<User> listUsers = repo.findAll();
		
		listUsers.forEach(user -> System.out.println(user));
	}
	
	
	@Test
	public void testGetUserById() {
		User user = repo.findById(1).get();
		System.out.println(user);
		assertThat(user).isNotNull();
	}
	
	@Test
	public void testUpdateUserDetails() {
		User user = repo.findById(1).get();
		user.setEnabled(true);
		user.setEmail("beingmohitagarwal@hotmail.com");
		
		repo.save(user);
	}
	
	@Test
	public void testUpdateUserRoles() {
		User user = repo.findById(2).get();
		
		user.getRoles().remove(entityManager.find(Role.class, 3));
		user.getRoles().remove(entityManager.find(Role.class, 5));
		user.addRole(entityManager.find(Role.class, 2));
		user.addRole(entityManager.find(Role.class, 4));
		
		repo.save(user);
	}
	
	@Test
	public void testDeleteUser() {
		repo.deleteById(2);
	}
	
	@Test
	public void testGetUserByEmail() {
		String email = "beingmohitagarwl@hotmail.com";
		User user = repo.getUserByEmail(email);
		
		assertThat(user).isNotNull();
	}
	
	@Test
	public void testCountById() {
		Integer id = 1;
		Long count = repo.countById(id);
		
		assertThat(count).isNotNull().isGreaterThan(0);
	}
	
	@Test
	public void testDisableUser() {
		Integer id = 1;
		repo.updateEnabledStatus(id, false);
	}
	
	@Test
	public void testEnableUser() {
		Integer id = 3;
		repo.updateEnabledStatus(id, true);
	}
	
	@Test
	public void testListFirstPage() {
		int pageNumber = 1;
		int pageSize = 4;
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(pageable);
		
		List<User> listUsers = page.getContent();
		
		listUsers.forEach(user -> System.out.println(user));
		
		assertThat(listUsers.size()).isEqualTo(pageSize);
	}
	
	@Test
	public void testSearchUsers() {
		String keyword = "bruce";
		
		int pageNumber = 0;
		int pageSize = 4;
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(keyword, pageable);
		
		List<User> listUsers = page.getContent();
		
		listUsers.forEach(user -> System.out.println(user));
		
		assertThat(listUsers.size()).isGreaterThan(0);
	}
}
