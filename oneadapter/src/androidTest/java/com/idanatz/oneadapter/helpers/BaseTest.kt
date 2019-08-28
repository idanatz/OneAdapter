package com.idanatz.oneadapter.helpers

import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.idanatz.oneadapter.models.ModelGenerator
import org.junit.runner.RunWith
import org.junit.Before
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.idanatz.oneadapter.OneAdapter
import com.idanatz.oneadapter.models.ModulesGenerator
import com.idanatz.oneadapter.test.R
import org.junit.After
import org.junit.Rule
import java.util.concurrent.CountDownLatch

@RunWith(AndroidJUnit4::class)
open class BaseTest {

    protected lateinit var recyclerView: RecyclerView
    protected lateinit var oneAdapter: OneAdapter
    protected lateinit var modelGenerator: ModelGenerator
    protected lateinit var modulesGenerator: ModulesGenerator

    private var screenHeight: Int = 0
    private var smallHolderHeight: Int = 0
    private var largeHolderHeight: Int = 0

    @get:Rule var activityScenarioRule = activityScenarioRule<TestActivity>()

    companion object {
        const val INVALID_RESOURCE = -1
    }

    @Before
    open fun setup() {
        modelGenerator = ModelGenerator()
        modulesGenerator = ModulesGenerator()

        activityScenarioRule.scenario.moveToState(Lifecycle.State.RESUMED)
        runOnActivity {
            screenHeight = it.resources.displayMetrics.heightPixels
            smallHolderHeight = it.resources.getDimension(R.dimen.small_model_height).toInt()
            largeHolderHeight = it.resources.getDimension(R.dimen.large_model_height).toInt()

            recyclerView = it.findViewById<RecyclerView>(R.id.recycler).apply {
                layoutManager = LinearLayoutManager(it)
                oneAdapter = OneAdapter(this)
            }
        }
    }

    @After
    open fun cleanup() {
    }

    protected fun runOnActivity(block: (activity: TestActivity) -> Unit) {
        activityScenarioRule.scenario.onActivity(block)
    }

    protected fun catchException(block: () -> Unit): Throwable {
        val countDownLatch = CountDownLatch(1)
        var thrownException: Throwable? = null

        Thread.setDefaultUncaughtExceptionHandler { _, e ->
            thrownException = e
            countDownLatch.countDown()
        }

        block()
        countDownLatch.await()
        return thrownException!!
    }

    protected fun getNumberOfHoldersCanBeOnScreen(layoutId: Int): Int {
        return when (layoutId) {
            R.layout.test_model_small -> screenHeight / smallHolderHeight
            R.layout.test_model_large -> screenHeight / largeHolderHeight
            else -> 0
        }
    }
}