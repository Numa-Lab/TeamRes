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
    val descriptions: List<Component> = listOf()
) {
    Health(
        "体力",
        lazyInit { HealthSync(it.first, it.second) },
        listOf("プレイヤーの体力を連帯責任に！", "チーム内で体力の増減少を同期！").map { Component.text(it) }),
    Ban(
        "BAN",
        lazyInit { BANSync(it.first, it.second) },
        listOf("BANを連帯責任に！", "チーム内の誰かがBANされるとチーム内全員がBANされます!").map { Component.text(it) }),
    Effect(
        "ポーション効果",
        lazyInit { EffectSync(it.first, it.second) },
        listOf("ポーション効果を連帯責任に！", "誰かがポーション効果を受けたとき/失ったとき", "ほかのチームメンバーにも同じことが起こります！").map { Component.text(it) }),
    Chat(
        "チャット",
        lazyInit { ChatSync(it.first, it.second) },
        listOf(
            "チャットを連帯責任に！",
            "チャットをすると名前の部分がチーム名になります",
            "Tips:名前の部分にマウスカーソルを合わせると発言者がわかります"
        ).map { Component.text(it) }),
    DeathSync(
        "死",
        lazyInit { DeathSync(it.first, it.second) },
        listOf("プレイヤーの死を連帯責任に！", "チーム内の誰かが死ぬとほかのメンバー全員が死にます").map { Component.text(it) }),
    JumpSync(
        "ジャンプ",
        lazyInit { JumpSync(it.first, it.second) },
        listOf("プレイヤーのジャンプを連帯責任に！", "チーム内の誰かがジャンプするとほかのメンバーもジャンプします").map { Component.text(it) }),
    EyeSync(
        "視点",
        lazyInit { EyeSync(it.first, it.second) },
        listOf("視点移動を連帯責任に！", "チーム内の誰かが視点移動をするとほかのメンバーも同じ動きをします").map { Component.text(it) }),
    MoveSync(
        "移動",
        lazyInit { MoveSync(it.first, it.second) },
        listOf(
            "プレイヤーの移動を連帯責任に！",
            "チーム内の誰かが移動をするとほかのメンバーも同じ動きをします",
            "注意:ジャンプはこれ単体では同期しません",
            "ジャンプの同期を入れない時はチームのほかの人が引っかかるので注意"
        ).map { Component.text(it) }),
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