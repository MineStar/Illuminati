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

import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.event.world.SpawnChangeEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldListener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.event.world.WorldUnloadEvent;

import de.minestar.illuminati.manager.TimeManager;

public class FWorldListener extends WorldListener {

    private TimeManager tManager;

    public FWorldListener(TimeManager tManager) {
        this.tManager = tManager;
    }

    @Override
    public void onChunkLoad(ChunkLoadEvent event) {
        tManager.EventHasStarted(event);
    }

    @Override
    public void onChunkPopulate(ChunkPopulateEvent event) {
        tManager.EventHasStarted(event);
    }

    @Override
    public void onChunkUnload(ChunkUnloadEvent event) {
        tManager.EventHasStarted(event);
    }

    @Override
    public void onSpawnChange(SpawnChangeEvent event) {
        tManager.EventHasStarted(event);
    }

    @Override
    public void onPortalCreate(PortalCreateEvent event) {
        tManager.EventHasStarted(event);
    }

    @Override
    public void onWorldSave(WorldSaveEvent event) {
        tManager.EventHasStarted(event);
    }

    @Override
    public void onWorldInit(WorldInitEvent event) {
        tManager.EventHasStarted(event);
    }

    @Override
    public void onWorldLoad(WorldLoadEvent event) {
        tManager.EventHasStarted(event);
    }

    @Override
    public void onWorldUnload(WorldUnloadEvent event) {
        tManager.EventHasStarted(event);
    }

    @Override
    public void onStructureGrow(StructureGrowEvent event) {
        tManager.EventHasStarted(event);
    }
}
