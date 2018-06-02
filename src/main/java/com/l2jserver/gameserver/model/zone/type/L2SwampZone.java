/*
 * Copyright (C) 2004-2018 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jserver.gameserver.model.zone.type;

import java.util.concurrent.ScheduledFuture;

import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.instancemanager.CastleManager;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.entity.Castle;
import com.l2jserver.gameserver.model.zone.L2ZoneType;
import com.l2jserver.gameserver.model.zone.ZoneId;
import com.l2jserver.gameserver.network.serverpackets.OnEventTrigger;

/**
 * another type of zone where your speed is changed
 * @author kerberos
 */
public class L2SwampZone extends L2ZoneType
{
	private double _move_bonus;
	
	private int _castleId;
	private Castle _castle;
	int _eventId;
	
	ScheduledFuture<?> _broadcastTrapAnimation = null;
	
	public L2SwampZone(int id)
	{
		super(id);
		
		// Setup default speed reduce (in %)
		_move_bonus = 0.5;
		
		// no castle by default
		_castleId = 0;
		_castle = null;
		
		// no event by default
		_eventId = 0;
	}
	
	@Override
	public void setParameter(String name, String value)
	{
		if (name.equals("move_bonus"))
		{
			_move_bonus = Double.parseDouble(value);
		}
		else if (name.equals("castleId"))
		{
			_castleId = Integer.parseInt(value);
		}
		else if (name.equals("eventId"))
		{
			_eventId = Integer.parseInt(value);
		}
		else
		{
			super.setParameter(name, value);
		}
	}
	
	Castle getCastle()
	{
		if ((_castleId > 0) && (_castle == null))
		{
			_castle = CastleManager.getInstance().getCastleById(_castleId);
		}
		
		return _castle;
	}
	
	@Override
	public void setEnabled(boolean state)
	{
		_enabled = state;
		if (_enabled && (_broadcastTrapAnimation == null))
		{
			_broadcastTrapAnimation = ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new broadcastTrapAnimation(), 1000, 15000);
		}
	}
	
	private class broadcastTrapAnimation implements Runnable
	{
		public broadcastTrapAnimation()
		{
		}
		
		@Override
		public void run()
		{
			final Castle castle = getCastle();
			if (castle != null)
			{
				if (castle.getSiege().isInProgress() && isEnabled())
				{
					castle.getZone().broadcastPacket(new OnEventTrigger(_eventId, true));
				}
				else
				{
					castle.getZone().broadcastPacket(new OnEventTrigger(_eventId, false));
					// Keep task running?
					// What when player returns to castle after task canceled?
					// _broadcastTrapAnimation.cancel(true);
				}
			}
		}
	}
	
	@Override
	protected void onEnter(L2Character character)
	{
		if (getCastle() != null)
		{
			// castle zones active only during siege
			if (!getCastle().getSiege().isInProgress() || !isEnabled())
			{
				return;
			}
			
			// defenders not affected
			final L2PcInstance player = character.getActingPlayer();
			if ((player != null) && player.isInSiege() && (player.getSiegeState() == 2))
			{
				return;
			}
		}
		
		character.setInsideZone(ZoneId.SWAMP, true);
		if (character.isPlayer())
		{
			character.getActingPlayer().broadcastUserInfo();
		}
	}
	
	@Override
	protected void onExit(L2Character character)
	{
		// don't broadcast info if not needed
		if (character.isInsideZone(ZoneId.SWAMP))
		{
			character.setInsideZone(ZoneId.SWAMP, false);
			if (character.isPlayer())
			{
				character.getActingPlayer().broadcastUserInfo();
			}
		}
	}
	
	public double getMoveBonus()
	{
		return _move_bonus;
	}
}