# API GATEWAY SERVICE

### Realisation of Api Gateway Application

Authentic serivce (BFF pattern realization)

| #   | Переменная конфигурации     | Назначение                                     | Значение по умолчанию |
|-----|-----------------------------|:-----------------------------------------------|:----------------------|
| 1   | `OPERATION_HOST`            | Хост роутинга для сервиса модификации новостей | http://localhost:8084 |
| 2   | `NEWS_HOST`                 | Хост роутинга для сервиса получения новостей   | http://localhost:8083 |
| 3   | `JWT_PUBLIC_KEY`            | Публичный ключ для проверки JWT                | -                     |
| 4   | `SERVER_PORT`               | Порт сервиса                                   | 8080                  |

## Execute project

    docker-compose up

