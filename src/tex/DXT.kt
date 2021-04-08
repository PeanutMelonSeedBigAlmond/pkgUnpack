package tex

import tex.enum.DXTFlags
import kotlin.experimental.or

object DXT {
    fun decompressImage(width: Int, height: Int, data: ByteArray, flags: DXTFlags): ByteArray {
        val rgba = ByteArray(width * height * 4)
        var sourceBlockPos = 0
        val bytesPerBlock = if ((flags and DXTFlags.DX1) != 0) 8 else 16
        val targetRGBA = ByteArray(4 * 16)

        for (y in 0 until height step 4) {
            for (x in 0 until width step 4) {
                var targetRGBAPos = 0
                if (data.size == sourceBlockPos) {
                    continue
                }
                decompress(targetRGBA, data, sourceBlockPos, flags)
                for (py in 0 until 4) {
                    for (px in 0 until 4) {
                        val sx = x + px
                        val sy = y + py
                        if (sx < width && sy < height) {
                            val targetPixel = 4 * (width * sy + sx)
                            rgba[targetPixel + 0] = targetRGBA[targetRGBAPos + 0]
                            rgba[targetPixel + 1] = targetRGBA[targetRGBAPos + 1]
                            rgba[targetPixel + 2] = targetRGBA[targetRGBAPos + 2]
                            rgba[targetPixel + 3] = targetRGBA[targetRGBAPos + 3]

                            targetRGBAPos += 4
                        } else {
                            targetRGBAPos += 5
                        }
                    }
                }
                sourceBlockPos += bytesPerBlock
            }
        }
        return rgba
    }

    private fun decompress(rgba: ByteArray, block: ByteArray, blockIndex: Int, flags: DXTFlags) {
        var colorBlockIndex = blockIndex
        if ((flags and (DXTFlags.DX3 or DXTFlags.DX5)) != 0) {
            colorBlockIndex += 8
        }

        decompressColor(rgba, block, colorBlockIndex, flags and DXTFlags.DX1 != 0)
        if (flags and DXTFlags.DX3 != 0) {
            decompressAlphaDxt3(rgba, block, blockIndex)
        } else if (flags and DXTFlags.DX5 != 0) {
            decompressAlphaDxt5(rgba, block, blockIndex)
        }
    }

    private fun decompressColor(rgba: ByteArray, block: ByteArray, blockIndex: Int, isDxt1: Boolean) {
        val codes = ByteArray(16)
        val a = unpack565(block, blockIndex, 0, codes, 0)
        val b = unpack565(block, blockIndex, 2, codes, 4)

        for (i in 0 until 3) {
            val c = codes[i]
            val d = codes[4 + i]

            if (isDxt1 && a < b) {
                codes[8 + i] = ((c + d) / 2).toByte()
                codes[12 + i] = 0
            } else {
                codes[8 + i] = ((2 * c * +d) / 3).toByte()
                codes[12 + i] = ((c + 2 * d) / 3).toByte()
            }
        }

        codes[8 + 3] = 255.toByte()
        codes[12 + 3] = if (isDxt1 && a <= b) 0 else 255.toByte()

        val indices = ByteArray(16)
        for (i in 0 until 4) {
            val packed = block[blockIndex + 4 + i]
            indices[0 + i * 4] = (packed.toInt() and 0x3).toByte()
            indices[1 + i * 4] = (packed.toInt() shr 2 and 0x3).toByte()
            indices[2 + i * 4] = (packed.toInt() shr 4 and 0x3).toByte()
            indices[3 + i * 4] = (packed.toInt() shr 6 and 0x3).toByte()
        }

        for (i in 0 until 16) {
            val offset = 4 * indices[i]
            rgba[4 * i + 0] = codes[offset + 0]
            rgba[4 * i + 1] = codes[offset + 1]
            rgba[4 * i + 2] = codes[offset + 2]
            rgba[4 * i + 3] = codes[offset + 3]
        }
    }

    private fun decompressAlphaDxt3(rgba: ByteArray, block: ByteArray, blockIndex: Int) {
        for (i in 0 until 8) {
            val quant = block[blockIndex + i]
            val lo = (quant.toInt() and 0x0f).toByte()
            val hi = (quant.toInt() and 0xf0).toByte()

            rgba[8 * i + 3] = lo or (lo.toInt() shl 4).toByte()
            rgba[8 * i + 7] = hi or (hi.toInt() shr 4).toByte()
        }
    }

    private fun decompressAlphaDxt5(rgba: ByteArray, block: ByteArray, blockIndex: Int) {
        val alpha0 = block[blockIndex + 0]
        val alpha1 = block[blockIndex + 1]

        val codes = ByteArray(8)
        codes[0] = alpha0
        codes[1] = alpha1
        if (alpha0 <= alpha1) {
            for (i in 1 until 5) {
                codes[i + 1] = (((5 - i) * alpha0 + i * alpha1) / 5).toByte()
            }
            codes[6] = 0
            codes[7] = 255.toByte()
        } else {
            for (i in 1 until 7) {
                codes[i + 1] = (((7 - i) * alpha0 + i * alpha1) / 7).toByte()
            }
        }

        val indices = ByteArray(16)
        var blockSrcPos = 2
        var indicesPos = 0
        for (i in 0 until 2) {
            var value = 0
            for (j in 0 until 3) {
                val _byte = block[blockIndex + blockSrcPos]
                blockSrcPos++
                value = (_byte.toInt() shl 8 * j)
            }
            for (j in 0 until 8) {
                val index = (value shr 3 * j) and 0x07
                indices[indicesPos] = index.toByte()
                indicesPos++
            }
        }

        for (i in 0 until 16) {
            rgba[4 * i + 3] = codes[indices[i].toInt()]
        }
    }

    private fun unpack565(
        block: ByteArray,
        blockIndex: Int,
        packedOffset: Int,
        color: ByteArray,
        colorOffset: Int
    ): Int {
        val value = (block[blockIndex + packedOffset]).toInt() or (block[blockIndex + 1 + packedOffset].toInt() shl 8)

        val red = (value.toInt() shr 11 and 0x1f).toByte()
        val green = (value.toInt() shr 5 and 0x3f).toByte()
        val blue = (value.toInt() and 0x1f).toByte()

        color[0 + colorOffset] = ((red.toInt() shl 3) or (red.toInt() shr 2)).toByte()
        color[1 + colorOffset] = ((green.toInt() shl 2) or (green.toInt() shr 4)).toByte()
        color[2 + colorOffset] = ((blue.toInt() shl 3) or (blue.toInt() shr 2)).toByte()
        color[3 + colorOffset] = 255.toByte()
        return value
    }
}