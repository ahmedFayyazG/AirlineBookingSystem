-- Table: flights
CREATE TABLE flights (
    id SERIAL PRIMARY KEY,
    flight_number VARCHAR(20),
    origin VARCHAR(100),
    destination VARCHAR(100),
    departure_time VARCHAR(50),
    available_seats INT
);

-- Table: users
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE,
    password VARCHAR(100),
    role VARCHAR(20) -- e.g., 'admin', 'passenger'
);

-- Table: bookings
CREATE TABLE bookings (
    id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id),
    flight_id INT REFERENCES flights(id),
    booking_date DATE
);
