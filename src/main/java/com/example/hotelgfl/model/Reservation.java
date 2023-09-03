package com.example.hotelgfl.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "reservations")
@NoArgsConstructor
@Getter
@Setter
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "from_datetime", nullable = false)
    private LocalDateTime fromDateTime;

    @Column(name = "to_datetime", nullable = false)
    private LocalDateTime toDateTime;

    @ManyToOne
    @JoinColumn(name = "administrator_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Administrator administrator;

    @ManyToOne
    @JoinColumn(name = "room_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Room room;

    @ManyToOne
    @JoinColumn(name = "renter_id")
    private Renter renter;

    @OneToOne(mappedBy = "reservation", orphanRemoval = true, cascade = CascadeType.ALL)
    private Receipt receipt;

    public Reservation(LocalDateTime fromDateTime, LocalDateTime toDateTime, Administrator administrator, Room room, Renter renter, Receipt receipt) {
        this.fromDateTime = fromDateTime;
        this.toDateTime = toDateTime;
        this.administrator = administrator;
        this.room = room;
        this.renter = renter;
        this.receipt = receipt;
    }

    public Reservation(LocalDateTime fromDateTime, LocalDateTime toDateTime, Administrator administrator, Room room, Renter renter) {
        this.fromDateTime = fromDateTime;
        this.toDateTime = toDateTime;
        this.administrator = administrator;
        this.room = room;
        this.renter = renter;
    }

    public void createReceipt(double totalPrice) {
        if (receipt != null) {
            throw new IllegalArgumentException("Receipt has already been added for this reservation");
        }
        Receipt receipt = new Receipt(this, totalPrice);
        setReceipt(receipt);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Reservation that = (Reservation) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
