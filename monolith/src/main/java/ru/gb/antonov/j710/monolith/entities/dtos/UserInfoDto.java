package ru.gb.antonov.j710.monolith.entities.dtos;


public class UserInfoDto
{
//    private Long ouruserId;
    private String login;
    private String email;
//--------------------------------------------------
    public UserInfoDto (){}
    public UserInfoDto (String login, String email/*, Long uid*/)
    {
        setLogin (login);
        setEmail (email);
        //setOuruserId (uid);
    }
//--------------------------------------------------
    public String getLogin ()    {    return login;    }
    public void setLogin (String login)    {    this.login = login;    }

    public String getEmail ()    {    return email;    }
    public void setEmail (String email)    {    this.email = email;    }

    //public Long getOuruserId ()    {    return ouruserId;    }
    //public void setOuruserId (Long ouruserId)    {    this.ouruserId = ouruserId;    }
}
