# EmbianComponent

```
repositories {
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation("io.github.zzzyyylllty:EmbianComponent:VERSION")
    // If this not working use taboo("com.github.zzzyyylllty:EmbianComponent:VERSION")
}
```

## Taboo Packing


```
repositories {
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    taboo("io.github.zzzyyylllty:EmbianComponent:VERSION")
    // If this not working use taboo("com.github.zzzyyylllty:EmbianComponent:VERSION")
}
```

## Usage

We only support **Paper**.

### Java
```java
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import com.google.gson.JsonElement; // Assuming getComponent returns a JsonElement
import io.github.zzzyyylllty.embiancomponent.EmbianComponent;
import io.github.zzzyyylllty.embiancomponent.tools.ComponentSetter;

import java.util.Collections;
import java.util.Map;

public class ComponentExample {

    public void demonstrateComponentAPI() {
        // First, get an instance of your ComponentSetter.
        // The EmbianComponent object handles the version check.
        ComponentSetter safetyComponentSetter = EmbianComponent.INSTANCE.getSafetyComponentSetter();

        // 1. Create a new ItemStack.
        ItemStack item = new ItemStack(Material.DIAMOND_CHESTPLATE);
        System.out.println("Original item: " + item);

        // 2. Add a custom data component to the item.
        // We create a map for the component's value. In Java, this is a bit more verbose.
        Map<String, Object> customData = Collections.singletonMap("test", "abc");
        item = safetyComponentSetter.setComponent(item, "minecraft:custom_data", customData);
        System.out.println("Item after adding custom_data: " + item);

        // 3. Remove a default component from the item.
        // For instance, removing attribute modifiers to make it a plain armor piece.
        item = safetyComponentSetter.removeComponent(item, "minecraft:attribute_modifiers");
        System.out.println("Item after removing attribute_modifiers: " + item);

        // 4. Get all components that have been modified or added (filtered).
        // This will only show components that differ from the item's default state.
        // The result is a Map<String, Object>.
        Map<String, Object> filteredComponents = safetyComponentSetter.getAllComponentsFiltered(item);
        // Expected output might be something like: {minecraft:custom_data={test=abc}}
        System.out.println("Filtered Components: " + filteredComponents);

        // 5. Get all components present on the item, including default ones.
        // The result is a Map<String, JsonElement>.
        Map<String, JsonElement> allComponents = safetyComponentSetter.getAllComponents(item);
        // Expected output might be something like: {minecraft:custom_data={"test":"abc"}, minecraft:max_damage=528, ...}
        System.out.println("All Components: " + allComponents);

        // 6. Get a specific component's value as a Java object (e.g., a Map).
        // The type must be specified in a generic way.
        Map<?, ?> customDataJava = safetyComponentSetter.getComponentJava(item, "minecraft:custom_data");
        // Expected output: {test=abc}
        System.out.println("Get Java Component (custom_data): " + customDataJava);

        // 7. Get a specific component's value as a JSON representation (JsonElement).
        JsonElement customDataJson = safetyComponentSetter.getComponent(item, "minecraft:custom_data");
        // Expected output: {"test":"abc"}
        System.out.println("Get JSON Component (custom_data): " + customDataJson);
    }
}

```
### Kotlin

```kotlin
//import org.bukkit.Material
//import org.bukkit.inventory.ItemStack
//import io.github.zzzyyylllty.embiancomponent.EmbianComponent

class ComponentExample {

    fun demonstrateComponentAPI() {
        // First, get an instance of your ComponentSetter.
        // The EmbianComponent object handles the version check and provides the singleton.
        val safetyComponentSetter = EmbianComponent.SafetyComponentSetter

        // 1. Create a new ItemStack.
        var item = ItemStack(Material.DIAMOND_CHESTPLATE)
        println("Original item: $item")

        // 2. Add a custom data component to the item.
        // Kotlin's `mapOf` provides a clean way to create the component's value.
        // The `!!` operator asserts the result is not null, similar to the original snippet.
        item = safetyComponentSetter.setComponent(
            item,
            "minecraft:custom_data",
            mapOf("test" to "abc")
        )!!
        println("Item after adding custom_data: $item")

        // 3. Remove a default component from the item.
        // For instance, removing attribute modifiers to make it a plain armor piece.
        item = safetyComponentSetter.removeComponent(item, "minecraft:attribute_modifiers")!!
        println("Item after removing attribute_modifiers: $item")

        // 4. Get all components that have been modified or added (filtered).
        // This will only show components that differ from the item's default state.
        val filteredComponents = safetyComponentSetter.getAllComponentsFiltered(item)
        // Expected output might be something like: {minecraft:custom_data={test=abc}}x
        // output Map.value is JsonObject(Map)/JsonPrimitive(Number/String)/JsonArray(List)
        println("Filtered Components: $filteredComponents")

        // 5. Get all components present on the item, including default ones.
        val allComponents = safetyComponentSetter.getAllComponents(item)
        // Expected output might be something like: {minecraft:custom_data={"test":"abc"}, minecraft:max_damage=528, ...}
        // output Map.value is JsonObject(Map)/JsonPrimitive(Number/String)/JsonArray(List)
        println("All Components: $allComponents")

        // 6. Get a specific component's value as a Java object (e.g., a Map).
        // Kotlin's type inference and generics syntax are very clean.
        val customDataJava = safetyComponentSetter.getComponentJava<Map<*, *>>(item, "minecraft:custom_data")
        // Expected output: {test=abc}
        println("Get Java Component (custom_data): $customDataJava")

        // 7. Get a specific component's value as a JSON representation (JsonElement).
        val customDataJson = safetyComponentSetter.getComponent(item, "minecraft:custom_data")
        // Expected output: {"test":"abc"}
        println("Get JSON Component (custom_data): $customDataJson")
    }
}

```
