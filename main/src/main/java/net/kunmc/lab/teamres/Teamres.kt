package net.kunmc.lab.teamres

import com.flylib.flylib3.FlyLibPlugin
import com.flylib.flylib3.event.ex.FCommandEvent
import com.flylib.flylib3.util.command
import net.kunmc.lab.teamres.gui.SyncableControlGUI
import net.kunmc.lab.teamres.gui.TeamControlGUI
import net.kunmc.lab.teamres.syncable.Syncables
import net.kunmc.lab.teamres.util.SessionSafePlayer
import net.kunmc.lab.teamres.util.ex.TeamResEventEx
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Team
import kotlin.reflect.full.createType

class Teamres : FlyLibPlugin() {
    private val teamManager = TeamManager(flyLib)

    override fun enable() {
        TeamResEventEx(flyLib)
        command("teamres") {
//            part<Syncables>(
//                Syncables::class.createType(),
//                { _: CommandSender, _: Command, _: String, _: Array<out String> ->
//                    Syncables.values().toList()
//                },
//                {
//                    Syncables.getByString(it)
//                },
//                { true }
//            ) {
//                part<OnOff>(
//                    OnOff::class.createType(),
//                    { _: CommandSender, _: Command, _: String, _: Array<out String> -> listOf(OnOff.ON, OnOff.OFF) },
//                    { OnOff.getByString(it) },
//                    lazyMatcher = { true }
//                ) {
//                    terminal {
//                        execute(this@Teamres::setSync)
//                        permission { commandSender, _, _, _ -> commandSender.isOp }
//                    }
//                }
//            }
//
//            part<String>("addToTeam") {
//                part<ResTeam>(
//                    ResTeam::class.createType(),
//                    { _: CommandSender, _: Command, _: String, _: Array<out String> -> teamManager.teams() },
//                    { teamManager.getTeamByName(it) },
//                    lazyMatcher = { true }
//                ) {
//                    part<Player>(
//                        Player::class.createType(),
//                        lazyValues = { _: CommandSender, _: Command, _: String, _: Array<out String> ->
//                            Bukkit.getOnlinePlayers().toList()
//                        },
//                        lazyParser = { Bukkit.getOnlinePlayers().find { p -> p.name == it } },
//                        lazyTabCompleter = { it.name },
//                        lazyMatcher = { true }) {
//                        terminal {
//                            execute(this@Teamres::addToTeam)
//                            permission { commandSender, _, _, _ -> commandSender.isOp }
//                        }
//                    }
//                }
//            }

            part("registerTeam") {
                part<Team>(
                    Team::class.createType(),
                    lazyValues = { _: CommandSender, _: Command, _: String, _: Array<out String> -> Bukkit.getScoreboardManager().mainScoreboard.teams.toList() },
                    lazyParser = { Bukkit.getScoreboardManager().mainScoreboard.teams.first { t -> it == t.name } },
                    lazyTabCompleter = { it.name },
                    lazyMatcher = { true }) {
                    terminal {
                        execute(this@Teamres::registerTeam)
                        permission { commandSender, _, _, _ -> commandSender.isOp }
                    }
                }
            }

            part("gui") {
                terminal {
                    execute(this@Teamres::syncableGUI)
                    permission { commandSender, _, _, _ -> commandSender.isOp }
                }
            }

//            part("teamGUI") {
//                terminal {
//                    execute(this@Teamres::teamGUI)
//                    permission { _, _, _, _ -> true }
//                }
//            }
        }
    }

    fun setSync(e: FCommandEvent, syncable: Syncables, onOff: OnOff): Boolean {
        teamManager.setSync(syncable.lazy.get(Pair(flyLib, teamManager)), onOff)
        return true
    }

    fun addToTeam(e: FCommandEvent, str: String, team: ResTeam, player: Player): Boolean {
        team.add(SessionSafePlayer(player))
        e.commandSender.sendMessage("Player:${player} is registered to team:${team}")
        return true
    }

    fun registerTeam(e: FCommandEvent, str: String, team: Team): Boolean {
        teamManager.genTeam(team.name)
        e.commandSender.sendMessage("Team:${team.name} is registered.")
        return true
    }

    override fun disable() {
    }

    fun syncableGUI(e: FCommandEvent, str: String): Boolean {
        if (e.commandSender is Player) {
            SyncableControlGUI(flyLib, teamManager).gui.open(e.commandSender as Player)
        }
        return true
    }

    fun teamGUI(e: FCommandEvent, str: String): Boolean {
        if (e.commandSender is Player) {
            TeamControlGUI(flyLib, teamManager).gui.open(e.commandSender as Player)
        }
        return true
    }
}