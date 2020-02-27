package com.idanatz.oneadapter.internal.threading

import com.idanatz.oneadapter.external.MissingModuleDefinitionException
import com.idanatz.oneadapter.external.MultipleModuleConflictException
import java.util.concurrent.*

internal class OneSingleThreadPoolExecutor : ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, LinkedBlockingQueue()) {

    fun submit(taskId: Int, task: Runnable): IdentifiableFuture<*> {
        val future = super.submit(task)
        return object : IdentifiableFuture<Any> {
            override val id: Int = taskId
            override fun isDone(): Boolean = future.isDone
            override fun get() = future.get()
            override fun get(timeout: Long, unit: TimeUnit?) = future.get(timeout, unit)
            override fun cancel(mayInterruptIfRunning: Boolean) = future.cancel(mayInterruptIfRunning)
            override fun isCancelled() = future.isCancelled
        }
    }

    override fun afterExecute(r: Runnable?, t: Throwable?) {
        super.afterExecute(r, t)

        // If submit() method is called instead of execute() then any exception thrown
        // will be handled by the Future and suppressed.
        // That's good but when the exceptions are OneAdapter related we want them to
        // reach the user in order to let him to know he did something wrong.
        if (t == null && r is Future<*>) {
            try {
                // Retrieve the Future computation result
                (r as Future<*>).get()
            } catch (throwable: Throwable) {
                when(throwable.cause) {
                    is MissingModuleDefinitionException, is MultipleModuleConflictException -> throw throwable.cause!!
                }
            }
        }
    }
}

internal interface IdentifiableFuture<V : Any> : Future<V> {
    val id: Int
}