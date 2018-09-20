package com.solstice.service;

import com.solstice.model.domain.Account;
import com.solstice.model.domain.Address;
import com.solstice.repository.AccountRepository;
import com.solstice.repository.AddressRepository;
import com.solstice.util.AddressPresenter;
import com.solstice.util.BadRequestException;
import com.solstice.util.NotFoundException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

  private AccountRepository accountRepository;
  private AddressRepository addressRepository;
  private AddressPresenter addressPresenter;

  @Transient
  private Logger logger = LoggerFactory.getLogger(getClass());

  protected AccountService() {
  }

  @Autowired
  public AccountService(AccountRepository accountRepository, AddressRepository addressRepository, AddressPresenter addressPresenter) {
    this.accountRepository = accountRepository;
    this.addressRepository = addressRepository;
    this.addressPresenter = addressPresenter;
  }

//  public Account updateAccount(long id) {
//    checkId(accountRepository, id);
//
//    Account existing =
//  }

  public List<Account> fetchAccounts() {
    return accountRepository.findAll();
  }

  public List<Address> fetchAddresses(long id) {
    checkId(accountRepository, id);
    Account account = accountRepository.getOne(id);
    if(account.getAddresses() == null) {
      logger.warn("Account {} has null address list", id);
    }
    return account.getAddresses();
  }

  public Address getAccountAddress(long accountId, long addressId) {
    checkId(accountRepository, accountId);
    checkId(addressRepository, addressId);

    Account myAccount = accountRepository.getOne(accountId);
    Address myAddress = addressRepository.getOne(addressId);

    if(!myAccount.getAddresses().contains(myAddress) || myAddress.getAccount().getAccountId() != accountId) {
      logger.error("Address {} found but does not belong to account {}", addressId, accountId);
      throw new BadRequestException();
    }

    return myAddress;
  }

  public Address addAccountAddress(long accountId, Address address) {
    checkId(accountRepository, accountId);
    Account myAccount = accountRepository.getOne(accountId);
    if(address == null) {
      logger.error("Client attempted to add null address to account " + myAccount.getAccountId());
      return null;
    }
    Address saved = addressRepository.save(address);
    myAccount.addAddress(saved);
    accountRepository.save(myAccount);
    return saved;
  }

  public Address updateAccountAddress(long accountId, long addressId, Address toUpdate) {
    checkId(accountRepository, accountId);
    checkId(addressRepository, addressId);

    if(toUpdate == null) {
      logger.warn("Null request body when attempting to update address " + addressId + " for account " + accountId);
      return addressRepository.getOne(addressId);
    }

    if(toUpdate.getAccount() != null && accountId != toUpdate.getAccount().getAccountId()) {
      logger.error("Address " + addressId + "'s parent account " + accountId + " does not match updated order's account " + toUpdate.getAccount().getAccountId());
      logger.error("An address can only be associated with exclusively one account.");
      throw new BadRequestException();
    }

    // Disassociate old address with account
    Account myAccount = accountRepository.getOne(accountId);
    Address old = addressRepository.getOne(addressId);
    int oldIdx = myAccount.getAddresses().indexOf(old);
    myAccount.removeAddress(myAccount.getAddresses().get(oldIdx));

    // Update old address with new attributes in repository
    addressRepository.updateAddress(
        toUpdate.getStreetName(),
        toUpdate.getBuildingNum(),
        toUpdate.getCity(),
        toUpdate.getState(),
        toUpdate.getZipCode(),
        toUpdate.getCountry(),
        addressId
    );

    // Associate updated address with account
    Address updated = addressRepository.getOne(addressId);
    myAccount.addAddress(updated);
    accountRepository.save(myAccount);

    return updated;
  }

  public void deleteAccountAddress(long accountId, long addressId) {
    checkId(accountRepository, accountId);
    checkId(addressRepository, addressId);



    addressRepository.deleteById(addressId);
  }

  private void checkId(JpaRepository repository, long id) {
    Optional checker = repository.findById(id);
    if(!checker.isPresent()) {
      logger.error("Entity with id " + id + " not found");
      throw new NotFoundException();
    }
  }
}
