package org.l2jdevs.drops.strategies

import com.l2jserver.gameserver.model.actor.instance.L2NpcInstance
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance
import com.l2jserver.gameserver.model.holders.ItemHolder
import org.l2jdevs.commons.Utils.getRandomChance
import org.l2jdevs.drops.interfaces.IDrop
import org.l2jdevs.drops.models.LevelPenalty

object DropStrategy {

    fun calculateDrop(player: L2PcInstance, npc: L2NpcInstance, drops: List<IDrop>): Collection<ItemHolder> {
        return drops.filterDrop(LevelPenalty(player, npc)).mapItems()
    }

    private fun List<IDrop>.filterDrop(penalty: LevelPenalty): List<IDrop> {
        val calculatedGroupChance = getRandomChance()
        return this.filter { drop -> drop.getChance(penalty) <= calculatedGroupChance }
    }

    private fun List<IDrop>.mapItems(): List<ItemHolder> {
        return this.map { drop -> drop.getItem() }
    }
}