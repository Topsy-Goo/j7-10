package ru.gb.antonov.j710.monolith.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.gb.antonov.j710.monolith.beans.errorhandlers.UserCreatingException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;

import static ru.gb.antonov.j710.monolith.Factory.*;

@Entity
@Table (name="ourusers")
@NoArgsConstructor
public class OurUser
{
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)    @Getter
    @Column (name="id")
    private Long id;

    @Column(name="login", nullable=false, unique=true)    @Getter
    private String login;

    @Column(name="password", nullable=false)    @Getter
    private String password;

    @Column(name="email", nullable=false, unique=true)    @Getter
    private String email;

    @CreationTimestamp
    @Column(name="created_at", nullable=false)    @Getter @Setter
    private LocalDateTime createdAt;

    @CreationTimestamp
    @Column(name="updated_at", nullable=false)    @Getter @Setter
    private LocalDateTime updatedAt;
//--------------неколонки
    @ManyToMany
    @JoinTable (name="ourusers_roles",
                joinColumns        = @JoinColumn (name="ouruser_id"),
                inverseJoinColumns = @JoinColumn (name="role_id"))    @Getter @Setter
    private Collection<Role> roles;
/*  Роли и разрешения не стоит убирать из класса т.к. при создании юзера они должны сохарняться одновременно с ним.   */
    @ManyToMany
    @JoinTable (name="ourusers_ourpermissions",
                joinColumns        = @JoinColumn (name="ouruser_id"),
                inverseJoinColumns = @JoinColumn (name="ourpermission_id"))    @Getter @Setter
    private Collection<OurPermission> ourPermissions;
//------------------------ Конструкторы -------------------------------------

    public static OurUser dummyOurUser (String login, String password, String email)
    {
        OurUser u = new OurUser();
        if (!u.setLogin (login) || !u.setPass (password) || !u.setEmail (email))
        {
            throw new UserCreatingException (String.format (
                "\rНедопустимый набор значений:\r    логин = %s,\r    пароль = %s,\r    почта = %s",
                login, password, email));
        }
        u.roles          = new HashSet<>();
        u.ourPermissions = new HashSet<>();
        return u;
    }
//----------------------- Геттеры и сеттеры ---------------------------------

    private void setId (Long id) {   this.id = id;   }
    private void setPassword (String password) {   this.password = password;   }

    private boolean setLogin (String login)
    {
        String s = validateString (login, LOGIN_LEN_MIN, LOGIN_LEN_MAX);
        boolean ok = s != null;
        if (ok)
            this.login = s;
        return ok;
    }

    private boolean setEmail (String email)
    {
        String s = validateString (email, EMAIL_LEN_MIN, EMAIL_LEN_MAX);
        boolean ok = s != null && hasEmailFormat (email);
        if (ok)
            this.email = s;
        return ok;
    }
//----------------------- Аутентификация ------------------------------------
/*  Отдельный метод для установки пароля вручную, чтобы иметь возможность сообщать юзеру о некорректно
заданном пароле и при этом выводить в сообщении пароль, а не хэш пароля.
*/
    private boolean setPass (String password)
    {
        String s = validateString (password, PASS_LEN_MIN, PASS_LEN_MAX);
        boolean ok = s != null;
        if (ok)
            setPassword (new BCryptPasswordEncoder().encode(s));
        return ok;
    }

    public boolean addRole (Role role) { return (role != null) && roles.add (role); }

    public boolean addPermission (OurPermission permission)
    {   return (permission != null) && ourPermissions.add (permission);
    }
//--------------------- Другие методы ---------------------------------------

    @Override public String toString()
    {   return String.format("OurUser:[id:%d, login:%s, email:%s].", id, login, email);
    }
}
