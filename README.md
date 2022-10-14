# BlogEngineGoriacheva
## Дипломная работа по курсу "Java разработчик с нуля" от Skillbox. Итоговый проект курса "Блоговый движок".
[English version bellow](#English-version)
___


# Навигация
- [Описание](#Описание)
- [Локальный запуск](#Локальный-запуск)
- [База данных](#База-данных)
- [Навигация по проекту](#Навигация-по-проекту)
- [Скриншоты](#Скриншоты)
- [Используемые технологии](#Используемые-технологии)
- [Контактные данные](#Контактные-данные)

___
## Описание:

Это дипломный проект образовательной платформы [Skillbox](https://skillbox.ru/), курса **Java разработчик с нуля**.

Данный проект представляет собой обычный блог, который базируется на технологии Spring Boot. (список исп. технологий см. [тут](#Используемые-технологии))

<h2 align="center">

Весь фронтенд был предоставлен платформой [Skillbox](https://skillbox.ru/)
</h2>

На данном блоге можно:

- Просматривать посты;
- Регистрироваться;
    - Есть возможность восстановление пароля с отправкой письма юзеру; 
- Редактировать свой профиль;
    - Загрузка аватарки (она будет автоматически обрезана). Допустимые форматы фотографии jpg\jpeg и png. Также можно меняит имя, пароль и email.
- Писать свои посты; 
    - Перед публикацией пост должен быть утвержден или отклонен модератором сайта;
- Редактировать свои посты;
- Писать комментарии и отвечать на них;
- Ставить лайки или дизлайки;
- Смотреть статистику;
    - Смотреть статистику можно как свою, так и общую по всему блогу.
- Искать публикации: по слову, по тэгу, по дате публикации;
- Для модераторов есть возможность изменять глобальные настройки блока (изменение возможности регистрации, изменение необходимости модерации постов, разрешение просмотра общей статистики для гостей блога)

Медиафайл для демонстрации:

<h2 align="center">

![image](https://github.com/Viktoria-Goriacheva/BlogEngineGoriacheva/blob/master/pictures/animation.gif)</h2>

____

## Локальный запуск:

Для того, чтобы запустить проект локально вам необходимы JDK 11, система контроля версий Git, сборщик проектов Maven.
Клонировать проект можно через git bash:

    git init
    git clone https://github.com/Viktoria-Goriacheva/BlogEngineGoriacheva.git
    
После того, как проект скопировался в ваш локальный репозиторий, вам необходимо изменить несколько значений. Что изменить:
  ![image](https://github.com/Viktoria-Goriacheva/BlogEngineGoriacheva/blob/master/pictures/instr1.jpg)</h2> -> свойства подключения к базе данных, а также email и пароль; (проверка безопасности от Gmail);
    - информацию о базе данных можно прочитать вот [тут](#База-данных)
  ![image](https://github.com/Viktoria-Goriacheva/BlogEngineGoriacheva/blob/master/pictures/instr2.jpg)</h2> -> изменить адрес для отправки писем на почты юзеров.
  Так же изменить свойство jpa ddl-auto: update на create при первом запуске.
 
____

## База данных:

В качестве БД используется PostgreSQL 11.

В базе данных будут лежать глобальные настройки (таблица global settings), заполняемая при инициализации значениями по-умолчанию.
Остальные 7 таблиц заполняются из приложения.

____

## Навигация по проекту:

[src -> main -> java -> com -> goriacheva -> blog](https://github.com/Viktoria-Goriacheva/BlogEngineGoriacheva/tree/master/src/main/java/com/goriacheva/blog)

- annotations -> собственные аннотации на валидацию имени, пароля и почты;
- api -> 
    - request -> классы-запросы от сервера, содержащие определенные переменные;
    - response -> классы-ответы для сервера в результате обработки их сервисами;
- config -> настройка конфигурации проекта;
- controller -> слой контроллеров;
- dto -> объекты передачи данных; (DTO)
- model ->
    - класс Enum для Spring Security;
    - entity-классы для таблиц базы данных;
- repository -> слой репозиториев базы данных; 
- security -> настройки для авторизации и аутентификации пользователя;
- service -> сервисный слой; (обработка запросов, энамы и конвертеры для них)

Тесты:

[src -> test -> java -> com -> goriacheva -> blog](https://github.com/Viktoria-Goriacheva/BlogEngineGoriacheva/blob/master/src/test/java/com/goriacheva/blog/PostServiceTest.java)

____

## Скриншоты
<h2 align="center">

**Редактирование профиля**

![Image](https://github.com/Viktoria-Goriacheva/BlogEngineGoriacheva/blob/master/pictures/profile.jpg)

**Поиск по датам**

![Image](https://github.com/Viktoria-Goriacheva/BlogEngineGoriacheva/blob/master/pictures/calendar.jpg)

**Страница регистрации**

![Image](https://github.com/Viktoria-Goriacheva/BlogEngineGoriacheva/blob/master/pictures/reg.jpg)

**Страница поста с комментариями пользователей**

![Image](https://github.com/Viktoria-Goriacheva/BlogEngineGoriacheva/blob/master/pictures/comments.jpg)

**Редактирование поста**

![Image](https://github.com/Viktoria-Goriacheva/BlogEngineGoriacheva/blob/master/pictures/messeg.jpg)
</h2>

____

## Используемые технологии:

- Maven;
- Spring;
- Spring boot;
- Spring Security;
- Spring Data;
- Spring MVC
- Hibernate;
- Junit 5;
- Mockito;
- Jsoup;
- Lombok;
- Cage; (captcha)
- Commons-io;
- log4j2;
- Assertj-core;

____

## Контактные данные

По всем интересующим вас вопросам можно писать на почту:

grumbla@yandex.ru

grumblya@mail.ru

grumblaa@gmail.com


Или связаться со мной в телеграм:

https://t.me/Grumbla

____

## English version

# BlogEngineGoriacheva
## Diploma work on the course "Java Developer from scratch" from Skillbox. The final project of the course "Blog engine".

___

# Navigation
- [Description](#Description)
- [Local launch](#Local-launch)
- [Database](#Database)
- [Navigation on the project](#Navigation-on-the-project)
- [Screenshots](#Screenshots)
- [Used technologies](#Used-technologies)
- [Contact details](#Contact-details)

___
## Description:

This is a diploma project of the educational platform [Skillbox] (https://skillbox.ru/), course ** java developer from scratch **.

This project is a regular blog, which is based on Spring Boot technology. (list of Spanish technologies, see [here] (#used technologies))

<h2 align = "Center">

The entire frontend was provided by the [Skillbox](https://skillbox.ru/) platform
</h2>


On this blog you can:

- view posts;
- register;
    - there is the opportunity to restore the password with sending a letter to the user;
- edit your profile;
    - loading the avatar (it will be automatically cut). Permissible formats of photos JPG \ JPEG and PNG. You can also change the name, password and email.
- write your posts;
    - before publication, the post must be approved or rejected by the moderator of the site;
- edit your posts;
- write comments and answer them;
- make likes or diesels;
- watch statistics;
    - You can watch statistics, both your own and common throughout the blog.
- search for publications: by the way, by Tag, by date of publication;
- for moderators, it is possible to change the global settings of the block (changing the possibility of registration, a change in the need for moderation of posts, permission to view general statistics for blog guests)

Media file for demonstration:

<h2 align="center">

![image](https://github.com/Viktoria-Goriacheva/BlogEngineGoriacheva/blob/master/pictures/animation.gif)</h2>

____

## Local launch:

In order to launch the project locally you need JDK 11, the Git version control system, Maven project collector.
You can clone the project through Git Bash:

    git init
    git clone https://github.com/Viktoria-Goriacheva/BlogEngineGoriacheva.git
    
After the project has been copied to your local repository, you need to change several values. What to change:
  ![image](https://github.com/Viktoria-Goriacheva/BlogEngineGoriacheva/blob/master/pictures/instr1.jpg)</h2> -> properties of data connection, as well as email and password; (security check from Gmail);
    - information about the database can be read here [here](#Database)
   ![image](https://github.com/Viktoria-Goriacheva/BlogEngineGoriacheva/blob/master/pictures/instr2.jpg)</h2> -> change the address for sending users' mail.
  Also change the property of JPA DDL-Auto: Update on Create at the first launch.
 
____

## Database:

As a database, PostgreSQL 11 is used.

The database will be global settings (Global Settings table), filled with an initialization of defending.
The remaining 7 tables are filled out of the application.

____

## Navigation on the project:

[src -> main -> java -> com -> goriacheva -> blog](https://github.com/Viktoria-Goriacheva/BlogEngineGoriacheva/blob/master/src/main/java/com/goriacheva/blog)

- annotations -> own annotations for validation of the name, password and mail;
- api ->
    -request -> classes from the server containing certain variables;
    -response -> classes -fasteners for the server as a result of processing them with services;
- config -> setting up the project configuration;
- controller -> layer of controllers;
- dto -> data transfer objects; (DTO)
- model ->
    - enum class for Spring Security;
    - entity classes for database tables;
- repository -> a layer of database repositories;
- security -> settings for the authorization and authentication of the user;
- service -> service layer; (processing of requests, anam and converters for them)

Tests:

[src -> test -> java -> com -> goriacheva -> blog](https://github.com/Viktoria-Goriacheva/BlogEngineGoriacheva/blob/master/src/test/java/com/goriacheva/blog/PostServiceTest.java)

____

## Screenshots

<h2 align="center">

**Profile editing**

![Image](https://github.com/Viktoria-Goriacheva/BlogEngineGoriacheva/blob/master/pictures/profile.jpg)

**Search by dates**

![Image](https://github.com/Viktoria-Goriacheva/BlogEngineGoriacheva/blob/master/pictures/calendar.jpg)

**Registration page**

![Image](https://github.com/Viktoria-Goriacheva/BlogEngineGoriacheva/blob/master/pictures/reg.jpg)

**Page of the post with user comments**

![Image](https://github.com/Viktoria-Goriacheva/BlogEngineGoriacheva/blob/master/pictures/comments.jpg)

**Editing post**

![Image](https://github.com/Viktoria-Goriacheva/BlogEngineGoriacheva/blob/master/pictures/messeg.jpg)
</h2>

____

## Used technologies:

- Maven;
- Spring;
- Spring boot;
- Spring Security;
- Spring Data;
- Spring MVC
- Hibernate;
- Junit 5;
- Mockito;
- Jsoup;
- Lombok;
- Cage; (captcha)
- Commons-io;
- log4j2;
- Assertj-core;

____

## Contact details

On all questions you are interested, you can write to the mail:

grumbla@yandex.ru

grumblya@mail.ru

grumblaa@gmail.com


Or contact me in telegram:

https://t.me/Grumbla
