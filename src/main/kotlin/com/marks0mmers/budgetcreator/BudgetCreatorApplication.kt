package com.marks0mmers.budgetcreator

import com.marks0mmers.budgetcreator.config.GlobalErrorHandler
import com.marks0mmers.budgetcreator.config.RequestLoggingFilter
import com.marks0mmers.budgetcreator.config.security.AuthenticationManager
import com.marks0mmers.budgetcreator.config.security.JWTUtil
import com.marks0mmers.budgetcreator.config.security.WebSecurityConfig
import com.marks0mmers.budgetcreator.controllers.*
import com.marks0mmers.budgetcreator.services.*
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

/**
 * The main class that spring attaches itself to
 *
 * @author Mark Sommers
 */
@SpringBootApplication
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class BudgetCreatorApplication {
	companion object {
		/**
		 * The main function for the program. It configures all Spring beans as well
		 *
		 * @param args The command line args
		 * @see runApplication
		 * @see beans
		 * @author Mark Sommers
		 */
		@JvmStatic
		fun main(args: Array<String>) {
			runApplication<BudgetCreatorApplication>(*args) {
				addInitializers(ApplicationContextInitializer<GenericApplicationContext> { context ->
					beans {
						bean { JWTUtil() }
						bean { BCryptPasswordEncoder() }
						bean { AuthenticationManager(ref()) }
						bean { WebSecurityConfig(ref(), ref()) }

						profile("return-errors") {
							bean { GlobalErrorHandler() }
						}

						profile("logger") {
							bean { RequestLoggingFilter() }
						}

						bean { UserController(ref(), ref()) }
						bean { BudgetController(ref()) }
						bean { IncomeSourceController(ref()) }
						bean { ExpenseSourceController(ref()) }
						bean { ExpenseCategoryController(ref()) }
						bean { ExpenseSubCategoryController(ref()) }

						bean { UserService(ref()) }
						bean { BudgetService() }
						bean { IncomeSourceService() }
						bean { ExpenseSourceService() }
						bean { ExpenseCategoryService() }
						bean { ExpenseSubCategoryService() }
					}.initialize(context)
				})
			}
		}
	}
}

