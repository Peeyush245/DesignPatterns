package model;

import constants.VehicleType;

public class Vehicle {

    private final String vehiclePlate;
    private final VehicleType size;

    public Vehicle(String vehiclePlate, VehicleType size) {
        this.vehiclePlate = vehiclePlate;
        this.size = size;
    }

    public String getVehiclePlate() { return vehiclePlate; }
    public VehicleType getSize() { return size; }

}