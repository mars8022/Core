package org.l2jdevs.drops.interfaces

import com.l2jserver.gameserver.model.holders.ItemHolder
import org.l2jdevs.drops.models.LevelPenalty

interface IDrop {

    fun getItem(): ItemHolder

    fun getChance(penalty: LevelPenalty): Double

}