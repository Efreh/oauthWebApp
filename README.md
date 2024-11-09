
# Проект: Интеграция Spring Security и OAuth 2.0

## Описание
Данный проект представляет собой реализацию аутентификации с использованием OAuth 2.0 и интеграции с Spring Security. Приложение позволяет регистрировать клиентов в популярных провайдерах OAuth 2.0 (например, Google или GitHub), разделять доступ по ролям, получать данные профиля пользователя, а также поддерживать отзыв токенов и выход из системы. Дополнительно реализовано логирование действий пользователя и защита чувствительных эндпоинтов.

## Основные функции
1. **Регистрация клиента в провайдере OAuth 2.0**:
    - Регистрация приложения как клиента в провайдере OAuth 2.0 (Google, GitHub и т.д.).
    - Получение `client_id` и `client_secret` и настройка этих параметров в проекте.

2. **Интеграция Spring Security и OAuth 2.0**:
   Добавление зависимости:
   ```xml
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-oauth2-client</artifactId>
   </dependency>
   ```

   Настройка `application.yml`:
   ```yaml
   spring:
     security:
       oauth2:
         client:
           registration:
             google:
               client-id: your-client-id
               client-secret: your-client-secret
               scope: profile, email
               redirect-uri: "{baseUrl}/login/oauth2/code/{registrationId}"
   ```

   Использование Authorization Code потока для аутентификации.

3. **Разделение доступа на роли**:
   Реализация назначения ролей на основе данных, возвращаемых провайдером:
   ```java
   @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
       http
           .authorizeHttpRequests(authorizeRequests -> authorizeRequests
               .requestMatchers("/", "/login", "/error", "/webjars/**").permitAll()
               .requestMatchers("/h2-console/*").permitAll()
               .requestMatchers("/admin/**").hasRole("ADMIN")
               .anyRequest().authenticated()
           )
           .exceptionHandling(exceptionHandling -> exceptionHandling
               .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
           )
           .oauth2Login(oauth2Login -> oauth2Login
               .loginPage("/")
               .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                   .userService(socialAppService))
               .defaultSuccessUrl("/user")
           )
           .logout(logout -> logout
               .logoutUrl("/logout")
               .logoutSuccessHandler(oidcLogoutSuccessHandler())
               .invalidateHttpSession(true)
               .deleteCookies("JSESSIONID")
           );
       return http.build();
   }
   ```

4. **Получение и отображение профиля пользователя**:
   Контроллер для отображения информации о пользователе:
   ```java
   @GetMapping("/user")
   public String user(@AuthenticationPrincipal OAuth2User principal, Model model) {
       model.addAttribute("name", principal.getAttribute("name"));
       model.addAttribute("login", principal.getAttribute("login"));
       model.addAttribute("id", principal.getAttribute("id"));
       model.addAttribute("email", principal.getAttribute("email"));
       return "user";
   }
   ```

5. **Отзыв доступа и выход из системы**:
   Реализация механизма отзыва токена и выхода из системы с использованием URL logout провайдера.

6. **Логирование действий пользователя**:
   Логирование успешной аутентификации, выхода из системы и отзыва доступа с использованием SLF4J:
   ```java
   @Service
   @AllArgsConstructor
   public class SocialAppService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
   
       private final UserRepository userRepository;
       private static final Logger logger = LoggerFactory.getLogger(SocialAppService.class);
   
       @Override
       @Transactional
       public OAuth2User loadUser(OAuth2UserRequest userRequest) {
           OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
           OAuth2User oAuth2User = delegate.loadUser(userRequest);
           // Логика сохранения юзера и назначения роли
           return oAuth2User;
       }
   }
   ```

7. **Защита эндпоинтов**:
   Защита админ-панели и других чувствительных ресурсов:
   ```java
   http
       .authorizeHttpRequests()
       .requestMatchers("/admin/**").hasRole("ADMIN")
       .anyRequest().authenticated();
   ```

8. **Обработка ошибок**:
   Реализация обработчиков для различных ошибок:
    - Ошибки аутентификации.
    - Недостаток прав доступа (403 Forbidden).
    - Истечение токена и другие сценарии.

## Лицензия
Данный проект распространяется под лицензией MIT.