package io.github.zzzyyylllty.embiancomponent.tools

import com.google.gson.JsonElement
import io.github.zzzyyylllty.embiancomponent.utils.*
import org.bukkit.inventory.ItemStack
import java.lang.reflect.Method
import java.util.Objects.requireNonNull
import java.util.Optional
//import net.minecraft.core.component.DataComponentType
//import net.minecraft.core.RegistryAccess

val `clazz$ResourceLocation` by lazy {
    requireNonNull(
        // 1.21.4: resources.ResourceLocation → 26.1.2+: resources.Identifier
        resolveMCClass("resources.ResourceLocation", "resources.Identifier")
    )!!
}

val `clazz$Registry` by lazy {
    requireNonNull(
        // 1.21.4: core.IRegistryWritable → 26.1.2+: core.Registry
        resolveMCClass("core.IRegistryWritable", "core.Registry", "core.WritableRegistry")
    )!!
}

val `clazz$BuiltInRegistries` by lazy {
    requireNonNull(
        getClazz(
            assembleMCClass("core.registries.BuiltInRegistries")
        )
    )!!
}

val `clazz$DataComponentType` by lazy {
    requireNonNull(
        getClazz(
            assembleMCClass("core.component.DataComponentType")
        )
    )!!
}

val `clazz$DataComponentHolder` by lazy {
    requireNonNull(
        getClazz(
            assembleMCClass("core.component.DataComponentHolder")
        )
    )!!
}

val `clazz$MinecraftServer` by lazy {
    requireNonNull(
        getClazz(
            assembleMCClass("server.MinecraftServer")
        )
    )!!
}

val `field$BuiltInRegistries$DATA_COMPONENT_TYPE` by lazy {
    requireNonNull(
        getDeclaredField(
            `clazz$BuiltInRegistries`, "DATA_COMPONENT_TYPE"
        )
    )!!
}

val `instance$BuiltInRegistries$DATA_COMPONENT_TYPE` by lazy { `field$BuiltInRegistries$DATA_COMPONENT_TYPE`.get(null)!! }

val `method$ResourceLocation$fromNamespaceAndPath` by lazy {
    requireNonNull(
        getStaticMethod(
            `clazz$ResourceLocation`, `clazz$ResourceLocation`, String::class.java, String::class.java
        )
    )!!
}

val `method$Registry$getValue` by lazy {
    requireNonNull(
        getMethod(
            `clazz$Registry`, Any::class.java, 0, `clazz$ResourceLocation`
        )
    )!!
}

val `clazz$RegistryOps` by lazy {
    requireNonNull(
        getClazz(
            assembleMCClass("resources.RegistryOps")
        )
    )!!
}

val `clazz$HolderLookup$Provider` by lazy {
    requireNonNull(
        getClazz(
            assembleMCClass("core.HolderLookup\$Provider")
        )
    )!!
}

val `method$RegistryOps$create` by lazy {
    requireNonNull(
        getMethod(
            `clazz$RegistryOps`, `clazz$RegistryOps`, 0, `clazz$DynamicOps`, `clazz$HolderLookup$Provider`
        )
    )!!
}

val `method$MinecraftServer$getServer` by lazy {
    requireNonNull(
        getStaticMethod(
            `clazz$MinecraftServer`, `clazz$MinecraftServer`
        )
    )!!
}

val `instance$MinecraftServer$SERVER` by lazy {
    `method$MinecraftServer$getServer`.invoke(null)!!
}
val `clazz$RegistryAccess$Frozen` by lazy {
    requireNonNull(
        getClazz(
            assembleMCClass("core.RegistryAccess\$Frozen")
        )
    )!!
}

val `method$MinecraftServer$registryAccess` by lazy {
    requireNonNull(
        getMethod(
            `clazz$MinecraftServer`, `clazz$RegistryAccess$Frozen`, 0
        )
    )!!
}

val `instance$MinecraftServer$registryAccess` by lazy {
    `method$MinecraftServer$registryAccess`.invoke(`instance$MinecraftServer$SERVER`)!!
}
val `method$DataComponentType$codec` by lazy {
    // codec() 与 codecOrThrow() 都返回 Codec，按名字精确查找
    requireNonNull(`clazz$DataComponentType`.getMethod("codec"))!!
}

val `method$DataComponentHolder$getDataComponentType` by lazy {
    // 按名字精确查找：getTyped(DataComponentType) 与 get(DataComponentType) 参数/返回类型相同，
    // 按返回类型+索引匹配可能在 Spigot 上拿到 getTyped（返回 Optional），必须按名字
    requireNonNull(
        `clazz$DataComponentHolder`.getMethod("get", `clazz$DataComponentType`)
    )!!
}

