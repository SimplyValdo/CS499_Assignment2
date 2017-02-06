package com.cs499.assignment2.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Player.
 */
@Entity
@Table(name = "player")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "jersey_number")
    private Integer jerseyNumber;

    @Column(name = "position_title")
    private String positionTitle;

    @ManyToOne
    private Sport sport;

    @OneToOne
    @JoinColumn(unique = true)
    private Statistics stats;

    @OneToOne
    @JoinColumn(unique = true)
    private Quote quote;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public Player firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Player lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getJerseyNumber() {
        return jerseyNumber;
    }

    public Player jerseyNumber(Integer jerseyNumber) {
        this.jerseyNumber = jerseyNumber;
        return this;
    }

    public void setJerseyNumber(Integer jerseyNumber) {
        this.jerseyNumber = jerseyNumber;
    }

    public String getPositionTitle() {
        return positionTitle;
    }

    public Player positionTitle(String positionTitle) {
        this.positionTitle = positionTitle;
        return this;
    }

    public void setPositionTitle(String positionTitle) {
        this.positionTitle = positionTitle;
    }

    public Sport getSport() {
        return sport;
    }

    public Player sport(Sport sport) {
        this.sport = sport;
        return this;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public Statistics getStats() {
        return stats;
    }

    public Player stats(Statistics statistics) {
        this.stats = statistics;
        return this;
    }

    public void setStats(Statistics statistics) {
        this.stats = statistics;
    }

    public Quote getQuote() {
        return quote;
    }

    public Player quote(Quote quote) {
        this.quote = quote;
        return this;
    }

    public void setQuote(Quote quote) {
        this.quote = quote;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Player player = (Player) o;
        if (player.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, player.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Player{" +
            "id=" + id +
            ", firstName='" + firstName + "'" +
            ", lastName='" + lastName + "'" +
            ", jerseyNumber='" + jerseyNumber + "'" +
            ", positionTitle='" + positionTitle + "'" +
            '}';
    }
}
