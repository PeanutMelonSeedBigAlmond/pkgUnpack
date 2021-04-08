package tex.enum

enum class TEXFormat(val value: Int) {
    RGBA8888(0),
    DXT5(4),
    DXT3(6),
    DXT1(7),
    RG88(8),
    R8(9);

    companion object {
        fun Int.toTexFormat() = values().first { it.value == this }
    }
}