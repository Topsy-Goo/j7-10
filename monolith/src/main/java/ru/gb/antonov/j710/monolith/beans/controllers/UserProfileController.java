package ru.gb.antonov.j710.monolith.beans.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gb.antonov.j710.monolith.beans.services.OurUserService;
import ru.gb.antonov.j710.monolith.entities.dtos.UserInfoDto;

import java.security.Principal;

@RequestMapping ("/api/v1/user_profile")
@RestController
@RequiredArgsConstructor
public class UserProfileController
{
    private final OurUserService ourUserService;
//--------------------------------------------------------------------------

    @GetMapping ("/userinfo")
    public UserInfoDto getUserInfo (Principal principal)
    {
        return ourUserService.getUserInfoDto (principal);
    }

    @GetMapping ("/userid")
    public Long getUserId (Principal principal)
    {
        return ourUserService.getUserId (principal);
    }
}
