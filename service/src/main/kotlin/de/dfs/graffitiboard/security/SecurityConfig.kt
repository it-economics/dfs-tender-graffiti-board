package de.dfs.graffitiboard.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
class SecurityConfig(
    private val userConfiguration: UserConfiguration
) : WebSecurityConfigurerAdapter() {

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
            .antMatcher("/**").authorizeRequests().anyRequest()
            .hasAnyRole(Roles.USER)
            .and()
            .httpBasic()
    }

    override fun configure(web: WebSecurity?) {
        web!!.ignoring().antMatchers("/message-socket")
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        val passwordEncoder = passwordEncoder()
        val inMemoryUserDetailsManagerConfigurer = auth.inMemoryAuthentication().passwordEncoder(passwordEncoder)

        userConfiguration.users.map {
            inMemoryUserDetailsManagerConfigurer
                .withUser(it.name)
                .password(passwordEncoder.encode(it.password))
                .roles(Roles.USER)
        }
    }
}
