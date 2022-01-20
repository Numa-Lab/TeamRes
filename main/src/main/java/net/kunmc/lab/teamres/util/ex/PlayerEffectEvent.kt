package net.kunmc.lab.teamres.util.ex

import com.flylib.flylib3.event.ex.ExternalEvent
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.entity.EntityPotionEffectEvent
import org.bukkit.event.entity.EntityPotionEffectEvent.Cause.*
import org.bukkit.potion.PotionEffect

/**
 * Fires when player effect is added or removed.
 * This Event is called when player consumes potion/player affected by some reason.
 */
open class PlayerEffectEvent(
    val p: Player,
    val action: EntityPotionEffectEvent.Action,
    open val before: PotionEffect?,
    open val after: PotionEffect?,
    val cause: Cause,
    val event: Cancellable
) : ExternalEvent(), Cancellable {
    enum class Cause {
        AREA_EFFECT_CLOUD,
        ARROW,
        ATTACK,
        BEACON,
        COMMAND,
        CONDUIT,
        CONVERSION,
        DEATH,
        DOLPHIN,
        EXPIRATION,
        FOOD,
        ILLUSION,
        MILK,
        PATROL_CAPTAIN,
        PLUGIN,
        POTION_DRINK,
        POTION_SPLASH,
        SPIDER_SPAWN,
        TOTEM,
        TURTLE_HELMET,
        UNKNOWN,
        VILLAGER_TRADE,
        WITHER_ROSE,
        ;

        companion object {
            fun convert(cause: EntityPotionEffectEvent.Cause): Cause {
                return when (cause) {
                    EntityPotionEffectEvent.Cause.AREA_EFFECT_CLOUD -> AREA_EFFECT_CLOUD
                    EntityPotionEffectEvent.Cause.ARROW -> ARROW
                    EntityPotionEffectEvent.Cause.ATTACK -> ATTACK
                    EntityPotionEffectEvent.Cause.BEACON -> BEACON
                    EntityPotionEffectEvent.Cause.COMMAND -> COMMAND
                    EntityPotionEffectEvent.Cause.CONDUIT -> CONDUIT
                    EntityPotionEffectEvent.Cause.CONVERSION -> CONVERSION
                    EntityPotionEffectEvent.Cause.DEATH -> DEATH
                    EntityPotionEffectEvent.Cause.DOLPHIN -> DOLPHIN
                    EntityPotionEffectEvent.Cause.EXPIRATION -> EXPIRATION
                    EntityPotionEffectEvent.Cause.FOOD -> FOOD
                    EntityPotionEffectEvent.Cause.ILLUSION -> ILLUSION
                    EntityPotionEffectEvent.Cause.MILK -> MILK
                    EntityPotionEffectEvent.Cause.PATROL_CAPTAIN -> PATROL_CAPTAIN
                    EntityPotionEffectEvent.Cause.PLUGIN -> PLUGIN
                    EntityPotionEffectEvent.Cause.POTION_DRINK -> POTION_DRINK
                    EntityPotionEffectEvent.Cause.POTION_SPLASH -> POTION_SPLASH
                    EntityPotionEffectEvent.Cause.SPIDER_SPAWN -> SPIDER_SPAWN
                    EntityPotionEffectEvent.Cause.TOTEM -> TOTEM
                    EntityPotionEffectEvent.Cause.TURTLE_HELMET -> TURTLE_HELMET
                    EntityPotionEffectEvent.Cause.UNKNOWN -> UNKNOWN
                    EntityPotionEffectEvent.Cause.VILLAGER_TRADE -> VILLAGER_TRADE
                    EntityPotionEffectEvent.Cause.WITHER_ROSE -> WITHER_ROSE
                }
            }
        }
    }

    override fun isCancelled(): Boolean = event.isCancelled
    override fun setCancelled(cancel: Boolean) {
        event.isCancelled = cancel
    }
}

class PlayerEffectAddedEvent(
    p: Player, before: PotionEffect?,
    override val after: PotionEffect, cause: Cause, event: Cancellable
) : PlayerEffectEvent(
    p, EntityPotionEffectEvent.Action.ADDED,
    before, after, cause, event
)

class PlayerEffectRemovedEvent(
    p: Player, override val before: PotionEffect, cause: Cause, event: Cancellable
) : PlayerEffectEvent(
    p, EntityPotionEffectEvent.Action.REMOVED,
    before, null, cause, event
)

class PlayerEffectChangedEvent(
    p: Player, override val before: PotionEffect,
    override val after: PotionEffect, cause: Cause, event: Cancellable
) : PlayerEffectEvent(
    p, EntityPotionEffectEvent.Action.CHANGED,
    before, after, cause, event
)

class PlayerEffectClearedEvent(
    p: Player, override val before: PotionEffect,
    cause: Cause, event: Cancellable
) : PlayerEffectEvent(
    p, EntityPotionEffectEvent.Action.CHANGED,
    before, null, cause, event
)