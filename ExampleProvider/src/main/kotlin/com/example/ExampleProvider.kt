package com.example

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.loadExtractor

class ExampleProvider : MainAPI() {
    override var mainUrl = "https://proxy.garageband.rocks"
    override var name = "IMDbPlay"
    override val hasMainPage = false
    override val supportedTypes = setOf(TvType.Movie, TvType.TvSeries)

    override suspend fun loadLinks(
        data: String, 
        isCasting: Boolean, 
        subtitleCallback: (SubtitleFile) -> Unit, 
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        val parts = data.split(",")
        if (parts.size < 2) return false
        
        val imdbId = parts[0]
        val mediaType = parts[1]

        val videoUrl = if (mediaType == "tv") {
            "$mainUrl/embed/tv/$imdbId?autonext=1"
        } else {
            "$mainUrl/embed/movie/$imdbId"
        }

        loadExtractor(videoUrl, mainUrl, subtitleCallback, callback)
        return true
    }
}