val `clazz$ItemStack` by lazy {
    requireNonNull(
        getClazz(
            assembleMCClass("world.item.ItemStack")
        )
    )!!
}

val `method$ItemStack$setComponent` by lazy {
    // 按名字精确查找：旧版还有同签名的 set(DataComponentType, T)
    requireNonNull(
        `clazz$ItemStack`.getMethod("setComponent", `clazz$DataComponentType`, Any::class.java)
    )!!
}

val `clazz$CraftItemStack` by lazy {
    requireNonNull(
        getClazz(
            assembleCBClass("inventory.CraftItemStack")
        )
    )!!
}

val `clazz$NbtOps` by lazy {
    requireNonNull(
        getClazz(
            assembleMCClass("nbt.NbtOps")
        )
    )!!
}

val `field$NbtOps$INSTANCE` by lazy {
    // 按名字查找：NbtOps 有 INSTANCE 与 LEGACY_INSTANCE 两个同类型静态字段，按类型+索引可能拿错
    requireNonNull(
        getDeclaredField(`clazz$NbtOps`, "INSTANCE")
    )!!
}

val `instance$NbtOps$INSTANCE` by lazy {
    `field$NbtOps$INSTANCE`.get(null)!!
}

// ========== DFU 反射层 ==========
// DataResult 的 class→interface 变化发生在 DFU 8.0.16（MC 1.21.2），与服务端核心无关：
// MC ≤ 1.21.1（Paper/Spigot 都一样）是 class，MC ≥ 1.21.2 是 interface。
// 直接引用会因字节码指令差异（INVOKEINTERFACE vs INVOKEVIRTUAL）抛 IncompatibleClassChangeError，
// 因此 com.mojang.serialization 的一切调用都走反射，两种形态都能工作。
val `clazz$Codec` by lazy { requireNonNull(getClazz("com.mojang.serialization.Codec"))!! }
val `clazz$DataResult` by lazy { requireNonNull(getClazz("com.mojang.serialization.DataResult"))!! }
val `clazz$DynamicOps` by lazy { requireNonNull(getClazz("com.mojang.serialization.DynamicOps"))!! }

val `instance$JavaOps$INSTANCE` by lazy {
    val clazz = requireNonNull(getClazz("com.mojang.serialization.JavaOps"))!!
    clazz.getField("INSTANCE").get(null)!!
}
val `instance$JsonOps$INSTANCE` by lazy {
    val clazz = requireNonNull(getClazz("com.mojang.serialization.JsonOps"))!!
    clazz.getField("INSTANCE").get(null)!!
}

val `method$Codec$encodeStart` by lazy {
    // encodeStart 与 parse 擦除后签名相同（(DynamicOps, Object) → DataResult），
    // 按返回类型+索引匹配可能拿错，必须按名字精确查找
    requireNonNull(
        `clazz$Codec`.getMethod("encodeStart", `clazz$DynamicOps`, Any::class.java)
    )!!
}
val `method$Codec$parse` by lazy {
    requireNonNull(
        `clazz$Codec`.getMethod("parse", `clazz$DynamicOps`, Any::class.java)
    )!!
}
val `method$DataResult$result` by lazy {
    // 不能用 getMethod(返回类型) 匹配：result() 与 error() 都返回 Optional，必须按名字精确查找
    requireNonNull(`clazz$DataResult`.getMethod("result"))!!
}

fun codecEncodeStart(codec: Any, ops: Any, value: Any): Any =
    `method$Codec$encodeStart`.invoke(codec, ops, value)!!

fun codecParse(codec: Any, ops: Any, value: Any): Any =
    `method$Codec$parse`.invoke(codec, ops, value)!!

@Suppress("UNCHECKED_CAST")
fun <R> dataResultResult(dataResult: Any): Optional<R> =
    `method$DataResult$result`.invoke(dataResult) as Optional<R>

fun dataResultIsError(dataResult: Any): Boolean {
    // 注意：DFU 8.0.16 前后（class/interface）的 DataResult 都没有 isError() 方法，
    // 统一用 result() 判断（错误时 Optional 为空）
    return !dataResultResult<Any>(dataResult).isPresent
}

val `instance$DynamicOps$NBT` by lazy {
    `method$RegistryOps$create`.invoke(
        null,
        `instance$NbtOps$INSTANCE`,
        `instance$MinecraftServer$registryAccess`
    )!!
}

