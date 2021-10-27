package ru.gb.antonov.j710.users.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.gb.antonov.j710.users.services.OurUserService;
import ru.gb.antonov.j710.monolith.entities.dtos.UserInfoDto;

import java.security.Principal;

@RequestMapping ("/api/v1/user_profile")
@RestController
@RequiredArgsConstructor
//@CrossOrigin ("*")
public class UserProfileController
{
    private final OurUserService ourUserService;
//--------------------------------------------------------------------------

    @GetMapping ("/userinfo")
    public UserInfoDto getUserInfo (Principal principal)
    {
        return ourUserService.getUserInfoDto (principal);
    }

    @GetMapping ("/userid/{login}")
    public Long getUserId (Principal principal, @PathVariable String login)
    {
        return ourUserService.userIdByLogin (login);
    }

/*    @GetMapping("/")
    public void defaultResponse ()
    {
        System.err.println("\ndefaultResponse\n");
        return;
    }*/
}
