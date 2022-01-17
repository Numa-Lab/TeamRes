package net.kunmc.lab.teamres.util.ex

import com.flylib.flylib3.event.ex.ExternalEvent
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerQuitEvent

/**
 * Fires when player is banned
 */
class PlayerBannedEvent(val e: PlayerQuitEvent, val p: Player) : ExternalEvent() {
}

/**
 * Fires when player is unbanned
 * @Note [commandSender] can be not right one.
 */
class PlayerUnbannedEvent(val p: OfflinePlayer, val commandSender: CommandSender) : ExternalEvent() {
}