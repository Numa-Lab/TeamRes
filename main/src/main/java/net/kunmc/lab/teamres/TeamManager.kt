package net.kunmc.lab.teamres

import com.flylib.flylib3.FlyLib
import com.flylib.flylib3.FlyLibComponent
import com.flylib.flylib3.util.event
import com.flylib.flylib3.util.ready
import com.flylib.flylib3.util.warn
import net.kunmc.lab.teamres.syncable.Syncable
import net.kunmc.lab.teamres.syncable.Syncables
import net.kunmc.lab.teamres.util.SessionSafePlayer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.scoreboard.Team

class TeamManager(override val flyLib: FlyLib) : FlyLibComponent {
    private val teams = mutableListOf<ResTeamImpl2>()
    val activeSyncable = mutableListOf<Syncable>()

    fun getTeamByName(name: String): ResTeamImpl2? {
        return teams.find { it.toString() == name }
    }

    fun getTeam(p: SessionSafePlayer): ResTeamImpl2? = teams.find { it.all().contains(p) }

    fun teams() = teams.toList()

    fun genTeam(teamName: String, vararg members: SessionSafePlayer): ResTeam {
        val team = ResTeamImpl2(teamName, flyLib)
        teams.add(team)
        members.forEach {
            team.add(it)
        }
        activeSyncable.forEach {
            it.startSync(team)
        }
        return team
    }

    /**
     * Set Syncable Status to All Team
     */
    fun setSync(syncable: Syncable, onOff: OnOff) {
        if (onOff.isOn) {
            activeSyncable.add(syncable)
            teams.forEach {
                syncable.startSync(it)
            }
        } else {
            activeSyncable.remove(syncable)
            teams.forEach {
                syncable.endSync(it)
            }
        }
    }
}

/**
 * Express ResTeam,
 * @note Even if the player in this team gets offline,the player will not be removed from the team.
 */
@Deprecated("Use ResTeamImpl2 instead")
final class ResTeamImpl(
    members: List<SessionSafePlayer>,
    val teamName: Component,
    override val flyLib: FlyLib
) : ResTeam, FlyLibComponent {
    init {
        ready {
            registerTasks()
        }
    }

    /**
     * Internal Members variable
     */
    private var mes = members

    private fun registerTasks() {
        event<PlayerJoinEvent, Unit> {
            // Player join event
            if (all().contains(SessionSafePlayer(it.player))) {
                // If player is in team, need to effect
                affected().forEach { s ->
                    s.startSync(this@ResTeamImpl, SessionSafePlayer(it.player))
                }
            }
        }

        event<PlayerQuitEvent, Unit> {
            // Player quit event
//            if (all().contains(SessionSafePlayer(it.player))) {
            // If player is in team, need to effect
//                affected().forEach { s ->
//                    s.endSync(this@ResTeamImpl, SessionSafePlayer(it.player))
//                }
//            }
        }
    }

    override fun all(): List<SessionSafePlayer> = mes.toList()

    override fun getMembers(): List<SessionSafePlayer> = mes.toList()

    override fun add(p: SessionSafePlayer) {
        if (all().contains(p)) return

        mes = listOf(p, *(mes.toTypedArray()))

        affected().forEach {
            it.startSync(this, p)
        }
    }

    override fun remove(p: SessionSafePlayer) {
        if (!all().contains(p)) return

        affected().forEach {
            it.endSync(this, p)
        }

        mes = mes.filter { it != p }.toList()
    }

    private val effected = mutableListOf<Syncable>()

    override fun affected(): List<Syncable> = effected.toList()
    override fun effect(syncable: Syncable, isOnOff: OnOff) {
        if (isOnOff.isOn) {
            syncable.startSync(this)
            effected.add(syncable)
        } else {
            syncable.endSync(this)
            effected.remove(syncable)
        }
    }

    override fun name() = teamName

    override fun toString(): String {
        if (this.teamName is TextComponent) {
            return this.teamName.content()
        }
        return this.teamName.toString()
    }
}

final class ResTeamImpl2(
    val teamName: String,
    override val flyLib: FlyLib
) : ResTeam, FlyLibComponent {
    fun getTeam(): Team {
        val t = Bukkit.getScoreboardManager().mainScoreboard.getTeam(teamName)
        if (t != null) return t
        else {
            return Bukkit.getScoreboardManager().mainScoreboard.registerNewTeam(teamName)
        }
    }

    override fun all(): List<SessionSafePlayer> {
        val team = getTeam()
        return team.entries.mapNotNull { Bukkit.getPlayer(it) }.map { SessionSafePlayer(it) }
    }

    override fun getMembers(): List<SessionSafePlayer> {
        throw UnsupportedOperationException("Deprecated")
    }

    override fun add(p: SessionSafePlayer) {
        val team = getTeam()
        val n = p.offlinePlayer().name
        if (n == null) {
            warn("[TeamManager] Can't add Player:${p} to Team:$teamName")
        } else {
            team.addEntry(n)
        }
    }

    override fun remove(p: SessionSafePlayer) {
        val name = p.name()
        if (name == null) {
            warn("[TeamManager] Can't remove Player:${p} from Team:$teamName")
        } else {
            getTeam().removeEntry(name)
        }
    }

    override fun affected(): List<Syncable> {
        throw UnsupportedOperationException("Deprecated")
    }

    override fun effect(syncable: Syncable, isOnOff: OnOff) {
        throw UnsupportedOperationException("Deprecated")
    }

    override fun name() = Component.text(teamName)
}