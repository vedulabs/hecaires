package com.hecaires.rest.repositories;

import com.hecaires.rest.models.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, String> {
    Optional<Device> findById(String device_id);
    List<Device> findByUser_id(String user_id);
}