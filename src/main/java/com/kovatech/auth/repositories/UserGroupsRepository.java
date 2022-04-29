package com.kovatech.auth.repositories;

import com.kovatech.auth.datalayer.entities.UserGroups;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserGroupsRepository extends ReactiveCrudRepository<UserGroups, Integer> {
}
