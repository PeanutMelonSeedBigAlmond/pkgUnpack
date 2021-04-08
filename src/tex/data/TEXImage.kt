package tex.data

data class TEXImage(
    val mipmaps: ArrayList<TEXMipmap> = ArrayList(),
) {
    fun firstMipmap() = mipmaps.firstOrNull()
}