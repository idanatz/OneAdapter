package com.idanatz.oneadapter.external.interfaces

import android.animation.Animator

interface Config

interface LayoutModuleConfig : Config {
	var layoutResource: Int?
	var firstBindAnimation: Animator?
}