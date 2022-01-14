package net.kunmc.lab.teamres.util

import com.flylib.flylib3.FlyLib

class LazyWithInit<V : Any, P : Any>(private val initer: (P) -> V) {
    private var value: V? = null
    fun get(p: P): V {
        if (value == null) {
            value = initer(p)
        }
        return value!!
    }
}

fun <P : Any, V : Any> lazyInit(lambda: (P) -> V): LazyWithInit<V, P> = LazyWithInit(lambda)