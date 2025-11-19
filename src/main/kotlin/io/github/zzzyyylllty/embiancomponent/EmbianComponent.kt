package io.github.zzzyyylllty.embiancomponent

import io.github.zzzyyylllty.embiancomponent.tools.ComponentSetter
import taboolib.module.nms.MinecraftVersion.versionId

object EmbianComponent {

    val SafetyComponentSetter by lazy {
        if (versionId >= 12005) ComponentSetter() else throw IllegalStateException("Version must be 1.20.5+")
    }


}