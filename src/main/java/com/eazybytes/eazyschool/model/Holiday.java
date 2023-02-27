package com.eazybytes.eazyschool.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "holidays")
@Data
public class Holiday extends BaseEntity{
    @Id
    private String day;
    private String reason;

    @Enumerated(value = EnumType.STRING)
    private Type type;

    public enum Type {
        FEDERAL, FESTIVAL
    }
}
