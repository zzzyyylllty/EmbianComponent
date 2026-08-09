package io.github.zzzyyylllty.embiancomponent.utils

import org.bukkit.Bukkit
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.util.Optional

const val MC_PREFIX = "net.minecraft."
const val CB_PREFIX = "org.bukkit.craftbukkit."

fun assembleMCClass(className: String): String {
    return MC_PREFIX + className
}

/**
 * 解析 craftbukkit 类全名。
 * Paper 1.21.4+ 的包名没有版本后缀（org.bukkit.craftbukkit.inventory...），
 * 而 Spigot 等核心带版本后缀（org.bukkit.craftbukkit.v1_21_R3.inventory...）。
 * 优先尝试无后缀，失败后从服务端实现类包名提取版本后缀。
 */
fun assembleCBClass(className: String): String {
    return resolveCBClass(className) ?: (CB_PREFIX + className)
}

fun resolveCBClass(className: String): String? {
    val noSuffix = CB_PREFIX + className
    if (classExists(noSuffix)) return noSuffix
    val suffix = craftbukkitVersionSuffix
    if (suffix.isNotEmpty()) {
        val withSuffix = CB_PREFIX + suffix + "." + className
        if (classExists(withSuffix)) return withSuffix
    }
    return null
}

private object ClassLoaderHolder

private fun classExists(name: String): Boolean =
    // initialize=false：只加载类，不触发静态初始化
    runCatching { Class.forName(name, false, ClassLoaderHolder::class.java.classLoader) }.isSuccess

private val craftbukkitVersionSuffix: String by lazy {
    runCatching {
        // 用类名解析包名：Class.getPackage() 在部分类加载器环境下会返回 null
        val pkg = Bukkit.getServer().javaClass.name.substringBeforeLast('.')
        if (pkg.startsWith(CB_PREFIX)) pkg.removePrefix(CB_PREFIX).substringBefore('.') else ""
    }.getOrDefault("")
}


val holderClass by lazy { getClazz("net.minecraft.core.Holder")!! }

fun unwrapValue(obj: Any): Any? {
    if (obj is Optional<*>) {
        if (obj.isPresent) return unwrapValue(obj.get())
        return null
        // throw IllegalArgumentException("Optional empty")
    }
    if (holderClass.isInstance(obj)) {
        val methodNameCandidates = listOf("get", "value")
        val getMethod = methodNameCandidates.asSequence()
            .mapNotNull {
                try {
                    holderClass.getDeclaredMethod(it)
                } catch (_: NoSuchMethodException) {
                    null
                }
            }
            .firstOrNull()

        if (getMethod != null) {
            getMethod.isAccessible = true
            return unwrapValue(getMethod.invoke(obj)!!)
        } else {
            throw IllegalStateException("No suitable get method found on Holder class")
        }
    }
    return obj
}


fun getClazz(className: String): Class<*>? {
    return try {
        Class.forName(className)
    } catch (e: Throwable) {
        e.printStackTrace()
        null
    }
}

/**
 * Try multiple class names, for cross-version compatibility (e.g. ResourceLocation → Identifier).
 * Returns the first successfully loaded class, or null if all fail.
 */
fun resolveMCClass(vararg candidates: String): Class<*>? {
    for (candidate in candidates) {
        try {
            return Class.forName(MC_PREFIX + candidate)
        } catch (_: ClassNotFoundException) {
            continue
        }
    }
    return null
}

fun getDeclaredField(clazz: Class<*>, type: Class<*>, index: Int): Field? =
    clazz.declaredFields
        .filter { it.type == type }
        .getOrNull(index)
        ?.apply { isAccessible = true }

fun getDeclaredField(clazz: Class<*>, name: String): Field? =
    runCatching {
        clazz.getDeclaredField(name).apply { isAccessible = true }
    }.getOrNull()

fun getStaticMethod(
    clazz: Class<*>,
    returnType: Class<*>,
    vararg parameterTypes: Class<*>,
): Method? {
    outer@ for (method in clazz.methods) {
        if (method.parameterCount != parameterTypes.size) continue
        if (!Modifier.isStatic(method.modifiers)) continue

        val types = method.parameterTypes
        for (i in types.indices) {
            if (types[i] != parameterTypes[i]) continue@outer
        }

        if (returnType.isAssignableFrom(method.returnType)) {
            method.isAccessible = true
            return method
        }
    }
    return null
}

fun getMethod(
    clazz: Class<*>,
    returnType: Class<*>,
    index: Int,
    vararg parameterTypes: Class<*>,
): Method? =
    clazz.methods
        .filter { method ->
            method.parameterCount == parameterTypes.size &&
                    method.parameterTypes.contentEquals(parameterTypes) &&
                    returnType.isAssignableFrom(method.returnType)
        }
        .getOrNull(index)
