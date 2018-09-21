package com.solstice.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.solstice.model.domain.Address;
import java.util.List;
import javax.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@AutoConfigureTestDatabase(replace= Replace.NONE)
@TestExecutionListeners({
    DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class
})
@DatabaseSetup("classpath:test-dataset.xml")
public class AccountServiceIntegrationTest {

  @Autowired
  private AccountService accountService;

  @Test
  @Transactional
  public void testFetchAddresses_validAccountId_success() {
    List<Address> outcome = accountService.fetchAddresses(1);

    assertThat(outcome.size(), is(2));
    assertThat(outcome.get(0).getAccount().getAccountId(), is((long) 1));
  }

  @Test
  @Transactional
  public void testGetAccountAddress_validIds_success() {
    Address outcome = accountService.getAccountAddress(1,2);

    assertThat(outcome.getStreetName(), is("Road St"));
  }

  @Test
  @Transactional
  public void testAddAccountAddress_validAccountId_success() {
    Address created = new Address();
    created.setCity("FAKE!");

    Address outcome = accountService.addAccountAddress(2, created);

    assertThat(outcome.getAccount().getAddresses().size(),is(2));
    assertThat(outcome.getCity(), is(equalTo(created.getCity())));
  }

  @Test
  @Transactional
  public void testUpdateAccountAddress_validIds_success() {
    Address toUpdate = new Address();
    toUpdate.setCity("FAKE!");

    Address outcome = accountService.updateAccountAddress(1, 2, toUpdate);

    assertThat(outcome.getAccount().getAddresses().size(),is(2));
    assertThat(outcome.getCity(), is(equalTo(toUpdate.getCity())));
  }
}
