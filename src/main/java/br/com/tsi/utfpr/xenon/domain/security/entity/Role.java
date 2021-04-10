package br.com.tsi.utfpr.xenon.domain.security.entity;

import br.com.tsi.utfpr.xenon.structure.dtos.RoleDTO;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table
@Data
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 10)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    public RoleDTO createDto() {
        return RoleDTO.builder()
            .id(id)
            .description(description)
            .name(name)
            .build();
    }
}
