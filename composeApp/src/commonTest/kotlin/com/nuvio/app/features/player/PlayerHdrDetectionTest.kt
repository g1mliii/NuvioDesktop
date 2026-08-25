package com.nuvio.app.features.player

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class PlayerHdrDetectionTest {
    @Test
    fun identifiesHdrSubtypesFromDecodedMpvMetadata() {
        assertEquals("Dolby Vision", hdrPlaybackBadgeLabel("pq", dolbyVisionProfile = 8))
        assertEquals("Dolby Vision", hdrPlaybackBadgeLabel("pq", videoColorMatrix = "dolbyvision"))
        assertEquals("HDR10+", hdrPlaybackBadgeLabel("pq", hasHdr10PlusMetadata = true))
        assertEquals("HDR10", hdrPlaybackBadgeLabel("pq"))
        assertEquals("HLG", hdrPlaybackBadgeLabel("HLG"))
        assertEquals("HLG", hdrPlaybackBadgeLabel("arib-std-b67"))
        assertEquals("HDR", hdrPlaybackBadgeLabel("smpte-st-2084"))
    }

    @Test
    fun leavesSdrAndMissingTransfersUnbadged() {
        assertNull(hdrPlaybackBadgeLabel(null))
        assertNull(hdrPlaybackBadgeLabel(""))
        assertNull(hdrPlaybackBadgeLabel("bt.1886"))
        assertNull(hdrPlaybackBadgeLabel("srgb"))
    }
}
