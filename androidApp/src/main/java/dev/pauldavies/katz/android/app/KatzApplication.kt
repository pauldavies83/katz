package dev.pauldavies.katz.android.app

import android.app.Application
import dev.pauldavies.katz.android.app.di.androidAppModule
import dev.pauldavies.katz.di.initKoin
import org.koin.android.ext.koin.androidContext

class KatzApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin(
            appDeclaration = {
                androidContext(this@KatzApplication)
            },
            appModule = androidAppModule()
        )
    }
}
