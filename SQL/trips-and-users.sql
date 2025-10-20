-- select subquery from Trips
-- joined with users from Users devided to clients and drivers
-- choose trips, where no user is banned
-- takes status and request_at columns
WITH unbannedTrips AS (
    SELECT
        t.status,
        t.request_at
    FROM Trips AS t
    JOIN Users AS client ON t.client_id = client.users_id
    JOIN Users AS driver ON t.driver_id = driver.users_id
    WHERE client.banned = 'No' AND driver.banned = 'No'
) 
-- counts cancellation rate (cancelled trips / all trips (with unbanned users))
SELECT
    request_at AS Day,
    ROUND(
        SUM(CASE WHEN status IN ('cancelled_by_client', 'cancelled_by_driver')
         THEN 1.0 
         ELSE 0 END)
          / COUNT(*),
        2
    ) AS "Cancellation Rate"
FROM unbannedTrips
WHERE request_at BETWEEN '2013-10-01' AND '2013-10-03'
GROUP BY request_at
ORDER BY request_at;