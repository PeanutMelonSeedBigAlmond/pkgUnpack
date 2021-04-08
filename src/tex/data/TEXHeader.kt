package tex.data

import tex.enum.TEXFlags
import tex.enum.TEXFormat

data class TEXHeader(
    val magic1: String,
    val magic2: String,
    val texFormat: TEXFormat,
    val texFlags: TEXFlags,
    val texWidth: Int,
    val texHeight: Int,
    val imageWidth: Int,
    val imageHeight: Int,
    val unused: UInt,
)