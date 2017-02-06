package com.cs499.assignment2.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Quote.
 */
@Entity
@Table(name = "quote")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Quote implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phrase")
    private String phrase;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhrase() {
        return phrase;
    }

    public Quote phrase(String phrase) {
        this.phrase = phrase;
        return this;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Quote quote = (Quote) o;
        if (quote.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, quote.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Quote{" +
            "id=" + id +
            ", phrase='" + phrase + "'" +
            '}';
    }
}
