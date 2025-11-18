package com.mycompany.repository;

import com.mycompany.model.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPreferenceRepository extends JpaRepository<UserPreference, Integer> {
    List<UserPreference> findByUsername(String username);
}
