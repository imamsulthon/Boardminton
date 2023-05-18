package com.imams.boardminton

import com.imams.boardminton.ui.utils.even
import com.imams.boardminton.ui.utils.lastName
import com.imams.boardminton.ui.utils.prettifyName
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

    @Test
    fun check_prettified_naming() {

        val `1name` = "Ahmad"
        Assert.assertEquals(`1name`.prettifyName(), "Ahmad")

        val `2name` = "Ahmad Sugiorto"
        Assert.assertEquals(`2name`.prettifyName(), "Ahmad Sugiorto")

        val `3name` = "Ahmad Rudi Sugiorto"
        Assert.assertEquals(`3name`.prettifyName(), "Ahmad R. Sugiorto")

        val longName = "Muhammad Willow Harding Lescott Baxter"
        Assert.assertEquals(longName.prettifyName(), "M. W. H. L. Baxter")

        val longName2 = "Maitlandwillow HardingLescott"
        Assert.assertEquals(longName2.prettifyName(), "M. HardingLescott")

        val longName3 = "Muhammad Ahsan"
        Assert.assertEquals(longName3.prettifyName(), "Muhammad Ahsan")

        val longName4 = "Hendrarudi Setiawan"
        Assert.assertEquals(longName4.prettifyName(), "H. Setiawan")

        val longName5 = "Alexis Mac Allister"
        Assert.assertEquals(longName5.prettifyName(), "Alexis M. Allister")

    }
}