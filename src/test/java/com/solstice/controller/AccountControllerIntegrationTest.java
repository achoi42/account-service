package com.solstice.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.solstice.model.domain.Address;
import com.solstice.model.summary.AddressSummary;
import java.util.List;
import javax.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace= Replace.NONE)
@TestExecutionListeners({
    DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class
})
@DatabaseSetup("classpath:test-dataset.xml")
public class AccountControllerIntegrationTest {

  @Autowired
  private AccountController accountController;

  @Test
  @Transactional
  public void testReadAddresses_validIds_success() {
    List<AddressSummary> outcome = accountController.readAddresses(1);

    assertThat(outcome.size(), is(2));
    assertThat(outcome.get(0).getAddressId(), is((long)1));
    assertThat(outcome.get(1).getAddressId(), is((long)2));
  }

  @Test
  @Transactional
  public void testCreateAddress_validAccountId_success() {
    Address address = new Address();
    address.setBuildingNum(99999);

    AddressSummary outcome = accountController.createAddress(1, address);

    assertThat(outcome.getBuildingNum(), is(99999));
  }

  @Test
  @Transactional
  public void testReadSingleAddress_validIds_success() {
    AddressSummary outcome = accountController.readSingleAddress(1, 2);

    assertThat(outcome.getAddressId(),is((long) 2));
    assertThat(outcome.getBuildingNum(), is(2));
    assertThat(outcome.getAddressId(), is((long) 2));
    assertThat(outcome.getAccountId(), is((long) 1));
  }

  @Test
  @Transactional
  public void testUpdateAddress_validIdsValidBody_success() {

    Address toUpdate = new Address();
    toUpdate.setBuildingNum(-99);

    AddressSummary outcome = accountController.updateAddress(1, 2, toUpdate);

    assertThat(outcome.getBuildingNum(), is(-99));
    assertThat(outcome.getAccountId(), is((long) 1));
    assertThat(outcome.getAddressId(), is((long) 2));
  }

  @Test
  @Transactional
  public void testDeleteAddress_validIds_success() {
    ResponseEntity outcome = accountController.deleteAddress(1, 2);

    assertThat(outcome.getStatusCode(), is(equalTo(HttpStatus.NO_CONTENT)));
  }
}
