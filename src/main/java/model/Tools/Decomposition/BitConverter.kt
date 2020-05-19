//package model.Tools
//
//
//fun toInt16(value: ByteArray, startIndex: Int): Short {
//    validate(value, startIndex)
//    return (value[startIndex] + (value[startIndex + 1] * 256)).toShort()
//}
//
//// Converts an array of bytes into an int.
//fun toInt32(value: ByteArray, startIndex: Int): Int {
//    validate(value, startIndex)
//    return value[startIndex] + (value[startIndex + 1] * 256) + (value[startIndex + 2] * 65536) + (value[startIndex + 3] * 1677216)
//}
//
//// Converts an array of bytes into a long.
//fun toInt64(value: ByteArray, startIndex: Int): Long {
//    validate(value, startIndex)
//    return value[startIndex] + (value[startIndex + 1] * 256) + (value[startIndex + 2] * 65536) + (value[startIndex + 3] * 1677216) +
//            (value[startIndex + 4] * 4294967296) + (value[startIndex + 5] * 1099511627776) + (value[startIndex + 6] * 281474976710656) + (value[startIndex + 7] * 72057594037927936)
//}
//
//fun toSingle(value: ByteArray, startIndex: Int): Float {
//    validate(value, startIndex)
//    val `val` = toInt32(value, startIndex)
//    return `val` / 100.0f
//}
//
//fun getBytes(value: Boolean): Byte {
//    return (if (value) 1 else 0).toByte()
//}
//
//// Converts a char into an array of bytes with length two.
//fun getBytes(value: Char): ByteArray? {
//    return getBytes(value.toShort())
//}
//
//// Converts a short into an array of bytes with length
//// two.
//fun getBytes(value: Short): ByteArray? {
//    val bytes = ByteArray(2)
//    bytes[0] = value.toByte()
//    bytes[1] = (value / 256).toByte()
//    return bytes
//}
//
//// Converts an int into an array of bytes with length
//// four.
//fun getBytes(value: Int): ByteArray? {
//    val bytes = ByteArray(4)
//    bytes[0] = value.toByte()
//    bytes[1] = (value shr 8).toByte()
//    bytes[2] = (value shr 16).toByte()
//    bytes[3] = (value shr 24).toByte()
//    return bytes
//}
//
//// Converts a long into an array of bytes with length
//// eight.
//fun getBytes(value: Long): ByteArray? {
//    val bytes = ByteArray(8)
//    bytes[0] = value.toByte()
//    bytes[1] = (value shr 8).toByte()
//    bytes[2] = (value shr 16).toByte()
//    bytes[3] = (value shr 24).toByte()
//    bytes[4] = (value shr 32).toByte()
//    bytes[5] = (value shr 40).toByte()
//    bytes[6] = (value shr 48).toByte()
//    bytes[7] = (value shr 56).toByte()
//    return bytes
//}
//
//fun getBytes(value: Float): ByteArray? {
//    return getBytes((value * 100).toInt())
//}
//
//private fun validate(value: ByteArray?, startIndex: Int) {
//    if (value == null) {
//        throw NullPointerException("Argument \"value\" is null.")
//    }
//    if (startIndex >= value.size) {
//        throw IndexOutOfBoundsException("Index \"startIndex\" is null.")
//    }
//    require(startIndex <= value.size - 2) { "Array \"value\" is too small." }
//}
//
//