/*
 * Copyright (C) 2012 MineStar.de 
 * 
 * This file is part of Illuminati.
 * 
 * Illuminati is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 * 
 * Illuminati is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Illuminati.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.minestar.illuminati.listener.inTime;

import org.bukkit.event.vehicle.VehicleBlockCollisionEvent;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.vehicle.VehicleListener;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.event.vehicle.VehicleUpdateEvent;

import de.minestar.illuminati.manager.TimeManager;

public class FVehicleListener extends VehicleListener {

    private TimeManager tManager;

    public FVehicleListener(TimeManager tManager) {
        this.tManager = tManager;
    }

    @Override
    public void onVehicleCreate(VehicleCreateEvent event) {
        tManager.EventHasStarted(event);
    }

    @Override
    public void onVehicleDamage(VehicleDamageEvent event) {
        tManager.EventHasStarted(event);
    }

    @Override
    public void onVehicleBlockCollision(VehicleBlockCollisionEvent event) {
        tManager.EventHasStarted(event);
    }

    @Override
    public void onVehicleEntityCollision(VehicleEntityCollisionEvent event) {
        tManager.EventHasStarted(event);
    }

    @Override
    public void onVehicleEnter(VehicleEnterEvent event) {
        tManager.EventHasStarted(event);
    }

    @Override
    public void onVehicleExit(VehicleExitEvent event) {
        tManager.EventHasStarted(event);
    }

    @Override
    public void onVehicleMove(VehicleMoveEvent event) {
        tManager.EventHasStarted(event);
    }

    @Override
    public void onVehicleDestroy(VehicleDestroyEvent event) {
        tManager.EventHasStarted(event);
    }

    @Override
    public void onVehicleUpdate(VehicleUpdateEvent event) {
        tManager.EventHasStarted(event);
    }
}
