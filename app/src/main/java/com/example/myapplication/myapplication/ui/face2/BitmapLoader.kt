//package com.example.myapplication.myapplication.ui.face2
//
//import okhttp3.internal.and
//import java.io.IOException
//import java.io.InputStream
//
//
///**
// * Utility class for loading windows bitmap files
// *
// *
// * Based on code from author Abdul Bezrati and Pepijn Van Eeckhoudt
// */
//object BitmapLoader {
//    /**
//     * Static method to load a bitmap file based on the filename passed in.
//     * Based on the bit count, this method will either call the 8 or 24 bit
//     * bitmap reader methods
//     *
//     * @param file The name of the bitmap file to read
//     * @throws IOException
//     * @return A BufferedImage of the bitmap
//     */
//    @Throws(IOException::class)
//    fun loadBitmap(file: String?): BufferedImage? {
//        val image: BufferedImage?
//        var input: InputStream? = null
//        try {
//            input = ResourceRetriever.getResourceAsStream(file)
//            val bitmapFileHeaderLength = 14
//            val bitmapInfoHeaderLength = 40
//            val bitmapFileHeader = ByteArray(bitmapFileHeaderLength)
//            val bitmapInfoHeader = ByteArray(bitmapInfoHeaderLength)
//            input.read(bitmapFileHeader, 0, bitmapFileHeaderLength)
//            input.read(bitmapInfoHeader, 0, bitmapInfoHeaderLength)
//            val nSize = bytesToInt(bitmapFileHeader, 2)
//            val nWidth = bytesToInt(bitmapInfoHeader, 4)
//            val nHeight = bytesToInt(bitmapInfoHeader, 8)
//            val nBiSize = bytesToInt(bitmapInfoHeader, 0)
//            val nPlanes = bytesToShort(bitmapInfoHeader, 12).toInt()
//            val nBitCount = bytesToShort(bitmapInfoHeader, 14).toInt()
//            val nSizeImage = bytesToInt(bitmapInfoHeader, 20)
//            val nCompression = bytesToInt(bitmapInfoHeader, 16)
//            val nColoursUsed = bytesToInt(bitmapInfoHeader, 32)
//            val nXPixelsMeter = bytesToInt(bitmapInfoHeader, 24)
//            val nYPixelsMeter = bytesToInt(bitmapInfoHeader, 28)
//            val nImportantColours = bytesToInt(bitmapInfoHeader, 36)
//            image = if (nBitCount == 24) {
//                read24BitBitmap(nSizeImage, nHeight, nWidth, input)
//            } else if (nBitCount == 8) {
//                read8BitBitmap(nColoursUsed, nBitCount, nSizeImage, nWidth, nHeight, input)
//            } else {
//                println("Not a 24-bit or 8-bit Windows Bitmap, aborting...")
//                null
//            }
//        } finally {
//            try {
//                input?.close()
//            } catch (e: IOException) {
//            }
//        }
//        return image
//    }
//
//    /**
//     * Static method to read a 8 bit bitmap
//     *
//     * @param nColoursUsed Number of colors used
//     * @param nBitCount The bit count
//     * @param nSizeImage The size of the image in bytes
//     * @param nWidth The width of the image
//     * @param input The input stream corresponding to the image
//     * @throws IOException
//     * @return A BufferedImage of the bitmap
//     */
//    @Throws(IOException::class)
//    private fun read8BitBitmap(
//        nColoursUsed: Int,
//        nBitCount: Int,
//        nSizeImage: Int,
//        nWidth: Int,
//        nHeight: Int,
//        input: InputStream?
//    ): BufferedImage {
//        var nSizeImage = nSizeImage
//        val nNumColors = if (nColoursUsed > 0) nColoursUsed else 1 and 0xff shl nBitCount
//        if (nSizeImage == 0) {
//            nSizeImage = nWidth * nBitCount + 31 and 31.inv() shr 3
//            nSizeImage *= nHeight
//        }
//        val npalette = IntArray(nNumColors)
//        val bpalette = ByteArray(nNumColors * 4)
//        readBuffer(input, bpalette)
//        var nindex8 = 0
//        for (n in 0 until nNumColors) {
//            npalette[n] = 255 and 0xff shl 24 or (
//                    bpalette[nindex8 + 2] and 0xff shl 16) or (
//                    bpalette[nindex8 + 1] and 0xff shl 8) or
//                    (bpalette[nindex8 + 0] and 0xff)
//            nindex8 += 4
//        }
//        val npad8 = nSizeImage / nHeight - nWidth
//        val bufferedImage = BufferedImage(nWidth, nHeight, BufferedImage.TYPE_INT_ARGB)
//        val dataBufferByte: DataBufferInt =
//            bufferedImage.getRaster().getDataBuffer() as DataBufferInt
//        val bankData: Array<IntArray> = dataBufferByte.getBankData()
//        val bdata = ByteArray((nWidth + npad8) * nHeight)
//        readBuffer(input, bdata)
//        nindex8 = 0
//        for (j8 in nHeight - 1 downTo 0) {
//            for (i8 in 0 until nWidth) {
//                bankData[0][j8 * nWidth + i8] = npalette[bdata[nindex8].toInt() and 0xff]
//                nindex8++
//            }
//            nindex8 += npad8
//        }
//        return bufferedImage
//    }
//
//    /**
//     * Static method to read a 24 bit bitmap
//     *
//     * @param nSizeImage size of the image  in bytes
//     * @param nHeight The height of the image
//     * @param nWidth The width of the image
//     * @param input The input stream corresponding to the image
//     * @throws IOException
//     * @return A BufferedImage of the bitmap
//     */
//    @Throws(IOException::class)
//    private fun read24BitBitmap(
//        nSizeImage: Int,
//        nHeight: Int,
//        nWidth: Int,
//        input: InputStream?
//    ): BufferedImage {
//        var npad = nSizeImage / nHeight - nWidth * 3
//        if (npad == 4 || npad < 0) npad = 0
//        var nindex = 0
//        val bufferedImage = BufferedImage(nWidth, nHeight, BufferedImage.TYPE_4BYTE_ABGR)
//        val dataBufferByte: DataBufferByte =
//            bufferedImage.getRaster().getDataBuffer() as DataBufferByte
//        val bankData: Array<ByteArray> = dataBufferByte.getBankData()
//        val brgb = ByteArray((nWidth + npad) * 3 * nHeight)
//        readBuffer(input, brgb)
//        for (j in nHeight - 1 downTo 0) {
//            for (i in 0 until nWidth) {
//                val base = (j * nWidth + i) * 4
//                bankData[0][base] = 255.toByte()
//                bankData[0][base + 1] = brgb[nindex]
//                bankData[0][base + 2] = brgb[nindex + 1]
//                bankData[0][base + 3] = brgb[nindex + 2]
//                nindex += 3
//            }
//            nindex += npad
//        }
//        return bufferedImage
//    }
//
//    /**
//     * Converts bytes to an int
//     *
//     * @param bytes An array of bytes
//     * @param index
//     * @returns A int representation of the bytes
//     */
//    private fun bytesToInt(bytes: ByteArray, index: Int): Int {
//        return bytes[index + 3] and 0xff shl 24 or (
//                bytes[index + 2] and 0xff shl 16) or (
//                bytes[index + 1] and 0xff shl 8) or (
//                bytes[index + 0] and 0xff)
//    }
//
//    /**
//     * Converts bytes to a short
//     *
//     * @param bytes An array of bytes
//     * @param index
//     * @returns A short representation of the bytes
//     */
//    private fun bytesToShort(bytes: ByteArray, index: Int): Int {
//        return (bytes[index + 1] and 0xff shl 8 or
//                (bytes[index + 0] and 0xff))
//    }
//
//    /**
//     * Reads the buffer
//     *
//     * @param in An InputStream
//     * @param buffer An array of bytes
//     * @throws IOException
//     */
//    @Throws(IOException::class)
//    private fun readBuffer(`in`: InputStream?, buffer: ByteArray) {
//        var bytesRead = 0
//        var bytesToRead = buffer.size
//        while (bytesToRead > 0) {
//            val read = `in`!!.read(buffer, bytesRead, bytesToRead)
//            bytesRead += read
//            bytesToRead -= read
//        }
//    }
//}