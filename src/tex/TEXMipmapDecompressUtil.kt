package tex

import net.jpountz.lz4.LZ4Factory
import tex.data.TEXMipmap
import tex.enum.DXTFlags
import tex.enum.MipmapFormat

object TEXMipmapDecompressUtil {
    fun decompressMipmap(mipmap: TEXMipmap) {
        if (mipmap.isLZ4Compressed) {
            mipmap.bytes = lz4Decompress(mipmap.bytes!!, mipmap.decompressedBytesCount)
            mipmap.isLZ4Compressed = false
        }
        if (mipmap.format.isImage()) {
            return
        }
        when (mipmap.format) {
            MipmapFormat.CompressedDXT5 -> {
                mipmap.bytes = DXT.decompressImage(mipmap.width, mipmap.height, mipmap.bytes!!, DXTFlags.DX5)
                mipmap.format = MipmapFormat.RGBA8888
            }
            MipmapFormat.CompressedDXT3 -> {
                mipmap.bytes = DXT.decompressImage(mipmap.width, mipmap.height, mipmap.bytes!!, DXTFlags.DX3)
                mipmap.format = MipmapFormat.RGBA8888
            }
            MipmapFormat.CompressedDXT1 -> {
                mipmap.bytes = DXT.decompressImage(mipmap.width, mipmap.height, mipmap.bytes!!, DXTFlags.DX1)
                mipmap.format = MipmapFormat.RGBA8888
            }
            else -> {
            }
        }
    }

    private fun lz4Decompress(bytes: ByteArray, length: Int): ByteArray {
        val buffer = ByteArray(length)
        val decompressor = LZ4Factory.fastestInstance().fastDecompressor()
        decompressor.decompress(bytes, buffer)
        return buffer
    }
}