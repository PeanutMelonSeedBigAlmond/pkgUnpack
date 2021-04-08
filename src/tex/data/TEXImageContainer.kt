package tex.data

import tex.enum.ImageFormat
import java.util.*

data class TEXImageContainer(
    val magic: String,
    var imageFormat: ImageFormat = ImageFormat.FIF_UNKNOWN,
    val images: ArrayList<TEXImage> = ArrayList(),
    var imageContainerVersion: TEXImageContainerVersion = TEXImageContainerVersion.Invalid,
)