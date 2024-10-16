package com.example.prac.service.data;

import com.example.prac.DTO.data.DragonDTO;
import com.example.prac.mappers.Mapper;
import com.example.prac.model.dataEntity.Dragon;
import com.example.prac.repository.data.DragonRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;


@Service
@AllArgsConstructor
public class DragonService {
    private final DragonRepository dragonRepository;
    private final Mapper<Dragon, DragonDTO> dragonMapper;


    public DragonDTO save(DragonDTO dragonDTO) {
        Dragon dragon = dragonMapper.mapFrom(dragonDTO);
        return dragonMapper.mapTo(dragonRepository.save(dragon));
    }

    public List<DragonDTO> findAllDragons() {
        return StreamSupport.stream(dragonRepository.findAll().spliterator(), false)
                .map(dragonMapper::mapTo).toList();
    }

    public Optional<DragonDTO> findById(Long dragonId) {
        Optional<Dragon> optionalDragon = dragonRepository.findById(dragonId);
        return optionalDragon.map(dragonMapper::mapTo);
    }

    public boolean isExists(Long dragonId) {
        return dragonRepository.existsById(dragonId);
    }

    public DragonDTO partialUpdate(Long dragonId, DragonDTO dragonDTO) {
        dragonDTO.setId(dragonId);
        return dragonRepository.findById(dragonId).map(existingDragon -> {
            DragonDTO existingDragonDto = dragonMapper.mapTo(existingDragon);
            Optional.ofNullable(dragonDTO.getName()).ifPresent(existingDragonDto::setName);
            Optional.ofNullable(dragonDTO.getCoordinates()).ifPresent(existingDragonDto::setCoordinates);
            Optional.ofNullable(dragonDTO.getCave()).ifPresent(existingDragonDto::setCave);
            Optional.ofNullable(dragonDTO.getKiller()).ifPresent(existingDragonDto::setKiller);
            Optional.ofNullable(dragonDTO.getAge()).ifPresent(existingDragonDto::setAge);
            Optional.ofNullable(dragonDTO.getColor()).ifPresent(existingDragonDto::setColor);
            Optional.ofNullable(dragonDTO.getType()).ifPresent(existingDragonDto::setType);
            Optional.ofNullable(dragonDTO.getCharacter()).ifPresent(existingDragonDto::setCharacter);
            Optional.ofNullable(dragonDTO.getHead()).ifPresent(existingDragonDto::setHead);
            return dragonMapper.mapTo(dragonRepository.save(dragonMapper.mapFrom(existingDragonDto)));
        }).orElseThrow(() -> new RuntimeException("Dragon doesn't exist"));
    }

    public void delete(Long dragonId) {
        dragonRepository.deleteById(dragonId);
    }
}