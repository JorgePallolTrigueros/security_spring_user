package com.clases.security.usuarios.dao.repository;

import com.clases.security.usuarios.dao.entity.ActorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ActorRepository  extends JpaRepository<ActorEntity, Long> {
}
