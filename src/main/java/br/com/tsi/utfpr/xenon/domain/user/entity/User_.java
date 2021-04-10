package br.com.tsi.utfpr.xenon.domain.user.entity;


import br.com.tsi.utfpr.xenon.domain.security.entity.AccessCard;
import java.time.LocalDate;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(User.class)
public class User_ {
    public static volatile SingularAttribute<User, Long> id;
    public static volatile SingularAttribute<User, String> name;
    public static volatile SingularAttribute<User, TypeUser> typeUser;
    public static volatile SingularAttribute<User, AccessCard> accessCard;
    public static volatile SingularAttribute<User, Car> car;
    public static volatile SingularAttribute<User, Boolean> authorisedAccess;
    public static volatile SingularAttribute<User, Integer> numberAccess;
    public static volatile SingularAttribute<User, LocalDate> updatedAt;
    public static volatile SingularAttribute<User, LocalDate> createdAt;
}
