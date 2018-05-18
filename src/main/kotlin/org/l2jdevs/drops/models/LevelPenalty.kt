package org.l2jdevs.drops.models

import com.l2jserver.gameserver.model.actor.instance.L2NpcInstance
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance
import org.l2jdevs.drops.CustomConfig

class LevelPenalty(player: L2PcInstance, npc: L2NpcInstance) {

    val groupPenalty: Double
    val groupItemsPenalty: Double
    val corpseItemsPenalty: Double

    init {
        val diffLevel = getDiffLevel(player, npc)
        groupPenalty = diffLevel * CustomConfig.GROUP_DROP_PENALTY_PER_LEVEL
        groupItemsPenalty = diffLevel * CustomConfig.GROUP_DROP_ITEM_PENALTY_PER_LEVEL
        corpseItemsPenalty = diffLevel * CustomConfig.CORPSE_ITEM_PENALTY_PER_LEVEL
    }

    /**
     * Returns the exceed the threshold between the player's level and npc's level
     * For example:
     * A) The player's level is 75, npc level is 69 and DROP_MAX_LEVEL_DIFF is 5, the exceed is 1
     * B) The player's level is 64, npc level is 75 and DROP_MAX_LEVEL_DIFF is 10, the exceed is 1
     */
    private fun getDiffLevel(player: L2PcInstance, npc: L2NpcInstance): Int {
        val playerLvl = player.level
        val npcLvl = npc.level
        var diff = 0

        if (playerLvl > npcLvl) {
            diff = playerLvl - npcLvl - CustomConfig.DROP_MAX_LEVEL_DIFF
        } else if (playerLvl < npcLvl) {
            diff = npcLvl - playerLvl - CustomConfig.DROP_MIN_LEVEL_DIFF
        }

        return Math.min(0, diff)
    }
}