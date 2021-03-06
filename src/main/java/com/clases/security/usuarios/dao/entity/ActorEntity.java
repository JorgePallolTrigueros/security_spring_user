package com.clases.security.usuarios.dao.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="actor")
public class ActorEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private Long id;

    @Column(length =50, name="nombre")
    private String name;

    @Column(length =5660, name="image")
    private String image;

    @Column(length =5660, name="biography")
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ActorEntity(String name, String image, String description, String s) {

        this.name = name;
        this.image = image;
        this.description = description;
    }


    public ActorEntity() {
    }


    @Override
    public String toString() {
        return "ActorEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", imagen='" + image + '\'' +
                ", description='" + description + '\'' +
                '}';
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ActorEntity)) {
            return false;
        }
        ActorEntity other = (ActorEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }


}
