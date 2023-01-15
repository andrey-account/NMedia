package ru.netology.nmedia

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("ru.netology.nmedia", appContext.packageName)
    }

//Tests for fun likeText(likeClickCount)  for like button
@Test
fun tenLikes() {
    likeClickCount = 10 //The value of the counter that the function is being tested with
    val expected = "10"
    val actual = likeText(likeClickCount)
    assertEquals(expected, actual)
}

    @Test
    fun thousandLikes() {
        likeClickCount = 1000
        val expected = "1K"
        val actual = likeText(likeClickCount)
        assertEquals(expected, actual)
    }

    @Test
    fun thousandAndHundredLikes() {
        likeClickCount = 1100
        val expected = "1.1K"
        val actual = likeText(likeClickCount)
        assertEquals(expected, actual)
    }

    @Test
    fun tenThousandLikes() {
        likeClickCount = 10_000
        val expected = "10K"
        val actual = likeText(likeClickCount)
        assertEquals(expected, actual)
    }

    @Test
    fun millionLikes() {
        likeClickCount = 1_000_000
        val expected = "1M"
        val actual = likeText(likeClickCount)
        assertEquals(expected, actual)
    }

    @Test
    fun millionAndThousandLikes() {
        likeClickCount = 1_100_000
        val expected = "1.1M"
        val actual = likeText(likeClickCount)
        assertEquals(expected, actual)
    }


//---------------------------------------------------------------------------------------------
// Tests for fun likeText(shareClickCount)  for share button

    @Test
    fun tenShares() {
        shareClickCount = 10
        val expected = "10"
        val actual = likeText(shareClickCount)
        assertEquals(expected, actual)
    }
    @Test
    fun thousandShares() {
        shareClickCount = 1000
        val expected = "1K"
        val actual = likeText(shareClickCount)
        assertEquals(expected, actual)
    }

    @Test
    fun thousandAndHundredShares() {
        shareClickCount = 1100
        val expected = "1.1K"
        val actual = likeText(shareClickCount)
        assertEquals(expected, actual)
    }

    @Test
    fun tenThousandShares() {
        shareClickCount = 10_000
        val expected = "10K"
        val actual = likeText(shareClickCount)
        assertEquals(expected, actual)
    }

    @Test
    fun millionShares() {
        shareClickCount = 1_000_000
        val expected = "1M"
        val actual = likeText(shareClickCount)
        assertEquals(expected, actual)
    }

    @Test
    fun millionAndThousandShares() {
        shareClickCount = 1_100_000
        val expected = "1.1M"
        val actual = likeText(shareClickCount)
        assertEquals(expected, actual)
    }


//---------------------------------------------------------------------------------------------
// Tests for fun likeText(lookClickCount)  for look button


    @Test
    fun lookTen() {
        lookClickCount = 10
        val expected = "10"
        val actual = likeText(lookClickCount)
        assertEquals(expected, actual)
    }
    @Test
    fun lookThousand() {
        lookClickCount = 1000
        val expected = "1K"
        val actual = likeText(lookClickCount)
        assertEquals(expected, actual)
    }

    @Test
    fun lookThousandAndHundred() {
        lookClickCount = 1100
        val expected = "1.1K"
        val actual = likeText(lookClickCount)
        assertEquals(expected, actual)
    }

    @Test
    fun lookTenThousand() {
        lookClickCount = 10_000
        val expected = "10K"
        val actual = likeText(lookClickCount)
        assertEquals(expected, actual)
    }

    @Test
    fun lookMillion() {
        lookClickCount = 1_000_000
        val expected = "1M"
        val actual = likeText(lookClickCount)
        assertEquals(expected, actual)
    }

    @Test
    fun lookMillionAndThousand() {
        lookClickCount = 1_100_000
        val expected = "1.1M"
        val actual = likeText(lookClickCount)
        assertEquals(expected, actual)
    }
}