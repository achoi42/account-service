package com.solstice.controller;

import com.solstice.model.domain.Address;
import com.solstice.service.AccountService;
import com.solstice.model.summary.AddressSummary;
import com.solstice.util.AddressPresenter;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

  private AccountService accountService;
  private AddressPresenter addressPresenter;

  protected AccountController() {
  }

  @Autowired
  public AccountController(AccountService accountService,
      AddressPresenter addressPresenter) {
    this.accountService = accountService;
    this.addressPresenter = addressPresenter;
  }

  @GetMapping("/accounts/{id}/address")
  public List<AddressSummary> readAddresses(@PathVariable(name="id") long id) {
    return accountService.fetchAddresses(id).stream().map(a -> addressPresenter.present(a, id)).collect(Collectors.toList());
  }

  @PostMapping("/accounts/{id}/address")
  public AddressSummary createAddress(@PathVariable(name="id") long id, @RequestBody Address address) {
    return addressPresenter.present(accountService.addAccountAddress(id, address), id);
  }

  @GetMapping("/accounts/{acctId}/address/{addrId}")
  public AddressSummary readSingleAddress(@PathVariable(name="acctId") long accountId, @PathVariable(name="addrId") long addressId){
    return addressPresenter.present(accountService.getAccountAddress(accountId, addressId), accountId);
  }

  @PutMapping("/accounts/{acctId}/address/{addrId}")
  public AddressSummary updateAddress(
      @PathVariable(name="acctId") long accountId,
      @PathVariable(name="addrId") long addressId,
      @RequestBody Address address
  ) {
    return addressPresenter.present(accountService.updateAccountAddress(accountId, addressId, address), accountId);
  }

  @DeleteMapping("/accounts/{acctId}/address/{addrId}")
  @ResponseBody
  public ResponseEntity deleteAddress(@PathVariable(name="acctId") long accountId, @PathVariable(name="addrId") long addressId) {
    accountService.deleteAccountAddress(accountId, addressId);
    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  @RequestMapping("/addresses")
  public String getAddress() {
    return "No.";
  }
}
