package net.kunmc.lab.teamres

import com.flylib.flylib3.FlyLibPlugin
import com.flylib.flylib3.event.ex.FCommandEvent
import com.flylib.flylib3.util.command
import net.kunmc.lab.teamres.syncable.Syncable
import net.kunmc.lab.teamres.syncable.Syncables
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import kotlin.reflect.full.createType

class Teamres : FlyLibPlugin() {
    private val teamManager = TeamManager(flyLib)

    override fun enable() {
        command("teamres") {
            part<Syncables>(
                Syncable::class.createType(),
                { _: CommandSender, _: Command, _: String, _: Array<out String> ->
                    Syncables.values().toList()
                },
                {
                    Syncables.getByString(it)
                },
                { true }
            ) {
                part<OnOff>(
                    OnOff::class.createType(),
                    { _: CommandSender, _: Command, _: String, _: Array<out String> -> listOf(OnOff.ON, OnOff.OFF) },
                    { OnOff.getByString(it) },
                    { true }
                ) {
                    terminal {
                        // TODO Set State of Syncable
                        execute(this@Teamres::setSync)
                        permission { commandSender, _, _, _ -> commandSender.isOp }
                    }
                }
            }

            part<String>("addToTeam") {
                part<ResTeam>(
                    ResTeam::class.createType(),
                    { _: CommandSender, _: Command, _: String, _: Array<out String> -> teamManager.teams() },
                    { teamManager.getTeamByName(it) },
                    { true }
                ) {
                    part<Player>(
                        Player::class.createType(),
                        { _: CommandSender, _: Command, _: String, _: Array<out String> ->
                            Bukkit.getOnlinePlayers().toList()
                        },
                        { Bukkit.getOnlinePlayers().find { p -> p.name == it } },
                        { true }) {
                        terminal {
                            execute(this@Teamres::addToTeam)
                        }
                    }
                }
            }
        }
    }

    private fun setSync(e: FCommandEvent, syncable: Syncable, onOff: OnOff): Boolean {
        teamManager.setSync(syncable, onOff)
        return true
    }

    private fun addToTeam(e: FCommandEvent, team: ResTeam, player: Player): Boolean {
        team.add(player)
        return true
    }

    override fun disable() {
    }
}