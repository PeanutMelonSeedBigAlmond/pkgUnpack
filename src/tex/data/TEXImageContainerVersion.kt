package tex.data

enum class TEXImageContainerVersion(val value: Int) {
    // default, placeholder
    Invalid(0),
    Version1(1),
    Version2(2),
    Version3(3);

    companion object {
        fun Int.toTexImageContainerVersion() = values().first { it.value == this }
    }
}