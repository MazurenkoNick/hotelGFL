package com.example.hotelgfl.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "administrators")
@NoArgsConstructor
@Getter
@Setter
public class Administrator extends User {

    @Enumerated(EnumType.STRING)
    @Column(name = "admin_rank", nullable = false)
    private Rank rank;

    @Column(name = "salary")
    private double salary;

    @Pattern(regexp = "(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}",
            message = "Must be minimum 6 characters, at least one letter and one number")
    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "administrator", orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    private List<Reservation> reservations;

    @ManyToMany
    @JoinTable(
            name = "admin_roles",
            joinColumns = @JoinColumn(name = "administrator_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;

    public Administrator(String firstName, String lastName, String passportId, String email,String phoneNumber,
                         Rank rank, double salary, String password, List<Reservation> reservations) {

        super(firstName, lastName, passportId, email, phoneNumber);
        this.rank = rank;
        this.salary = salary;
        this.password = password;
        this.reservations = reservations;
    }

    public Administrator(String firstName, String lastName, String passportId, String email,
                         String phoneNumber, Rank rank, double salary, String password) {

        super(firstName, lastName, passportId, email, phoneNumber);
        this.rank = rank;
        this.salary = salary;
        this.password = password;
    }

    public void addReservation(Reservation reservation) {
        Administrator currentAdmin = reservation.getAdministrator();
        if (currentAdmin != null && currentAdmin != this) {
            // todo: add custom exception
            throw new IllegalArgumentException("Reservation already has an administrator!");
        }
        reservation.setAdministrator(this);
        reservations.add(reservation);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Administrator that = (Administrator) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
