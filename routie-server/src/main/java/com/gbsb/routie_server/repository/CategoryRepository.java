package com.gbsb.routie_server.repository;

import com.gbsb.routie_server.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
