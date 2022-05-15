package com.example.myapplication.myapplication.ui.face2

import android.graphics.Bitmap
import org.jcodec.common.VideoEncoder
import org.jcodec.common.model.ColorSpace
import org.jcodec.common.model.Picture
import org.jcodec.scale.BitmapUtil
import org.jcodec.scale.ColorUtil


/**
 * This class is part of JCodec ( www.jcodec.org ) This software is distributed
 * under FreeBSD License
 *
 * @author The JCodec project
 */
class AndroidUtil(private val bitmapUtil: BitmapUtil) {
    fun toBitmapImpl(pic: Picture?): Bitmap? {
        if (pic == null) return null
        val transform = ColorUtil.getTransform(pic.color, ColorSpace.RGB)
        val rgb = Picture.createCropped(pic.width, pic.height, ColorSpace.RGB, pic.crop)
        transform.transform(pic, rgb)
        return bitmapUtil.toBitmapImpl(rgb)
    }

    fun toBitmapImpl(pic: Picture?, out: Bitmap?) {
        requireNotNull(pic) { "Input pic is null" }
        requireNotNull(out) { "Out bitmap is null" }
        val transform = ColorUtil.getTransform(pic.color, ColorSpace.RGB)
        val rgb = Picture.createCropped(pic.width, pic.height, ColorSpace.RGB, pic.crop)
        transform.transform(pic, rgb)
        bitmapUtil.toBitmapImpl(rgb, out)
    }

    fun fromBitmapImpl(bitmap: Bitmap?, colorSpace: ColorSpace?): Picture? {
        if (bitmap == null) return null
        val out = Picture.create(bitmap.width, bitmap.height, colorSpace)
        fromBitmapImpl(bitmap, out)
        return out
    }

    fun fromBitmapImpl(bitmap: Bitmap?, encoder: VideoEncoder): Picture? {
        if (bitmap == null) return null
        var selectedColorSpace: ColorSpace? = null
        for (colorSpace in encoder.getSupportedColorSpaces()) {
            if (ColorUtil.getTransform(ColorSpace.RGB, colorSpace) != null) {
                selectedColorSpace = colorSpace
                break
            }
        }
        if (selectedColorSpace == null) {
            throw RuntimeException("Could not find a transform to convert to a codec-supported colorspace.")
        }
        val out = Picture.create(bitmap.width, bitmap.height, selectedColorSpace)
        fromBitmapImpl(bitmap, out)
        return out
    }

    fun fromBitmapImpl(bitmap: Bitmap?, out: Picture?) {
        requireNotNull(bitmap) { "Input pic is null" }
        requireNotNull(out) { "Out bitmap is null" }
        if (bitmap.config != Bitmap.Config.ARGB_8888) {
            throw RuntimeException("Unsupported bitmap config: " + bitmap.config)
        }
        val rgb = bitmapUtil.fromBitmapImpl(bitmap)
        val transform = ColorUtil.getTransform(ColorSpace.RGB, out.color)
        transform.transform(rgb, out)
    }

    companion object {
        private var inst: AndroidUtil? = null
        private fun inst(): AndroidUtil? {
            if (inst == null) {
                inst = AndroidUtil(BitmapUtil())
            }
            return inst
        }

        fun toBitmap(pic: Picture?): Bitmap? {
            return inst()!!.toBitmapImpl(pic)
        }

        fun toBitmap(pic: Picture?, out: Bitmap?) {
            inst()!!.toBitmapImpl(pic, out)
        }

        fun fromBitmap(bitmap: Bitmap?, colorSpace: ColorSpace?): Picture? {
            return inst()?.fromBitmapImpl(bitmap, colorSpace)
        }

        fun fromBitmap(bitmap: Bitmap?, encoder: VideoEncoder): Picture? {
            return inst()!!.fromBitmapImpl(bitmap, encoder)
        }

        fun fromBitmap(bitmap: Bitmap?, out: Picture?) {
            inst()?.fromBitmapImpl(bitmap, out)
        }
    }
}