val `instance$DynamicOps$JAVA` by lazy {
    `method$RegistryOps$create`.invoke(
        null,
        `instance$JavaOps$INSTANCE`,
        `instance$MinecraftServer$registryAccess`
    )!!
}
val `instance$DynamicOps$JSON` by lazy {
    `method$RegistryOps$create`.invoke(
        null,
        `instance$JsonOps$INSTANCE`,
        `instance$MinecraftServer$registryAccess`
    )!!
}

val `clazz$Tag` by lazy {
    requireNonNull(
        getClazz(
            assembleMCClass("nbt.Tag")
        )
    )!!
}

val `method$ResourceLocation$tryParse` by lazy {
    requireNonNull(
        getStaticMethod(
            `clazz$ResourceLocation`, `clazz$ResourceLocation`, String::class.java
        )
    )!!
}

val `method$ItemStack$removeComponent` by lazy {
    // 按名字精确查找：旧版还有同签名的 remove(DataComponentType)
    requireNonNull(
        `clazz$ItemStack`.getMethod("removeComponent", `clazz$DataComponentType`)
    )!!
}


@Suppress("UNCHECKED_CAST")
fun <T> getComponent(itemStack: Any, type: Any, ops: Any): Optional<T> {
    val res = ensureDataComponentType(type) ?: return Optional.empty<T>() as Optional<T>
    val codec = `method$DataComponentType$codec`.invoke(res)!!
    val componentData = `method$DataComponentHolder$getDataComponentType`.invoke(itemStack, res)
        ?: return Optional.empty<T>() as Optional<T>
    val dataResult = codecEncodeStart(codec, ops, componentData)
    return dataResultResult(dataResult)
}


fun setComponentInternal(itemStack: Any, type: Any, ops: Any, value: Any) {
    val res = ensureDataComponentType(type)
    if (res == null) {
        return // Component not exist
    }
    val codec = `method$DataComponentType$codec`.invoke(res)!!
    val result = codecParse(codec, ops, value)
    if (dataResultIsError(result)) throw IllegalArgumentException(result.toString())
    dataResultResult<Any>(result).ifPresent {
        `method$ItemStack$setComponent`.invoke(itemStack, res, it)
    }
}



fun setComponentInternal(itemStack: Any, type: Any, value: Any) {
    when (value) {
        is JsonElement -> setComponentInternal(
            itemStack,
            type,
            `instance$DynamicOps$JSON`,
            value
        )
        `clazz$Tag`.isInstance(value) -> setComponentInternal(itemStack, type, `instance$DynamicOps$NBT`, value)
        else -> setComponentInternal(itemStack, type, `instance$DynamicOps$JAVA`, value)
    }
}

fun removeComponentInternal(itemStack: Any, type: Any, value: Any) {
    when (value) {
        is JsonElement -> setComponentInternal(itemStack, type, `instance$DynamicOps$JSON`, value)
        `clazz$Tag`.isInstance(value) -> setComponentInternal(itemStack, type, `instance$DynamicOps$NBT`, value)
        else -> setComponentInternal(itemStack, type, `instance$DynamicOps$JAVA`, value)
    }
}

@Suppress("UNCHECKED_CAST")
fun <T> getJavaComponent(itemStack: Any, type: Any): Optional<T> {
    return getComponent<T>(itemStack, type, `instance$DynamicOps$JAVA`)
}


fun getJsonComponent(itemStack: Any, type: Any): Optional<JsonElement> {
    return getComponent<JsonElement>(itemStack, type, `instance$DynamicOps$JSON`)
}

fun getNBTComponent(itemStack: Any, type: Any): Optional<Any> {
    return getComponent<Any>(itemStack, type, `instance$DynamicOps$NBT`)
}

fun removeComponent(itemStack: Any, type: Any) {
    `method$ItemStack$removeComponent`.invoke(itemStack, type)
}


val craftItemStackClass by lazy { getClazz(assembleCBClass("inventory.CraftItemStack"))!! }
val asNMSCopyMethod by lazy { craftItemStackClass.getMethod("asNMSCopy", ItemStack::class.java) }
val asBukkitCopyMethod by lazy { craftItemStackClass.getMethod("asBukkitCopy", `clazz$ItemStack`) }

fun asNMSCopy(itemStack: ItemStack?): Any {
    return asNMSCopyMethod.invoke(null, itemStack)
}


fun asBukkitCopy(itemStack: Any?): ItemStack {
    return asBukkitCopyMethod.invoke(null, itemStack) as ItemStack
}




fun Any.setComponentNMS(componentId: String,value: Any): Any {
    try {
        setComponentInternal(this, componentId, value)
        return this
    } catch (e: IllegalArgumentException) {
        // Component 在当前版本不存在!
        return this
    }
}

