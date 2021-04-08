package tex

import tex.enum.ImageFormat
import tex.enum.MipmapFormat
import tex.enum.TEXFormat

object TEXMipmapFormatGetter {
    fun getFormatForTex(imageFormat: ImageFormat, texFormat: TEXFormat): MipmapFormat {
        if (imageFormat != ImageFormat.FIF_UNKNOWN) {
            return imageFormatToMipmapFormat(imageFormat)
        }
        return when (texFormat) {
            TEXFormat.RGBA8888 -> MipmapFormat.RGBA8888
            TEXFormat.DXT5 -> MipmapFormat.CompressedDXT5
            TEXFormat.DXT3 -> MipmapFormat.CompressedDXT3
            TEXFormat.DXT1 -> MipmapFormat.CompressedDXT1
            TEXFormat.R8 -> MipmapFormat.R8
            TEXFormat.RG88 -> MipmapFormat.RG88
            else -> throw IllegalArgumentException()
        }
    }

    private fun imageFormatToMipmapFormat(imageFormat: ImageFormat): MipmapFormat {
        return when (imageFormat) {
            ImageFormat.FIF_UNKNOWN -> throw Exception("Cannot convert ${imageFormat.name} to ${MipmapFormat::class.simpleName}")
            ImageFormat.FIF_BMP -> MipmapFormat.ImageBMP
            ImageFormat.FIF_ICO -> MipmapFormat.ImageICO
            ImageFormat.FIF_JPEG -> MipmapFormat.ImageJPEG
            ImageFormat.FIF_JNG -> MipmapFormat.ImageJNG
            ImageFormat.FIF_KOALA -> MipmapFormat.ImageKOALA
            ImageFormat.FIF_LBM -> MipmapFormat.ImageLBM
            ImageFormat.FIF_MNG -> MipmapFormat.ImageMNG
            ImageFormat.FIF_PBM -> MipmapFormat.ImagePBM
            ImageFormat.FIF_PBMRAW -> MipmapFormat.ImagePBMRAW
            ImageFormat.FIF_PCD -> MipmapFormat.ImagePCD
            ImageFormat.FIF_PCX -> MipmapFormat.ImagePCX
            ImageFormat.FIF_PGM -> MipmapFormat.ImagePGM
            ImageFormat.FIF_PGMRAW -> MipmapFormat.ImagePGMRAW
            ImageFormat.FIF_PNG -> MipmapFormat.ImagePNG
            ImageFormat.FIF_PPM -> MipmapFormat.ImagePPM
            ImageFormat.FIF_PPMRAW -> MipmapFormat.ImagePPMRAW
            ImageFormat.FIF_RAS -> MipmapFormat.ImageRAS
            ImageFormat.FIF_TARGA -> MipmapFormat.ImageTARGA
            ImageFormat.FIF_TIFF -> MipmapFormat.ImageTIFF
            ImageFormat.FIF_WBMP -> MipmapFormat.ImageWBMP
            ImageFormat.FIF_PSD -> MipmapFormat.ImagePSD
            ImageFormat.FIF_CUT -> MipmapFormat.ImageCUT
            ImageFormat.FIF_XBM -> MipmapFormat.ImageXBM
            ImageFormat.FIF_XPM -> MipmapFormat.ImageXPM
            ImageFormat.FIF_DDS -> MipmapFormat.ImageDDS
            ImageFormat.FIF_GIF -> MipmapFormat.ImageGIF
            ImageFormat.FIF_HDR -> MipmapFormat.ImageHDR
            ImageFormat.FIF_FAXG3 -> MipmapFormat.ImageFAXG3
            ImageFormat.FIF_SGI -> MipmapFormat.ImageSGI
            ImageFormat.FIF_EXR -> MipmapFormat.ImageEXR
            ImageFormat.FIF_J2K -> MipmapFormat.ImageJ2K
            ImageFormat.FIF_JP2 -> MipmapFormat.ImageJP2
            ImageFormat.FIF_PFM -> MipmapFormat.ImagePFM
            ImageFormat.FIF_PICT -> MipmapFormat.ImagePICT
            ImageFormat.FIF_RAW -> MipmapFormat.ImageRAW
            else -> throw IllegalArgumentException()
        }
    }
}