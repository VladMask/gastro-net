package grsu.by.repository;

import grsu.by.entity.MealCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealCategoryRepository extends JpaRepository<MealCategory, Long> {
}
