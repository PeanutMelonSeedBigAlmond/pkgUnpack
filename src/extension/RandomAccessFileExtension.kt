package extension

import java.io.RandomAccessFile

fun RandomAccessFile.readNextString(maxLength: Int): String {
    val sb = StringBuffer()
    var count = 0
    var lastRead: Byte
    do {
        lastRead = readByte()
        if (lastRead != 0.toByte()) {
            sb.append(lastRead.toChar())
            count++
        } else {
            break
        }
    } while (count < maxLength)
    return sb.toString()
}

fun RandomAccessFile.readNextString(): String {
    val sb = StringBuffer()
    var lastRead: Byte
    do {
        lastRead = readByte()
        if (lastRead != 0.toByte()) {
            sb.append(lastRead.toChar())
        } else {
            break
        }
    } while (true)
    return sb.toString()
}

// 转换成小端
fun RandomAccessFile.readInt32(): Int {
    val v1 = readByte().toInt() and 0xff
    val v2 = readByte().toInt() shl 8 and 0xff_00
    val v3 = readByte().toInt() shl 16 and 0xff_00_00
    val v4 = readByte().toInt() shl 24
    return v1 or v2 or v3 or v4
}

fun RandomAccessFile.readUInt32(): UInt {
    return readInt32().toUInt()
}

// 以小端序读取下一个单精度浮点数
fun RandomAccessFile.readSingle(): Float {
    return Float.fromBits(readInt32())
}

