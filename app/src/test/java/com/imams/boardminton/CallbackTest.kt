package com.imams.boardminton

import com.imams.boardminton.domain.model.ITeam
import org.junit.Assert
import org.junit.Test

class CallbackTest {


    @Test
    fun testCallback() {
        var n = 5
        val tl = TopListener(
            onReset = { n -= it },
            onEdit = { n += 1 },
            onSwap = {
                n += 2
            },
            onPause = { 1 },
        )
        someFunction(2, tl)
        Assert.assertEquals(n, 6)
    }

    @Test
    fun testSealedCallback() {

        val ml = object : MidListener {
            override val swap: (Int) -> Unit
                get() = TODO("Not yet implemented")
            override val onEdit: () -> Unit
                get() = TODO("Not yet implemented")

            override fun onReset(): (Int) -> Unit = {

            }

        }

    }

}

fun someFunction(n: Int, l: TopListener) {
    l.onEdit.invoke()
    l.onReset.invoke(n)
    val x = l.onPause.invoke()
}

fun otherFunction(bl: (BottomListener) -> Unit) {
    bl.invoke(BottomListener.Clear)
    bl.invoke(BottomListener.Plus(ITeam.A1))
}

data class TopListener(
    val onSwap: () -> Unit,
    val onEdit: () -> Unit,
    val onReset: (Int) -> Unit,
    val onPause: () -> Int
)

interface MidListener {
    val swap: (Int) -> Unit
    val onEdit: () -> Unit
    fun onReset(): (Int) -> Unit
}

sealed class BottomListener {
    data class Plus(val iTeam: ITeam): BottomListener()
    object Min: BottomListener()
    object Clear: BottomListener()
}
