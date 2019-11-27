package com.redhat.loja.selenium;


import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class JoinFacesSmokeTest extends WebDriverUtil {

	@Test
	  public void testWelcomePageRedirect() {
	    driver.get("http://localhost:8090/");

	    assertThat(driver.getTitle(),is("Loja Online"));
	        
	  }
	
}
