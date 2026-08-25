package com.nuvio.app.features.player

import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Modifier
import com.nuvio.app.features.streams.StreamsUiState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PlayerScreenRuntimeStateTest {

    @Test
    fun sourceFilterUpdatesInvalidateUiWithoutPlaybackUpdates() {
        val runtime = PlayerScreenRuntime(testPlayerScreenArgs())
        val selectedFilter = derivedStateOf { runtime.sourceStreamsState.selectedFilter }

        assertNull(selectedFilter.value)

        runtime.sourceStreamsState = StreamsUiState(selectedFilter = "addon-id")

        assertEquals("addon-id", selectedFilter.value)
    }

    @Test
    fun episodeFilterUpdatesInvalidateUiWithoutPlaybackUpdates() {
        val runtime = PlayerScreenRuntime(testPlayerScreenArgs())
        val selectedFilter = derivedStateOf { runtime.episodeStreamsRepoState.selectedFilter }

        assertNull(selectedFilter.value)

        runtime.episodeStreamsRepoState = StreamsUiState(selectedFilter = "addon-id")

        assertEquals("addon-id", selectedFilter.value)
    }

    @Test
    fun copyStreamLinkUsesPlayerNotificationChannel() {
        val runtime = PlayerScreenRuntime(testPlayerScreenArgs())
        var copiedValue = ""
        runtime.copyToClipboard = { copiedValue = it }
        runtime.streamLinkCopiedLabel = "Stream link copied"

        runtime.copyActiveStreamLink()

        assertEquals("https://example.com/video.mp4", copiedValue)
        assertEquals("Stream link copied", runtime.playerNotificationMessage)
        assertEquals(1L, runtime.playerNotificationToken)
    }

    @Test
    fun copyStreamLinkReportsWhenNoDirectLinkIsAvailable() {
        val runtime = PlayerScreenRuntime(testPlayerScreenArgs())
        var copied = false
        runtime.copyToClipboard = { copied = true }
        runtime.noDirectStreamLinkLabel = "No direct stream link available"
        runtime.activeTorrentInfoHash = "0123456789abcdef"

        runtime.copyActiveStreamLink()

        assertFalse(copied)
        assertEquals("No direct stream link available", runtime.playerNotificationMessage)
        assertEquals(1L, runtime.playerNotificationToken)
    }

    @Test
    fun streamActionsSkipTorrentAndLocalSources() {
        val runtime = PlayerScreenRuntime(testPlayerScreenArgs())

        assertEquals("https://example.com/video.mp4", runtime.activeShareableStreamUrl())
        assertTrue(runtime.canDownloadActiveStream())

        runtime.activeTorrentInfoHash = "0123456789abcdef"
        assertNull(runtime.activeShareableStreamUrl())
        assertFalse(runtime.canDownloadActiveStream())

        runtime.activeTorrentInfoHash = null
        runtime.activeSourceUrl = "file:/downloads/video.mkv"
        assertNull(runtime.activeShareableStreamUrl())
        assertFalse(runtime.canDownloadActiveStream())

        runtime.activeSourceUrl = "https://example.com/master.m3u8"
        assertEquals("https://example.com/master.m3u8", runtime.activeShareableStreamUrl())
        assertFalse(runtime.canDownloadActiveStream())
    }

    @Test
    fun seekScrobbleUpdate_requiresActiveIncompletePlayback() {
        assertTrue(
            shouldUpdateTrackingScrobbleAfterSeek(
                hasActiveScrobble = true,
                progressPercent = 50f,
            ),
        )
        assertFalse(
            shouldUpdateTrackingScrobbleAfterSeek(
                hasActiveScrobble = false,
                progressPercent = 50f,
            ),
        )
        assertFalse(
            shouldUpdateTrackingScrobbleAfterSeek(
                hasActiveScrobble = true,
                progressPercent = 80f,
            ),
        )
    }

    @Test
    fun stopScrobble_closesActiveSessionBelowOnePercent() {
        assertTrue(
            shouldSendStopScrobble(
                hasActiveScrobble = true,
                progressPercent = 0f,
            ),
        )
        assertTrue(
            shouldSendStopScrobble(
                hasActiveScrobble = true,
                progressPercent = 0.5f,
            ),
        )
    }

    @Test
    fun stopScrobble_skipsEarlyProgressWithoutActiveSession() {
        assertFalse(
            shouldSendStopScrobble(
                hasActiveScrobble = false,
                progressPercent = 0.5f,
            ),
        )
        assertFalse(
            shouldSendStopScrobble(
                hasActiveScrobble = false,
                progressPercent = 79.99f,
            ),
        )
    }

    @Test
    fun stopScrobble_allowsCompletionWithoutActiveSession() {
        assertTrue(
            shouldSendStopScrobble(
                hasActiveScrobble = false,
                progressPercent = 80f,
            ),
        )
        assertTrue(
            shouldSendStopScrobble(
                hasActiveScrobble = false,
                progressPercent = 100f,
            ),
        )
    }

    private fun testPlayerScreenArgs() = PlayerScreenArgs(
        profileId = 1,
        title = "Title",
        sourceUrl = "https://example.com/video.mp4",
        sourceAudioUrl = null,
        sourceHeaders = emptyMap(),
        sourceResponseHeaders = emptyMap(),
        streamType = null,
        providerName = "Provider",
        streamTitle = "Source",
        streamSubtitle = null,
        initialBingeGroup = null,
        pauseDescription = null,
        onBack = {},
        onOpenInExternalPlayer = null,
        onOpenExternalUrl = null,
        modifier = Modifier,
        logo = null,
        poster = null,
        background = null,
        seasonNumber = null,
        episodeNumber = null,
        episodeTitle = null,
        episodeThumbnail = null,
        contentType = "movie",
        videoId = "tt1234567",
        parentMetaId = "tt1234567",
        parentMetaType = "movie",
        providerAddonId = null,
        torrentInfoHash = null,
        torrentFileIdx = null,
        torrentFilename = null,
        torrentTrackers = emptyList(),
        initialPositionMs = 0L,
        initialProgressFraction = null,
    )
}
