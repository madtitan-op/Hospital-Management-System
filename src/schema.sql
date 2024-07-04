CREATE TABLE patients(
    id INT AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL ,
    age INT NOT NULL ,
    gender VARCHAR(10) NOT NULL ,
    PRIMARY KEY(id)
);

CREATE TABLE doctors(
    id INT AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL ,
    specialization VARCHAR(255) NOT NULL ,
    PRIMARY KEY (id)
);

CREATE TABLE appointments(
    id INT AUTO_INCREMENT,
    patient_id INT NOT NULL ,
    doctor_id INT NOT NULL ,
    appointment_date DATE NOT NULL ,
    transaction_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (patient_id) REFERENCES patients(id),
    FOREIGN KEY (doctor_id) REFERENCES doctors(id)
);