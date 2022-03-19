package by.teachmeskills.homework.entity;

import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@ToString
public abstract class Entity implements Serializable {
    protected static final long serialVersionUID = 1L;

    protected Integer id;
}