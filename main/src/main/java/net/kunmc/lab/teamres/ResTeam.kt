package net.kunmc.lab.teamres

import net.kunmc.lab.teamres.syncable.Syncable
import org.bukkit.OfflinePlayer

/**
 * Team that has same responsibility
 */
interface ResTeam {
    /**
     * @return all players in this team
     */
    fun all(): List<OfflinePlayer>

    /**
     * @return all members in this team that are online
     */
    fun getMembers(): List<OfflinePlayer>

    /**
     * @return leader of this team
     */
    fun getLeader(): OfflinePlayer

    /**
     * Change leader of this team to [next]
     */
    fun changeLeader(next: OfflinePlayer)

    /**
     * Add OfflinePlayer to this team
     */
    fun add(p: OfflinePlayer)

    /**
     * Remove OfflinePlayer from this team
     */
    fun remove(p: OfflinePlayer)

    /**
     * @return all affected syncable entry
     */
    fun affected(): List<Syncable>

    /**
     * Effect this team
     */
    fun effect(syncable: Syncable, isOnOff: OnOff)
}