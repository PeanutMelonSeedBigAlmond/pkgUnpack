package tex.data

import tex.enum.MipmapFormat

data class TEXMipmap(
    var bytes: ByteArray?,
    val width: Int,
    val height: Int,
    var decompressedBytesCount: Int = 0,
    var isLZ4Compressed: Boolean = false,
    var format: MipmapFormat = MipmapFormat.Invalid,
)