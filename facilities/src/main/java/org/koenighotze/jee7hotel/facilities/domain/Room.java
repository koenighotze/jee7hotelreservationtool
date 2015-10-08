

package org.koenighotze.jee7hotel.facilities.domain;

import org.koenighotze.jee7hotel.facilities.domain.converter.RoomEquipmentConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Represents a single room.
 *
 * @author dschmitz
 */
@Entity
@NamedQueries({
        @NamedQuery(
                // TODO: fix query with actual date operations
                name = "Room.findAvailable",
                query = "select r from Room r"
        ),
        @NamedQuery(
                name = "Room.findByRoomNumber",
                query = "select r from Room r where r.roomNumber = :number"
        )
})
@Cacheable
public class Room implements Serializable {
    private static final Room NULL_VALUE = new Room("", RoomEquipment.BUDGET);

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Version
    private Long version;

    @NotNull
    private String roomNumber;

    @NotNull
    @Convert(converter = RoomEquipmentConverter.class)
    private RoomEquipment roomEquipment;

    Room() {
    }

    public Room(String roomNumber, RoomEquipment roomEquipment) {
        this.roomNumber = roomNumber;
        this.roomEquipment = roomEquipment;
    }

    public Long getId() {
        return id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public RoomEquipment getRoomEquipment() {
        return roomEquipment;
    }

    public Long getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "Room{" + "id=" + id + ", version=" + version + ", roomNumber=" + roomNumber + ", roomEquipment=" + roomEquipment + '}';
    }

    public static Room nullValue() {
        return NULL_VALUE;
    }
}