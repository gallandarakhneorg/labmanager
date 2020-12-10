package com.spring.rest.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

public class AuthorshipPK implements Serializable {

    private static final long serialVersionUID = -1083342117826188053L;

    @Id
    @ManyToOne
    private Publication pub;

    @Id
    @ManyToOne
    private Author aut;

    public Publication getPub() {
        return pub;
    }

    public void setPub(Publication pub) {
        this.pub = pub;
    }

    public Author getAut() {
        return aut;
    }

    public void setAut(Author aut) {
        this.aut = aut;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorshipPK that = (AuthorshipPK) o;
        return Objects.equals(pub, that.pub) &&
                Objects.equals(aut, that.aut);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pub, aut);
    }
}


