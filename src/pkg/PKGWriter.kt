package pkg

import pkg.data.PKGEntryInfo
import pkg.data.PKGHeader
import unpackTEXFile
import java.io.File
import java.io.FileOutputStream
import java.io.RandomAccessFile

object PKGWriter {
    fun writePKGEntries(pkgFile: RandomAccessFile, header: PKGHeader, baseDir: String) {
        val entries = header.entries
        val entryCount = header.entryCount
        val headerLength = header.headerLength
        for (i in 0 until entryCount) {
            writePKGEntry(pkgFile, headerLength, entries[i], baseDir)
        }
        unpackTEXFileFromPKGFile(entries as ArrayList<PKGEntryInfo>, baseDir)
    }

    private fun unpackTEXFileFromPKGFile(texFiles: ArrayList<PKGEntryInfo>, baseDir: String) {
        texFiles.filter { File(it.fileName).extension == "tex" }.forEach {
            val mbaseDir = File("$baseDir/${it.fileName}").parent
            unpackTEXFile("$baseDir/${it.fileName}", mbaseDir)
        }
    }

    private fun writePKGEntry(
        pkgFile: RandomAccessFile,
        headerLength: Long,
        entryInfo: PKGEntryInfo,
        baseDir: String
    ) {
        println("Extracting $baseDir/${entryInfo.fileName}")
        pkgFile.seek(headerLength + entryInfo.fileOffset)
        val outFile = File("$baseDir/${entryInfo.fileName}")
        val fileDir = outFile.parentFile
        if (!fileDir.exists()) {
            fileDir.mkdirs()
        }
        val buffer = ByteArray(2048)
        var len = 0
        FileOutputStream(outFile).use { fos ->
            val count = entryInfo.fileLength / buffer.size
            val remain = entryInfo.fileLength % buffer.size
            for (i in 0 until count) {
                pkgFile.read(buffer)
                fos.write(buffer)
            }
            pkgFile.read(buffer, 0, remain)
            fos.write(buffer, 0, remain)
        }
    }
}