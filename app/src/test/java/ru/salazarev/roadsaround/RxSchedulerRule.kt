package ru.salazarev.roadsaround

import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.internal.schedulers.TrampolineScheduler
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.util.concurrent.Callable

/**
 * https://stackoverflow.com/questions/46549405/testing-asynchronous-rxjava-code-android
 */
class RxSchedulerRule : TestRule {
    override fun apply(base: Statement, description: Description?): Statement? {
        return object : Statement() {
            override fun evaluate() {
                RxAndroidPlugins.setInitMainThreadSchedulerHandler { schedulerCallable: Callable<Scheduler?>? -> TrampolineScheduler.instance() }
                RxJavaPlugins.setIoSchedulerHandler { scheduler: Scheduler? -> TrampolineScheduler.instance() }
                RxJavaPlugins.setComputationSchedulerHandler { scheduler: Scheduler? -> TrampolineScheduler.instance() }
                try {
                    base.evaluate()
                } finally {
                    RxAndroidPlugins.reset()
                    RxJavaPlugins.reset()
                }
            }
        }
    }
}