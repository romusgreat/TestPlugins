package com.lagradost.cloudstream3.plugins

import com.lagradost.cloudstream3.*
import com.lagradost.cloudstream3.utils.ExtractorLink
import com.lagradost.cloudstream3.utils.loadExtractor

class GaragebandProvider : MainAPI() {
    // السيرفر الأساسي اللي استخرجناه من الإضافة
    override var mainUrl = "https://proxy.garageband.rocks"
    override var name = "IMDbPlay"
    override val hasMainPage = false
    override val supportedTypes = setOf(TvType.Movie, TvType.TvSeries)

    // هذه الدالة تعادل منطق بناء الرابط الموجود في JavaScript
    override suspend fun loadLinks(
        data: String, 
        isCasting: Boolean, 
        subtitleCallback: (SubtitleFile) -> Unit, 
        callback: (ExtractorLink) -> Unit
    ): Boolean {
        // في CloudStream، المتغير "data" سيحتوي على رابط أو رقم الـ IMDb الممرر من التطبيق
        // نفترض هنا أننا مررنا الـ ID مع نوع المحتوى (مثال: "tt1375666,movie" أو "tt1375666,tv")
        
        val parts = data.split(",")
        if (parts.size < 2) return false
        
        val imdbId = parts[0]
        val mediaType = parts[1] // "movie" أو "tv"

        // بناء الرابط بنفس طريقة الإضافة بالضبط
        val videoUrl = if (mediaType == "tv") {
            "$mainUrl/embed/tv/$imdbId?autonext=1"
        } else {
            "$mainUrl/embed/movie/$imdbId"
        }

        // في الإضافة، يتم عرض الرابط في iframe. 
        // في CloudStream، نستخدم دالة loadExtractor لتدخل إلى الرابط وتستخرج ملف الفيديو الفعلي (.mp4 أو .m3u8)
        loadExtractor(videoUrl, mainUrl, subtitleCallback, callback)
        
        return true
    }
}
