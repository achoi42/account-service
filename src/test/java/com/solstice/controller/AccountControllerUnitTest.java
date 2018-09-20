package com.solstice.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.solstice.model.Account;
import com.solstice.model.Address;
import com.solstice.repository.AccountRepository;
import com.solstice.service.AccountService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(AccountController.class)
public class AccountControllerUnitTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AccountRepository accountRepository;

  @MockBean
  private AccountService accountService;

  @InjectMocks
  private AccountController accountController;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testReadAddressHappyPath() {

    Account account = new Account();
    Address address = new Address();

    when(accountService.fetchAddresses(anyLong()))
        .thenReturn(null);
  }
}
