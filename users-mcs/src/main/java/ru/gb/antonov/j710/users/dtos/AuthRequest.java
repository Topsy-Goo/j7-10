package ru.gb.antonov.j710.users.dtos;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;

import static ru.gb.antonov.j710.monolith.Factory.*;

public class AuthRequest
{
    @NotNull (message="\rНе указан логин!")
    @Length (min=LOGIN_LEN_MIN, max=LOGIN_LEN_MAX, message="\rДлина логина: 3…32 символов!")
    private String login;

    @NotNull (message="\rНе указан пароль!")
    @Length (min=PASS_LEN_MIN, max=PASS_LEN_MAX, message="\rДлина пароля: 3…128 символов!")
    private String password;
//--------------------------------------------------------------------------------
    public AuthRequest (){}
//--------------------------------------------------------------------------------
    public String getLogin ()           { return login; }
    public void setLogin (String value) { login = value; }

    public String getPassword ()           { return password; }
    public void setPassword (String value) { password = value; }
//--------------------------------------------------------------------------------
    public static AuthRequest dummyAuthRequest (String login, String password)
    {
        AuthRequest ar = new AuthRequest();
        ar.login = login;
        ar.password = password;
        return ar;
    }
}
