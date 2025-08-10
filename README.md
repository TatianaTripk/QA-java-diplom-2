# QA-java-diplom-2

Проект автоматизированного тестирования API веб-приложения Stellar Burgers

## 📦 Технологии

| Компонент               | Версия      |
|-------------------------|-------------|
| Java                    | 11          |
| JUnit                   | 4.13.2      |
| REST Assured            | 5.5.5       |
| Allure Framework        | 2.23.0      |
| DataFaker               | 1.8.1       |
| Jackson (Databind)      | 2.20.0-rc1  |
| Maven Surefire Plugin   | 3.0.0-M7    |

## 🚀 Запуск тестов

Основная команда:
```bash
mvn clean test
```
## Генерация отчета Allure

```bash
allure serve target/surefire-reports/