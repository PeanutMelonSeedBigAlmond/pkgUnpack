package tex

import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.nio.GifSequenceWriter
import tex.data.TEX
import tex.enum.MipmapFormat
import tex.imageconverter.Gray8Converter
import tex.imageconverter.RG88Converter
import tex.imageconverter.RGBA8888Converter
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO
import kotlin.test.assertTrue

object TEXImageConverter {
    fun convertFormat(tex: TEX): MipmapFormat {
        if (tex.isGif) {
            return MipmapFormat.ImageGIF
        }
        val format = tex.firstImage!!.firstMipmap()!!.format
        assertTrue(!format.isCompressed(), "Raw mipmap format must be uncompressed")
        return if (format.isRawFormat()) MipmapFormat.ImagePNG else format
    }

    fun convertToImage(tex: TEX): ImageResult {
        if (tex.isGif) {

            return convertToGif(tex)
        }
        val sourceMipmap = tex.firstImage!!.firstMipmap()!!
        val format = sourceMipmap.format
        assertTrue(!format.isCompressed(), "Raw mipmap format must be uncompressed")

        if (format.isRawFormat()) {
            var image = imageFromRawFormat(format, sourceMipmap.bytes, sourceMipmap.width, sourceMipmap.height)
            if (sourceMipmap.width != tex.texHeader.imageWidth || sourceMipmap.height != tex.texHeader.imageHeight) {
                image = image.getSubimage(0, 0, tex.texHeader.imageWidth, tex.texHeader.imageHeight)
            }
            ByteArrayOutputStream().use {
                ImageIO.write(image, "png", it)
                return ImageResult(
                    bytes = it.toByteArray(),
                    format = MipmapFormat.ImagePNG
                )
            }
        }
        return ImageResult(
            bytes = sourceMipmap.bytes!!,
            format = format
        )
    }

    // TODO: 2021/4/8 可能有问题
    private fun imageFromRawFormat(
        mipmapFormat: MipmapFormat,
        bytes: ByteArray?,
        width: Int,
        height: Int
    ): BufferedImage {
        val flag = when (mipmapFormat) {
            MipmapFormat.R8 -> BufferedImage.TYPE_BYTE_GRAY
            MipmapFormat.RG88, MipmapFormat.RGBA8888 -> BufferedImage.TYPE_4BYTE_ABGR
            else -> throw UnsupportedOperationException("Mipmap format: $mipmapFormat is not supported")
        }
        if (bytes == null) {
            return BufferedImage(width, height, flag)
        }
        val converter = when (mipmapFormat) {
            MipmapFormat.R8 -> Gray8Converter
            MipmapFormat.RG88 -> RG88Converter
            MipmapFormat.RGBA8888 -> RGBA8888Converter
            else -> throw UnsupportedOperationException("Mipmap format: $mipmapFormat is not supported")
        }
        return converter.convert(bytes, width, height, flag)
    }


    private fun convertToGif(tex: TEX): ImageResult {
        val frameFormat = tex.firstImage!!.firstMipmap()!!.format
        assertTrue(frameFormat.isRawFormat(), "Only raw mipmap formats are supported right now while converting gif")
        val sequenceImages = Array<BufferedImage?>(tex.imageContainer.images.size) { null }

        for (i in sequenceImages.indices) {
            val mipmap = tex.imageContainer.images[i].firstMipmap()
            sequenceImages[i] = imageFromRawFormat(frameFormat, mipmap?.bytes, mipmap!!.width, mipmap.height)
        }

        val gif = GifSequenceWriter(tex.frameInfoContainer!!.frames[0].frameTime.toLong(), true)
        val gifSequence = tex.frameInfoContainer?.frames?.map {
            val frame = with(sequenceImages[it.imageId]!!) {
                val cm = colorModel
                val isAlphaPremultiplied = isAlphaPremultiplied
                val raster = copyData(null)
                return@with ImmutableImage.fromAwt(
                    BufferedImage(cm, raster, isAlphaPremultiplied, null).also { image ->
                        image.getSubimage(it.x.toInt(), it.y.toInt(), it.width.toInt(), it.height.toInt())
                    }
                )
            }
            return@map frame
        }?.toTypedArray()
        return ImageResult(
            gif.bytes(gifSequence),
            MipmapFormat.ImageGIF
        )
    }

    data class ImageResult(
        val bytes: ByteArray,
        val format: MipmapFormat,
    )
}