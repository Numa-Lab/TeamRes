package net.kunmc.lab.teamres

import net.kunmc.lab.teamres.syncable.Syncable
import net.kunmc.lab.teamres.util.SessionSafePlayer
import org.bukkit.OfflinePlayer

/**
 * Team that has same responsibility
 */
interface ResTeam {
    /**
     * @return all players in this team
     */
    fun all(): List<SessionSafePlayer>

    /**
     * @return all members in this team that are online
     */
    fun getMembers(): List<SessionSafePlayer>

    /**
     * Add OfflinePlayer to this team
     */
    fun add(p: SessionSafePlayer)

    /**
     * Remove OfflinePlayer from this team
     */
    fun remove(p: SessionSafePlayer)

    /**
     * @return all affected syncable entry
     */
    fun affected(): List<Syncable>

    /**
     * Effect this team
     */
    fun effect(syncable: Syncable, isOnOff: OnOff)
}