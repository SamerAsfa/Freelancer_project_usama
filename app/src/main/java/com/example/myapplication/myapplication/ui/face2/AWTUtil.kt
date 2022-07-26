//package com.example.myapplication.myapplication.ui.face2
//
//import android.graphics.Bitmap
//import org.jcodec.codecs.png.PNGDecoder
//import org.jcodec.codecs.png.PNGEncoder
//import org.jcodec.common.Preconditions
//import org.jcodec.common.io.NIOUtils
//import org.jcodec.common.model.ColorSpace
//import org.jcodec.common.model.Picture
//import org.jcodec.scale.ColorUtil
//import org.jcodec.scale.RgbToBgr
//import java.io.File
//import java.io.IOException
//import java.nio.ByteBuffer
//
//
//object AWTUtil {
//    private const val alphaR = 0xff
//    private const val alphaG = 0xff
//    private const val alphaB = 0xff
//    fun toBufferedImage2(src: Picture, dst: Bitmap) {
//        val data: ByteArray = dst
//        val srcData = src.getPlaneData(0)
//        for (i in data.indices) {
//            data[i] = (srcData[i] + 128).toByte()
//        }
//    }
//
//    fun toBufferedImage(src: Picture): BufferedImage {
//        var src = src
//        if (src.color != ColorSpace.BGR) {
//            val bgr = Picture.createCropped(src.width, src.height, ColorSpace.BGR, src.crop)
//            if (src.color == ColorSpace.RGB) {
//                RgbToBgr().transform(src, bgr)
//            } else {
//                val transform: Transform = ColorUtil.getTransform(src.color, ColorSpace.RGB)
//                transform.transform(src, bgr)
//                RgbToBgr().transform(bgr, bgr)
//            }
//            src = bgr
//        }
//        val dst = BufferedImage(
//            src.croppedWidth, src.croppedHeight,
//            BufferedImage.TYPE_3BYTE_BGR
//        )
//        if (src.crop == null) toBufferedImage2(src, dst) else toBufferedImageCropped(src, dst)
//        return dst
//    }
//
//    private fun toBufferedImageCropped(src: Picture, dst: BufferedImage) {
//        val data: ByteArray = (dst.getRaster().getDataBuffer() as DataBufferByte).getData()
//        val srcData = src.getPlaneData(0)
//        val dstStride: Int = dst.getWidth() * 3
//        val srcStride = src.width * 3
//        var line = 0
//        var srcOff = 0
//        var dstOff = 0
//        while (line < dst.getHeight()) {
//            var id = dstOff
//            var `is` = srcOff
//            while (id < dstOff + dstStride) {
//                data[id] = (srcData[`is`] + 128).toByte()
//                data[id + 1] = (srcData[`is` + 1] + 128).toByte()
//                data[id + 2] = (srcData[`is` + 2] + 128).toByte()
//                id += 3
//                `is` += 3
//            }
//            srcOff += srcStride
//            dstOff += dstStride
//            line++
//        }
//    }
//
//    @Throws(IOException::class)
//    fun writePNG(picture: Picture, pngFile: File?) {
//        val rgb = if (picture.color == RGB) picture else convertColorSpace(picture, RGB)
//        val encoder = PNGEncoder()
//        val tmpBuf = ByteBuffer.allocate(encoder.estimateBufferSize(rgb))
//        val encoded = encoder.encodeFrame(rgb, tmpBuf).data
//        NIOUtils.writeTo(encoded, pngFile)
//    }
//
//    @Throws(IOException::class)
//    fun decodePNG(f: File, tgtColor: ColorSpace?): Picture {
//        val picture = decodePNG0(f)
//        Preconditions.checkNotNull(picture, "cant decode " + f.path)
//        return convertColorSpace(picture, tgtColor)
//    }
//
//    @Throws(IOException::class)
//    fun decodePNG0(f: File?): Picture {
//        val pngDec = PNGDecoder()
//        val buf = NIOUtils.fetchFromFile(f)
//        val codecMeta = pngDec.getCodecMeta(buf)
//        val pic = Picture.create(
//            codecMeta.size.width, codecMeta.size.height,
//            ColorSpace.RGB
//        )
//        return pngDec.decodeFrame(buf, pic.data)
//    }
//
//    fun convertColorSpace(pic: Picture, tgtColor: ColorSpace?): Picture {
//        val tr: Transform = ColorUtil.getTransform(pic.color, tgtColor)
//        val res = Picture.create(pic.width, pic.height, tgtColor)
//        tr.transform(pic, res)
//        return res
//    }
//
//    fun fromBufferedImage(src: BufferedImage, tgtColor: ColorSpace?): Picture {
//        return convertColorSpace(fromBufferedImageRGB(src), tgtColor)
//    }
//
//    fun fromBufferedImageRGB(src: BufferedImage): Picture {
//        val dst = Picture.create(src.getWidth(), src.getHeight(), RGB)
//        bufImgToPicture(src, dst)
//        return dst
//    }
//
//    fun bufImgToPicture(src: BufferedImage, dst: Picture) {
//        val dstData = dst.getPlaneData(0)
//        var off = 0
//        for (i in 0 until src.getHeight()) {
//            for (j in 0 until src.getWidth()) {
//                val rgb1: Int = src.getRGB(j, i)
//                val alpha = rgb1 shr 24 and 0xff
//                if (alpha == 0xff) {
//                    dstData[off++] = ((rgb1 shr 16 and 0xff) - 128).toByte()
//                    dstData[off++] = ((rgb1 shr 8 and 0xff) - 128).toByte()
//                    dstData[off++] = ((rgb1 and 0xff) - 128).toByte()
//                } else {
//                    val nalpha = 255 - alpha
//                    dstData[off++] =
//                        (((rgb1 shr 16 and 0xff) * alpha + alphaR * nalpha shr 8) - 128).toByte()
//                    dstData[off++] =
//                        (((rgb1 shr 8 and 0xff) * alpha + alphaG * nalpha shr 8) - 128).toByte()
//                    dstData[off++] =
//                        (((rgb1 and 0xff) * alpha + alphaB * nalpha shr 8) - 128).toByte()
//                }
//            }
//        }
//    }
//}