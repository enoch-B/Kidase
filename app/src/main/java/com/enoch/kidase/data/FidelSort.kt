package com.enoch.kidase.data

object FidelSort {
    val fidelOrder = listOf(
        'ሀ', 'ለ', 'ሐ', 'መ', 'ሠ', 'ረ', 'ሰ', 'ሸ', 'ቀ', 'በ', 'ቨ', 'ተ', 'ቸ', 'ኀ', 'ነ', 'ኘ',
        'አ', 'ከ', 'ኸ', 'ወ', 'ዐ', 'ዘ', 'ዠ', 'የ', 'ደ', 'ጀ', 'ገ', 'ጠ', 'ጨ', 'ጰ', 'ጸ', 'ፀ', 'ፈ', 'ፐ'
    )

    fun getBaseChar(c: Char): Char {
        val code = c.code
        if (code !in 0x1200..0x137F) return c

        // Clear the last 3 bits to get the family's base character
        val familyBase = (code and 0xFFF8).toChar()

        // Map extended characters and labiovelars to the standard 33 base consonants
        return when (familyBase) {
            '\u1248', '\u1250', '\u1258' -> 'ቀ' // ቈ, ቐ, ቘ -> ቀ
            '\u1288' -> 'ኀ' // ኈ -> ኀ
            '\u12B8' -> 'ኸ' // ዀ -> ኸ
            '\u12C0' -> 'ከ' // ኰ -> ከ
            '\u1300' -> 'ገ' // ጐ -> ገ
            else -> if (fidelOrder.contains(familyBase)) familyBase else c
        }
    }

    fun groupAndSortByFidel(clips: List<ClipEntry>): List<Pair<Char, List<ClipEntry>>> {
        val grouped = clips.groupBy { entry ->
            val firstChar = entry.displayText.firstOrNull() ?: ' '
            getBaseChar(firstChar)
        }

        return grouped.entries
            .sortedBy { (char, _) ->
                val index = fidelOrder.indexOf(char)
                if (index != -1) index else Int.MAX_VALUE
            }
            .map { it.key to it.value }
    }
}
