package com.cs499.assignment2.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Statistics.
 */
@Entity
@Table(name = "statistics")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Statistics implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "wins")
    private Integer wins;

    @Column(name = "losses")
    private Integer losses;

    @Column(name = "draws")
    private Integer draws;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getWins() {
        return wins;
    }

    public Statistics wins(Integer wins) {
        this.wins = wins;
        return this;
    }

    public void setWins(Integer wins) {
        this.wins = wins;
    }

    public Integer getLosses() {
        return losses;
    }

    public Statistics losses(Integer losses) {
        this.losses = losses;
        return this;
    }

    public void setLosses(Integer losses) {
        this.losses = losses;
    }

    public Integer getDraws() {
        return draws;
    }

    public Statistics draws(Integer draws) {
        this.draws = draws;
        return this;
    }

    public void setDraws(Integer draws) {
        this.draws = draws;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Statistics statistics = (Statistics) o;
        if (statistics.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, statistics.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Statistics{" +
            "id=" + id +
            ", wins='" + wins + "'" +
            ", losses='" + losses + "'" +
            ", draws='" + draws + "'" +
            '}';
    }
}