fun Any.getComponentNMS(componentId: String): JsonElement? {
    return getJsonComponent(this, componentId).orElse(null)
}
fun <T> Any.getComponentJavaNMS(componentId: String): T? {
    return getJavaComponent<T>(this, componentId).orElse(null)
}
@Suppress("UNCHECKED_CAST")
fun Any.getComponentsNMS(): Map<String, JsonElement> {
    val result = mutableMapOf<String, JsonElement>()

    if (!`clazz$DataComponentHolder`.isInstance(this)) {
        return result
    }

    val getComponentsMethod = `clazz$DataComponentHolder`.getMethod("getComponents")
    val dataComponentMap = getComponentsMethod.invoke(this)!!

    // 获取 iterator() 方法
    val iteratorMethod = dataComponentMap.javaClass.getMethod("iterator")
    val iterator = iteratorMethod.invoke(dataComponentMap) as Iterator<Any>

    while (iterator.hasNext()) {
        val typedDataComponent = iterator.next()

        // typedDataComponent 需要通过反射访问 type() 和 value()
        val typeMethod = typedDataComponent.javaClass.getMethod("type")
        val valueMethod = typedDataComponent.javaClass.getMethod("value")

        val componentType = typeMethod.invoke(typedDataComponent)
        val componentValue = valueMethod.invoke(typedDataComponent)

        val id = componentType.toString()

        val codec = `method$DataComponentType$codec`.invoke(componentType)!!
        val encodedResult = codecEncodeStart(codec, `instance$DynamicOps$JSON`, componentValue)
        val jsonElement = dataResultResult<JsonElement>(encodedResult).orElse(null) ?: continue

        result[id] = jsonElement
    }

    return result
}
@Suppress("UNCHECKED_CAST")
fun Any.getComponentsJavaNMS(): Map<String, Any?> {
    val result = mutableMapOf<String, Any?>()

    if (!`clazz$DataComponentHolder`.isInstance(this)) {
        return result
    }

    val getComponentsMethod = `clazz$DataComponentHolder`.getMethod("getComponents")
    val dataComponentMap = getComponentsMethod.invoke(this)!!

    // 获取 iterator() 方法
    val iteratorMethod = dataComponentMap.javaClass.getMethod("iterator")
    val iterator = iteratorMethod.invoke(dataComponentMap) as Iterator<Any>

    while (iterator.hasNext()) {
        val typedDataComponent = iterator.next()

        // typedDataComponent 需要通过反射访问 type() 和 value()
        val typeMethod = typedDataComponent.javaClass.getMethod("type")
        val valueMethod = typedDataComponent.javaClass.getMethod("value")

        val componentType = typeMethod.invoke(typedDataComponent)
        val componentValue = valueMethod.invoke(typedDataComponent)

        val id = componentType.toString()

        val codec = `method$DataComponentType$codec`.invoke(componentType)!!
        val encodedResult = codecEncodeStart(codec, `instance$DynamicOps$JAVA`, componentValue)
        val element = dataResultResult<Any>(encodedResult).orElse(null) ?: continue

        result[id] = element
    }

    return result
}
@Suppress("UNCHECKED_CAST")
fun Any.getComponentsNMSFilteredLegacy(): Map<String, Any?> {
    val result = mutableMapOf<String, Any?>()

    if (!`clazz$DataComponentHolder`.isInstance(this)) {
        return result
    }

    val getComponentsMethod = `clazz$DataComponentHolder`.getMethod("getComponents")
    val dataComponentMapInstance = getComponentsMethod.invoke(this) ?: return result

    val iteratorMethod = dataComponentMapInstance.javaClass.getMethod("iterator")
    val iterator = iteratorMethod.invoke(dataComponentMapInstance) as Iterator<Any>

    while (iterator.hasNext()) {
        val typedDataComponent = iterator.next()

        val typeMethod = typedDataComponent.javaClass.getMethod("type")
        val componentTypeRaw = typeMethod.invoke(typedDataComponent)

        // 反射调用 toString 得到 resourceLocation字符串
        val resourceLocationStr = componentTypeRaw.toString()

        // 用注册表去获取对应完整组件类型实例，避免版本差异或者动态生成的子类导致反射异常
        val resourceLocation = safeParseResourceLocation(resourceLocationStr)
        val componentTypeOptional = `method$Registry$getValue`.invoke(`instance$BuiltInRegistries$DATA_COMPONENT_TYPE`, resourceLocation)
            ?: continue
        val componentType = unwrapValue(componentTypeOptional) ?: continue

        val valueMethod = typedDataComponent.javaClass.getMethod("value")
        val componentValue = valueMethod.invoke(typedDataComponent)

        println("componentType class: ${componentType?.javaClass?.name}")
        println("expected clazz: ${`clazz$DataComponentType`.name}")
        println("isInstance: ${`clazz$DataComponentType`.isInstance(componentType)}")

        val codec = findCodecMethod(componentType)?.invoke(componentType) ?: continue

        // 序列化当前数据
        val encodedResult = codecEncodeStart(codec, `instance$DynamicOps$NBT`, componentValue)
        if (dataResultIsError(encodedResult)) continue
        val currentNbtTag = dataResultResult<Any>(encodedResult).orElse(null) ?: continue

        // 比较默认值，反序列化空Json
        val emptyJson = com.google.gson.JsonObject()
        val defaultParseResult = codecParse(codec, `instance$DynamicOps$JSON`, emptyJson)
        val defaultValue = if (!dataResultIsError(defaultParseResult))
            dataResultResult<Any>(defaultParseResult).orElse(null)
        else null

        if (defaultValue != null) {
            val defaultEncoded = codecEncodeStart(codec, `instance$DynamicOps$NBT`, defaultValue)
            if (!dataResultIsError(defaultEncoded)) {
                val defaultNbtTag = dataResultResult<Any>(defaultEncoded).orElse(null)
                if (defaultNbtTag != null && defaultNbtTag == currentNbtTag) {
                    // 当前组件等价于默认值，过滤掉
                    continue
                }
            }
        }

        // 转换NBT数据为Java
        val jsonResult = codecEncodeStart(codec, `instance$DynamicOps$JAVA`, componentValue)
        if (dataResultIsError(jsonResult)) continue
        val componentJson = dataResultResult<Any>(jsonResult).orElse(null) ?: continue

        result[resourceLocationStr] = componentJson
    }

    return result
}

