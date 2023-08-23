INSERT INTO room_classes(name) VALUES ('STANDARD'), ('PRESIDENTIAL');

INSERT INTO rooms(id, bed_count, day_price, room_class_id, room_num)
VALUES
    (10, 1, 199.99, 1, 1),
    (11, 3, 499.99, 1, 2),
    (12, 2, 349.99, 1, 3),
    (13, 3, 499.99, 2, 4);

INSERT INTO renters(id, EMAIL, FIRST_NAME, LAST_NAME, PASSPORT_ID, PHONE_NUM)
VALUES
    (10, 'n@gmail.com', 'first1', 'last1', '1234543', '2902928'),
    (11, 'm@gmail.com', 'first2', 'last2', '1324543', '2902929'),
    (12, 'l@gmail.com', 'first3', 'last3', '2134543', '2902930'),
    (13, 'k@gmail.com', 'first4', 'last4', '2314543', '2902931');

INSERT INTO discounts(id, PERCENT, RENTER_ID, ROOM_CLASS_ID)
VALUES
    (10, 25, 10, 1),
    (11, 15, 11, 2),
    (12, 20, 12, 1),
    (13, 15, 13, 2);

INSERT INTO administrators(id, salary, admin_rank, email, first_name, last_name, passport_id, password, phone_num)
VALUES
    (10, 9999, 'JUNIOR', 'n1@gmail.com', 'first1', 'last1', '1234530', 'Qwerty123', '3102928'),
    (11, 15000, 'MIDDLE', 'n2@gmail.com', 'first2', 'last2', '1234531', 'Qwerty123', '3102929'),
    (12, 20000, 'SENIOR', 'n3@gmail.com', 'first3', 'last3', '1234532', 'Qwerty123', '3102930'),
    (13, 10000, 'JUNIOR', 'n4@gmail.com', 'first4', 'last4', '1234533', 'Qwerty123', '3102931'),
    (14, 13500, 'MIDDLE', 'n5@gmail.com', 'first5', 'last5', '1234534', 'Qwerty123', '3102932');

INSERT INTO reservations(id, administrator_id, from_datetime, renter_id, room_id, to_datetime)
VALUES
    (10, 10, '2000-02-02', 10, 10, '2000-02-05'),
    (11, 11, '2000-02-02', 11, 11, '2000-02-05'),
    (12, 12, '2000-02-02', 12, 12, '2000-02-05'),
    (13, 13, '2000-02-02', 12, 13, '2000-02-05');