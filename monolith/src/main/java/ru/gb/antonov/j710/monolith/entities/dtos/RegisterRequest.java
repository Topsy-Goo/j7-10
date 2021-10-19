package ru.gb.antonov.j710.monolith.entities.dtos;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

import static ru.gb.antonov.j710.monolith.Factory.*;

public class RegisterRequest
{
    @NotNull (message="\rЗадайте логин (3…32 латинских сиволов и/или цифр).")
    @Length (min= LOGIN_LEN_MIN, max=LOGIN_LEN_MAX, message="\rДлина логина — 3…36 латинских символов и/или цифр.")
    private String login;

    @NotNull (message="\rЗадайте пароль (3…128 символов).")
    @Length (min=PASS_LEN_MIN, max=PASS_LEN_MAX, message="\rДлина пароля — 3…128 символов.")
    private String password;

    /*  Можно сделать два пароля при регистрации : пароль и подтверждение, -- и проверять,
    чтобы они совпадали.
    */

    @NotNull (message="\rПочта указана неполностью: Имя пользователя.")
    @Length (min=1/*, message=""*/)
    private String emailUser;

    @NotNull (message="\rПочта указана неполностью: Имя сервера.")
    @Length (min=1/*, message=""*/)
    private String emailServer;

    @NotNull (message="\rПочта указана неполностью: Домен.")
    @Length (min=1/*, message=""*/)
    private String emailDomain;
//-------------------------------------------------------------------------
    public RegisterRequest(){}
//-------------------------------------------------------------------------

    public String getLogin ()    {    return login;    }

    public void setLogin (String login)    {    this.login = login;    }

    public String getPassword ()    {    return password;    }

    public void setPassword (String password)    {    this.password = password;    }

    public String getEmailUser ()    {    return emailUser;    }

    public void setEmailUser (String emailUser)    {    this.emailUser = emailUser;    }

    public String getEmailServer ()    {    return emailServer;    }

    public void setEmailServer (String emailServer)    {    this.emailServer = emailServer;    }

    public String getEmailDomain ()    {    return emailDomain;    }

    public void setEmailDomain (String emailDomain)    {    this.emailDomain = emailDomain;    }
}
