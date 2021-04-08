package tex.enum

enum class DXTFlags(val value: Int) {
    DX1(1),
    DX3(1 shl 1),
    DX5(1 shl 2);

    companion object {
        fun Int.toDXTFlags() = values().first { it.value == this }
    }

    infix fun and(another: DXTFlags) = this.value and another.value

    infix fun and(value: Int) = this.value and value

    infix fun or(another: DXTFlags) = this.value or another.value
}