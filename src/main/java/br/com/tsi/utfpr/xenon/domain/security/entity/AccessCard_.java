package br.com.tsi.utfpr.xenon.domain.security.entity;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(AccessCard.class)
public class AccessCard_ {

    public static volatile SingularAttribute<AccessCard, Long> id;
    public static volatile SingularAttribute<AccessCard, String> username;
    public static volatile SingularAttribute<AccessCard, String> password;
    public static volatile SingularAttribute<AccessCard, Boolean> enabled;
    public static volatile SingularAttribute<AccessCard, Boolean> accountNonExpired;
    public static volatile SingularAttribute<AccessCard, Boolean> accountNonLocked;
    public static volatile SingularAttribute<AccessCard, Boolean> credentialsNonExpired;
    public static volatile ListAttribute<AccessCard, Role> roles;
}
