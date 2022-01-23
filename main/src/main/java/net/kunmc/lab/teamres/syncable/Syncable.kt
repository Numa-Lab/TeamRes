package net.kunmc.lab.teamres.syncable

import net.kunmc.lab.teamres.ResTeam
import net.kunmc.lab.teamres.util.SessionSafePlayer

/**
 * Syncable in team
 */
interface Syncable {
    /**
     * Start sync in team
     * @note do not call this directly,
     * @see ResTeam#effect
     */
    fun startSync(team: ResTeam)

    /**
     * Start sync in team
     * This method is for player that have not joined the team when the game start
     * @note do not call this directly,
     * @see ResTeam#effect
     */
    fun startSync(team: ResTeam, p: SessionSafePlayer)

    /**
     * End sync in team
     * This method is for player that quit game, or removed from team
     * @note do not call this directly,
     * @see ResTeam#effect
     */
    fun endSync(team: ResTeam, p: SessionSafePlayer)

    /**
     * Stop sync in team
     * @note do not call this directly,
     * @see ResTeam#effect
     */
    fun endSync(team: ResTeam)
}