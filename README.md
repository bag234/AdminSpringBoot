# AdminSpringBoot

## Описание
AdminSpringBoot — это Spring Boot приложение для управления пользователями, их авторизацией и предоставления защищённых API эндпоинтов. Проект реализует базовую безопасность, хранение и обновление информации о пользователях, а также предоставляет REST API для получения данных о пользователях.

## Структура проекта
- `src/main/java/org/mrbag/AdminSpringBoot/` — основной код приложения
  - `AdminSpringBootApplication.java` — точка входа
  - `SecurityConfig.java` — конфигурация безопасности (Spring Security)
  - `UsersUpdateList.java` — логика обновления пользователей
  - `User/` — пакет с моделями и сервисами пользователя
    - `UserApp.java` — модель пользователя
    - `UserDetalisWithConfig.java` — детали пользователя и интеграция с конфигом
- `src/main/resources/application.properties` — настройки приложения
- `src/test/java/org/mrbag/AdminSpringBoot/` — тесты

## Сборка и запуск
1. Сборка jar:
   ```shell
   ./mvnw clean package
   ```
   Итоговый файл: `target/AdminSpringBoot.jar`
2. Запуск:
   ```shell
   java -jar target/AdminSpringBoot.jar
   ```

## Конфигурация пользователей
Пользователи и их роли настраиваются через класс `SecurityConfig.java` и/или внешний источник (например, база данных или файл). Пример конфигурации в памяти:
```java
@Override
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication()
        .withUser("admin").password(passwordEncoder().encode("adminpass")).roles("ADMIN")
        .and()
        .withUser("user").password(passwordEncoder().encode("userpass")).roles("USER");
}
```

## API эндпоинты
### Получение информации о пользователях
- **GET** `/api/users` — возвращает список пользователей
  - **Требуемые права:** `ADMIN`

  # AdminSpringBoot

  ## Описание
  AdminSpringBoot — это Spring Boot приложение для управления пользователями и их авторизацией. Пользователи и их роли хранятся во внешнем файле, а доступ к API защищён с помощью Spring Security.

  ## Структура проекта
  - `AdminSpringBootApplication.java` — точка входа
  - `SecurityConfig.java` — настройка безопасности и прав доступа
  - `UsersUpdateList.java` — REST-контроллер для управления пользователями
  - `User/UserApp.java` — модель пользователя, реализующая UserDetails
  - `User/UserDetalisWithConfig.java` — сервис загрузки пользователей из файла
  - `application.properties` — конфигурация приложения

  ## Сборка и запуск
  1. Сборка jar:
     ```shell
     ./mvnw clean package
     ```
     Итоговый файл: `target/AdminSpringBoot.jar`
  2. Запуск:
     ```shell
     java -jar target/AdminSpringBoot.jar
     ```

  ## Конфигурация пользователей
  Пользователи хранятся во внешнем файле, путь к которому задаётся в `application.properties`:
  ```
  app.file.users=./users.txt
  ```
  Формат файла:
  ```
  # - это комментарий
  # Формат: [login]:[{password}]:[Role1,Role2,...]
  admin:{noop}admin:USERS,ADMIN
  user:{noop}user:USERS
  ```
  - Пароль может быть закодирован (например, `{noop}` — без шифрования)
  - Роли перечисляются через запятую

  ## API эндпоинты
  ### Обновление пользователей из файла
  - **POST** `/users/refresh`
    - Перечитывает файл пользователей
    - Требует роль `USERS`

  ### Получение логинов пользователей
  - **GET** `/users/logins`
    - Возвращает список логинов
    - Требует роль `USERS`

  ## Авторизация
  - Форма логина доступна по адресу `/admin/login` (или `/login` в зависимости от конфигурации)
  - После успешной авторизации доступ к защищённым эндпоинтам предоставляется согласно ролям

  ## Права доступа
  - `/users/**` — доступ только для пользователей с ролью `USERS`
  - `/admin/assets/**`, `/admin/login` — доступны всем
  - Все остальные эндпоинты требуют авторизации

  ## Пример настройки SecurityConfig
  ```java
  http
      .authorizeHttpRequests((req) -> req
          .requestMatchers(adminServer.getContextPath() + "/assets/**").permitAll()
          .requestMatchers(adminServer.getContextPath() + "/login").permitAll()
          .requestMatchers("/users/**").hasRole("USERS")
          .anyRequest().authenticated()
      )
      .formLogin((form) -> form
          .loginPage(adminServer.getContextPath() + "/login")
          .successHandler(successHandler)
      )
      .logout((logout) -> logout.logoutUrl(adminServer.getContextPath() + "/logout"))
      .httpBasic(Customizer.withDefaults())
      .csrf((csrf) -> csrf.disable());
  ```

  ## Тестирование
  - Тесты находятся в `src/test/java/org/mrbag/AdminSpringBoot/`
  - Запуск тестов:
    ```shell
    ./mvnw test
    ```

  ## Дополнительно
  - При первом запуске, если файл пользователей отсутствует, он будет создан с инструкцией по формату
  - Для хранения паролей рекомендуется использовать шифрование (например, BCrypt)
  - Для расширения функционала реализуйте дополнительные роли и эндпоинты

  ---

  **Для доступа к защищённым эндпоинтам требуется basic authenticated.**
