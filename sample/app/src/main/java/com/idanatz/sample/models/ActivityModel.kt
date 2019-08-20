package com.idanatz.sample.models

import android.content.Intent
import com.idanatz.oneadapter.external.interfaces.Diffable

data class ActivityModel(val text: String, val intent: Intent) : Diffable {

    override fun getUniqueIdentifier() = intent.hashCode().toLong()

    override fun areContentTheSame(other: Any) = other is ActivityModel &&
            text == other.text &&
            intent == other.intent

}