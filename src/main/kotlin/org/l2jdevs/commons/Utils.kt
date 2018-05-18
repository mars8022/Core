package org.l2jdevs.commons

import com.l2jserver.util.Rnd

object Utils {

    fun getRandomChance() = Rnd.get(0, 100) + Rnd.nextDouble()
}