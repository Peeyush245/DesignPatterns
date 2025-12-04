package model;

import constants.VehicleType;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class ParkingSpot {
    private final String id;
    private final VehicleType size;
    private final AtomicReference<Ticket> ticket = new AtomicReference<>(null);

    public ParkingSpot(String id, VehicleType size) {
        this.id = id;
        this.size = size;
    }

    public String getId() { return id; }
    public VehicleType getSize() { return size; }

    public boolean tryClaim(Ticket t) {
        return ticket.compareAndSet(null, t);
    }

    public boolean tryRelease(Ticket t) {
        return ticket.compareAndSet(t, null);
    }

    public Optional<Ticket> currentTicket() {
        return Optional.ofNullable(ticket.get());
    }

    public boolean isFree() { return ticket.get() == null; }

    @Override
    public String toString() {
        return "model.ParkingSpot{" +
                "size=" + size +
                ", id='" + id + '\'' +
                '}';
    }
}
