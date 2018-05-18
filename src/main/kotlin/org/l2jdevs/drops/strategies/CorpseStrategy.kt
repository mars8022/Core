package org.l2jdevs.drops.strategies

import com.l2jserver.gameserver.model.actor.instance.L2NpcInstance
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance
import com.l2jserver.gameserver.model.holders.ItemHolder
import org.l2jdevs.commons.Utils
import org.l2jdevs.drops.models.items.CorpseGroupItems
import org.l2jdevs.drops.models.LevelPenalty

object CorpseStrategy {

    fun calculateCorpseDrop(player: L2PcInstance, npc: L2NpcInstance, group: CorpseGroupItems): Collection<ItemHolder> {
        val penalty = LevelPenalty(player, npc)
        val calculatedItemChance = Utils.getRandomChance()
        return group.items
                .filter { item -> item.getChance(penalty) >= calculatedItemChance }
                .map { item -> ItemHolder(item.itemId, item.getRndAmount()) }
    }
}