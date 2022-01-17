package net.kunmc.lab.teamres.util

import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import java.util.*

/**
 * Bukkit Player is not cross-session.
 * so I made this.
 */
class SessionSafePlayer(val uuid: UUID) {
    constructor(p: Player) : this(p.uniqueId)
    constructor(offP: OfflinePlayer) : this(offP.uniqueId)


    fun player(): Player? = Bukkit.getPlayer(uuid)
    fun offlinePlayer(): OfflinePlayer = Bukkit.getOfflinePlayer(uuid)
    val isOnline: Boolean
        get() {
            val p = player()
            return p != null && p.isOnline
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (other is SessionSafePlayer) {
            if (uuid == other.uuid) return true
        } else if (other is OfflinePlayer) {
            if (uuid == other.uniqueId) return true
        }
        return false
    }

    override fun hashCode(): Int {
        return uuid.hashCode()
    }
}