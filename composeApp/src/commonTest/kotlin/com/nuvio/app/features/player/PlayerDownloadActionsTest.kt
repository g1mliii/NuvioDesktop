package com.nuvio.app.features.player

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PlayerDownloadActionsTest {
    @Test
    fun directFilesAreDownloadableButManifestsAndTorrentsAreNot() {
        assertTrue("https://cdn.example/movie.mkv?token=abc".isSupportedPlayerDownloadUrl())
        assertTrue("http://cdn.example/video".isSupportedPlayerDownloadUrl())
        assertFalse("https://cdn.example/master.m3u8?token=abc".isSupportedPlayerDownloadUrl())
        assertFalse("https://cdn.example/manifest.mpd".isSupportedPlayerDownloadUrl())
        assertFalse("https://cdn.example/source.torrent".isSupportedPlayerDownloadUrl())
        assertFalse("magnet:?xt=urn:btih:test".isSupportedPlayerDownloadUrl())
    }
}
