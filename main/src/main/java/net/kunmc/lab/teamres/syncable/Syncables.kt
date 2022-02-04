@file:Suppress("SpellCheckingInspection")

package net.kunmc.lab.teamres.syncable

import com.flylib.flylib3.FlyLib
import net.kunmc.lab.teamres.TeamManager
import net.kunmc.lab.teamres.util.LazyWithInit
import net.kunmc.lab.teamres.util.lazyInit
import net.kyori.adventure.text.Component

/**
 * Enum of all syncable types.
 */
// TODO 説明文長すぎ
enum class Syncables(
    val displayName: String,
    val lazy: LazyWithInit<Syncable, Pair<FlyLib, TeamManager>>,
    val descriptions: List<Component> = listOf(),
    // 特にバグなく使えるかどうか
    // falseだと一斉ONのコマンドでONにならない
    val isUseful: Boolean = true
) {
    Health(
        "体力",
        lazyInit { HealthSync(it.first, it.second) },
        listOf(
            "体力を連帯責任に！",
            "チーム内で体力の増減少を同期！"
        ).map { Component.text(it) }),
    Ban(
        "BAN",
        lazyInit { BANSync(it.first, it.second) },
        listOf(
            "BANを連帯責任に！",
            "チーム内の誰かがBANされると",
            "チーム全員がBANされます!"
        ).map { Component.text(it) }),
    Effect(
        "ポーション効果",
        lazyInit { EffectSync(it.first, it.second) },
        listOf(
            "ポーション効果を連帯責任に！",
            "誰かのポーション効果を",
            "ほかのチームメンバーにも！"
        ).map { Component.text(it) }),
    Chat(
        "チャット",
        lazyInit { ChatSync(it.first, it.second) },
        listOf(
            "チャットを連帯責任に！",
            "チャットをすると名前がチーム名に！",
            "§b名前の部分にマウスカーソルを合わせると..."
        ).map { Component.text(it) }),
    DeathSync(
        "死亡",
        lazyInit { DeathSync(it.first, it.second) },
        listOf(
            "死を連帯責任に！",
            "チームの誰かが死ぬとメンバー全員が死！"
        ).map { Component.text(it) }),
    JumpSync(
        "ジャンプ",
        lazyInit { JumpSync(it.first, it.second) },
        listOf(
            "ジャンプを連帯責任に！",
            "チームの誰かがジャンプすると",
            "ほかのメンバーもジャンプします"
        ).map { Component.text(it) }),
    EyeSync(
        "視点",
        lazyInit { EyeSync(it.first, it.second) },
        listOf(
            "視点移動を連帯責任に！",
            "チーム内の誰かが視点移動をすると",
            "ほかのメンバーも同じ動きをします",
            "§e§n§l注意:かなりガクつきます",
            "§c§n§l注意:ONにするとこの画面は開けません",
            "§c§n§lOFF用:/teamres off",
        ).map { Component.text(it) },
        false
    ),
    MoveSync(
        "移動",
        lazyInit { MoveSync(it.first, it.second) },
        listOf(
            "移動を連帯責任に！",
            "チーム内の誰かが移動をすると",
            "ほかのメンバーも同じ動きをします",
            "§e§n§l注意:かなりガクつきます",
            "§c§n§l注意:ONにするとこの画面は開けません",
            "§c§n§lOFF用:/teamres off",
        ).map { Component.text(it) },
        false
    ),
    InventorySync(
        "インベントリ",
        lazyInit { InventorySync(it.first, it.second) },
        listOf(
            "インベントリを連帯責任に!",
            "インベントリが共同になります"
        ).map { Component.text(it) }
    )
    ;

    companion object {
        fun getByString(name: String): Syncables? {
            return values().find { it.displayName == name }
        }
    }

    override fun toString(): String {
        return displayName
    }
}