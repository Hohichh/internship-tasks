-- 1st option better in terms of speed
-- could easily modified to grouping-like queries
WITH RankedBySalary AS(
    SELECT salary,
    DENSE_RANK() OVER 
        (ORDER BY salary DESC) as salary_rank
    FROM Employee
) SELECT
    MAX(CASE WHEN salary_rank = 2 THEN salary END)
    as SecondHighestSalary
    FROM RankedBySalary;

-- 2nd option
SELECT(
    SELECT DISTINCT salary FROM Employee
    ORDER BY salary DESC
    LIMIT 1 OFFSET 1
) as SecondHighestSalary