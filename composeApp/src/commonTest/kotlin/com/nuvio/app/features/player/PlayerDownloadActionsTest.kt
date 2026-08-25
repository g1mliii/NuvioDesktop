package com.nuvio.app.features.player

import com.nuvio.app.features.downloads.isSupportedDownloadUrl
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PlayerDownloadActionsTest {
    @Test
    fun directFilesAreDownloadableButManifestsAndTorrentsAreNot() {
        assertTrue("https://cdn.example/movie.mkv?token=abc".isSupportedDownloadUrl())
        assertTrue("http://cdn.example/video".isSupportedDownloadUrl())
        assertFalse("https://cdn.example/master.m3u8?token=abc".isSupportedDownloadUrl())
        assertFalse("https://cdn.example/manifest.mpd".isSupportedDownloadUrl())
        assertFalse("https://cdn.example/source.torrent".isSupportedDownloadUrl())
        assertFalse("magnet:?xt=urn:btih:test".isSupportedDownloadUrl())
    }
}
