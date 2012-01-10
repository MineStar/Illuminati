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

import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreeperPowerEvent;
import org.bukkit.event.entity.EndermanPickupEvent;
import org.bukkit.event.entity.EndermanPlaceEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PigZapEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.SlimeSplitEvent;
import org.bukkit.event.painting.PaintingBreakEvent;
import org.bukkit.event.painting.PaintingPlaceEvent;

import de.minestar.illuminati.manager.TimeManager;

public class LEntityListener extends EntityListener {

    private TimeManager tManager;

    public LEntityListener(TimeManager tManager) {
        this.tManager = tManager;
    }

    @Override
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onItemSpawn(ItemSpawnEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onEntityCombust(EntityCombustEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onEntityDamage(EntityDamageEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onEntityExplode(EntityExplodeEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onExplosionPrime(ExplosionPrimeEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onEntityDeath(EntityDeathEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onEntityTarget(EntityTargetEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onEntityInteract(EntityInteractEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onEntityPortalEnter(EntityPortalEnterEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onPaintingPlace(PaintingPlaceEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onPaintingBreak(PaintingBreakEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onPigZap(PigZapEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onCreeperPower(CreeperPowerEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onEntityTame(EntityTameEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onEntityRegainHealth(EntityRegainHealthEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onProjectileHit(ProjectileHitEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onEndermanPickup(EndermanPickupEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onEndermanPlace(EndermanPlaceEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onSlimeSplit(SlimeSplitEvent event) {
        tManager.EventHasEnded(event);
    }
}
