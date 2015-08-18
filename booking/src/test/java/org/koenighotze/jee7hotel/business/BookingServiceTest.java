

package org.koenighotze.jee7hotel.business;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.koenighotze.jee7hotel.business.events.NewReservationEvent;
import org.koenighotze.jee7hotel.business.events.ReservationStatusChangeEvent;
import org.koenighotze.jee7hotel.domain.Reservation;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.enterprise.event.Event;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 *
 * @author dschmitz
 */

@RunWith(MockitoJUnitRunner.class)
public class BookingServiceTest extends AbstractBasePersistenceTest {
    private static final Long WELL_KNOWN_ID = 9999L;
    private static final String WELL_KNOWN_ROOM_NUMBER = "001";
    
//    private RoomService roomService = new RoomService();
    private BookingService bookingService = new BookingService();
//    private GuestService guestService = new GuestService();

    @Mock
    private Event<NewReservationEvent> mockEvent;

    @Mock
    private Event<ReservationStatusChangeEvent> mockResEvent;


    @Override
    protected void initHook() {
        this.bookingService.setEntityManager(super.getEntityManager());
        this.bookingService.setReservationEvents(this.mockEvent);
        this.bookingService.setReservationStateChangeEvents(this.mockResEvent);

//        this.guestService.setEntityManager(super.getEntityManager());
//        this.roomService.setEntityManager(super.getEntityManager());
    }

    @Test
    public void testGetAllReservations() {
        List<Reservation> allReservations = this.bookingService.getAllReservations();
        assertThat(allReservations, is(not(nullValue())));
    }
//
//    @Test
//    public void testBookRoom() {
//        Optional<Guest> guest = this.guestService.findById(WELL_KNOWN_ID);
//        Optional<Room> room = this.roomService.findRoomByNumber(WELL_KNOWN_ROOM_NUMBER);
//        Reservation reservation =
//                this.bookingService.bookRoom(guest.get(), room.get(), LocalDate.now(), LocalDate.now());
//        getEntityManager().flush();
//        assertThat(reservation, is(not(nullValue())));
//        assertThat(reservation.getReservationNumber(), is(not(nullValue())));
//    }
//
//    @Test
//    public void calcRate() {
//        LocalDate from = LocalDate.of(1976, Month.SEPTEMBER, 8);
//        LocalDate to = LocalDate.of(1976, Month.SEPTEMBER, 12);
//        BigDecimal rate = this.bookingService.calculateRateFor(RoomEquipment.BUDGET, from, to);
//        assertThat(rate, is(equalTo(BigDecimal.valueOf(240L))));
//    }
//
//    @Test
//    public void findReservationsForGuest() {
//        Optional<Guest> guest = this.guestService.findById(WELL_KNOWN_ID);
//        List<Reservation> reservations = this.bookingService.findReservationForGuest(guest.get());
//
//        assertThat(reservations.size(), is(equalTo(0)));
//
//        Optional<Room> room = this.roomService.findRoomByNumber(WELL_KNOWN_ROOM_NUMBER);
//        Reservation reservation =
//                this.bookingService.bookRoom(guest.get(), room.get(), LocalDate.now(), LocalDate.now());
//
//        reservations = this.bookingService.findReservationForGuest(guest.get());
//        assertThat(reservations.size(), is(equalTo(1)));
//
//    }
}