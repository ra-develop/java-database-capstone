mysql> CALL GetDailyAppointmentReportByDoctor('2025-04-15');
+------------------+----------------------------+--------+----------------+---------------+
| doctor_name      | appointment_time           | status | patient_name   | patient_phone |
+------------------+----------------------------+--------+----------------+---------------+
| Dr. Ava Hall     | 2025-04-15 11:00:00.000000 |      1 | Lucas Turner   | 889-666-6666  |
| Dr. Mark Johnson | 2025-04-15 12:00:00.000000 |      1 | Michael Jordan | 888-444-4444  |
| Dr. Mark Johnson | 2025-04-15 13:00:00.000000 |      1 | Olivia Moon    | 888-555-5555  |
+------------------+----------------------------+--------+----------------+---------------+
3 rows in set (0.032 sec)

Query OK, 0 rows affected (0.034 sec)