package com.nuvio.app.features.player

import com.nuvio.app.features.downloads.DownloadsRepository
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PlayerDownloadActionsTest {
    @Test
    fun directFilesAreDownloadableButManifestsAndTorrentsAreNot() {
        assertTrue(DownloadsRepository.isDownloadableUrl("https://cdn.example/movie.mkv?token=abc"))
        assertTrue(DownloadsRepository.isDownloadableUrl("http://cdn.example/video"))
        assertFalse(DownloadsRepository.isDownloadableUrl("https://cdn.example/master.m3u8?token=abc"))
        assertFalse(DownloadsRepository.isDownloadableUrl("https://cdn.example/manifest.mpd"))
        assertFalse(DownloadsRepository.isDownloadableUrl("https://cdn.example/source.torrent"))
        assertFalse(DownloadsRepository.isDownloadableUrl("magnet:?xt=urn:btih:test"))
    }
}
