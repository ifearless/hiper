package app.spidy.hiper

import app.spidy.hiper.data.Headers
import app.spidy.hiper.data.HiperResponse
import java.io.File


/* Hiper */

class Hiper {
    fun get(
        url: String, isStream: Boolean = false, byteSize: Int = 4096,
        args: HashMap<String, Any> = hashMapOf(),
        headers: HashMap<String, Any> = hashMapOf(),
        cookies: HashMap<String, Any> = hashMapOf(),
        username: String? = null,
        password: String? = null,
        timeout: Long? = null
    ): HiperResponse {
        return GetRequest(url, isStream, byteSize, args=args, headers=headers, cookies=cookies,
            username = username, password = password, timeout=timeout).sync()
    }

    fun post(
        url: String, isStream: Boolean = false, byteSize: Int = 4096,
        args: HashMap<String, Any> = hashMapOf(),
        form: HashMap<String, Any> = hashMapOf(),
        files: List<File> = listOf(),
        headers: HashMap<String, Any> = hashMapOf(),
        cookies: HashMap<String, Any> = hashMapOf(),
        username: String? = null,
        password: String? = null,
        timeout: Long? = null
    ): HiperResponse {
        return PostRequest(url, isStream, byteSize, args=args, form=form, files=files, headers=headers,
            cookies=cookies, username=username, password = password, timeout = timeout).sync()
    }

    fun head(
        url: String, isStream: Boolean = false, byteSize: Int = 4096,
        args: HashMap<String, Any> = hashMapOf(),
        headers: HashMap<String, Any> = hashMapOf(),
        cookies: HashMap<String, Any> = hashMapOf(),
        username: String? = null,
        password: String? = null,
        timeout: Long? = null
    ): HiperResponse {
        return HeadRequest(url, isStream, byteSize, args=args, headers=headers, cookies=cookies,
            username = username, password = password, timeout=timeout).sync()
    }

    fun async() = asyncInstance ?: synchronized(this) {
        asyncInstance ?: Asynchronous().also { asyncInstance = it }
    }


    inner class Asynchronous {
        fun get(
            url: String, isStream: Boolean = false, byteSize: Int = 4096,
            args: HashMap<String, Any> = hashMapOf(),
            headers: HashMap<String, Any> = hashMapOf(),
            cookies: HashMap<String, Any> = hashMapOf(),
            username: String? = null,
            password: String? = null,
            timeout: Long? = null
        ): Queue {
            val req = GetRequest(url, isStream, byteSize, args=args, headers=headers, cookies=cookies,
                username = username, password = password, timeout=timeout)
            return Queue { req.async(it) }
        }

        fun post(
            url: String, isStream: Boolean = false, byteSize: Int = 4096,
            args: HashMap<String, Any> = hashMapOf(),
            form: HashMap<String, Any> = hashMapOf(),
            files: List<File> = listOf(),
            headers: HashMap<String, Any> = hashMapOf(),
            cookies: HashMap<String, Any> = hashMapOf(),
            username: String? = null,
            password: String? = null,
            timeout: Long? = null
        ): Queue {
            val req = PostRequest(url, isStream, byteSize, args=args, form=form, files=files,
                headers=headers, cookies=cookies, username=username, password=password, timeout = timeout)
            return Queue { req.async(it) }
        }

        fun head(
            url: String, isStream: Boolean = false, byteSize: Int = 4096,
            args: HashMap<String, Any> = hashMapOf(),
            headers: HashMap<String, Any> = hashMapOf(),
            cookies: HashMap<String, Any> = hashMapOf(),
            username: String? = null,
            password: String? = null,
            timeout: Long? = null
        ): Queue {
            val req = HeadRequest(url, isStream, byteSize, args=args, headers=headers, cookies=cookies,
                username = username, password = password, timeout=timeout)
            return Queue { req.async(it) }
        }
    }

    companion object {
        @Volatile private var instance: Hiper? = null
        @Volatile private var asyncInstance: Hiper.Asynchronous? = null
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: Hiper().also { instance = it }
        }
    }
}