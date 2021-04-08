package tex.data

import tex.enum.TEXFlags

data class TEX(
    val texHeader: TEXHeader,
    val imageContainer: TEXImageContainer,
    var frameInfoContainer: TEXFrameInfoContainer? = null,
) {
    val isGif: Boolean
    val firstImage: TEXImage?

    init {
        isGif = hasFlag(TEXFlags.IsGif)
        firstImage = imageContainer.images.firstOrNull()
    }

    fun hasFlag(flags: TEXFlags): Boolean = texHeader.texFlags.value and flags.value == flags.value
}
