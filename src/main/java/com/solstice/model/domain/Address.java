package com.solstice.model.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "addressId", scope = Long.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler","_links"})
public class Address {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long addressId;

  private String streetName;
  private int buildingNum;
  private String city;
  private String state;
  private int zipCode;
  private String country;

  @ManyToOne
  @JoinColumn(name = "account_id")
  private Account account;

  @Transient
  private Logger logger = LoggerFactory.getLogger(getClass());

  public Address(String streetName, int buildingNum, String city, String state,
      int zipCode, String country, Account account) {
    this.streetName = streetName;
    this.buildingNum = buildingNum;
    this.city = city;
    this.state = state;
    this.zipCode = zipCode;
    this.country = country;
    this.account = account;
  }

  public Address() {
  }

  public long getAddressId() {
    return addressId;
  }

  public void setAddressId(long addressId) {
    this.addressId = addressId;
  }

  public String getStreetName() {
    return streetName;
  }

  public void setStreetName(String streetName) {
    this.streetName = streetName;
  }

  public int getBuildingNum() {
    return buildingNum;
  }

  public void setBuildingNum(int buildingNum) {
    this.buildingNum = buildingNum;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public int getZipCode() {
    return zipCode;
  }

  public void setZipCode(int zipCode) {
    this.zipCode = zipCode;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public Account getAccount() {
    return account;
  }

  public void setAccount(Account account) {
    this.account = account;
    if(this.account == null) {
      logger.warn("Setting null value for address " + this.addressId + "'s account");
      return;
    }
    if(this.account.getAddresses() == null) {
      logger.warn("Passed account " + account.getAccountId() + " with null address list");
    }
    if(this.account.getAddresses() != null && !this.account.getAddresses().contains(this)) {
      this.account.addAddress(this);
      logger.info("Successfully added account " + account.getAccountId() + " to address " + getAddressId());
    }
  }
}
