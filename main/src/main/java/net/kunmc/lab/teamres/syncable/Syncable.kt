package net.kunmc.lab.teamres.syncable

import com.flylib.flylib3.FlyLibComponent
import net.kunmc.lab.teamres.ResTeam
import net.kunmc.lab.teamres.util.SessionSafePlayer
import org.bukkit.OfflinePlayer

/**
 * Syncable in team
 */
interface Syncable  {
    /**
     * Start sync in team
     */
    fun startSync(team: ResTeam)

    /**
     * Start sync in team
     * This method is for player that have not joined the team when the game start
     */
    fun startSync(team: ResTeam, p: SessionSafePlayer)

    /**
     * End sync in team
     * This method is for player that quit game, or removed from team
     */
    fun endSync(team: ResTeam, p: SessionSafePlayer)

    /**
     * Stop sync in team
     */
    fun endSync(team: ResTeam)
}