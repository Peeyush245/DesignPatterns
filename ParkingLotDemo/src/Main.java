import constants.VehicleType;
import helper.SpotManager;
import model.ParkingFloor;
import model.ParkingSpot;
import model.Ticket;
import model.Vehicle;
import strategy.NearestFirst;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws Exception {
        List<ParkingSpot> floor1Spots = new ArrayList<>();
        List<ParkingSpot> floor2Spots = new ArrayList<>();
        int spotsPerFloor = 10;

        for (int i = 0; i < spotsPerFloor; i++) {
            floor1Spots.add(new ParkingSpot("F1-" + i, VehicleType.CAR));
            floor2Spots.add(new ParkingSpot("F2-" + i, VehicleType.CAR));
        }

        ParkingFloor floor1 = new ParkingFloor(1, floor1Spots);
        ParkingFloor floor2 = new ParkingFloor(2, floor2Spots);

        SpotManager manager = new SpotManager(List.of(floor1, floor2), new NearestFirst());

        System.out.println("\n--- Initial availability ---");
        displayAvailability(floor1);
        displayAvailability(floor2);

        System.out.println("\n--- Vehicle entries ---");
        Vehicle bike = new Vehicle("B-123", VehicleType.BIKE);
        Vehicle car  = new Vehicle("C-456", VehicleType.CAR);
        Vehicle truck= new Vehicle("T-789", VehicleType.TRUCK);

        Optional<Ticket> bikeTicketOpt  = manager.parkVehicle(bike);
        System.out.println("Bike B-123 parked: " + bikeTicketOpt.map(Ticket::spotId).orElse("FAILED"));

        Optional<Ticket> carTicketOpt   = manager.parkVehicle(car);
        System.out.println("Car C-456 parked: "  + carTicketOpt.map(Ticket::spotId).orElse("FAILED"));

        Optional<Ticket> truckTicketOpt = manager.parkVehicle(truck);
        System.out.println("Truck T-789 parked: "+ truckTicketOpt.map(Ticket::spotId).orElse("FAILED"));

        System.out.println("\n--- Availability after parking ---");
        displayAvailability(floor1);
        displayAvailability(floor2);

        Vehicle car2 = new Vehicle("C-999", VehicleType.CAR);
        Optional<Ticket> car2TicketOpt = manager.parkVehicle(car2);
        System.out.println("\nCar C-999 parked: " + car2TicketOpt.map(Ticket::spotId).orElse("FAILED"));

        Vehicle bike2 = new Vehicle("B-000", VehicleType.BIKE);
        Optional<Ticket> failedBikeTicketOpt = manager.parkVehicle(bike2);
        System.out.println("Bike B-000 parked: " + failedBikeTicketOpt.map(Ticket::spotId).orElse("FAILED"));

        System.out.println("\n--- Vehicle exits ---");
        if (carTicketOpt.isPresent()) {
            Ticket carTicket = carTicketOpt.get();
            boolean exited = manager.exitVehicle(carTicket.ticketId());
            System.out.println("Car C-456 unparked (ticket " + carTicket.ticketId() + "): " + exited);
        } else {
            System.out.println("Car C-456 had no ticket to unpark.");
        }

        System.out.println("\n--- Availability after one car leaves ---");
        displayAvailability(floor1);
        displayAvailability(floor2);
    }

    private static void displayAvailability(ParkingFloor floor) {
        Map<VehicleType, Long> freeCounts = floor.getSpots().stream()
                .filter(ParkingSpot::isFree)
                .collect(Collectors.groupingBy(ParkingSpot::getSize, Collectors.counting()));

        System.out.println("Floor " + floor.getFloorNumber() + " availability:");
        for (VehicleType s : VehicleType.values()) {
            long c = freeCounts.getOrDefault(s, 0L);
            System.out.printf("  %s: %d free%n", s, c);
        }
    }
}
