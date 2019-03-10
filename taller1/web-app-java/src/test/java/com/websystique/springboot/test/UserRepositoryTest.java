package com.websystique.springboot.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.websystique.springboot.model.User;
import com.websystique.springboot.repositories.UserRepository;

/**
 * http://www.baeldung.com/spring-boot-testing
 * https://github.com/eugenp/tutorials/tree/master/spring-boot
 * 
 * 
 * @author
 *
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
public class UserRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private UserRepository userRepository;

	// write test cases here
	@Test
	public void whenFindByName_thenReturnEmployee() {
		// given
		User user = new User();
		user.setName("alex");
		user.setAge(35);

		entityManager.persist(user);
		entityManager.flush();

		// when
		User found = userRepository.findByName(user.getName());

		// then
		assertEquals(user.getName(), found.getName());

	}

}
