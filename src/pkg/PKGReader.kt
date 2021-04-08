package pkg

import extension.readInt32
import extension.readNextString
import pkg.data.PKGEntryInfo
import pkg.data.PKGHeader
import java.io.RandomAccessFile

object PKGReader {
    private fun readPKGEntry(file: RandomAccessFile): PKGEntryInfo {
        val fileNameLength = file.readInt32()
        return PKGEntryInfo(
            fileName = file.readNextString(fileNameLength),
            fileOffset = file.readInt32(),
            fileLength = file.readInt32()
        )
    }

    fun readPKGEntries(file: RandomAccessFile, entryCount: Int): MutableList<PKGEntryInfo> {
        return MutableList(entryCount) {
            readPKGEntry(file)
        }
    }

    fun readPKGHeader(file: RandomAccessFile): PKGHeader {
        val magicLength = file.readInt32()
        val magic = file.readNextString(magicLength)
        val entryCount = file.readInt32()
        val entries = readPKGEntries(file, entryCount)
        val headerLength = file.filePointer
        return PKGHeader(magicLength, magic, entryCount, entries, headerLength)
    }
}