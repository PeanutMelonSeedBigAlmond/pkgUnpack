package tex

import org.json.JSONArray
import org.json.JSONObject
import tex.data.TEX
import tex.enum.TEXFlags

object TEXJsonGenerator {
    fun getJsonString(tex: TEX): String {
        val jsonObject = JSONObject().also {
            it.put("bleedtransparentcolors", true)
            it.put("clampuvs", tex.hasFlag(TEXFlags.ClampUVs))
            it.put("format", tex.texHeader.texFormat.name.toLowerCase())
            it.put("nomip", (tex.firstImage!!.mipmaps.size == 1).toString().toLowerCase())
            it.put("nointerpolation", tex.hasFlag(TEXFlags.NoInterpolation).toString().toLowerCase())
            it.put(
                "nonpoweroftwo",
                (!isPowOfTwo(tex.texHeader.imageHeight) || !isPowOfTwo(tex.texHeader.imageWidth)).toString()
                    .toLowerCase()
            )
            if (tex.isGif) {
                it.put(
                    "spritesheetsequences",
                    JSONArray().also { jsonArray ->
                        jsonArray.put(
                            JSONObject().also { jsonObject ->
                                jsonObject.put("duration", 1)
                                jsonObject.put("frames", tex.frameInfoContainer!!.frames.size)
                                jsonObject.put("width", tex.frameInfoContainer!!.gifWidth)
                                jsonObject.put("height", tex.frameInfoContainer!!.gifHeight)
                            }
                        )
                    }
                )
            }
        }
        return jsonObject.toString()
    }

    // 判断一个数是不是2的整数次幂，0除外
    private fun isPowOfTwo(value: Int): Boolean = (value != 0) && ((value and (value - 1)) == 0)
}