package com.gbsb.routie_server.repository;

import com.gbsb.routie_server.entity.CharacterStyle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CharacterRepository extends JpaRepository<CharacterStyle, Long> {
    Optional<CharacterStyle> findByUserId(String userId);
}
