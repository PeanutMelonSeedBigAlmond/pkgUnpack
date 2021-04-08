package tex.data

data class TEXFrameInfoContainer(
    val magic: String,
    val frames: ArrayList<TEXFrameInfo> = ArrayList(),
    var gifWidth: Int = 0,
    var gifHeight: Int = 0,
)