package tex

import tex.data.TEX
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter

object TEXWriter {
    fun writeTEXEntries(tex: TEX, texFileName: String, baseDir: String) {
        val filePath = baseDir + "/" + File(texFileName).nameWithoutExtension
        convertToImageAndSave(tex, filePath)
        writeTexJson(tex, filePath)
    }

    private fun convertToImageAndSave(tex: TEX, path: String) {
        val format = TEXImageConverter.convertFormat(tex)
        val outputPath = "$path.${format.getFileExtension()}"
        ensurePathExists(outputPath)

        println("Extracting $outputPath")

        val resultImage = TEXImageConverter.convertToImage(tex)
        FileOutputStream(outputPath).use { it.write(resultImage.bytes) }
    }

    private fun writeTexJson(tex: TEX, path: String) {
        val jsonText = TEXJsonGenerator.getJsonString(tex)
        val outputPath = "$path.tex-json"
        ensurePathExists(outputPath)

        println("Extracting $outputPath")

        FileWriter(outputPath).use { it.write(jsonText) }
    }

    private fun ensurePathExists(path: String) = with(File(path).parentFile) {
        if (!exists()) {
            mkdirs()
        }
    }
}