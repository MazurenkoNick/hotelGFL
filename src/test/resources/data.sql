INSERT INTO room_classes(name) VALUES ('STANDARD'), ('PRESIDENTIAL');

INSERT INTO rooms(bed_count, day_price, room_class_id, room_num)
VALUES
    (1, 199.99, 1, 1),
    (3, 499.99, 1, 2),
    (2, 349.99, 1, 3),
    (3, 499.99, 2, 4);

INSERT INTO renters(EMAIL, FIRST_NAME, LAST_NAME, PASSPORT_ID, PHONE_NUM)
VALUES
    ('n@gmail.com', 'first1', 'last1', '1234543', '2902928'),
    ('m@gmail.com', 'first2', 'last2', '1324543', '2902929'),
    ('l@gmail.com', 'first3', 'last3', '2134543', '2902930'),
    ('k@gmail.com', 'first4', 'last4', '2314543', '2902931');

INSERT INTO discounts(PERCENT, RENTER_ID, ROOM_CLASS_ID)
VALUES
    (25, 1, 1),
    (15, 2, 2),
    (20, 3, 1),
    (15, 4, 2);

INSERT INTO administrators(salary, admin_rank, email, first_name, last_name, passport_id, password, phone_num)
VALUES
    (9999, 'JUNIOR', 'n1@gmail.com', 'first1', 'last1', '1234530', 'Qwerty123', '3102928'),
    (15000, 'MIDDLE', 'n2@gmail.com', 'first2', 'last2', '1234531', 'Qwerty123', '3102929'),
    (20000, 'SENIOR', 'n3@gmail.com', 'first3', 'last3', '1234532', 'Qwerty123', '3102930'),
    (10000, 'JUNIOR', 'n4@gmail.com', 'first4', 'last4', '1234533', 'Qwerty123', '3102931'),
    (13500, 'MIDDLE', 'n5@gmail.com', 'first5', 'last5', '1234534', 'Qwerty123', '3102932');

INSERT INTO reservations(administrator_id, from_datetime, renter_id, room_id, to_datetime)
VALUES
    (1, '2000-02-02', 1, 1, '2000-02-05'),
    (1, '2000-02-02', 2, 2, '2000-02-05'),
    (1, '2000-02-02', 3, 3, '2000-02-05'),
    (1, '2000-02-02', 4, 4, '2000-02-05');