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

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.block.SignChangeEvent;

import de.minestar.illuminati.manager.TimeManager;

public class LBlockListener extends BlockListener {

    private TimeManager tManager;

    public LBlockListener(TimeManager tManager) {
        this.tManager = tManager;
    }

    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onBlockBurn(BlockBurnEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onBlockCanBuild(BlockCanBuildEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onBlockDamage(BlockDamageEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onBlockDispense(BlockDispenseEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onBlockFade(BlockFadeEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onBlockForm(BlockFormEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onBlockFromTo(BlockFromToEvent event) {
        tManager.EventHasEnded(event);
    }
    @Override
    public void onBlockIgnite(BlockIgniteEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onBlockPhysics(BlockPhysicsEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onBlockPlace(BlockPlaceEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onBlockRedstoneChange(BlockRedstoneEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onLeavesDecay(LeavesDecayEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onSignChange(SignChangeEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onBlockSpread(BlockSpreadEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onBlockPistonRetract(BlockPistonRetractEvent event) {
        tManager.EventHasEnded(event);
    }

    @Override
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        tManager.EventHasEnded(event);
    }
}
