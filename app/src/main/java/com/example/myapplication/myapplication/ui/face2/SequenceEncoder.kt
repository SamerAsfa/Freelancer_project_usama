//package com.example.myapplication.myapplication.ui.face2
//
//import android.app.TaskStackBuilder.create
//import android.graphics.ColorSpace
//import android.graphics.Picture
//import org.jcodec.codecs.h264.H264Encoder
//import org.jcodec.containers.mp4.muxer.MP4Muxer
//
// import java.io.File
//import java.io.IOException
//import java.net.URI.create
//import java.nio.ByteBuffer
//import java.nio.channels.SeekableByteChannel
//import java.util.*
//import kotlin.collections.ArrayList
//
//
//class SequenceEncoder(out: File?) {
//    private val ch: SeekableByteChannel
//    private var toEncode: Picture? = null
//    private val transform: RgbToYuv420
//    private val encoder: H264Encoder
//    private val spsList: ArrayList<ByteBuffer>
//    private val ppsList: ArrayList<ByteBuffer>
//    private val outTrack: CompressedTrack
//    private val _out: ByteBuffer
//    private var frameNo = 0
//    private val muxer: MP4Muxer
//    @Throws(IOException::class)
//    fun encodeImage(bi: BufferedImage) {
//        if (toEncode == null) {
//            toEncode = Picture.create(bi.getWidth(), bi.getHeight(), ColorSpace.YUV420)
//        }
//
//        // Perform conversion
//        for (i in 0..2) Arrays.fill(toEncode.getData().get(i), 0)
//        transform.transform(AWTUtil.fromBufferedImage(bi), toEncode)
//
//        // Encode image into H.264 frame, the result is stored in '_out' buffer
//        _out.clear()
//        val result: ByteBuffer = encoder.encodeFrame(_out, toEncode)
//
//        // Based on the frame above form correct MP4 packet
//        spsList.clear()
//        ppsList.clear()
//        H264Utils.encodeMOVPacket(result, spsList, ppsList)
//
//        // Add packet to video track
//        outTrack.addFrame(MP4Packet(result, frameNo, 25, 1, frameNo, true, null, frameNo, 0))
//        frameNo++
//    }
//
//    @Throws(IOException::class)
//    fun finish() {
//        // Push saved SPS/PPS to a special storage in MP4
//        outTrack.addSampleEntry(H264Utils.createMOVSampleEntry(spsList, ppsList))
//
//        // Write MP4 header and finalize recording
//        muxer.writeHeader()
//        NIOUtils.closeQuietly(ch)
//    }
//
//    init {
//        ch = NIOUtils.writableFileChannel(out)
//
//        // Transform to convert between RGB and YUV
//        transform = RgbToYuv420(0, 0)
//
//        // Muxer that will store the encoded frames
//        muxer = MP4Muxer(ch, Brand.MP4)
//
//        // Add video track to muxer
//        outTrack = muxer.addTrackForCompressed(TrackType.VIDEO, 25)
//
//        // Allocate a buffer big enough to hold output frames
//        _out = ByteBuffer.allocate(1920 * 1080 * 6)
//
//        // Create an instance of encoder
//        encoder = H264Encoder()
//
//        // Encoder extra data ( SPS, PPS ) to be stored in a special place of
//        // MP4
//        spsList = ArrayList<ByteBuffer>()
//        ppsList = ArrayList<ByteBuffer>()
//    }
//}