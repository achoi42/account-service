package com.solstice;

import com.solstice.repository.AccountRepositoryIntegrationTest;
import com.solstice.repository.AddressRepositoryIntegrationTest;
import com.solstice.service.AccountServiceUnitTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
    AccountRepositoryIntegrationTest.class,
    AddressRepositoryIntegrationTest.class,
    AccountServiceUnitTest.class
})
public class AccountServiceApplicationTests {

  @Test
  public void contextLoads() {
  }

}
