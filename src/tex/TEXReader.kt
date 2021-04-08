package tex

import extension.readInt32
import extension.readNextString
import extension.readUInt32
import tex.data.*
import tex.data.TEXImageContainerVersion.Companion.toTexImageContainerVersion
import tex.enum.ImageFormat.Companion.toImageFormat
import tex.enum.TEXFlags.Companion.toTexFlags
import tex.enum.TEXFormat
import tex.enum.TEXFormat.Companion.toTexFormat
import java.io.RandomAccessFile
import kotlin.test.assertEquals
import kotlin.test.assertTrue

object TEXReader {
    private var readMipmapBytes = true
    private var decompressMipmapBytes = true

    fun readTEX(file: RandomAccessFile): TEX {
        val texHeader = readTEXHeader(file)
        val imageContainer = readTEXImageContainer(file, texHeader.texFormat)
        val tex = TEX(texHeader, imageContainer)
        if (tex.isGif) {
            tex.frameInfoContainer = TEXFrameContainerReader.readFrameInfoContainer(file)
        }
        return tex
    }

    private fun readTEXHeader(file: RandomAccessFile): TEXHeader {
        val magic1 = file.readNextString()
        val magic2 = file.readNextString()

        assertEquals("TEXV0005", magic1)
        assertEquals("TEXI0001", magic2)

        val texFormat = file.readInt32().toTexFormat()
        val texFlags = file.readInt32().toTexFlags()
        val texWidth = file.readInt32()
        val texHeight = file.readInt32()
        val imageWidth = file.readInt32()
        val imageHeight = file.readInt32()
        val unused = file.readUInt32()
        return TEXHeader(magic1, magic2, texFormat, texFlags, texWidth, texHeight, imageWidth, imageHeight, unused)
    }

    fun readTEXImageContainer(file: RandomAccessFile, texFormat: TEXFormat): TEXImageContainer {
        val container = TEXImageContainer(
            magic = file.readNextString(16)
        )
        val imageCount = file.readInt32()
        assert(imageCount < TEXConstants.maxImageCount) { "Image count exceeds limit: $imageCount" }
        when (container.magic) {
            "TEXB0001", "TEXB0002" -> {
            }
            "TEXB0003" -> container.imageFormat = file.readInt32().toImageFormat()
            else -> {
                throw Exception("Image container magic is invalid")
            }
        }
        container.imageContainerVersion = container.magic.substring(4).toInt().toTexImageContainerVersion()
        container.imageFormat.ensureValid()
        for (i in 0 until imageCount) {
            container.images.add(readTexImage(file, container, texFormat))
        }
        return container
    }

    private fun readTexImage(file: RandomAccessFile, container: TEXImageContainer, texFormat: TEXFormat): TEXImage {
        val mipmapCount = file.readInt32()
        assertTrue(mipmapCount < TEXConstants.maxMipmapCount, "Mipmap count exceeds limit: $mipmapCount")
        val readFunction = pickupReadFunction(container.imageContainerVersion)
        val format = TEXMipmapFormatGetter.getFormatForTex(container.imageFormat, texFormat)
        val image = TEXImage()
        for (i in 0 until mipmapCount) {
            val mipmap = readFunction(file)
            mipmap.format = format

            if (decompressMipmapBytes) {
                TEXMipmapDecompressUtil.decompressMipmap(mipmap)
            }
            image.mipmaps.add(mipmap)
        }
        return image
    }

    private fun pickupReadFunction(containerVersion: TEXImageContainerVersion): (RandomAccessFile) -> TEXMipmap {
        return when (containerVersion) {
            TEXImageContainerVersion.Version1 -> ::readMipmapV1
            TEXImageContainerVersion.Version2, TEXImageContainerVersion.Version3 -> ::readMipmapV2AndV3
            else -> throw Exception("Tex image container version: $containerVersion is not supported!")
        }
    }

    private fun readMipmapV1(file: RandomAccessFile): TEXMipmap {
        return TEXMipmap(
            width = file.readInt32(),
            height = file.readInt32(),
            bytes = readBytes(file)
        )
    }

    private fun readMipmapV2AndV3(file: RandomAccessFile): TEXMipmap {
        return TEXMipmap(
            width = file.readInt32(),
            height = file.readInt32(),
            isLZ4Compressed = file.readInt32() == 1,
            decompressedBytesCount = file.readInt32(),
            bytes = readBytes(file)
        )
    }


    private fun readBytes(file: RandomAccessFile): ByteArray? {
        val byteCount = file.readInt32()
        assertTrue(
            byteCount + file.filePointer <= file.length(),
            "Detected invalid mipmap byte count - exceeds stream length"
        )
        assertTrue(byteCount <= TEXConstants.maxMipmapByteCount)
        if (!readMipmapBytes) {
            file.seek(file.filePointer + byteCount)
            return null
        }
        val bytes = ByteArray(byteCount)
        val bytesRead = file.read(bytes, 0, byteCount)
        assertEquals(byteCount, bytesRead, "Failed to read bytes from stream while reading mipmap")

        return bytes
    }

}