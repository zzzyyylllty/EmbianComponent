package io.github.zzzyyylllty.embiancomponent.utils

import com.mojang.serialization.DataResult


val <R> DataResult<R>.isSuccess: Boolean
    get() = this.result().isPresent

val <R> DataResult<R>.isError: Boolean
    get() = this.result().isPresent