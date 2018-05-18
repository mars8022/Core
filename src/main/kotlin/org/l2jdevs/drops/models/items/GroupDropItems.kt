package org.l2jdevs.drops.models.items

import com.l2jserver.gameserver.model.holders.ItemHolder
import org.l2jdevs.drops.CustomConfig
import org.l2jdevs.drops.interfaces.IDrop
import org.l2jdevs.drops.models.LevelPenalty

class GroupDropItems(chance: Double, val items: List<Pair<Double, DropItem>>) : IDrop {

    private val chance: Double

    init {
        val calculatedChance = chance * CustomConfig.GROUP_DROP_RATE

        if (CustomConfig.GROUP_DROP_OVERFLOW) this.chance = calculatedChance
        else this.chance = Math.max(100.0, calculatedChance)

        
    }

    fun getChance(penalty: LevelPenalty) = Math.min(0.0, chance - penalty.groupPenalty)

    override fun getItem(): ItemHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}