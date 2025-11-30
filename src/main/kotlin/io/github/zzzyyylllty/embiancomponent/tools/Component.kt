package io.github.zzzyyylllty.embiancomponent.tools

import com.google.gson.JsonElement
import org.bukkit.inventory.ItemStack

class ComponentSetter {
    /**
     * Gets **all** data components of an item.
     * Includes unfiltered components, e.g., a sword **will** return the default component with damage=0.
     * @param item The Bukkit ItemStack.
     * @return Map.value is JsonObject/JsonPrimitive/JsonArray
     */
    fun getAllComponents(item: ItemStack): Map<String, Any?> {
        return asNMSCopy(item).getComponentsNMS()
    }

    /**
     * Gets **all** data components of an item **after filtering out default ones**.
     * Excludes unfiltered components, e.g., a sword **will not** return the default component with damage=0.
     * @param item The Bukkit ItemStack.
     * @return Map.value is JsonObject/JsonPrimitive/JsonArray
     */
    fun getAllComponentsFiltered(item: ItemStack): Map<String, Any?> {
        return asNMSCopy(item).getComponentsNMSFiltered()
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
        return asNMSCopy(item).getComponentNMS(component)
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
        return asNMSCopy(item).getComponentJavaNMS<T>(component)
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
        return asBukkitCopy(asNMSCopy(item).setComponentNMS(component, value)) as ItemStack?
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
     * Removes a specific data component from this NMS ItemStack instance via reflection.
     * This method modifies the item stack in-place.
     *
     * @param componentId The resource location string of the component to remove, e.g., "minecraft:custom_name".
     */
    fun removeComponentNMSDirectly(item: Any, componentId: String) { // Return type changed to Unit for clarity
        val componentType = ensureDataComponentType(componentId) ?: return

        try {
            // Invoke the NMS ItemStack.removeComponent(DataComponentType) method.
            // This method modifies 'this' (the NMS ItemStack) and returns the old value, which we discard.
            `method$ItemStack$removeComponent`.invoke(item, componentType)
        } catch (e: Exception) {
            // Log the error if the reflection call fails.
            e.printStackTrace()
        }
    }


    /**
     * Removes a specific data component from a Bukkit ItemStack and returns a new, modified ItemStack.
     * This function is safe to use as it creates a copy and does not modify the original item.
     *
     * For performance-critical scenarios involving multiple modifications, consider converting
     * the ItemStack to its NMS representation once, performing all operations, and then converting it back.
     * Example:
     * ```
     * val nmsItem = asNMSCopyMethod.invoke(null, yourBukkitItem)
     * nmsItem.removeComponentNMS("minecraft:custom_data")
     * nmsItem.removeComponentNMS("minecraft:map_color")
     * val finalItem = asBukkitCopyMethod.invoke(null, nmsItem) as ItemStack
     * ```
     *
     * @param item The Bukkit ItemStack to modify.
     * @param componentId The component ID to remove, e.g., "minecraft:custom_name".
     * @return A new, modified Bukkit ItemStack, or null if an error occurs.
     */
    fun removeComponent(item: ItemStack, componentId: String): ItemStack? {
        // 1. Create a modifiable NMS copy of the Bukkit ItemStack.
        // This prevents modification of the original item stack.
        val nmsItem = asNMSCopy(item)

        // 2. Call the extension function to remove the component.
        // This modifies the 'nmsItem' instance in-place.
        nmsItem.removeComponentNMS(componentId)

        // 3. Convert the modified NMS item back to a Bukkit ItemStack and return it.
        return asBukkitCopy(nmsItem)
    }


    /**
     * Applies the component removal to an NMS ItemStack and returns the modified item.
     *
     * Note: This function is redundant. The `removeComponentNMSDirectly` function
     * should be used directly on the NMS item object.
     *
     * @param item The NMS ItemStack object.
     * @param componentId The component ID to remove.
     * @return The same NMS ItemStack object that was passed in, now modified.
     */
    fun removeComponentNMS(item: Any, componentId: String): Any {
        // The extension function modifies the item in-place.
        item.removeComponentNMS(componentId)
        // Return the same item that was modified.
        return item
    }



}