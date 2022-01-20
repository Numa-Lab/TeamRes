@file:Suppress("SpellCheckingInspection")

package net.kunmc.lab.teamres.syncable

import com.flylib.flylib3.FlyLib
import net.kunmc.lab.teamres.TeamManager
import net.kunmc.lab.teamres.util.LazyWithInit
import net.kunmc.lab.teamres.util.lazyInit

/**
 * Enum of all syncable types.
 */
enum class Syncables(val displayName: String, val lazy: LazyWithInit<Syncable, Pair<FlyLib, TeamManager>>) {
    Health("体力", lazyInit { HealthSync(it.first, it.second) }),
    Ban("BAN", lazyInit { BANSync(it.first, it.second) }),
    Effect("ポーション効果", lazyInit { net.kunmc.lab.teamres.syncable.EffectSync(it.first, it.second) }),
    ;

    companion object {
        fun getByString(name: String): Syncables? {
            return values().find { it.displayName == name }
        }
    }

    override fun toString(): String {
        return displayName
    }
}