package io.github.zzzyyylllty.embiancomponent

import io.github.zzzyyylllty.embiancomponent.tools.ComponentSetter
import org.bukkit.Bukkit
import kotlin.getValue

//import taboolib.module.nms.MinecraftVersion.versionId

object EmbianComponent {


    /**
     * 当前运行的版本（数字版本），例如：1.8.8
     */
    val runningVersion by lazy {
        val version = Bukkit.getServer().version.split("MC:")[1]
        version.substring(0, version.length - 1).trim()
    }

    val versionId: Int by lazy {
        // 无补丁版本号（如 "1.21"）只有两段，缺省补丁按 0 处理
        val split = runningVersion.split(".")
        split[0].toInt() * 10000 + (split.getOrNull(1)?.toInt() ?: 0) * 100 + (split.getOrNull(2)?.toInt() ?: 0)
    }

    val SafetyComponentSetter by lazy {
        if (versionId >= 12005) ComponentSetter() else throw IllegalStateException("Version must be 1.20.5+. Current version is $versionId")
    }


}