# Проект 3 - Анализатор логов

## Описание задачи

Лог-файлы являются важной частью работы любого сервера, так как они содержат информацию о запросах, ошибках и действиях. Для автоматизации анализа логов напишем программу-анализатор NGINX логов.

---

## Функциональные требования

Программа должна:
1. Принимать путь к одному или нескольким лог-файлам (локальный шаблон или URL).
2. Поддерживать опциональные параметры:
   - `from` и `to` для анализа записей в заданном временном диапазоне (ISO8601).
   - Формат вывода: `markdown` или `adoc`.
3. Выполнять следующие задачи:
   - Подсчитывать общее количество запросов.
   - Определять наиболее часто запрашиваемые ресурсы.
   - Определять наиболее часто встречающиеся коды ответа.
   - Рассчитывать средний размер ответа сервера.
   - Рассчитывать 95% перцентиль размера ответа сервера.

---

## Нефункциональные требования

1. **Эффективность обработки**: Программа должна работать потоково, не загружая весь файл в память.
2. **Кодирование**: Код должен быть структурированным и соответствовать стандартам.
3. **Обработка ошибок**: Логика обработки ошибок должна быть четкой и понятной.
4. **Человекочитаемый вывод**: Результат должен быть легко интерпретируемым.

---

## Входные данные

- Путь к лог-файлам: локальный путь (с поддержкой glob) или URL.
- Временные параметры: `from` и `to` (ISO8601, опционально).
- Формат вывода: `markdown` или `adoc` (опционально).

ps. Одновременно должен работать с ссылкой и с ленивой загрузкой из файла
---

## Выходные данные

Текстовый отчет в выбранном формате с анализом логов.

**Пример вывода:**

#### Общая информация
| Метрика | Значение |
|:---------------------:|-------------:|
| Файл(-ы) | `access.log` |
| Начальная дата | 31.08.2024 |
| Конечная дата | - |
| Количество запросов | 10_000 |
| Средний размер ответа | 500b |
| 95p размера ответа | 950b |

#### Запрашиваемые ресурсы
| Ресурс | Количество |
|:---------------:|-----------:|
| `/index.html` | 5_000 |
| `/about.html` | 2_000 |
| `/contact.html` | 1_000 |

#### Коды ответа
| Код | Имя | Количество |
|:---:|:---------------------:|-----------:|
| 200 | OK | 8000 |
| 404 | Not Found | 1000 |
| 500 | Internal Server Error | 500 |


# Пример запуска программы

```
--path logs/2024* --from 2024-08-31 --format markdown
--path https://raw.githubusercontent.com/elastic/examples/master/Common%20Data%20Formats/nginx_logs/nginx_logs --format adoc
--path logs/**/2024-08-31.txt
```



