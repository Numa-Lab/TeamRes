package net.kunmc.lab.teamres.util.ex

import com.flylib.flylib3.event.ex.ExternalEvent
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityRegainHealthEvent

class PlayerHealEvent(val p: Player, val amount: Double, val e: EntityRegainHealthEvent) : ExternalEvent() {
}

class PlayerDamageEvent(val p: Player, val amount: Double, val e: EntityDamageEvent) : ExternalEvent() {
}