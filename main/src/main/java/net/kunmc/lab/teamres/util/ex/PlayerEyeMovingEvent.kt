package net.kunmc.lab.teamres.util.ex

import com.flylib.flylib3.event.ex.ExternalEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.util.Vector

class PlayerEyeMovingEvent(
    val e: PlayerMoveEvent,
    val fromPitch: Float = e.from.pitch,
    val fromYaw: Float = e.from.yaw,
    val toPitch: Float = e.to.pitch,
    val toYaw: Float = e.to.yaw,
    val deltaPitch: Float = toPitch - fromPitch,
    val deltaYaw: Float = toYaw - fromYaw
) : ExternalEvent() {
}