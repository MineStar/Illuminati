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

import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerInventoryEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.event.player.PlayerVelocityEvent;

import de.minestar.illuminati.manager.TimeManager;

public class LPlayerListener extends PlayerListener {

    private TimeManager tManager;

    public LPlayerListener(TimeManager tManager) {
        this.tManager = tManager;
    }

    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onPlayerQuit(PlayerQuitEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onPlayerKick(PlayerKickEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onPlayerChat(PlayerChatEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onPlayerMove(PlayerMoveEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onPlayerVelocity(PlayerVelocityEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onPlayerLogin(PlayerLoginEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onPlayerPreLogin(PlayerPreLoginEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onPlayerEggThrow(PlayerEggThrowEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onPlayerAnimation(PlayerAnimationEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onInventoryOpen(PlayerInventoryEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onItemHeldChange(PlayerItemHeldEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onPlayerToggleSprint(PlayerToggleSprintEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onPlayerBucketFill(PlayerBucketFillEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onPlayerBedLeave(PlayerBedLeaveEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onPlayerPortal(PlayerPortalEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onPlayerFish(PlayerFishEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onPlayerGameModeChange(PlayerGameModeChangeEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        tManager.EventHasEnded(event);
    }
}
