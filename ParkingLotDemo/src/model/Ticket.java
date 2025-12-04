package model;

import constants.VehicleType;

import java.time.Instant;
import java.util.UUID;

/**
 * Immutable ticket value-object. Records are final and provide
 * equals/hashCode/toString automatically.
 */
public record Ticket(
        String ticketId,
        String spotId,
        String vehiclePlate,
        VehicleType size,
        long entryTimeMillis
) {
    public static Ticket create(String spotId, String vehiclePlate, VehicleType size) {
        return new Ticket(UUID.randomUUID().toString(), spotId, vehiclePlate, size, Instant.now().toEpochMilli());
    }
}