@Suppress("UNCHECKED_CAST")
fun Any.getComponentsNMSFilteredWithoutCache(): Map<String, JsonElement> {
    val result = mutableMapOf<String, JsonElement>()

    if (!`clazz$DataComponentHolder`.isInstance(this)) {
        return result
    }

    val getComponentsPatchMethod = try {
        this.javaClass.getMethod("getComponentsPatch")
    } catch (e: NoSuchMethodException) {
        return result
    }

    val patch = getComponentsPatchMethod.invoke(this) ?: return result

    val getItemMethod = try {
        this.javaClass.getMethod("getItem")
    } catch (e: NoSuchMethodException) {
        return result
    }

    val item = getItemMethod.invoke(this) ?: return result

    val getComponentsMethodOfItem = try {
        item.javaClass.getMethod("components")
    } catch (e: NoSuchMethodException) {
        return result
    }

    val prototype = getComponentsMethodOfItem.invoke(item) ?: return result


    // 获取 patch.entrySet()
    val entrySetMethod = patch.javaClass.getMethod("entrySet")
    val entrySet = entrySetMethod.invoke(patch) as Set<*>

    for (entryObj in entrySet) {
        val entry = entryObj as Map.Entry<*, *>

        val componentTypeRaw = entry.key ?: continue
        val componentValue = entry.value?.let { unwrapValue(it) } ?: continue

        val resourceLocationStr = componentTypeRaw.toString()
        val resourceLocation = safeParseResourceLocation(resourceLocationStr) ?: continue

        val componentTypeOptional = `method$Registry$getValue`.invoke(
            `instance$BuiltInRegistries$DATA_COMPONENT_TYPE`,
            resourceLocation
        ) ?: continue
        val componentType = unwrapValue(componentTypeOptional) ?: continue

        println("componentType class: ${componentType.javaClass.name}")
        println("isInstance: ${`clazz$DataComponentType`.isInstance(componentType)}")


        val prototypeGetTypedMethod = prototype.javaClass.getMethod("getTyped", `clazz$DataComponentType`)
        val prototypeTyped = prototypeGetTypedMethod.invoke(prototype, componentType)



        if (prototypeTyped != null) {
            val prototypeValueMethod = prototypeTyped.javaClass.getMethod("value")
            val prototypeValue = prototypeValueMethod.invoke(prototypeTyped)
            if (prototypeValue == componentValue) {
                // 补丁值和原型相同，跳过
                continue
            }
        }

        val codec = findCodecMethod(componentType)?.invoke(componentType) ?: continue

        val encodedResultNBT = codecEncodeStart(codec, `instance$DynamicOps$NBT`, componentValue)
        if (dataResultIsError(encodedResultNBT)) {
            continue
        }

        val encodedResultJson = codecEncodeStart(codec, `instance$DynamicOps$JSON`, componentValue)
        if (dataResultIsError(encodedResultJson)) {
            continue
        }
        val componentJson = dataResultResult<JsonElement>(encodedResultJson).orElse(null) ?: continue

        result[resourceLocationStr] = componentJson
    }

    return result
}
// ========== 缓存反射方法 ==========
private val `method$ItemStack$getComponentsPatch` by lazy {
    `clazz$ItemStack`.getMethod("getComponentsPatch")
}

