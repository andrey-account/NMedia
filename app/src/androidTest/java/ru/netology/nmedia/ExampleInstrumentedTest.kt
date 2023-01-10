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

    @Test
    fun heartTen() {
        heartClickCount = 10 //Значение счётчика, с которым тестируется функция
        val expected = "11" //Функция heartIncrement() увеличит счётчик на 1 и преобразует число в строку
        val actual = heartIncrement()
        assertEquals(expected, actual)
    }

    @Test
    fun heartThousand() {
        heartClickCount = 1000 //Значение счётчика, с которым тестируется функция
        val expected = "1K" //Функция heartIncrement() увеличит счётчик на 1 и преобразует число в строку
        val actual = heartIncrement()
        assertEquals(expected, actual)
    }

    @Test
    fun heartThousandAndHundred() {
        heartClickCount = 1100 //Значение счётчика, с которым тестируется функция
        val expected = "1.1K" //Функция heartIncrement() увеличит счётчик на 1 и преобразует число в строку
        val actual = heartIncrement()
        assertEquals(expected, actual)
    }

    @Test
    fun heartTenThousand() {
        heartClickCount = 10_000 //Значение счётчика, с которым тестируется функция
        val expected = "10K" //Функция heartIncrement() увеличит счётчик на 1 и преобразует число в строку
        val actual = heartIncrement()
        assertEquals(expected, actual)
    }

    @Test
    fun heartMillion() {
        heartClickCount = 1_000_000 //Значение счётчика, с которым тестируется функция
        val expected = "1M" //Функция heartIncrement() увеличит счётчик на 1 и преобразует число в строку
        val actual = heartIncrement()
        assertEquals(expected, actual)
    }
    @Test
    fun heartMillionAndThousand() {
        heartClickCount = 1_100_000 //Значение счётчика, с которым тестируется функция
        val expected = "1.1M" //Функция heartIncrement() увеличит счётчик на 1 и преобразует число в строку
        val actual = heartIncrement()
        assertEquals(expected, actual)
    }


//---------------------------------------------------------------------------------------------

//Tests for fun shareIncrement
    @Test
    fun shareTen() {
        shareClickCount = 10
        val expected = "11"
        val actual = shareIncrement()
        assertEquals(expected, actual)
    }
    @Test
    fun shareThousand() {
        shareClickCount = 1000
        val expected = "1K"
        val actual = shareIncrement()
        assertEquals(expected, actual)
    }

    @Test
    fun shareThousandAndHundred() {
        shareClickCount = 1100
        val expected = "1.1K"
        val actual = shareIncrement()
        assertEquals(expected, actual)
    }

    @Test
    fun shareTenThousand() {
        shareClickCount = 10_000
        val expected = "10K"
        val actual = shareIncrement()
        assertEquals(expected, actual)
    }

    @Test
    fun shareMillion() {
        shareClickCount = 1_000_000
        val expected = "1M"
        val actual = shareIncrement()
        assertEquals(expected, actual)
    }

    @Test
    fun shareMillionAndThousand() {
        shareClickCount = 1_100_000
        val expected = "1.1M"
        val actual = shareIncrement()
        assertEquals(expected, actual)
    }



//---------------------------------------------------------------------------------------------

// Tests for fun lookIncrement
    @Test
    fun lookTen() {
        lookClickCount = 10
        val expected = "11"
        val actual = lookIncrement()
        assertEquals(expected, actual)
    }
    @Test
    fun lookThousand() {
        lookClickCount = 1000
        val expected = "1K"
        val actual = lookIncrement()
        assertEquals(expected, actual)
    }

    @Test
    fun lookThousandAndHundred() {
        lookClickCount = 1100
        val expected = "1.1K"
        val actual = lookIncrement()
        assertEquals(expected, actual)
    }

    @Test
    fun lookTenThousand() {
        lookClickCount = 10_000
        val expected = "10K"
        val actual = lookIncrement()
        assertEquals(expected, actual)
    }

    @Test
    fun lookMillion() {
        lookClickCount = 1_000_000
        val expected = "1M"
        val actual = lookIncrement()
        assertEquals(expected, actual)
    }

    @Test
    fun lookMillionAndThousand() {
        lookClickCount = 1_100_000
        val expected = "1.1M"
        val actual = lookIncrement()
        assertEquals(expected, actual)
    }
}