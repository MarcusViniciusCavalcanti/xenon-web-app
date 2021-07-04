package br.com.tsi.utfpr.xenon.domain.user.entity;

import java.time.LocalDateTime;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@SuppressWarnings("ALL")
@StaticMetamodel(Car.class)
public class Car_ {

    public static volatile SingularAttribute<Car, Long> id;
    public static volatile SingularAttribute<Car, String> plate;
    public static volatile SingularAttribute<Car, String> model;
    public static volatile SingularAttribute<Car, User> user;
    public static volatile SingularAttribute<Car, LocalDateTime> lastAccess;
    public static volatile SingularAttribute<Car, LocalDateTime> createdAt;
    public static volatile SingularAttribute<Car, LocalDateTime> updatedAt;
    public static volatile SingularAttribute<Car, String> document;

}
