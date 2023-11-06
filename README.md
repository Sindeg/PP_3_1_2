Необходимо перенести ваше CRUD-приложение на Spring Boot

Склонируйте [проект](https://github.com/KataAcademy/PP_3_1_2_Boot_Security) по ссылке и просмотрите его.

Модуль Spring Security позволяет нам внедрять права доступа, а также контролировать их исполнение без ручных проверок.

`Spring Security` базируется на 2х интерфейсах, которые определяют связь сущностей с секьюрностью: 
- **UserDetails** - то, что будет интерпретироваться системой как пользователь
- **GrantedAuthority** - сущность, описывающая права юзера

Рассмотрим приложение.
Новые классы:
- **WebSecurityConfig** - настройка секьюрности по определенным `URL`, а также настройка `UserDetails` и `GrantedAuthority`.
- **LoginSuccessHandler** - хэндлер, содержащий в себе алгоритм действий при успешной аутентификации. Например, тут мы можем отправить пользователя с ролью админа на админку после логина, а с ролью юзер на главную страницу сайта и т.п.

Задание:
1. Перенесите классы и зависимости из предыдущей задачи.
2. Создайте класс `Role` и свяжите `User` с ролями так, чтобы юзер мог иметь несколько ролей.
3. Имплементируйте модели `Role` и `User` интерфейсами `GrantedAuthority` и `UserDetails` соответственно. Измените настройку секьюрности с `inMemory` на `userDetailService`.
4. Все `CRUD` - операции и страницы для них должны быть доступны только пользователю с ролью `admin` по **url**: `/admin/`.
5. Пользователь с ролью user должен иметь доступ только к своей домашней странице `/user`, где выводятся его данные. Доступ к этой странице должен быть только у пользователей с ролью `user` и `admin`. Не забывайте про несколько ролей у пользователя!
6. Настройте `logout` с любой страницы с использованием возможностей `thymeleaf`.
7. Настройте `LoginSuccessHandler` так, чтобы админа после аутентификации направляло на страницу `/admin`, а юзера на его страницу /user.
8. Добавить `bootstrap`

## Информация для запуска
- URL веб приложения: `localhost:8080`

**Пользователи по умолчанию:**

|     Email      | Password | Role  |
|:--------------:|:--------:|:-----:|
|  user@user.ru  |   user   | USER  |
| admin@admin.ru |  admin   | ADMIN |
