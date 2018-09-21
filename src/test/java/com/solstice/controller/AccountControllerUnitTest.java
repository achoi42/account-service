package com.solstice.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.solstice.model.domain.Address;
import com.solstice.model.summary.AddressSummary;
import com.solstice.repository.AccountRepository;
import com.solstice.repository.AddressRepository;
import com.solstice.service.AccountService;
import com.solstice.util.AddressPresenter;
import java.util.Collections;
import javax.ws.rs.core.MediaType;
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
@WebMvcTest(value = AccountController.class, secure = false)
public class AccountControllerUnitTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AccountService accountService;

  @MockBean
  private AddressPresenter addressPresenter;

  @MockBean
  private AccountRepository accountRepository;

  @MockBean
  private AddressRepository addressRepository;

  @InjectMocks
  private AccountController accountController;

  private Address address;
  private AddressSummary addressSummary;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    address = new Address(
        "Fake St",
        123,
        "Phony",
        "IL",
        99999,
        "USA",
        null
    );

    addressSummary = new AddressSummary(
        999,
        "Fake St",
        123,
        "Phony",
        "IL",
        99999,
        "USA",
        1
    );
  }

  @Test
  public void testReadAddresses_validIds_success() throws Exception {
    when(addressPresenter.present(any(), anyLong()))
        .thenReturn(addressSummary);

    when(accountService.fetchAddresses(anyLong()))
        .thenReturn(Collections.singletonList(address));

    mockMvc.perform(get("/accounts/1/address"))
        .andExpect(jsonPath("$[0].addressId",is(999)))
        .andExpect(jsonPath("$[0].accountId", is(1)))
        .andExpect(status().isOk())
        .andReturn();
  }

  @Test
  public void testCreateAddress_validAccountId_success() throws Exception {


    when(addressPresenter.present(any(Address.class), anyLong()))
        .thenReturn(addressSummary);

    Address created = new Address();
    created.setBuildingNum(00);

    when(accountService.addAccountAddress(anyLong(), any(Address.class)))
        .thenReturn(created);


    mockMvc.perform(post("/accounts/1/address").content(asJsonString(created)).contentType(
        MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.addressId", is(999)))
        .andExpect(jsonPath("$.accountId", is(1)))
        .andExpect(jsonPath("$.city", is("Phony")))
        .andExpect(status().isOk())
        .andReturn();
  }

  @Test
  public void testReadSingleAddress_validIds_success() throws Exception {
    when(addressPresenter.present(any(), anyLong()))
        .thenReturn(addressSummary);

    when(accountService.getAccountAddress(anyLong(), anyLong()))
        .thenReturn(address);

    mockMvc.perform(get("/accounts/1/address/2"))
        .andExpect(jsonPath("$.addressId",is(999)))
        .andExpect(jsonPath("$.accountId", is(1)))
        .andExpect(status().isOk())
        .andReturn();

//    AddressSummary outcome = accountController.readSingleAddress(anyLong(), anyLong());
//    assertThat(outcome.getAccountId(), is((long)1));
//    assertThat(outcome.getAddressId(), is((long) 999));
  }

  @Test
  public void testUpdateAddress_validIdsValidBody_success() throws Exception {
    when(addressPresenter.present(any(), anyLong()))
        .thenReturn(addressSummary);

    when(accountService.updateAccountAddress(anyLong(), anyLong(), any()))
        .thenReturn(address);

    mockMvc.perform(put("/accounts/1/address/1").content(asJsonString(address)).contentType(
        MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.addressId",is(999)))
        .andExpect(jsonPath("$.accountId", is(1)))
        .andExpect(status().isOk())
        .andReturn();

//    AddressSummary outcome = accountController.updateAddress(anyLong(), anyLong(), any());
//    assertThat(outcome.getAccountId(), is((long)1));
//    assertThat(outcome.getAddressId(), is((long) 999));
  }

  @Test
  public void testDeleteAddress_validIds_success() throws Exception {
    doNothing().when(accountService).deleteAccountAddress(anyLong(), anyLong());

    mockMvc.perform(delete("/accounts/1/address/1"))
        .andExpect(status().is2xxSuccessful())
        .andReturn();

//    ResponseEntity outcome = accountController.deleteAddress(anyLong(), anyLong());

//    assertThat(outcome.getStatusCode(), is(equalTo(HttpStatus.NO_CONTENT)));
  }

  private static String asJsonString(final Object obj) {
    try {
      final ObjectMapper mapper = new ObjectMapper();
      final String jsonContent = mapper.writeValueAsString(obj);
      return jsonContent;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
