package com.solstice.util;

import com.solstice.model.domain.Address;
import com.solstice.model.summary.AddressSummary;
import org.springframework.stereotype.Component;

@Component
public class AddressPresenter {
  public AddressSummary present(Address address, long accountId) {
    if(address == null) {
      return null;
    }
    return new AddressSummary(
      address.getAddressId(),
      address.getStreetName(),
      address.getBuildingNum(),
      address.getCity(),
      address.getState(),
      address.getZipCode(),
      address.getCountry(),
      accountId
    );
  }
}
