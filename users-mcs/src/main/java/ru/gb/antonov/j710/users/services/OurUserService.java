package ru.gb.antonov.j710.users.services;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gb.antonov.j710.monolith.beans.errorhandlers.UserNotFoundException;
import ru.gb.antonov.j710.users.repositos.OurUserRepo;
import ru.gb.antonov.j710.users.entities.OurPermission;
import ru.gb.antonov.j710.users.entities.OurUser;
import ru.gb.antonov.j710.users.entities.Role;
import ru.gb.antonov.j710.monolith.entities.dtos.UserInfoDto;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.gb.antonov.j710.monolith.Factory.STR_EMPTY;

@Service
@RequiredArgsConstructor
public class OurUserService implements UserDetailsService
{
    private final OurUserRepo          ourUserRepo;
    private final RoleService          roleService;
    private final OurPermissionService ourPermissionService;
    public static final String NO_SUCH_LOGIN_ = "Логин не зарегистрирован: ";
    public static final String NO_SUCH_UID_   = "ID не зарегистрирован: ";
//-----------------------------------------------------------------------------------
//TODO: если юзера можно будет удалять из БД, то нужно не забыть удалить и его корзину из Memurai.

/** @throws UserNotFoundException */
    public OurUser userByPrincipal (Principal principal)
    {
        String login = (principal != null) ? principal.getName() : STR_EMPTY;
        return findByLogin (login).orElseThrow (()->new UserNotFoundException (NO_SUCH_LOGIN_ + login));
    }

    @Transactional
    public Long getUserId (Principal principal)
    {   return userByPrincipal (principal).getId();
    }

    @Transactional
    public Long userIdByLogin (@NotNull String login)
    {   return findByLogin (login).orElseThrow (()->new UserNotFoundException (NO_SUCH_LOGIN_ + login))
                                  .getId();
    }

    @Transactional
    public @NotNull String userNameById (Long uid)
    {
        String username = STR_EMPTY;
        if (uid != null)
            username = ourUserRepo.findById (uid)
                                  .orElseThrow(()->new UserNotFoundException (NO_SUCH_UID_ + uid))
                                  .getLogin();
        return username;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername (String login)
    {
        OurUser ourUser = findByLogin(login).orElseThrow(()->new UsernameNotFoundException (NO_SUCH_LOGIN_+login));

        return new User(ourUser.getLogin(),
                        ourUser.getPassword(),
                        mapRolesToAuthorities (ourUser.getRoles(), ourUser.getOurPermissions()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities (
                                        Collection<Role> roles,
                                        Collection<OurPermission> permissions)
    {   List<String> list =
        roles.stream()
             .map (Role::getName)
             .collect (Collectors.toList());

        list.addAll (permissions.stream()
                                .map (OurPermission::getName)
                                .collect (Collectors.toList()));
        return list.stream()
                   .map (SimpleGrantedAuthority::new)
                   .collect (Collectors.toList());
    }

    @Transactional
    public Optional<OurUser> createNewOurUser (String login, String password, String email)
    {
        OurUser dummyUser = OurUser.dummyOurUser (login, password, email);
        Role role = roleService.getRoleUser();
        OurPermission ourPermission = ourPermissionService.getPermissionDefault();

        OurUser saved = ourUserRepo.save (dummyUser);
        saved.addRole (role);
        saved.addPermission (ourPermission);
        return Optional.of (saved);
    }

    public Optional<OurUser> findByLogin (String login)
    {   return ourUserRepo.findByLogin (login);
    }

    @Transactional
    public UserInfoDto getUserInfoDto (Principal principal)
    {
        OurUser u = userByPrincipal (principal);
        return new UserInfoDto (u.getLogin(), u.getEmail());
    }

/** Редактировать информацию о товарах могут только те пользователи, у которых есть
разрешение {@code PERMISSION_EDIT_PRODUCT}. */
    @Transactional
    public Boolean canEditProduct (Principal principal)
    {
        OurUser ourUser = userByPrincipal (principal);
        Collection<OurPermission> permissions = ourUser.getOurPermissions();
        return permissions.contains (ourPermissionService.getPermissionEditProducts());
    }
}
