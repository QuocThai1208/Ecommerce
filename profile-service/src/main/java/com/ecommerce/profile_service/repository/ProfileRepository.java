package com.ecommerce.profile_service.repository;

import com.ecommerce.profile_service.entity.Profile;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProfileRepository extends Neo4jRepository<Profile, String> {
}