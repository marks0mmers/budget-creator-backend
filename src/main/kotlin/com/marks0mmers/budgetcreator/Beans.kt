package com.marks0mmers.budgetcreator

import com.marks0mmers.budgetcreator.config.AuthenticationManager
import com.marks0mmers.budgetcreator.config.PBKDF2Encoder
import com.marks0mmers.budgetcreator.handlers.BudgetHandler
import com.marks0mmers.budgetcreator.handlers.IncomeSourceHandler
import com.marks0mmers.budgetcreator.handlers.UserHandler
import com.marks0mmers.budgetcreator.repositories.BudgetRepository
import com.marks0mmers.budgetcreator.repositories.SecurityContextRepository
import com.marks0mmers.budgetcreator.repositories.UserRepository
import com.marks0mmers.budgetcreator.services.BudgetService
import com.marks0mmers.budgetcreator.services.IncomeSourceService
import com.marks0mmers.budgetcreator.services.UserService
import com.marks0mmers.budgetcreator.util.JWTUtil
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans

fun beans() = beans {
    // Web
    bean<Routes>()
    bean {
        ref<Routes>().routes()
    }

    // Handlers
    bean<UserHandler>()
    bean<BudgetHandler>()
    bean<IncomeSourceHandler>()

    // Services
    bean<UserService>()
    bean<BudgetService>()
    bean<IncomeSourceService>()

    // Security
    bean<AuthenticationManager>()
    bean<JWTUtil>()
    bean<PBKDF2Encoder>()
    bean<SecurityContextRepository>()
}

class BeansInitializer : ApplicationContextInitializer<GenericApplicationContext> {
    override fun initialize(context: GenericApplicationContext) = beans().initialize(context)
}