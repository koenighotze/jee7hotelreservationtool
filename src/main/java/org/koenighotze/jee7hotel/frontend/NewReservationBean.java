

package org.koenighotze.jee7hotel.frontend;

import org.koenighotze.jee7hotel.business.BookingService;
import org.koenighotze.jee7hotel.business.GuestService;
import org.koenighotze.jee7hotel.business.RoomService;
import org.koenighotze.jee7hotel.domain.Guest;
import org.koenighotze.jee7hotel.domain.Reservation;
import org.koenighotze.jee7hotel.domain.Room;
import org.koenighotze.jee7hotel.domain.RoomEquipment;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Optional;

import static org.koenighotze.jee7hotel.frontend.FacesMessageHelper.addMessage;
/**
 *
 * @author dschmitz
 */
@Named
@ViewScoped
public class NewReservationBean implements Serializable {
    private Long guestId;
    
    private Long roomId;
    
    private Booking booking = new Booking();
    
    @Inject
    private BookingService bookingService;

    @Inject
    private RoomService roomService;

    @Inject
    private GuestService guestService;
    
    public Booking getBooking() {
        return this.booking;
    }

    public Long getGuestId() {
        return guestId;
    }

    public void setGuestId(Long guestId) {
        this.guestId = guestId;
        if (null != this.guestId) {
            Optional<Guest> guest = this.guestService.findById(this.guestId);

            this.booking.setGuest(guest.orElseGet(() -> {
                addMessage(FacesMessage.SEVERITY_WARN,
                        "Cannot find guest " + this.guestId);
                return null;
            }));
        }
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }
    
    public void init(ComponentSystemEvent evt) { 
        Room room = new Room("", RoomEquipment.BUDGET);
        if (null != this.roomId) {
            room = this.roomService.findRoomById(this.roomId);
            if (null == room) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, 
                                "Cannot find room " + this.roomId, null));
            }
        }
        
        Guest guest = new Guest("", "");
        if (null != this.guestId) {
            Optional<Guest> tmpGuest = this.guestService.findById(this.guestId);
            guest = tmpGuest.orElseGet(() -> {
                addMessage(FacesMessage.SEVERITY_ERROR,
                        "Cannot find guest " + this.guestId);
                return new Guest("", "");
            });
        }
        
        this.booking = new Booking();
        this.booking.setGuest(guest);
        this.booking.setRoom(room);
        this.booking.setCheckinDate(LocalDate.now().plusDays(1));
        this.booking.setCheckoutDate(LocalDate.now().plusDays(2));
    }
    
    public void setRoomNumber(String number) {
        Room room = this.roomService.findRoomByNumber(number);
        if (null == room) { // TODO use Optional
            FacesMessage message 
                    = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Cannot find room with number " + number, "");
            FacesContext.getCurrentInstance().addMessage(null, message);
            return;
        }
        this.roomId = room.getId();
        this.booking.setRoom(room);
    }
    
    public String getRoomNumber() {
        if (null == this.booking.getRoom()) {
            return null;
        }
        
        return this.booking.getRoom().getRoomNumber();
    }
           
    
    
    public String addReservation() {         
        Reservation realReservation 
                = this.bookingService.bookRoom(this.booking.getGuest(), this.booking.getRoom(),                        
                        this.booking.getCheckinDate(), this.booking.getCheckoutDate());
        
        Flash flash = FacesContext.getCurrentInstance().getExternalContext().getFlash();
        flash.setKeepMessages(true);
        FacesMessage message = new FacesMessage("Room " 
                + this.booking.getRoom().getRoomNumber() 
                + " booked for " 
                + this.booking.getGuest() 
                + "; Reservation number " 
                + realReservation.getReservationNumber() 
                + " Costs: " 
                + realReservation.getCostsInEuro() + " EUR");
        FacesContext.getCurrentInstance().addMessage(null, message);
        this.booking = null;
        
        return String.format("/booking/booking.xhtml?reservationNumber=%s&faces-redirect=true", realReservation.getReservationNumber());
    }
    
    
    
}
