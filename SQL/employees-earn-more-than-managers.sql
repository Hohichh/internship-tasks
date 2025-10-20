-- 1st option
SELECT employee.name as Employee
    FROM Employee as employee
    JOIN Employee as manager 
        ON employee.managerId = manager.id
WHERE (employee.salary > manager.salary)


-- 2nd option 
-- much worse in terms of speed
SELECT name FROM Employee e
WHERE salary > (
    SELECT salary FROM Employee m
    WHERE e.managerId = m.id
)
