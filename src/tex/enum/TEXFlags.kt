package tex.enum

enum class TEXFlags(val value: Int) {
    None(0),
    NoInterpolation(1),
    ClampUVs(2),
    IsGif(4),
    Unk3(8),
    Unk4(16),
    Unk5(32),
    Unk6(64),
    Unk7(128);

    companion object {
        fun Int.toTexFlags() = values().first { it.value == this }
    }
}