private val `method$ItemStack$getItem` by lazy {
    `clazz$ItemStack`.getMethod("getItem")
}

private val `method$Item$components` by lazy {
    val itemClass = getClazz(assembleMCClass("world.item.Item"))!!
    itemClass.getMethod("components")
}

private val `method$DataComponentPatch$entrySet` by lazy {
    val patchClass = getClazz(assembleMCClass("core.component.DataComponentPatch"))!!
    patchClass.getMethod("entrySet")
}

private val `method$DataComponentMap$getTyped` by lazy {
    val mapClass = getClazz(assembleMCClass("core.component.DataComponentMap"))!!
    mapClass.getMethod("getTyped", `clazz$DataComponentType`)
}

private val `method$TypedDataComponent$value` by lazy {
    val typedClass = getClazz(assembleMCClass("core.component.TypedDataComponent"))!!
    typedClass.getMethod("value")
}
//
//// ========== 优化后的主函数 ==========
//@Suppress("UNCHECKED_CAST")
//fun Any.getComponentsNMSFiltered(): Map<String, JsonElement?> {
//    val result = mutableMapOf<String, JsonElement?>()
//
//    if (!`clazz$DataComponentHolder`.isInstance(this)) {
//        return result
//    }
//
//    // 使用缓存的反射方法
//    val patch = `method$ItemStack$getComponentsPatch`.invoke(this) ?: return result
//    val item = `method$ItemStack$getItem`.invoke(this) ?: return result
//    val prototype = `method$Item$components`.invoke(item) ?: return result
//
//    // 获取 patch.entrySet()
//    val entrySet = `method$DataComponentPatch$entrySet`.invoke(patch) as Set<*>
//
//    for (entryObj in entrySet) {
//        val entry = entryObj as? Map.Entry<*, *> ?: continue
//
//        val componentTypeRaw = entry.key ?: continue
//        val componentValue = entry.value?.let { unwrapValue(it) } ?: continue
//
//        // 转换资源位置字符串
//        val resourceLocationStr = componentTypeRaw.toString()
//        // 672 val resourceLocation = `method$ResourceLocation$tryParse`.invoke(null, resourceLocationStr) ?: continue
//        val resourceLocation = when {
//            `clazz$ResourceLocation`.isInstance(componentTypeRaw) -> componentTypeRaw
//            else -> {
//                val str = componentTypeRaw.toString()
//                try {
//                    `method$ResourceLocation$tryParse`.invoke(null, str)
//                } catch (e: Exception) {
//                    null
//                }
//            }
//        } ?: continue
//
//        // 从注册表获取组件类型
//        val componentTypeOptional = `method$Registry$getValue`.invoke(
//            `instance$BuiltInRegistries$DATA_COMPONENT_TYPE`,
//            resourceLocation
//        ) ?: continue
//        val componentType = unwrapValue(componentTypeOptional)
//
//        // 比较原型值，过滤未修改的组件
//        val prototypeTyped = `method$DataComponentMap$getTyped`.invoke(prototype, componentType)
//        if (prototypeTyped != null) {
//            val prototypeValue = `method$TypedDataComponent$value`.invoke(prototypeTyped)
//            if (prototypeValue == componentValue) {
//                continue
//            }
//        }
//
//        // 获取 codec（使用缓存的方法查找逻辑）
//        val codec = componentType?.let { getCodecForComponentType(it) } ?: continue
//
//        // 使用 JSON DynamicOps 编码
//        val encodedResultJava = codec.encodeStart(`instance$DynamicOps$JSON`, componentValue)
//        if (encodedResultJava.isError) {
//            continue
//        }
//        val componentJavaObject = encodedResultJava.result().orElse(null) ?: continue
//
//        result[resourceLocationStr] = componentJavaObject
//    }
//
//    return result
//}
@Suppress("UNCHECKED_CAST")
fun Any.getComponentsNMSFiltered(): Map<String, JsonElement> {
    val result = mutableMapOf<String, JsonElement>()

    if (!`clazz$DataComponentHolder`.isInstance(this)) {
        return result
    }

    // 1. 获取物品原型的默认组件 Map（用于比较）
    val item = `method$ItemStack$getItem`.invoke(this) ?: return result
    val prototype = `method$Item$components`.invoke(item) ?: return result

    // 2. 获取 ItemStack 全量组件（已合并 patch）
    val getComponentsMethod = `clazz$DataComponentHolder`.getMethod("getComponents")
    val dataComponentMap = getComponentsMethod.invoke(this) ?: return result

    val iteratorMethod = dataComponentMap.javaClass.getMethod("iterator")
    val iterator = iteratorMethod.invoke(dataComponentMap) as Iterator<Any>

    while (iterator.hasNext()) {
        val typedDataComponent = iterator.next()

        val typeMethod = typedDataComponent.javaClass.getMethod("type")
        val valueMethod = typedDataComponent.javaClass.getMethod("value")

        val componentType = typeMethod.invoke(typedDataComponent) ?: continue
        val componentValue = valueMethod.invoke(typedDataComponent) ?: continue

        // 3. 与原型比较：如果原型中有相同值，则过滤掉
        val prototypeTyped = try {
            `method$DataComponentMap$getTyped`.invoke(prototype, componentType)
        } catch (e: Exception) {
            null
        }

        if (prototypeTyped != null) {
            val prototypeValue = try {
                `method$TypedDataComponent$value`.invoke(prototypeTyped)
            } catch (e: Exception) {
                null
            }
            // 原型值与当前值相同 → 跳过（未修改）
            if (prototypeValue == componentValue) {
                continue
            }
        }

        // 4. 序列化为 JSON
        val codec = getCodecForComponentType(componentType) ?: continue

        val encodedResult = codecEncodeStart(codec, `instance$DynamicOps$JSON`, componentValue)
        if (dataResultIsError(encodedResult)) continue

        val jsonElement = dataResultResult<JsonElement>(encodedResult).orElse(null) ?: continue
        result[componentType.toString()] = jsonElement
    }

    return result
}

