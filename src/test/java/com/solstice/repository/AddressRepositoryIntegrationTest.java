package com.solstice.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.solstice.model.Address;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestExecutionListeners({
    DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class
})
@DatabaseSetup("classpath:test-dataset.xml")
public class AddressRepositoryIntegrationTest {
  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private AddressRepository addressRepository;

  @Test
  public void testUpdateAddress_validAddress_success() {
    addressRepository.updateAddress(
        "Test St",
        111,
        "Sandwich",
        "IL",
60548,
        "USA",
        2
    );

    Address outcome = addressRepository.getOne((long)2);

    assertThat(outcome.getStreetName(), is(equalTo("Test St")));
    assertThat(outcome.getBuildingNum(), is(equalTo(111)));
    assertThat(outcome.getCity(), is(equalTo("Sandwich")));
    assertThat(outcome.getZipCode(), is(equalTo(60548)));
    assertThat(outcome.getAddressId(), is(equalTo((long)2)));
    assertThat(outcome.getAccount().getAccountId(), is(equalTo((long)1)));
  }

  @Test
  public void testUpdateAddress_invalidAddressId_failure() {
    addressRepository.updateAddress(
        "Fake St",
        123,
        "Phony",
        "IL",
        99999,
        "USA",
        8675309
    );

    List<Address> addressList = addressRepository.findAll();
    Optional checkUpdated = addressRepository.findById((long)8675309);

    assertThat(addressList.size(), is(3));
    assertThat(checkUpdated.isPresent(), is(false));
  }
}