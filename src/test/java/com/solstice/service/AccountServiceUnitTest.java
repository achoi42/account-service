package com.solstice.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.solstice.model.domain.Account;
import com.solstice.model.domain.Address;
import com.solstice.repository.AccountRepository;
import com.solstice.repository.AddressRepository;
import com.solstice.util.NotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class AccountServiceUnitTest {

  @Mock
  private AccountRepository accountRepository;

  @Mock
  private AddressRepository addressRepository;

  @InjectMocks
  private AccountService accountService;

  @Rule
  public ExpectedException exceptionGrabber = ExpectedException.none();

  private Address address;
  private Account account;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    account = new Account();
    account.setEmail("TestEmail");
    account.setFirstName("TestFirst");
    account.setLastName("TestLast");
    account.setAddresses(new ArrayList<>(Collections.emptyList()));

    address = new Address();
    address.setStreetName("TestStreet");
    address.setBuildingNum(1);
    address.setCity("Chicago");
    address.setState("IL");
    address.setZipCode(60606);
    address.setCountry("United States");
    address.setAccount(account);
  }

  @Test
  public void testFetchAddresses_validAccountId_success() {
    when(accountRepository.findById(anyLong()))
        .thenReturn(Optional.of(account));

    when(accountRepository.getOne(anyLong()))
        .thenReturn(account);

    List<Address> outcome = accountService.fetchAddresses(-1);

    assertThat(outcome.size(), is(equalTo(1)));
    assertThat(outcome.get(0).getStreetName(), is(equalTo(address.getStreetName())));
    assertThat(outcome.get(0).getAccount().getEmail(), is(equalTo(account.getEmail())));
  }

  @Test
  public void testFetchAddresses_validAccountIdNoAddresses_success() {
    Account noAddresses = new Account();

    when(accountRepository.findById(anyLong()))
        .thenReturn(Optional.of(noAddresses));

    when(accountRepository.getOne(anyLong()))
        .thenReturn(noAddresses);

    List<Address> outcome = accountService.fetchAddresses(-1);

    assertThat(outcome, is(empty()));
  }

  @Test
  public void testFetchAddresses_invalidAccountId_failure() {
    when(accountRepository.findById(anyLong()))
        .thenReturn(Optional.empty());

    exceptionGrabber.expect(NotFoundException.class);

    List<Address> outcome = accountService.fetchAddresses(-1);

    assertThat(outcome, is(nullValue()));
  }

  @Test
  public void testAddAddress_validAccountIdValidAddress_success() {
    Address created = new Address();
    created.setStreetName("New Street");
    created.setBuildingNum(111);
    created.setCity("Chicago");
    created.setState("IL");
    created.setZipCode(60606);
    created.setCountry("United States");

    when(accountRepository.findById(anyLong()))
        .thenReturn(Optional.of(account));

    when(accountRepository.getOne(anyLong()))
        .thenReturn(account);

    when(addressRepository.save(any()))
        .thenReturn(created);

    Address outcome = accountService.addAccountAddress(-1, created);

    assertThat(outcome.getAddressId(), is(equalTo(created.getAddressId())));
    assertThat(outcome.getAccount().getAccountId(), is(equalTo(account.getAccountId())));
  }

  @Test
  public void testAddAddress_validAccountIdNullAddress_failure() {
    when(accountRepository.findById(anyLong()))
        .thenReturn(Optional.of(account));

    when(accountRepository.getOne(anyLong()))
        .thenReturn(account);

    Address outcome = accountService.addAccountAddress(-1, null);

    assertThat(outcome,is(nullValue()));
  }

  @Test
  public void testAddAddress_invalidAccountId_failure() {
    when(accountRepository.findById(anyLong()))
        .thenReturn(Optional.empty());

    exceptionGrabber.expect(NotFoundException.class);

    Address outcome = accountService.addAccountAddress(-1, null);

    assertThat(outcome, is(nullValue()));
  }

  @Test
  public void testUpdateAddress_validIds_success() {
    when(accountRepository.findById(anyLong()))
        .thenReturn(Optional.of(account));

    when(addressRepository.findById(anyLong()))
        .thenReturn(Optional.of(address));

    when(accountRepository.getOne(anyLong()))
        .thenReturn(account);

    when(addressRepository.save(any()))
        .thenReturn(address);

    Address outcome = accountService.updateAccountAddress(account.getAccountId(), address.getAddressId(), address);

    assertThat(outcome.getStreetName(),is(equalTo(address.getStreetName())));
    assertThat(outcome.getBuildingNum(), is(equalTo(address.getBuildingNum())));
    assertThat(outcome.getAccount().getAccountId(),is(equalTo(address.getAddressId())));
  }

  @Test
  public void testUpdateAddress_invalidIds_failure() {
    when(accountRepository.findById(anyLong()))
        .thenReturn(Optional.empty());

    exceptionGrabber.expect(NotFoundException.class);

    Address outcome = accountService.updateAccountAddress(-1,-1,null);

    assertThat(outcome, is(nullValue()));
  }
}
