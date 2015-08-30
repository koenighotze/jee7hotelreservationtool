package org.koenighotze.jee7hotel.booking.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.koenighotze.jee7hotel.booking.business.BookingService;
import org.koenighotze.jee7hotel.booking.business.events.NewReservationEvent;
import org.koenighotze.jee7hotel.booking.business.events.ReservationStatusChangeEvent;
import org.koenighotze.jee7hotel.business.AbstractBasePersistenceTest;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.enterprise.event.Event;
import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ReservationTest extends AbstractBasePersistenceTest {
    private static final String WELL_KNOWN_ID = "9999";
    private static final String WELL_KNOWN_ROOM_NUMBER = "001";

    private BookingService bookingService;
    @Mock
    private Event<NewReservationEvent> mockEvent;

    @Mock
    private Event<ReservationStatusChangeEvent> mockResEvent;

    @Override
    protected String getPersistenceUnitName() {
        return "booking-integration-test";
    }

    @Override
    protected void initHook() {
        super.initHook();
        this.bookingService = new BookingService(this.mockEvent, this.mockResEvent);

        this.bookingService.setEntityManager(super.getEntityManager());
    }

    @Test
    public void testSave() {
        Reservation reservation = this.bookingService.bookRoom(WELL_KNOWN_ID, WELL_KNOWN_ROOM_NUMBER, LocalDate.now(), LocalDate.now());
        assertThat(reservation, is(not(nullValue())));
        getEntityManager().flush();
    }
}