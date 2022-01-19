package net.kunmc.lab.teamres.syncable.base

import com.flylib.flylib3.FlyLib
import com.flylib.flylib3.FlyLibComponent
import com.flylib.flylib3.util.everyTick
import net.kunmc.lab.teamres.ResTeam
import net.kunmc.lab.teamres.TeamManager
import net.kunmc.lab.teamres.syncable.Syncable
import net.kunmc.lab.teamres.util.SessionSafePlayer
import org.bukkit.entity.Player

open class OnlineEveryTickSync(
    override val flyLib: FlyLib,
    private val teamManager: TeamManager,
    val start: (ResTeam, List<Player>) -> Unit = { team: ResTeam, members: List<Player> -> },
    val everyTick: (ResTeam, List<Player>) -> Unit,
    val end: (ResTeam, List<Player>) -> Unit = { team: ResTeam, members: List<Player> -> }
) : Syncable, FlyLibComponent {
    init {
        register()
    }

    private fun register() {
        everyTick {
            val members = mutableMapOf<ResTeam, List<Player>>()
            syncedOnlinePlayers
                .mapNotNull { it.player() }
                .forEach {
                    val team = teamManager.getTeam(SessionSafePlayer(it))
                    if (team != null) {
                        val list = members[team]
                        if (list == null) {
                            members[team] = listOf(it)
                        } else {
                            members[team] = list + it
                        }
                    }
                }
            return@everyTick members
        }.then {
            it.forEach { (team, list) -> everyTick(team, list) }
        }
    }

    private val syncedOnlinePlayers = mutableSetOf<SessionSafePlayer>()

    override fun startSync(team: ResTeam, p: SessionSafePlayer) {
        if (!team.all().contains(p)) throw IllegalStateException("$p is not in $team")
        if (p.isOnline) {
            start(team, listOf(p.player()!!))
        }
    }

    override fun startSync(team: ResTeam) {
        start(team, team.all())
    }

    private fun start(team: ResTeam, members: List<SessionSafePlayer>) {
        val m = members.mapNotNull { it.player() }
        if (m.isNotEmpty()) {
            syncedOnlinePlayers.addAll(m.map { SessionSafePlayer(it) })
            start(team, m)
        }
    }

    override fun endSync(team: ResTeam, p: SessionSafePlayer) {
        if (!team.all().contains(p)) throw IllegalStateException("$p is not in $team")
        if (p.isOnline) {
            end(team, listOf(p.player()!!))
        }
    }

    override fun endSync(team: ResTeam) {
        end(team, team.all())
    }

    private fun end(team: ResTeam, members: List<SessionSafePlayer>) {
        val m = members.mapNotNull { it.player() }
        if (m.isNotEmpty()) {
            syncedOnlinePlayers.removeAll(m.map { SessionSafePlayer(it) }.toSet())
            end(team, m)
        }
    }
}