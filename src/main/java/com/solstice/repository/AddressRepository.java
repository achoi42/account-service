package com.solstice.repository;

import com.solstice.model.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

  @Modifying
  @Query("UPDATE Address a SET "
      + "a.streetName = :streetName, "
      + "a.buildingNum = :buildingNum, "
      + "a.city = :city, "
      + "a.state = :state, "
      + "a.zipCode = :zipCode, "
      + "a.country = :country "
      + "WHERE a.addressId = :addressId")
  void updateAddress(
      @Param("streetName") String streetName,
      @Param("buildingNum") int buildingNum,
      @Param("city") String city,
      @Param("state") String state,
      @Param("zipCode") int zipCode,
      @Param("country") String country,
      @Param("addressId") long addressId);
}
