package com.example.marvelapp

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

// Um executor customizado para configurar a classe de aplicativo instrumentado para testes.
@Suppress("unused")
class CustomTestRunner : AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader?,
        name: String?,
        context: Context?,
    ): Application =
        super.newApplication(cl, HiltTestApplication::class.java.name, context)
}
