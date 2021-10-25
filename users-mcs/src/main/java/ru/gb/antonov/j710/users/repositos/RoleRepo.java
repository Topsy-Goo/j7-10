package ru.gb.antonov.j710.users.repositos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.gb.antonov.j710.users.entities.Role;

import java.util.Optional;

@Repository
public interface RoleRepo extends CrudRepository<Role, Integer>
{
    Optional<Role> findByName (String name);
}
