package io.github.zzzyyylllty.embiancomponent.tools

import com.google.gson.JsonElement
import org.bukkit.inventory.ItemStack

class ComponentSetter {

    /**
     * 获取物品的 **所有** 数据组件。
     * 包括没有过滤的组件，例如剑 **会** 返回包括damage=0的默认组件。
     * @param item Bukkit物品
     * */
    fun getAllComponents(item: ItemStack): Map<String, Any?> {
        return asNMSCopyMethod(item).getComponentsNMS()
    }

    /**
     * 获取物品的 **所有过滤掉默认组件后的** 数据组件。
     * 不包括没有过滤的组件，例如剑 **不会** 返回包括damage=0的默认组件。
     * @param item Bukkit物品
     * */
    fun getAllComponentsFiltered(item: ItemStack): Map<String, Any?> {
        return asNMSCopyMethod(item).getComponentsNMSFiltered()
    }

    /**
     * 获取物品的 **所有** 数据组件。(以 NMS 物品获取)
     * 包括没有过滤的组件，例如剑 **会** 返回包括damage=0的默认组件。
     * @param item NMS物品
     * */
    fun getAllComponentsNMS(item: Any): Map<String, Any?> {
        return item.getComponentsNMS()
    }

    /**
     * 获取物品的 **所有过滤掉默认组件后的** 数据组件。(以 NMS 物品获取)
     * 不包括没有过滤的组件，例如剑 **不会** 返回包括damage=0的默认组件。
     * @param item NMS物品
     * */
    fun getAllComponentsFilteredNMS(item: Any): Map<String, Any?> {
        return item.getComponentsNMSFiltered()
    }

    /**
     * 获取物品的指定的数据组件。
     *
     * 如果多次使用，请使用如下代码以避免频繁转换 ItemStack。
     * ```
     * val nmsItem = asNMSCopy(yourBukkitItem)
     * ComponentSetter().getComponentNMS(nmsItem, ...)
     * ComponentSetter().getComponentNMS(nmsItem, ...) ...
     * ```
     *
     * @param item Bukkit物品
     * @param component 组件id，形如 `minecraft:custom_data`
     * */
    fun getComponent(item: ItemStack, component: String): JsonElement? {
        return asNMSCopyMethod(item).getComponentNMS(component)
    }


    /**
     * 获取物品的指定的数据组件。
     *
     * @param item NMS物品
     * @param component 组件id，形如 `minecraft:custom_data`
     * */
    fun getComponentNMS(item: Any, component: String): JsonElement? {
        return item.getComponentNMS(component)
    }

    /**
     * 获取物品的指定的数据组件。
     *
     * 使用 Java 组件解析器。
     *
     * 如果多次使用，请使用如下代码以避免频繁转换 ItemStack。
     * ```
     * val nmsItem = asNMSCopy(yourBukkitItem)
     * ComponentSetter().getComponentJavaNMS(nmsItem, ...)
     * ComponentSetter().getComponentJavaNMS(nmsItem, ...) ...
     * ```
     *
     * @param item Bukkit物品
     * @param component 组件id，形如 `minecraft:custom_data`
     * */
    fun <T> getComponentJava(item: ItemStack, component: String): T? {
        return asNMSCopyMethod(item).getComponentJavaNMS<T>(component)
    }


    /**
     * 获取物品的指定的数据组件。(以 NMS 物品获取)
     *
     * 使用 Java 组件解析器。
     *
     * @param item NMS物品
     * @param component 组件id，形如 `minecraft:custom_data`
     * */
    fun <T> getComponentJavaNMS(item: Any, component: String): T? {
        return item.getComponentJavaNMS<T>(component)
    }


    /**
     * 修改给定物品的指定的数据组件并作为返回值返回，不修改原物品。
     *
     * 如果多次使用，请直接使用 `setComponentNMS` 以避免额外转换的性能损耗。示例:
     * ```
     * var nmsItem = asNMSCopy(yourBukkitItem)
     * nmsItem = ComponentSetter().setComponentNMS(nmsItem, ...)
     * nmsItem = ComponentSetter().setComponentNMS(nmsItem, ...) ...
     * val finalItem = asBukkitCopy(nmsItem)
     * ```
     *
     * @param item Bukkit物品
     * @param component 组件id，形如 `minecraft:custom_data`
     * @param value 组件值，直接传入对应类型即可，一般是传入一个Map<String, Any>
     * @return 修改后的 Bukkit 物品。
     * */
    fun setComponent(item: ItemStack, component: String, value: Any): ItemStack? {
        return asBukkitCopyMethod(asNMSCopyMethod(item).setComponentNMS(component, value)) as ItemStack?
    }


    /**
     * 修改给定物品的指定的数据组件并作为返回值返回，不修改原物品。(以 NMS 物品获取)
     *
     * @param item NMS物品
     * @param component 组件id，形如 `minecraft:custom_data`
     * @param value 组件值，直接传入对应类型即可，一般是传入一个Map<String, Any>
     * @return 修改后的 NMS 物品。
     * */
    fun setComponentNMS(item: Any, component: String, value: Any): Any? {
        return item.setComponentNMS(component, value)
    }


    /**
     * 移除给定物品的指定的数据组件并作为返回值返回，不修改原物品。
     *
     * 如果多次使用，请直接使用 `setComponentNMS` 以避免额外转换的性能损耗。示例:
     * ```
     * var nmsItem = asNMSCopy(yourBukkitItem)
     * nmsItem = ComponentSetter().setComponentNMS(nmsItem, ...)
     * nmsItem = ComponentSetter().setComponentNMS(nmsItem, ...) ...
     * val finalItem = asBukkitCopy(nmsItem)
     * ```
     * @param item Bukkit物品
     * @param component 组件id，形如 `minecraft:custom_data`
     * @return 修改后的 NMS 物品。
     * */
    fun removeComponent(item: ItemStack, component: String): ItemStack? {
        return asBukkitCopyMethod(asNMSCopyMethod(item).removeComponentNMS(component)) as ItemStack?
    }

    /**
     * 移除给定物品的指定的数据组件并作为返回值返回，不修改原物品。(以 NMS 物品获取)
     *
     * @param item NMS物品
     * @param component 组件id，形如 `minecraft:custom_data`
     * @return 修改后的 NMS 物品。
     * */
    fun removeComponentNMS(item: Any, component: String): Any? {
        return item.removeComponentNMS(component)
    }

}