//// ========== 优化后的主函数 ==========
//@Suppress("UNCHECKED_CAST")
//fun Any.getComponentsJavaNMSFiltered(): Map<String, Any?> {
//    val result = mutableMapOf<String, Any?>()
//
//    if (!`clazz$DataComponentHolder`.isInstance(this)) {
//        return result
//    }
//
//    // 使用缓存的反射方法
//    val patch = `method$ItemStack$getComponentsPatch`.invoke(this) ?: return result
//    val item = `method$ItemStack$getItem`.invoke(this) ?: return result
//    val prototype = `method$Item$components`.invoke(item) ?: return result
//
//    // 获取 patch.entrySet()
//    val entrySet = `method$DataComponentPatch$entrySet`.invoke(patch) as Set<*>
//
//    for (entryObj in entrySet) {
//        val entry = entryObj as? Map.Entry<*, *> ?: continue
//
//        val componentTypeRaw = entry.key ?: continue
//        val componentValue = entry.value?.let { unwrapValue(it) } ?: continue
//
//        // 转换资源位置字符串
//        val resourceLocationStr = componentTypeRaw.toString()
//        val resourceLocation = `method$ResourceLocation$tryParse`.invoke(null, resourceLocationStr) ?: continue
//
//        // 从注册表获取组件类型
//        val componentTypeOptional = `method$Registry$getValue`.invoke(
//            `instance$BuiltInRegistries$DATA_COMPONENT_TYPE`,
//            resourceLocation
//        ) ?: continue
//        val componentType = unwrapValue(componentTypeOptional)
//
//        // 比较原型值，过滤未修改的组件
//        val prototypeTyped = `method$DataComponentMap$getTyped`.invoke(prototype, componentType)
//        if (prototypeTyped != null) {
//            val prototypeValue = `method$TypedDataComponent$value`.invoke(prototypeTyped)
//            if (prototypeValue == componentValue) {
//                continue
//            }
//        }
//
//        // 获取 codec（使用缓存的方法查找逻辑）
//        val codec = componentType?.let { getCodecForComponentType(it) } ?: continue
//
//        // 使用 JSON DynamicOps 编码
//        val encodedResultJava = codec.encodeStart(`instance$DynamicOps$JAVA`, componentValue)
//        if (encodedResultJava.isError) {
//            continue
//        }
//        val componentJavaObject = encodedResultJava.result().orElse(null) ?: continue
//
//        result[resourceLocationStr] = componentJavaObject
//    }
//
//    return result
//}

