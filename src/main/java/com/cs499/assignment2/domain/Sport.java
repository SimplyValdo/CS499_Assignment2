package com.cs499.assignment2.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Sport.
 */
@Entity
@Table(name = "sport")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Sport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sport_name")
    private String sportName;

    @OneToMany(mappedBy = "sport")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Coach> typeofCoaches = new HashSet<>();

    @OneToMany(mappedBy = "sport")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Player> names = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSportName() {
        return sportName;
    }

    public Sport sportName(String sportName) {
        this.sportName = sportName;
        return this;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public Set<Coach> getTypeofCoaches() {
        return typeofCoaches;
    }

    public Sport typeofCoaches(Set<Coach> coaches) {
        this.typeofCoaches = coaches;
        return this;
    }

    public Sport addTypeofCoach(Coach coach) {
        this.typeofCoaches.add(coach);
        coach.setSport(this);
        return this;
    }

    public Sport removeTypeofCoach(Coach coach) {
        this.typeofCoaches.remove(coach);
        coach.setSport(null);
        return this;
    }

    public void setTypeofCoaches(Set<Coach> coaches) {
        this.typeofCoaches = coaches;
    }

    public Set<Player> getNames() {
        return names;
    }

    public Sport names(Set<Player> players) {
        this.names = players;
        return this;
    }

    public Sport addName(Player player) {
        this.names.add(player);
        player.setSport(this);
        return this;
    }

    public Sport removeName(Player player) {
        this.names.remove(player);
        player.setSport(null);
        return this;
    }

    public void setNames(Set<Player> players) {
        this.names = players;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sport sport = (Sport) o;
        if (sport.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, sport.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Sport{" +
            "id=" + id +
            ", sportName='" + sportName + "'" +
            '}';
    }
}
