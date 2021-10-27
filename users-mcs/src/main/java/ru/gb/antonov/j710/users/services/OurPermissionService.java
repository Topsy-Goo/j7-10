package ru.gb.antonov.j710.users.services;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.gb.antonov.j710.monolith.beans.errorhandlers.UnableToPerformException;
import ru.gb.antonov.j710.users.repositos.OurPermissionRepo;
import ru.gb.antonov.j710.users.entities.OurPermission;

import java.util.Optional;

import static ru.gb.antonov.j710.monolith.Factory.PERMISSION_EDIT_PRODUCT;
import static ru.gb.antonov.j710.monolith.Factory.PERMISSION_SHOPPING;

@Service
@RequiredArgsConstructor
public class OurPermissionService
{
    protected final static String PERMISSION_ABSENT = "Разрешение отсутствует.";
    private final OurPermissionRepo ourPermissionRepo;


    public Optional<OurPermission> findByName (String permissionName)
    {
        if (permissionName == null || permissionName.trim().isEmpty())
        {
            return Optional.empty();
        }
        return ourPermissionRepo.findByName (permissionName);
    }

/** @throws UnableToPerformException */
    @NotNull public OurPermission getPermissionEditProducts ()
    {
        return findByName (PERMISSION_EDIT_PRODUCT).orElseThrow (()->new UnableToPerformException (PERMISSION_ABSENT));
    }

/** @throws UnableToPerformException*/
    @NotNull public OurPermission getPermissionDefault ()
    {
        return findByName (PERMISSION_SHOPPING).orElseThrow (()->new UnableToPerformException (PERMISSION_ABSENT));
    }
}
