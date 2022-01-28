package net.kunmc.lab.teamres.syncable.base

import com.flylib.flylib3.FlyLib
import com.flylib.flylib3.FlyLibComponent
import net.kunmc.lab.teamres.ResTeam
import net.kunmc.lab.teamres.ResTeamImpl
import net.kunmc.lab.teamres.ResTeamImpl2
import net.kunmc.lab.teamres.TeamManager
import net.kunmc.lab.teamres.syncable.Syncable
import net.kunmc.lab.teamres.util.SessionSafePlayer
import org.bukkit.entity.Player

/**
 * Base syncable class
 */
open class BaseSync(override val flyLib: FlyLib, open val teamManager: TeamManager) : Syncable, FlyLibComponent {
    public val syncedPlayer = mutableListOf<SessionSafePlayer>()

    /**
     * @return Pair(Player,Team)
     */
    fun getWithPlayer(p: Player): Pair<Player, ResTeamImpl2>? {
        val sf = SessionSafePlayer(p)
        if (!syncedPlayer.contains(sf)) return null
        else {
            val team = teamManager.getTeam(sf)
            if (team != null) {
                return Pair(p, team)
            } else {
                return null
            }
        }
    }

    fun getWithSessionSafePlayer(p: SessionSafePlayer): Pair<SessionSafePlayer, ResTeamImpl2>? {
        if (!syncedPlayer.contains(p)) {
            return null
        } else {
            val team = teamManager.getTeam(p)
            if (team != null) {
                return Pair(p, team)
            } else {
                return null
            }
        }
    }


    /**
     * @return map of team and synced players
     */
    fun generateList(): MutableMap<ResTeam, MutableList<SessionSafePlayer>> {
        val list = mutableMapOf<ResTeam, MutableList<SessionSafePlayer>>()
        syncedPlayer.forEach {
            val team = teamManager.getTeam(it)
            if (team != null) {
                if (list.containsKey(team)) {
                    list[team]!!.add(it)
                } else {
                    list[team] = mutableListOf(it)
                }
            }
        }

        return list
    }

    override fun startSync(team: ResTeam) {
        team.all().forEach {
            startSync(team, it)
        }
    }

    override fun startSync(team: ResTeam, p: SessionSafePlayer) {
        if (!team.all().contains(p)) {
            throw IllegalStateException("Player is not in team")
        }
        syncedPlayer.add(p)
    }

    override fun endSync(team: ResTeam, p: SessionSafePlayer) {
        if (!team.all().contains(p)) {
            throw IllegalStateException("Player is not in team")
        }
        syncedPlayer.remove(p)
    }

    override fun endSync(team: ResTeam) {
        team.all().forEach {
            endSync(team, it)
        }
    }
}