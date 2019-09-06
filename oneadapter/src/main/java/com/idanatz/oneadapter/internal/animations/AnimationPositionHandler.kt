package com.idanatz.oneadapter.internal.animations

import android.util.SparseIntArray

internal class AnimationPositionHandler(private var highestPositionMap: SparseIntArray = SparseIntArray()) {

    fun shouldAnimateBind(viewType: Int, position: Int): Boolean {
        val highestPositionForType = highestPositionMap.get(viewType, -1)
        val shouldAnimateBind = position > highestPositionForType

        if (shouldAnimateBind) {
            highestPositionMap.put(viewType, position)
        }

        return shouldAnimateBind
    }

    fun resetState() {
        highestPositionMap.clear()
    }
}