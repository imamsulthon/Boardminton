package com.imams.boardminton

import com.imams.boardminton.ui.even
import com.imams.boardminton.ui.lastName
import org.junit.Assert
import org.junit.Test

class UtilsTest {

    @Test
    fun check_score_number_is_odd_or_even() {
        val zero = 0
        val odd = 5
        val even = 8

        Assert.assertEquals(zero.even(), true)
        Assert.assertEquals(odd.even(), false)
        Assert.assertEquals(even.even(), true)
    }

    @Test
    fun check_last_name() {

        val aName = "Muhammad"
        Assert.assertEquals(aName.lastName(), "Muhammad")

        val fullName = "Muhammad Kadafi"
        Assert.assertEquals(fullName.lastName(), "Kadafi")

        val fullNameLong = "Muhammad Kadafi Asrozi"
        Assert.assertEquals(fullNameLong.lastName(), "Asrozi")

    }

}