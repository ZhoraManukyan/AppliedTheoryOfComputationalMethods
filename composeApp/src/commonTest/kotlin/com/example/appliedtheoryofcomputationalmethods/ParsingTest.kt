package com.example.appliedtheoryofcomputationalmethods

import com.example.appliedtheoryofcomputationalmethods.utils.format3
import com.example.appliedtheoryofcomputationalmethods.utils.parseAllDoubles
import com.example.appliedtheoryofcomputationalmethods.utils.parseDoubleLocalized
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ParsingTest {

    @Test
    fun acceptsCommaAsDecimalSeparator() {
        assertEquals(1.5, "1,5".parseDoubleLocalized())
        assertEquals(0.125, " 0.125 ".parseDoubleLocalized())
        assertEquals(-3.0, "-3".parseDoubleLocalized())
    }

    @Test
    fun returnsNullForInvalidInput() {
        assertNull("abc".parseDoubleLocalized())
        assertNull("".parseDoubleLocalized())
    }

    @Test
    fun parsesWholeListOrNothing() {
        assertEquals(listOf(0.0, 0.5, 1.0), listOf("0", "0,5", "1").parseAllDoubles())
        assertNull(listOf("0", "x", "1").parseAllDoubles())
    }

    @Test
    fun format3RoundsToThreeDecimals() {
        assertEquals("0.667", format3(2.0 / 3))
        assertEquals("-202.667", format3(-76 / 0.375))
    }
}
