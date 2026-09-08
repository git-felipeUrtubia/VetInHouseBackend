package org.example.backend_vet_in_house.users.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.backend_vet_in_house.shared.exception.user.UserNotFoundException;
import org.example.backend_vet_in_house.users.dto.req.PhoneReqDTO;
import org.example.backend_vet_in_house.users.dto.res.PhoneResDTO;
import org.example.backend_vet_in_house.users.model.UserEntity;
import org.example.backend_vet_in_house.users.model.UserPhoneEntity;
import org.example.backend_vet_in_house.users.repository.UserEntityRepository;
import org.example.backend_vet_in_house.users.repository.UserPhoneEntityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserPhoneEntityService {

    private final UserEntityRepository userEntityRepository;
    private final UserPhoneEntityRepository userPhoneEntityRepository;

    @Transactional
    public PhoneResDTO addPhone(String username, PhoneReqDTO req) {
        UserEntity user = userEntityRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Usuario " + username + " not found"));

        if (user.getPhones().size() >= 3) {
            throw new IllegalStateException("Límite máximo de 3 números alcanzado.");
        }

        UserPhoneEntity phoneEntity = new UserPhoneEntity();
        phoneEntity.setPhoneNumber(req.phoneNumber());
        phoneEntity.setUser(user);

        UserPhoneEntity savedPhone = userPhoneEntityRepository.save(phoneEntity);

        return new PhoneResDTO(savedPhone.getId(), savedPhone.getPhoneNumber());
    }

    @Transactional
    public List<PhoneResDTO> findPhoneByUser(String username) {

        UserEntity user = userEntityRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Usuario " + username + " not found"));

        List<UserPhoneEntity> phones = userPhoneEntityRepository.findAll().stream()
                .filter(up -> up.getUser().getUserId().equals(user.getUserId()))
                .toList();

        return phones.stream().map(up -> new PhoneResDTO(
                up.getId(),
                up.getPhoneNumber()
        )).toList();
    }

    public void deletePhoneById(String username, Long id) {

        userEntityRepository.findUserByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Usuario " + username + " not found"));

        userPhoneEntityRepository.deleteById(id);
    }

}
