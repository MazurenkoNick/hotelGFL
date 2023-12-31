INSERT INTO room_classes(name) VALUES ('STANDARD'), ('PRESIDENTIAL');

INSERT INTO rooms(id, bed_count, day_price, room_class_id, room_num)
VALUES
    (10, 1, 199.99, 1, 1),
    (11, 3, 499.99, 1, 2),
    (12, 2, 349.99, 1, 3),
    (13, 3, 499.99, 2, 4),
    (14, 3, 499.99, 2, 5);

INSERT INTO renters(id, EMAIL, FIRST_NAME, LAST_NAME, PASSPORT_ID, PHONE_NUM)
VALUES
    (10, 'n@gmail.com', 'First', 'Last', '1234543', '2902928'),
    (11, 'm@gmail.com', 'Inokentii', 'Inokentiev', '1324543', '2902929'),
    (12, 'l@gmail.com', 'Alex', 'Shaldenko', '2134543', '2902930'),
    (13, 'k@gmail.com', 'Peter', 'Parker', '2314543', '2902931');

INSERT INTO discounts(id, PERCENT, RENTER_ID, ROOM_CLASS_ID)
VALUES
    (10, 25, 10, 1),
    (14, 50, 10, 1),
    (15, 50, 10, 2),
    (11, 15, 11, 2),
    (12, 20, 12, 1),
    (13, 15, 13, 2);

-- password 1111
INSERT INTO administrators(id, salary, admin_rank, email, first_name, last_name, passport_id, password, phone_num)
VALUES
    (10, 9999, 'JUNIOR', 'n1@gmail.com', 'First', 'Last', '1234530', '$2a$10$CdEJ2PKXgUCIwU4pDQWICuiPjxb1lysoX7jrN.Y4MTMoY9pjfPALO', '3102928'),
    (11, 15000, 'MIDDLE', 'n2@gmail.com', 'Ivan', 'Ivanenko', '1234531', '$2a$10$CdEJ2PKXgUCIwU4pDQWICuiPjxb1lysoX7jrN.Y4MTMoY9pjfPALO', '3102929'),
    (12, 20000, 'SENIOR', 'n3@gmail.com', 'Peter', 'Petrenko', '1234532', '$2a$10$CdEJ2PKXgUCIwU4pDQWICuiPjxb1lysoX7jrN.Y4MTMoY9pjfPALO', '3102930'),
    (13, 10000, 'JUNIOR', 'n4@gmail.com', 'Mykola', 'Mykolenko', '1234533', '$2a$10$CdEJ2PKXgUCIwU4pDQWICuiPjxb1lysoX7jrN.Y4MTMoY9pjfPALO', '3102931'),
    (14, 13500, 'MIDDLE', 'n5@gmail.com', 'Viktor', 'Viktorov', '1234534', '$2a$10$CdEJ2PKXgUCIwU4pDQWICuiPjxb1lysoX7jrN.Y4MTMoY9pjfPALO', '3102932');

INSERT INTO reservations(id, administrator_id, from_datetime, renter_id, room_id, to_datetime)
VALUES
    (10, 10, '2000-02-02', 10, 10, '2000-02-05'),
    (11, 11, '2000-02-03', 11, 11, '2000-02-07'),
    (12, 12, '2000-02-01', 12, 12, '2000-02-03'),
    (13, 13, '2000-02-04', 12, 13, '2000-02-08');

INSERT INTO roles(id, name)
VALUES
    (10, 'DEFAULT'),
    (11, 'ADMIN');

INSERT INTO admin_roles(role_id, administrator_id)
VALUES
    (10, 10),
    (10, 11),
    (11, 12),
    (11, 13),
    (10, 14);

INSERT INTO receipts(receipts.total_price, receipts.check_in, receipts.check_out, receipts.reservation_id)
VALUES (449.9775, '2000-02-02 00:00:00', '2000-02-05 00:00:00', 10);