package me.redstoner2019.bingobackend.bingoPreset;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BingoPresetJpaRepository extends JpaRepository<BingoPreset, String> {

    Optional<BingoPreset> findById(String id);
    //Optional<List<BingoPreset>> findAllByPublicIs(boolean isPublic);
    List<BingoPreset> findAll();
}
