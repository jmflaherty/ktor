package com.jmflaherty

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.SpecExecutionOrder
import io.kotest.extensions.junitxml.JunitXmlReporter
import io.kotest.extensions.allure.AllureTestReporter


class ProjectConfig : AbstractProjectConfig() {
    override val specExecutionOrder = SpecExecutionOrder.Annotated
    override fun extensions(): List<Extension> {
        return listOf(
            JunitXmlReporter(
                includeContainers = true,
                useTestPathAsName = true
            )
        )
    }
}