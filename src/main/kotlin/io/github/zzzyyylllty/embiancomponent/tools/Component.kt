package io.github.zzzyyylllty.embiancomponent.tools

import com.google.gson.JsonElement
import org.bukkit.inventory.ItemStack

class ComponentSetter {
    /**
     * Gets **all** data components of an item.
     * Includes unfiltered components, e.g., a sword **will** return the default component with damage=0.
     * @param item The Bukkit ItemStack.
     */
    fun getAllComponents(item: ItemStack): Map<String, Any?> {
        return asNMSCopyMethod(item).getComponentsNMS()
    }

    /**
     * Gets **all** data components of an item **after filtering out default ones**.
     * Excludes unfiltered components, e.g., a sword **will not** return the default component with damage=0.
     * @param item The Bukkit ItemStack.
     */
    fun getAllComponentsFiltered(item: ItemStack): Map<String, Any?> {
        return asNMSCopyMethod(item).getComponentsNMSFiltered()
    }

    /**
     * Gets **all** data components of an item (from an NMS item).
     * Includes unfiltered components, e.g., a sword **will** return the default component with damage=0.
     * @param item The NMS ItemStack.
     */
    fun getAllComponentsNMS(item: Any): Map<String, Any?> {
        return item.getComponentsNMS()
    }

    /**
     * Gets **all** data components of an item **after filtering out default ones** (from an NMS item).
     * Excludes unfiltered components, e.g., a sword **will not** return the default component with damage=0.
     * @param item The NMS ItemStack.
     */
    fun getAllComponentsFilteredNMS(item: Any): Map<String, Any?> {
        return item.getComponentsNMSFiltered()
    }

    /**
     * Gets a specific data component of an item.
     *
     * If used multiple times, consider the following code to avoid frequent ItemStack conversions:
     * ```
     * val nmsItem = asNMSCopy(yourBukkitItem)
     * ComponentSetter().getComponentNMS(nmsItem, ...)
     * ComponentSetter().getComponentNMS(nmsItem, ...) ...
     * ```
     *
     * @param item The Bukkit ItemStack.
     * @param component The component ID, e.g., `minecraft:custom_data`.
     */
    fun getComponent(item: ItemStack, component: String): JsonElement? {
        return asNMSCopyMethod(item).getComponentNMS(component)
    }


    /**
     * Gets a specific data component of an item.
     *
     * @param item The NMS ItemStack.
     * @param component The component ID, e.g., `minecraft:custom_data`.
     */
    fun getComponentNMS(item: Any, component: String): JsonElement? {
        return item.getComponentNMS(component)
    }

    /**
     * Gets a specific data component of an item.
     *
     * Uses the Java component parser.
     *
     * If used multiple times, consider the following code to avoid frequent ItemStack conversions:
     * ```
     * val nmsItem = asNMSCopy(yourBukkitItem)
     * ComponentSetter().getComponentJavaNMS(nmsItem, ...)
     * ComponentSetter().getComponentJavaNMS(nmsItem, ...) ...
     * ```
     *
     * @param item The Bukkit ItemStack.
     * @param component The component ID, e.g., `minecraft:custom_data`.
     */
    fun <T> getComponentJava(item: ItemStack, component: String): T? {
        return asNMSCopyMethod(item).getComponentJavaNMS<T>(component)
    }


    /**
     * Gets a specific data component of an item (from an NMS item).
     *
     * Uses the Java component parser.
     *
     * @param item The NMS ItemStack.
     * @param component The component ID, e.g., `minecraft:custom_data`.
     */
    fun <T> getComponentJavaNMS(item: Any, component: String): T? {
        return item.getComponentJavaNMS<T>(component)
    }


    /**
     * Modifies a specific data component of the given item and returns it as a new item, without modifying the original.
     *
     * If used multiple times, use `setComponentNMS` directly to avoid performance loss from extra conversions. Example:
     * ```
     * var nmsItem = asNMSCopy(yourBukkitItem)
     * nmsItem = ComponentSetter().setComponentNMS(nmsItem, ...)
     * nmsItem = ComponentSetter().setComponentNMS(nmsItem, ...) ...
     * val finalItem = asBukkitCopy(nmsItem)
     * ```
     *
     * @param item The Bukkit ItemStack.
     * @param component The component ID, e.g., `minecraft:custom_data`.
     * @param value The component value, usually a Map<String, Any>.
     * @return The modified Bukkit ItemStack.
     */
    fun setComponent(item: ItemStack, component: String, value: Any): ItemStack? {
        return asBukkitCopyMethod(asNMSCopyMethod(item).setComponentNMS(component, value)) as ItemStack?
    }


    /**
     * Modifies a specific data component of the given item and returns it as a new item, without modifying the original (from an NMS item).
     *
     * @param item The NMS ItemStack.
     * @param component The component ID, e.g., `minecraft:custom_data`.
     * @param value The component value, usually a Map<String, Any>.
     * @return The modified NMS ItemStack.
     */
    fun setComponentNMS(item: Any, component: String, value: Any): Any? {
        return item.setComponentNMS(component, value)
    }


    /**
     * Removes a specific data component from the given item and returns it as a new item, without modifying the original.
     *
     * If used multiple times, use `removeComponentNMS` directly to avoid performance loss from extra conversions. Example:
     * ```
     * var nmsItem = asNMSCopy(yourBukkitItem)
     * nmsItem = ComponentSetter().removeComponentNMS(nmsItem, ...)
     * nmsItem = ComponentSetter().removeComponentNMS(nmsItem, ...) ...
     * val finalItem = asBukkitCopy(nmsItem)
     * ```
     * @param item The Bukkit ItemStack.
     * @param component The component ID, e.g., `minecraft:custom_data`.
     * @return The modified Bukkit ItemStack.
     */
    fun removeComponent(item: ItemStack, component: String): ItemStack? {
        return asBukkitCopyMethod(asNMSCopyMethod(item).removeComponentNMS(component)) as ItemStack?
    }

    /**
     * Removes a specific data component from the given item and returns it as a new item, without modifying the original (from an NMS item).
     *
     * @param item The NMS ItemStack.
     * @param component The component ID, e.g., `minecraft:custom_data`.
     * @return The modified NMS ItemStack.
     */
    fun removeComponentNMS(item: Any, component: String): Any? {
        return item.removeComponentNMS(component)
    }


}