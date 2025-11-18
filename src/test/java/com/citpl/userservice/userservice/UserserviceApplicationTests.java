package com.citpl.userservice.userservice;

import com.citpl.userservice.userservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
	"spring.data.mongodb.uri=mongodb://localhost:27017/test",
	"spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration"
})
class UserserviceApplicationTests {

	@MockBean
	private UserRepository userRepository;

	@Test
	void contextLoads() {
	}

}
