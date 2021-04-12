package br.com.tsi.utfpr.xenon.structure.data;

import org.springframework.data.jpa.domain.Specification;

public interface BasicSpecification<T, U> {

    Specification<T> filterBy(U object);

}

