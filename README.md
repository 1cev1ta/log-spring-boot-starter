# Log Spring Boot Starter

`log-starter` — это Spring Boot Starter для легкой настройки аспектного логирования пользовательских методов в вашем проекте.

## Что он делает

— Автоматически подключает Aspect-логирование любых методов, помеченных аннотациями:
- `@BeforeLog`
- `@AfterReturningLog`
- `@AfterThrowingLog`
- `@AroundLog`

— Гибкая настройка уровня логирования через `application.yml`

— Включение и отключение логирования через свойство "enabled"

## Как использовать

**Шаг 1. Добавьте зависимость в `pom.xml` своего проекта:**

 ```xml
<dependency>
  <groupId>org.spring.bsdev.starter</groupId>
  <artifactId>log-starter</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```

**Шаг 2. В `application.yml` своего проекта добавьте:**

 ```yaml
logging:
  starter:
    enabled: true    # true — включено, false — отключено
    level: INFO      # Уровень логов: TRACE, DEBUG, INFO, WARN, ERROR
```

Изменяя значение параметра `enabled` можно включать/отлючать логгирование

Значение параметра `level` будет влиять на уровень логов

Чтобы стали доступны уровни логов DEBUG и TRACE можно разрешить уровни этих логов только для нашего стартера:

 ```yaml
logging:
  level:
    root: INFO                            # оставляем INFO для всего нашего приложения
    org.spring.bsdev.starter.log_starter: DEBUG  # включаем DEBUG-логи только для нашего стартера
```

или

 ```yaml
logging:
  level:
    root: INFO                            # оставляем INFO для всего нашего приложения
    org.spring.bsdev.starter.log_starter: TRACE  # включаем TRACE-логи только для нашего стартера
```

**Шаг 3. В контроллерах помечайте методы аннотациями, которые предоставляет `log-starter` и всё будет автоматически залогировано на выбранном вами уровне.**

Импортируйте аннотации из пакета `org.spring.bsdev.starter.log_starter.aspect.annotation` и используйте их над методами:

```java
import org.spring.bsdev.starter.log_starter.aspect.annotation.BeforeLog;
import org.spring.bsdev.starter.log_starter.aspect.annotation.AfterReturningLog;
import org.spring.bsdev.starter.log_starter.aspect.annotation.AfterThrowingLog;
import org.spring.bsdev.starter.log_starter.aspect.annotation.AroundLog;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @AroundLog
    @AfterReturningLog
    public TaskResponse createTask(@RequestBody TaskRequest taskRequest) {
        return taskService.createTask(taskRequest);
    }

    @GetMapping("/{id}")
    @AfterReturningLog
    @AfterThrowingLog
    public TaskResponse getTask(@PathVariable long id) {
        return taskService.findTaskByIdOrThrow(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @BeforeLog
    @AfterThrowingLog
    public void updateTask(@PathVariable long id, @RequestBody TaskRequest taskRequest) {
        taskService.updateTask(id, taskRequest);
    }
    //и так далее
}
```
**Шаг 4. Запустите свое приложение и при старте Spring Boot автоматически подключит `LoggingAutoConfig`, активирует аспект и начнёт логировать ваши методы согласно выставленным настройкам.**

После запуска приложения в консоли при вызове методов контроллера помеченных аннотациями должны выводиться логи в формате:

```
INFO  – Был вызван метод: createTask
INFO  – Метод createTask вернул: Task{id=1, …}
```

> При проведении тестов я выяснил, что версия Spring Boot в вашем проекте должна совпадать с версией в стартере (например, `2.7.5`).