@Suppress("UNCHECKED_CAST")
fun Any.getComponentsJavaNMSFiltered(): Map<String, Any?> {
    val result = mutableMapOf<String, Any?>()

    if (!`clazz$DataComponentHolder`.isInstance(this)) {
        return result
    }

    val item = `method$ItemStack$getItem`.invoke(this) ?: return result
    val prototype = `method$Item$components`.invoke(item) ?: return result

    val getComponentsMethod = `clazz$DataComponentHolder`.getMethod("getComponents")
    val dataComponentMap = getComponentsMethod.invoke(this) ?: return result

    val iteratorMethod = dataComponentMap.javaClass.getMethod("iterator")
    val iterator = iteratorMethod.invoke(dataComponentMap) as Iterator<Any>

    while (iterator.hasNext()) {
        val typedDataComponent = iterator.next()

        val typeMethod = typedDataComponent.javaClass.getMethod("type")
        val valueMethod = typedDataComponent.javaClass.getMethod("value")

        val componentType = typeMethod.invoke(typedDataComponent) ?: continue
        val componentValue = valueMethod.invoke(typedDataComponent) ?: continue

        // 与原型比较过滤
        val prototypeTyped = try {
            `method$DataComponentMap$getTyped`.invoke(prototype, componentType)
        } catch (e: Exception) { null }

        if (prototypeTyped != null) {
            val prototypeValue = try {
                `method$TypedDataComponent$value`.invoke(prototypeTyped)
            } catch (e: Exception) { null }
            if (prototypeValue == componentValue) continue
        }

        val codec = getCodecForComponentType(componentType) ?: continue

        val encodedResult = codecEncodeStart(codec, `instance$DynamicOps$JAVA`, componentValue)
        if (dataResultIsError(encodedResult)) continue

        val element = dataResultResult<Any>(encodedResult).orElse(null) ?: continue
        result[componentType.toString()] = element
    }

    return result
}
// ========== 辅助函数：获取 Codec（带缓存） ==========
private val codecCache = mutableMapOf<Any, Any>()

private fun findCodecMethod(componentType: Any): Method? {
    // 用 getMethod 而非 getDeclaredMethod：codecOrThrow 是接口上的 default 方法，
    // 实现类不声明它，getDeclaredMethod 会抛 NoSuchMethodException
    return try {
        componentType.javaClass.getMethod("codec")
    } catch (_: NoSuchMethodException) {
        try {
            componentType.javaClass.getMethod("codecOrThrow")
        } catch (_: NoSuchMethodException) {
            null
        }
    }
}

private fun getCodecForComponentType(componentType: Any): Any? {
    return codecCache.getOrPut(componentType) {
        runCatching { findCodecMethod(componentType)?.invoke(componentType) }.getOrNull() ?: return null
    }
}

/**
 * 通过反射从一个 ItemStack 实例中移除指定的数据组件。
 *
 * @param componentId 要移除的组件的资源路径字符串, 例如 "minecraft:custom_name"。
 * @return 如果移除成功，返回被移除的组件的旧值；如果失败或物品不是ItemStack，返回 null。
 */
fun Any.removeComponentNMS(componentId: String): Any? {

    val componentType = ensureDataComponentType(componentId) ?: return null

    return try {
        // 使用缓存的反射方法，传入 ItemStack 实例和获取到的 DataComponentType 实例
        `method$ItemStack$removeComponent`.invoke(this, componentType)
    } catch (e: Exception) {
        // 在反射调用失败时打印错误并返回 null
        e.printStackTrace()
        null
    }
}


private fun safeParseResourceLocation(str: String): Any? {
    return try {
        if (str.contains(':')) {
            val colonIndex = str.indexOf(':')
            val namespace = str.substring(0, colonIndex)
            val path = str.substring(colonIndex + 1)
            `method$ResourceLocation$fromNamespaceAndPath`.invoke(null, namespace, path)
        } else {
            `method$ResourceLocation$tryParse`.invoke(null, str)
        }
    } catch (e: Exception) {
        null
    }
}

fun ensureDataComponentType(type: Any): Any? {
    val rawResult = when {
        `clazz$DataComponentType`.isInstance(type) -> type
        `clazz$ResourceLocation`.isInstance(type) -> `method$Registry$getValue`.invoke(`instance$BuiltInRegistries$DATA_COMPONENT_TYPE`, type)
        else -> {
            val typeStr = type.toString()
            val rl = safeParseResourceLocation(typeStr)
            `method$Registry$getValue`.invoke(`instance$BuiltInRegistries$DATA_COMPONENT_TYPE`, rl)
        }
    }
    return unwrapValue(rawResult)
}


