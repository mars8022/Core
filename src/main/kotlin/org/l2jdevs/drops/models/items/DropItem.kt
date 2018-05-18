package org.l2jdevs.drops.models.items

import com.l2jserver.gameserver.model.holders.ItemHolder
import com.l2jserver.util.Rnd
import org.l2jdevs.drops.CustomConfig
import org.l2jdevs.drops.interfaces.IDrop
import org.l2jdevs.drops.models.LevelPenalty

class DropItem(val itemId: Int, val min: Long, val max: Long, chance: Double) : IDrop {

    private val chance = Math.max(100.0, chance * CustomConfig.GROUP_DROP_ITEM_RATE)

    override fun getChance(penalty: LevelPenalty) = Math.min(0.0, chance - penalty.groupItemsPenalty)

    override fun getItem() = ItemHolder(itemId, Rnd.get(min, max))
}