package com.example.hotelgfl.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "renters")
@NoArgsConstructor
@Getter
@Setter
public class Renter extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToMany(mappedBy = "renter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Discount> discounts;

    @OneToMany(mappedBy = "renter", cascade = CascadeType.PERSIST)
    private List<Reservation> reservations;

    public Renter(String firstName, String lastName, String passportId, String email,
                  String phoneNumber, List<Discount> discounts, List<Reservation> reservations) {

        super(firstName, lastName, passportId, email, phoneNumber);
        this.discounts = discounts;
        this.reservations = reservations;
    }

    public Renter(String firstName, String lastName, String passportId, String email, String phoneNumber) {
        super(firstName, lastName, passportId, email, phoneNumber);
    }

    public void addReservation(Reservation reservation) {
        Renter currentRenter = reservation.getRenter();
        assertValidCurrentRenter(currentRenter);
        reservation.setRenter(this);
        reservations.add(reservation);
    }

    public void addDiscount(Discount discount) {
        Renter currentRenter = discount.getRenter();
        assertValidCurrentRenter(currentRenter);
        discount.setRenter(this);
        discounts.add(discount);
    }

    private void assertValidCurrentRenter(Renter currentRenter) {
        if (currentRenter != null && currentRenter != this) {
            // todo: add custom exception
            throw new IllegalArgumentException("Reservation already has a renter!");
        }
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Renter renter = (Renter) o;
        return getId() != null && Objects.equals(getId(), renter.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
