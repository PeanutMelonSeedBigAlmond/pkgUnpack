package tex.imageconverter

import java.awt.image.BufferedImage

@FunctionalInterface
interface BaseImageConverter {
    fun convert(
        pixelData: ByteArray,
        width: Int,
        height: Int,
        colorType: Int
    ): BufferedImage

    fun byte2int(b: Byte): Int = b.toInt() and 0xff
}