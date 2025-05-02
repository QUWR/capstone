package org.example.capstone.statisfaction.repository;

import org.example.capstone.statisfaction.domain.Satisfaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SatisfactionRepository extends JpaRepository<Satisfaction, Long> {
}
