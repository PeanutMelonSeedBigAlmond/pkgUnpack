package tex.enum

enum class MipmapFormat(val value: Int) {
    // default, placeholder
    Invalid(0),

    /// Raw pixels (4 bytes per pixel) (RGBA8888)
    RGBA8888(1),

    /// Raw pixels (1 byte per pixel) (R8)
    R8(2),

    /// Raw pixels (2 bytes per pixel) (RG88)
    RG88(3),

    /// Raw pixels compressed using DXT5
    CompressedDXT5(4),

    /// Raw pixels compressed using DXT3
    CompressedDXT3(5),

    /// Raw pixels compressed using DXT1
    CompressedDXT1(6),

    /// Windows or OS/2 Bitmap File (*.BMP)
    /// Keep '= 1000' because MipmapFormatExtensions.IsImage uses this to check if format is an image format
    ImageBMP(1000),

    /// Windows Icon (*.ICO)
    ImageICO(1001),

    /// Independent JPEG Group (*.JPG, *.JIF, *.JPEG, *.JPE)
    ImageJPEG(1002),

    /// JPEG Network Graphics (*.JNG)
    ImageJNG(1003),

    /// Commodore 64 Koala format (*.KOA)
    ImageKOALA(1004),

    /// Amiga IFF (*.IFF, *.LBM)
    ImageLBM(1005),

    /// Amiga IFF (*.IFF, *.LBM)
    ImageIFF(1006),

    /// Multiple Network Graphics (*.MNG)
    ImageMNG(1007),

    /// Portable Bitmap (ASCII) (*.PBM)
    ImagePBM(1008),

    /// Portable Bitmap (BINARY) (*.PBM)
    ImagePBMRAW(1009),

    /// Kodak PhotoCD (*.PCD)
    ImagePCD(1010),

    /// Zsoft Paintbrush PCX bitmap format (*.PCX)
    ImagePCX(1011),

    /// Portable Graymap (ASCII) (*.PGM)
    ImagePGM(1012),

    /// Portable Graymap (BINARY) (*.PGM)
    ImagePGMRAW(1013),

    /// Portable Network Graphics (*.PNG)
    ImagePNG(1014),

    /// Portable Pixelmap (ASCII) (*.PPM)
    ImagePPM(1015),

    /// Portable Pixelmap (BINARY) (*.PPM)
    ImagePPMRAW(1016),

    /// Sun Rasterfile (*.RAS)
    ImageRAS(1017),

    /// truevision Targa files (*.TGA, *.TARGA)
    ImageTARGA(1018),

    /// Tagged Image File Format (*.TIF, *.TIFF)
    ImageTIFF(1019),

    /// Wireless Bitmap (*.WBMP)
    ImageWBMP(1020),

    /// Adobe Photoshop (*.PSD)
    ImagePSD(1021),

    /// Dr. Halo (*.CUT)
    ImageCUT(1022),

    /// X11 Bitmap Format (*.XBM)
    ImageXBM(1023),

    /// X11 Pixmap Format (*.XPM)
    ImageXPM(1024),

    /// DirectDraw Surface (*.DDS)
    ImageDDS(1025),

    /// Graphics Interchange Format (*.GIF)
    ImageGIF(1026),

    /// High Dynamic Range (*.HDR)
    ImageHDR(1027),

    /// Raw Fax format CCITT G3 (*.G3)
    ImageFAXG3(1028),

    /// Silicon Graphics SGI image format (*.SGI)
    ImageSGI(1029),

    /// OpenEXR format (*.EXR)
    ImageEXR(1030),

    /// JPEG-2000 format (*.J2K, *.J2C)
    ImageJ2K(1031),

    /// JPEG-2000 format (*.JP2)
    ImageJP2(1032),

    /// Portable FloatMap (*.PFM)
    ImagePFM(1033),

    /// Macintosh PICT (*.PICT)
    ImagePICT(1034),

    /// RAW camera image (*.*)
    ImageRAW(1035);

    companion object {
        fun Int.toMipmapFormat() = values().first { it.value == this }
    }

    fun isImage() = value >= 1000

    fun isCompressed() = when (this) {
        CompressedDXT5, CompressedDXT3, CompressedDXT1 -> true
        else -> false
    }

    fun isRawFormat() = value in 1..3

    fun getFileExtension() = when (this) {
        ImageBMP -> "bmp"
        ImageICO -> "ico"
        ImageJPEG -> "jpg"
        ImageJNG -> "jng"
        ImageKOALA -> "koa"
        ImageLBM -> "lbm"
        ImageIFF -> "iff"
        ImageMNG -> "mng"
        ImagePBM, ImagePBMRAW -> "pbm"
        ImagePCD -> "pcd"
        ImagePCX -> "pcx"
        ImagePGM, ImagePGMRAW -> "pgm"
        ImagePNG -> "png"
        ImagePPM, ImagePPMRAW -> "ppm"
        ImageRAS -> "ras"
        ImageTARGA -> "tga"
        ImageTIFF -> "tif"
        ImageWBMP -> "wbmp"
        ImagePSD -> "psd"
        ImageCUT -> "cut"
        ImageXBM -> "xbm"
        ImageXPM -> "xpm"
        ImageDDS -> "dds"
        ImageGIF -> "gif"
        ImageHDR -> "hdr"
        ImageFAXG3 -> "g3"
        ImageSGI -> "sgi"
        ImageEXR -> "exr"
        ImageJ2K -> "j2k"
        ImageJP2 -> "jp2"
        ImagePFM -> "pfm"
        ImagePICT -> "pict"
        ImageRAW -> "raw"
        else -> throw IllegalArgumentException("$this")
    }
}