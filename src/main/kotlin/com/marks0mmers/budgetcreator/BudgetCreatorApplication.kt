package com.marks0mmers.budgetcreator

import com.marks0mmers.budgetcreator.config.GlobalErrorHandler
import com.marks0mmers.budgetcreator.config.RequestLoggingFilter
import com.marks0mmers.budgetcreator.config.security.AuthenticationManager
import com.marks0mmers.budgetcreator.config.security.JWTUtil
import com.marks0mmers.budgetcreator.config.security.securityWebFilterChain
import com.marks0mmers.budgetcreator.controllers.*
import com.marks0mmers.budgetcreator.services.*
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@SpringBootApplication
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class BudgetCreatorApplication

fun main(args: Array<String>) {
	runApplication<BudgetCreatorApplication>(*args) {
		addInitializers(AppBeansInitializer())
	}
}

class AppBeansInitializer: ApplicationContextInitializer<GenericApplicationContext> {
	override fun initialize(context: GenericApplicationContext) = beans {
		bean { JWTUtil() }
		bean<PasswordEncoder> { BCryptPasswordEncoder() }
		bean { AuthenticationManager(ref()) }
		bean { securityWebFilterChain(ref(), ref()) }

		bean<ErrorWebExceptionHandler> { GlobalErrorHandler() }
		profile("logger") {
			bean<RequestLoggingFilter>()
		}

		bean { userRouter(ref(), ref()) }
		bean { budgetRouter(ref()) }
		bean { incomeSourceRouter(ref()) }
		bean { expenseCategoryRouter(ref()) }
		bean { expenseSubCategoryRouter(ref()) }

		bean { UserService(ref(), ref()) }
		bean { BudgetService(ref(), ref()) }
		bean { IncomeSourceService(ref()) }
		bean { ExpenseCategoryService(ref()) }
		bean { ExpenseSubCategoryService(ref()) }
	}.initialize(context)

}
