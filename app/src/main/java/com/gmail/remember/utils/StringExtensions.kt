package com.gmail.remember.utils

import android.annotation.SuppressLint
import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

const val SECRET_KEY = "DDJJDJFBvhjdsbvjebvj"

fun String.toDayName(): String = when (this) {
    "mon" -> "Понедельник"
    "tue" -> "Вторник"
    "wed" -> "Среда"
    "thu" -> "Четверг"
    "fri" -> "Пятница"
    "sat" -> "Суббота"
    "sun" -> "Воскресенье"
    else -> ""
}

@SuppressLint("GetInstance")
fun String.encrypt(encryptionKey: String = SECRET_KEY): String =
    try {
        val cipher = Cipher.getInstance("AES").apply {
            init(
                Cipher.ENCRYPT_MODE,
                SecretKeySpec(ByteArray(16), "AES"),
                IvParameterSpec(encryptionKey.toByteArray())
            )
        }
        Base64.encodeToString(cipher.doFinal(this.toByteArray()), Base64.DEFAULT)
    } catch (e: Exception) {
        this
    }

@SuppressLint("GetInstance")
fun String.decrypt(encryptionKey: String = SECRET_KEY): String =
    try {
        val cipher = Cipher.getInstance("AES").apply {
            init(
                Cipher.DECRYPT_MODE,
                SecretKeySpec(ByteArray(16), "AES"),
                IvParameterSpec(encryptionKey.toByteArray())
            )
        }
        String(cipher.doFinal(Base64.decode(this, Base64.DEFAULT)))
    } catch (e: Exception) {
        this
    }