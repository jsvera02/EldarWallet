package com.jsvera.eldarwallet.utils

import android.content.Context
import android.util.Base64
import com.jsvera.eldarwallet.di.EldarWalletApp
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

object EncryptionUtil {
    private const val ALGORITHM = "AES"
    private const val PREFS_NAME = "encryption_prefs"
    private const val KEY_NAME = "secret_key"

    private lateinit var appContext: Context

    fun initialize(context: Context) {
        appContext = context.applicationContext
    }

    private val secretKey: SecretKey by lazy {
        val keyString = getStoredKey()
        if (keyString.isEmpty()) {
            val newKey = generateNewKey()
            storeKey(newKey)
            newKey
        } else {
            getKeyFromString(keyString)
        }
    }

    private fun generateNewKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(ALGORITHM)
        keyGenerator.init(256)
        return keyGenerator.generateKey()
    }

    private fun storeKey(key: SecretKey) {
        val keyString = Base64.encodeToString(key.encoded, Base64.DEFAULT)
        val sharedPreferences = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(KEY_NAME, keyString).apply()
    }

    private fun getStoredKey(): String {
        val sharedPreferences = appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_NAME, "") ?: ""
    }

    private fun getKeyFromString(encodedKey: String): SecretKey {
        val decodedKey = Base64.decode(encodedKey, Base64.DEFAULT)
        return SecretKeySpec(decodedKey, 0, decodedKey.size, ALGORITHM)
    }

    fun encrypt(data: String): String {
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val encryptedData = cipher.doFinal(data.toByteArray())
        return Base64.encodeToString(encryptedData, Base64.DEFAULT)
    }

    fun decrypt(data: String): String {
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        val decodedData = Base64.decode(data, Base64.DEFAULT)
        val decryptedData = cipher.doFinal(decodedData)
        return String(decryptedData)
    }
}