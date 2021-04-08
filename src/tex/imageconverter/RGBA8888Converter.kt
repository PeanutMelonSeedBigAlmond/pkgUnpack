package tex.imageconverter

import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.pixels.Pixel
import java.awt.image.BufferedImage

object RGBA8888Converter : BaseImageConverter {
    // [pixelData]的每四字节分别是R,G,B,A
    override fun convert(pixelData: ByteArray, width: Int, height: Int, colorType: Int): BufferedImage {
        val rgbaLength = pixelData.size / 4
        val pixels = Array<Pixel?>(rgbaLength) {
            val x = it / width
            val y = it % width
            val r = byte2int(pixelData[it * 4 + 0])
            val g = byte2int(pixelData[it * 4 + 1])
            val b = byte2int(pixelData[it * 4 + 2])
            val a = byte2int(pixelData[it * 4 + 3])
            return@Array Pixel(
                x,
                y,
                r,
                g,
                b,
                a
            )
        }
        val image = ImmutableImage.create(width, height, pixels)
        return image.toNewBufferedImage(colorType)
    }
}