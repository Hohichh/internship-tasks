# Skynet

---
## Background

There are two factions: World and Wednesday and neutral object - Factory.
Each faction is trying to create an army of robots, but to do so they need parts for the robots.
Robot parts are divided into: head, torso, hand, feet.
To build one robot you need 1 head, 1 torso, 2 hands, 2 feet.
They are produced by a neutral Factory, which produces no more than 10 parts every day.
The type of parts is chosen randomly.
At night, the factions go to the Factory to get parts for the robots (each faction can carry no more than 5 parts).
The factions and the factory each work in their own thread.

## Aim
Determine who will have the strongest army after 100 days.

---

## Implementation details

The simulation is built on a multithreaded architecture using Java's core concurrency utilities. The primary goal was to model the independent actions of the Factory and the two Factions while ensuring proper synchronization for accessing the shared resource (the parts warehouse).

### 1. Core Architecture

The system is composed of three main entities, each implemented as a `Runnable` task, allowing them to be executed in separate threads:

*   **`Factory`**: A single producer thread responsible for creating new robot parts each "day".
*   **`Faction`**: Two consumer threads (`World` and `Wednesday`) that attempt to retrieve parts from the warehouse each "night". A single `Faction` class is used for both, distinguished by a `name` field.
*   **`Storage`**: The central, shared resource acting as a warehouse. It is implemented using a **`java.util.concurrent.ArrayBlockingQueue<Detail>`**.

### 2. Concurrency and Day/Night Cycle Synchronization

The core challenge was to orchestrate the "day" (production) and "night" (consumption) phases. This was achieved not with timers, but with a more robust and elegant solution: **`java.util.concurrent.CyclicBarrier`**.

*   A single `CyclicBarrier` was initialized to wait for **3 participants** (the Factory thread and the two Faction threads).
*   The simulation loop within each thread's `run()` method is structured around two calls to `barrier.await()`:

    1.  **End of Day Barrier**:
        *   The `Factory` thread produces parts and then calls `await()`.
        *   The `Faction` threads start their loop and immediately call `await()`, effectively pausing and waiting for the day to end.
        *   Once all three threads reach this point, the barrier breaks, and they all proceed simultaneously.

    2.  **End of Night Barrier**:
        *   The `Faction` threads, now active, retrieve parts from the storage and build robots. Afterward, they call the second `await()`.
        *   The `Factory` thread, having nothing to do at night, immediately calls the second `await()` and waits for the factions to finish.
        *   Once all three threads reach this second barrier, the "night" is officially over. The loop continues to the next day's iteration.

This pattern creates a reliable, lock-step simulation, ensuring that production and consumption phases are strictly separated without complex manual locking.

### 3. Thematic Logging

A key feature of the project is the atmospheric, sci-fi-themed logging configured with a custom `java.util.logging` setup.
---

## War chronicles

The entire 100-day conflict was automatically documented in the `chronicles.log` file. Below is a representative snippet from the generated chronicle.

```
[ХРОНИКА. ДЕНЬ ??] :: Источник: ВЕЛИКАЯ ХРОНИКА :: Запись: И восстали машины из пепла ядерного огня, и пошла война на уничтожения человечества. И шла она десятилетия, но последнее сражение состоится не в будущем, оно состоится здесь, в наше время, сегодня ночью.
[ХРОНИКА. ДЕНЬ ??] :: Источник: ВЕЛИКАЯ ХРОНИКА :: Запись: =======================================================================

[ХРОНИКА. ДЕНЬ 1] :: Источник: ЦЕНТРАЛЬНАЯ ФАБРИКА :: Запись: ПРОИЗВЕДЕНО: 7 ДЕТАЛЕЙ
[ХРОНИКА. ДЕНЬ 1] :: Источник: 'ФРАКЦИЯ WEDNESDAY' :: Запись: Совершена вылазка на склад. Захвачено ресурсов: 4 ед. Полученных компонентов недостаточно для запуска производственного цикла.
[ХРОНИКА. ДЕНЬ 1] :: Источник: 'ФРАКЦИЯ WORLD' :: Запись: Совершена вылазка на склад. Захвачено ресурсов: 3 ед. Полученных компонентов недостаточно для запуска производственного цикла.
[ХРОНИКА. ДЕНЬ 2] :: Источник: ЦЕНТРАЛЬНАЯ ФАБРИКА :: Запись: ПРОИЗВЕДЕНО: 4 ДЕТАЛЕЙ
[ХРОНИКА. ДЕНЬ 2] :: Источник: 'ФРАКЦИЯ WEDNESDAY' :: Запись: Совершена вылазка на склад. Захвачено ресурсов: 1 ед. Полученных компонентов недостаточно для запуска производственного цикла.
[ХРОНИКА. ДЕНЬ 2] :: Источник: 'ФРАКЦИЯ WORLD' :: Запись: Совершена вылазка на склад. Захвачено ресурсов: 3 ед. Полученных компонентов недостаточно для запуска производственного цикла.
......
[ХРОНИКА. ДЕНЬ 49] :: Источник: ЦЕНТРАЛЬНАЯ ФАБРИКА :: Запись: ПРОИЗВЕДЕНО: 2 ДЕТАЛЕЙ
[ХРОНИКА. ДЕНЬ 49] :: Источник: 'ФРАКЦИЯ WORLD' :: Запись: Совершена вылазка на склад. Захвачено ресурсов: 2 ед. Полученных компонентов недостаточно для запуска производственного цикла.
[ХРОНИКА. ДЕНЬ 49] :: Источник: 'ФРАКЦИЯ WEDNESDAY' :: Запись: Разведка донесла, что центральный склад пуст. Фракция вернулась с пустыми руками.
[ХРОНИКА. ДЕНЬ 50] :: Источник: ЦЕНТРАЛЬНАЯ ФАБРИКА :: Запись: ПРОИЗВЕДЕНО: 4 ДЕТАЛЕЙ
[ХРОНИКА. ДЕНЬ 50] :: Источник: 'ФРАКЦИЯ WORLD' :: Запись: Совершена вылазка на склад. Захвачено ресурсов: 2 ед. Сборочные цеха активированы: создано новых боевых единиц: 1.
[ХРОНИКА. ДЕНЬ 50] :: Источник: 'ФРАКЦИЯ WEDNESDAY' :: Запись: Совершена вылазка на склад. Захвачено ресурсов: 2 ед. Полученных компонентов недостаточно для запуска производственного цикла.
...... 
[ХРОНИКА. ДЕНЬ 100] :: Источник: 'ФРАКЦИЯ WEDNESDAY' :: Запись: Совершена вылазка на склад. Захвачено ресурсов: 3 ед. Полученных компонентов недостаточно для запуска производственного цикла.
[ХРОНИКА. ДЕНЬ ??] :: Источник: ВЕЛИКАЯ ХРОНИКА :: Запись: =======================================================================
[ХРОНИКА. ДЕНЬ ??] :: Источник: ВЕЛИКАЯ ХРОНИКА :: Запись: ИТОГИ ВЕЛИКОГО ПРОТИВОСТОЯНИЯ:

   Легионы фракции 'World' восстали числом в 28 стальных воинов.
   Технокульт 'Wednesday' породил армию из 35 несокрушимых конструктов.

И так гласит хроника этой эпохи машин. В битве за технологическое господство победу одержала фракция 'Wednesday', чья воля и сталь оказались крепче.

```