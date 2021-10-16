package ru.gb.antonov.j710.monolith.beans.repositos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.gb.antonov.j710.monolith.entities.OurUser;

import java.util.Optional;

@Repository
public interface OurUserRepo extends CrudRepository <OurUser, Long>
{
    Optional<OurUser> findByLogin (String login);
}
