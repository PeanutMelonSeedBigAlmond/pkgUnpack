package tex.enum

import kotlin.test.assertTrue

enum class ImageFormat(val value: Int) {
    /// Unknown format (returned value only, never use it as input value)
    FIF_UNKNOWN(-1),

    /// Windows or OS/2 Bitmap File (*.BMP)
    FIF_BMP(0),

    /// Windows Icon (*.ICO)
    FIF_ICO(1),

    /// Independent JPEG Group (*.JPG, *.JIF, *.JPEG, *.JPE)
    FIF_JPEG(2),

    /// JPEG Network Graphics (*.JNG)
    FIF_JNG(3),

    /// Commodore 64 Koala format (*.KOA)
    FIF_KOALA(4),

    /// Amiga IFF (*.IFF, *.LBM)
    FIF_LBM(5),

    /// Amiga IFF (*.IFF, *.LBM)
    FIF_IFF(5),

    /// Multiple Network Graphics (*.MNG)
    FIF_MNG(6),

    /// Portable Bitmap (ASCII) (*.PBM)
    FIF_PBM(7),

    /// Portable Bitmap (BINARY) (*.PBM)
    FIF_PBMRAW(8),

    /// Kodak PhotoCD (*.PCD)
    FIF_PCD(9),

    /// Zsoft Paintbrush PCX bitmap format (*.PCX)
    FIF_PCX(10),

    /// Portable Graymap (ASCII) (*.PGM)
    FIF_PGM(11),

    /// Portable Graymap (BINARY) (*.PGM)
    FIF_PGMRAW(12),

    /// Portable Network Graphics (*.PNG)
    FIF_PNG(13),

    /// Portable Pixelmap (ASCII) (*.PPM)
    FIF_PPM(14),

    /// Portable Pixelmap (BINARY) (*.PPM)
    FIF_PPMRAW(15),

    /// Sun Rasterfile (*.RAS)
    FIF_RAS(16),

    /// truevision Targa files (*.TGA, *.TARGA)
    FIF_TARGA(17),

    /// Tagged Image File Format (*.TIF, *.TIFF)
    FIF_TIFF(18),

    /// Wireless Bitmap (*.WBMP)
    FIF_WBMP(19),

    /// Adobe Photoshop (*.PSD)
    FIF_PSD(20),

    /// Dr. Halo (*.CUT)
    FIF_CUT(21),

    /// X11 Bitmap Format (*.XBM)
    FIF_XBM(22),

    /// X11 Pixmap Format (*.XPM)
    FIF_XPM(23),

    /// DirectDraw Surface (*.DDS)
    FIF_DDS(24),

    /// Graphics Interchange Format (*.GIF)
    FIF_GIF(25),

    /// High Dynamic Range (*.HDR)
    FIF_HDR(26),

    /// Raw Fax format CCITT G3 (*.G3)
    FIF_FAXG3(27),

    /// Silicon Graphics SGI image format (*.SGI)
    FIF_SGI(28),

    /// OpenEXR format (*.EXR)
    FIF_EXR(29),

    /// JPEG-2000 format (*.J2K, *.J2C)
    FIF_J2K(30),

    /// JPEG-2000 format (*.JP2)
    FIF_JP2(31),

    /// Portable FloatMap (*.PFM)
    FIF_PFM(32),

    /// Macintosh PICT (*.PICT)
    FIF_PICT(33),

    /// RAW camera image (*.*)
    FIF_RAW(34);

    companion object {
        fun Int.toImageFormat() = values().first { it.value == this }
    }

    fun ensureValid() {
        assertTrue(value in -1..34, "Image format is invalid: $value")
    }
}