package com.hecaires.rest.services;

import com.hecaires.rest.exceptions.DuplicateResourceException;
import com.hecaires.rest.exceptions.ResourceNotFoundException;
import com.hecaires.rest.models.Device;
import com.hecaires.rest.models.User;
import com.hecaires.rest.repositories.DeviceRepository;
import com.hecaires.rest.repositories.UserRepository;
import com.hecaires.rest.security.CurrentUser;
import com.hecaires.rest.security.services.UserDetailsServiceImpl;
import com.hecaires.rest.payloads.request.AddDeviceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DeviceService {
    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    UserService userService;


    public List<Device> getAllDevicesForAuthenticatedUser() {
        CurrentUser currentUser = userService.getAuthCurrentUser();
        return deviceRepository.findByUser_id(currentUser.getId());
    }

    public Device addDeviceForAuthenticatedUser(AddDeviceRequest addDeviceRequest) {
        try {
            User user = userService.getCurrentUser();

            Optional<Device> existingDeviceWithId = deviceRepository.findById(addDeviceRequest.getId());
            if (existingDeviceWithId.isEmpty()) {
                Device newDevice = new Device();
                newDevice.setId(addDeviceRequest.getId());
                newDevice.setUser(user);
                return deviceRepository.save(newDevice);
            } else {
                throw new DuplicateResourceException(String.format("Device with id=%s already exists!", addDeviceRequest.getId()));
            }
        } catch (ResourceNotFoundException ex) {
            throw new ResourceNotFoundException("Associated user not found!");
        }
    }

    public String generateUniqueDeviceId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}