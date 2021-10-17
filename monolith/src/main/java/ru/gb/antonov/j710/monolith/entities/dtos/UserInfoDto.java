package ru.gb.antonov.j710.monolith.entities.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserInfoDto
{
    private String login;
    private String email;
//--------------------------------------------------
    public UserInfoDto (String login, String email)
    {
        setLogin (login);
        setEmail (email);
    }
}
