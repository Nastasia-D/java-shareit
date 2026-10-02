package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = :bookerId " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByBooker(@Param("bookerId") Long bookerId);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = :bookerId " +
            "AND b.start > :start " +
            "ORDER BY b.start DESC")
    List<Booking> findFutureByBooker(@Param("bookerId") Long bookerId, @Param("start") LocalDateTime start);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = :bookerId " +
            "AND b.start < :start " +
            "AND b.end > :end " +
            "ORDER BY b.start DESC")
    List<Booking> findCurrentBookings(@Param("bookerId") Long bookerId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = :bookerId " +
            "AND b.end < :end " +
            "ORDER BY b.start DESC")
    List<Booking> findPastBookings(@Param("bookerId") Long bookerId, @Param("end") LocalDateTime end);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = :bookerId " +
            "AND b.status = :status " +
            "ORDER BY b.start DESC")
    List<Booking> findBookingsByBookerAndStatus(@Param("bookerId") Long bookerId, @Param("status") BookingStatus status);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = :ownerId " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByOwner(@Param("ownerId") Long ownerId);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = :ownerId " +
            "AND b.start > :start " +
            "ORDER BY b.start DESC")
    List<Booking> findFutureByOwner(@Param("ownerId") Long ownerId, @Param("start") LocalDateTime start);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = :ownerId " +
            "AND b.start < :start " +
            "AND b.end > :end " +
            "ORDER BY b.start DESC")
    List<Booking> findCurrentOwners(@Param("ownerId") Long ownerId, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = :ownerId " +
            "AND b.end < :end " +
            "ORDER BY b.start DESC")
    List<Booking> findPastOwners(@Param("ownerId") Long ownerId, @Param("end") LocalDateTime end);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.owner.id = :ownerId " +
            "AND b.status = :status " +
            "ORDER BY b.start DESC")
    List<Booking> findBookingsByOwnersAndStatus(@Param("ownerId") Long ownerId, @Param("status") BookingStatus status);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = :bookerId " +
            "AND b.item.id = :itemId " +
            "AND b.status = :status " +
            "AND b.end < :now")
    List<Booking> findCompletedBookings(
            @Param("bookerId") Long bookerId,
            @Param("itemId") Long itemId,
            @Param("status") BookingStatus status,
            @Param("now") LocalDateTime now
    );

    @Query("SELECT b FROM Booking b " +
            "WHERE b.item.id = :itemId " +
            "AND b.status = :status " +
            "ORDER BY b.start ASC")
    List<Booking> findByItemIdAndStatus(@Param("itemId") Long itemId, @Param("status") BookingStatus status);
}
