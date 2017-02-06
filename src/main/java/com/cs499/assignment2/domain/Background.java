package com.cs499.assignment2.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Background.
 */
@Entity
@Table(name = "background")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Background implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "language")
    private String language;

    @Column(name = "hometown")
    private String hometown;

    @Column(name = "years_in_current_position")
    private Integer yearsInCurrentPosition;

    @Column(name = "total_championships")
    private Integer totalChampionships;

    @OneToOne
    @JoinColumn(unique = true)
    private Coach coach;

    @OneToOne
    @JoinColumn(unique = true)
    private Player player;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLanguage() {
        return language;
    }

    public Background language(String language) {
        this.language = language;
        return this;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getHometown() {
        return hometown;
    }

    public Background hometown(String hometown) {
        this.hometown = hometown;
        return this;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public Integer getYearsInCurrentPosition() {
        return yearsInCurrentPosition;
    }

    public Background yearsInCurrentPosition(Integer yearsInCurrentPosition) {
        this.yearsInCurrentPosition = yearsInCurrentPosition;
        return this;
    }

    public void setYearsInCurrentPosition(Integer yearsInCurrentPosition) {
        this.yearsInCurrentPosition = yearsInCurrentPosition;
    }

    public Integer getTotalChampionships() {
        return totalChampionships;
    }

    public Background totalChampionships(Integer totalChampionships) {
        this.totalChampionships = totalChampionships;
        return this;
    }

    public void setTotalChampionships(Integer totalChampionships) {
        this.totalChampionships = totalChampionships;
    }

    public Coach getCoach() {
        return coach;
    }

    public Background coach(Coach coach) {
        this.coach = coach;
        return this;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }

    public Player getPlayer() {
        return player;
    }

    public Background player(Player player) {
        this.player = player;
        return this;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Background background = (Background) o;
        if (background.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, background.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Background{" +
            "id=" + id +
            ", language='" + language + "'" +
            ", hometown='" + hometown + "'" +
            ", yearsInCurrentPosition='" + yearsInCurrentPosition + "'" +
            ", totalChampionships='" + totalChampionships + "'" +
            '}';
    }
}
