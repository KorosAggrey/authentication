package com.kovatech.auth.repositories;

import com.kovatech.auth.datalayer.entities.Groups;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface GroupsRepository extends ReactiveCrudRepository<Groups,Integer> {
}
