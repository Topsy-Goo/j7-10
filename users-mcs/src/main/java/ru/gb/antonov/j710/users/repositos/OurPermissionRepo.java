package ru.gb.antonov.j710.users.repositos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.gb.antonov.j710.users.entities.OurPermission;

import java.util.Optional;

@Repository
public interface OurPermissionRepo extends CrudRepository<OurPermission, Integer>
{
    Optional<OurPermission> findByName (String name);
}
