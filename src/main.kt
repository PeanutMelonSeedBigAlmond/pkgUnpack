import pkg.PKGReader
import pkg.PKGWriter
import tex.TEXReader
import tex.TEXWriter
import java.io.File
import java.io.RandomAccessFile

fun main(args: Array<String>) {
    val fileName = if (args.isEmpty()) {
        print("Please input path for *.pkg file or *.tex file: ")
        var input = ""
        while (true) {
            input = readLine()!!.trim()
            if (input != "") {
                break
            }
        }
        input
    } else {
        args[0]
    }
    val file = File(fileName)
    val extension = file.extension
    if (extension == "pkg") {
        unpackPKGFile(fileName)
    } else if (extension == "tex") {
        unpackTEXFile(fileName)
    }
    println("Press Enter key to exit")
    readLine()
}

fun unpackPKGFile(filePath: String, baseDir: String = "./output") = RandomAccessFile(filePath, "r").use {
    val header = PKGReader.readPKGHeader(it)
    PKGWriter.writePKGEntries(it, header, baseDir)
}

fun unpackTEXFile(filePath: String, baseDir: String = "./output") = RandomAccessFile(filePath, "r").use {
    val tex = TEXReader.readTEX(it)
    TEXWriter.writeTEXEntries(tex, filePath, baseDir)
}