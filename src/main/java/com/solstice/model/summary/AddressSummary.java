package com.solstice.model.summary;

public class AddressSummary {

  private long addressId;
  private String streetName;
  private int buildingNum;
  private String city;
  private String state;
  private int zipCode;
  private String country;
  private long accountId;

  public AddressSummary() {
  }

  public AddressSummary(long addressId, String streetName, int buildingNum, String city,
      String state, int zipCode, String country, long accountId) {
    this.addressId = addressId;
    this.streetName = streetName;
    this.buildingNum = buildingNum;
    this.city = city;
    this.state = state;
    this.zipCode = zipCode;
    this.country = country;
    this.accountId = accountId;
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

  public long getAccountId() {
    return accountId;
  }

  public void setAccountId(long accountId) {
    this.accountId = accountId;
  }
}
