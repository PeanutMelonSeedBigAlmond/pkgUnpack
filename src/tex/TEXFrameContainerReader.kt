package tex

import extension.readInt32
import extension.readNextString
import extension.readSingle
import tex.data.TEXFrameInfo
import tex.data.TEXFrameInfoContainer
import java.io.RandomAccessFile
import kotlin.test.assertTrue

object TEXFrameContainerReader {
    fun readFrameInfoContainer(file: RandomAccessFile): TEXFrameInfoContainer {
        val container = TEXFrameInfoContainer(
            magic = file.readNextString(16)
        )
        val frameCount = file.readInt32()
        assertTrue(frameCount <= TEXConstants.maxFrameCount, "Frame count exceeds limit: $frameCount")
        when (container.magic) {
            "TEXS0002" -> {
            }
            "TEXS0003" -> {
                container.gifWidth = file.readInt32()
                container.gifHeight = file.readInt32()
            }
            else -> throw Exception("Frame magic is invalid: ${container.magic}")
        }

        // TODO:可能有问题：readDouble
        for (i in 0 until frameCount) {
            container.frames.add(
                TEXFrameInfo(
                    imageId = file.readInt32(),
                    frameTime = file.readSingle(),
                    x = file.readSingle(),
                    y = file.readSingle(),
                    width = file.readSingle(),
                    unk0 = file.readSingle(),
                    unk1 = file.readSingle(),
                    height = file.readSingle()
                )
            )
        }
        if (container.gifWidth == 0 || container.gifHeight == 0) {
            container.gifWidth = container.frames[0].width.toInt()
            container.gifHeight = container.frames[0].height.toInt()
        }
        return container
    }
}