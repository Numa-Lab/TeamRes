package net.kunmc.lab.teamres.syncable

import com.flylib.flylib3.FlyLib
import com.flylib.flylib3.util.nextTick
import io.papermc.paper.chat.ChatRenderer
import io.papermc.paper.event.player.AsyncChatEvent
import net.kunmc.lab.teamres.TeamManager
import net.kunmc.lab.teamres.syncable.base.BaseSync
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.event.HoverEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class ChatSync(flyLib: FlyLib, teamManager: TeamManager) : BaseSync(flyLib, teamManager), Listener {
    init {
        flyLib.plugin.server.pluginManager.registerEvents(this, flyLib.plugin)
    }

    @EventHandler
    fun onChat(e: AsyncChatEvent) {
        val p = getWithPlayer(e.player) ?: return
        setChatRenderer(e, p.second.teamName)
    }

    private fun setChatRenderer(e: AsyncChatEvent, teamName: Component) {
        e.renderer(ChatRenderer.viewerUnaware { _, _, message ->
            getTeamNameComponent(teamName, e.player.name)
                .append(message)
        })
    }

    private fun getTeamNameComponent(teamName: Component, playerName: String): TextComponent {
        return Component.text()
            .content("<")
            .append(teamName)
            .append(Component.text(">"))
            .hoverEvent(HoverEvent.showText(Component.text(playerName))).build()
    }

    private fun a(): TextComponent {
        return Component.text()
            .content("AAAAA")
            .hoverEvent(HoverEvent.showText(Component.text("AAAAAAAA")))
            .build()
    }
}