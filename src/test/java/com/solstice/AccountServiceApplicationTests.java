package com.solstice;

import com.solstice.controller.AccountControllerIntegrationTest;
import com.solstice.controller.AccountControllerUnitTest;
import com.solstice.repository.AccountRepositoryIntegrationTest;
import com.solstice.repository.AddressRepositoryIntegrationTest;
import com.solstice.service.AccountServiceIntegrationTest;
import com.solstice.service.AccountServiceUnitTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
    AccountRepositoryIntegrationTest.class,
    AddressRepositoryIntegrationTest.class,
    AccountServiceUnitTest.class,
    AccountServiceIntegrationTest.class,
    AccountControllerUnitTest.class,
    AccountControllerIntegrationTest.class
})
public class AccountServiceApplicationTests {

  @Test
  public void contextLoads() {
  }

}
