package tex.imageconverter

import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.pixels.Pixel
import java.awt.image.BufferedImage

object RG88Converter : BaseImageConverter {
    // [pixelData]的每两字节分别是 灰度，A
    // TODO: 2021/4/8 可能不正确
    override fun convert(pixelData: ByteArray, width: Int, height: Int, colorType: Int): BufferedImage {
        val rgbaLength = pixelData.size / 2
        val pixels = Array<Pixel?>(rgbaLength) {
            val x = it / width
            val y = it % width
            val r = byte2int(pixelData[it * 4 + 0])
            val a = byte2int(pixelData[it * 4 + 1])
            return@Array Pixel(
                x,
                y,
                r,
                r,
                r,
                a
            )
        }
        val image = ImmutableImage.create(width, height, pixels)
        return image.toNewBufferedImage(colorType)
    }
}