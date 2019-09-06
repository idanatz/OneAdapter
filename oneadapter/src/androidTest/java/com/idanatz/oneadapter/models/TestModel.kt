package com.idanatz.oneadapter.models

import com.idanatz.oneadapter.external.interfaces.Diffable

open class TestModel(var id: Int,
                 var content: String,
                 var onBindCalls: Int = 0,
                 var onUnbindCalls: Int = 0) : Diffable {

    override fun getUniqueIdentifier(): Long = id.toLong()

    override fun areContentTheSame(other: Any): Boolean =
            other is TestModel1 && content == other.content
}

class TestModel1(id: Int, content: String) : TestModel(id, content)
class TestModel2(id: Int, content: String) : TestModel(id, content)