package ru.gb.antonov.j710.users.services;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.gb.antonov.j710.monolith.beans.errorhandlers.UnableToPerformException;
import ru.gb.antonov.j710.users.repositos.RoleRepo;
import ru.gb.antonov.j710.users.entities.Role;

import java.util.Optional;

import static ru.gb.antonov.j710.monolith.Factory.*;

@Service
@RequiredArgsConstructor
public class RoleService
{
    protected final static String ROLE_ABSENT = "Роль отсутствует.";
    private final RoleRepo roleRepo;

    public Optional<Role> findByName (String roleName)
    {
        if (roleName == null || roleName.trim ().isEmpty ())
            return Optional.empty ();
        return roleRepo.findByName (roleName);
    }

/** @throws UnableToPerformException */
    @NotNull public Role getRoleUser ()
    {   return findByName (ROLE_USER).orElseThrow (()->new UnableToPerformException (ROLE_ABSENT));
    }

/** @throws UnableToPerformException */
    @NotNull public Role getRoleAdmin ()
    {   return findByName (ROLE_ADMIN).orElseThrow (()->new UnableToPerformException (ROLE_ABSENT));
    }

/** @throws UnableToPerformException */
    @NotNull public Role getRoleSuperAdmin ()
    {   return findByName (ROLE_SUPERADMIN).orElseThrow (()->new UnableToPerformException (ROLE_ABSENT));
    }

/** @throws UnableToPerformException */
    @NotNull public Role getRoleManager ()
    {   return findByName (ROLE_MANAGER).orElseThrow (()->new UnableToPerformException (ROLE_ABSENT));
    }
}
