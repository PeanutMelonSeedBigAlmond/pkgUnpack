package tex.imageconverter

import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.pixels.Pixel
import java.awt.image.BufferedImage

object Gray8Converter : BaseImageConverter {
    // TODO: 2021/4/8 可能不正确
    override fun convert(pixelData: ByteArray, width: Int, height: Int, colorType: Int): BufferedImage {
        val rgbaLength = pixelData.size
        val pixels = Array<Pixel?>(rgbaLength) {
            val x = it / width
            val y = it % width
            val r = byte2int(pixelData[it * 4])
            return@Array Pixel(
                x,
                y,
                r,
                r,
                r,
                255
            )
        }
        val image = ImmutableImage.create(width, height, pixels)
        return image.toNewBufferedImage(colorType)
    }
}