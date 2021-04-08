package pkg.data

data class PKGHeader(
    val magicLength: Int,
    val magic: String,
    val entryCount: Int,
    val entries: MutableList<PKGEntryInfo>,
    val headerLength: Long,
)