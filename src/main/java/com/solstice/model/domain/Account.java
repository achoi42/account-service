package com.solstice.model.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "accountId", scope = Long.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler","_links"})
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long accountId;

  private String firstName;
  private String lastName;
  private String email;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "account_id")
  private List<Address> address;

  @Transient
  private Logger logger = LoggerFactory.getLogger(getClass());

  public Account(String firstName, String lastName, String email,
      List<Address> address) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    if(address == null) {
      this.address = new ArrayList<Address>(Collections.emptyList());
    }
    else {
      this.address = address;
    }
  }

  public Account(){
    address = new ArrayList<Address>(Collections.emptyList());
  }

  public long getAccountId() {
    return accountId;
  }

  public void setAccountId(long accountId) {
    this.accountId = accountId;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public List<Address> getAddresses() {
    return address;
  }

  public void setAddresses(List<Address> address) {
    this.address = address;
    if(this.address != null && !this.address.isEmpty()) {
      logger.info("Attempting to set " + address.size() + " addresses to account " + accountId);
      for (Address a : this.address) {
        if(a != null) {
          a.setAccount(this);
          logger.info("Successfully set account " + accountId + " to address " + a.getAddressId());
        }
      }
    }
  }

  public void addAddress(Address address) {
    if(this.address == null) {
      logger.warn("Client attempted to add null address to account " + this.accountId);
      return;
    }
    if(!this.address.contains(address)) {
      this.address.add(address);
      logger.info("Successfully added address " + address.getAddressId() + " to account " + accountId);
    }
    address.setAccount(this);
  }

  public void removeAddress(Address address) {
    this.address.remove(address);
    address.setAccount(null);
  }
}
