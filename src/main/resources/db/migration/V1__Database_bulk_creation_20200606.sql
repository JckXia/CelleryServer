CREATE TABLE IF NOT EXISTS `users` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `user_id` varchar(255),
  `firstName` varchar(255),
  `lastName` varchar(255),
  `encrypted_password` varchar(255),
  `created_at` timestamp
);

CREATE TABLE  IF NOT EXISTS `routines` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `user_fk` int,
  `routine_id` varchar(255),
  `log_fk` int
);

CREATE TABLE  IF NOT EXISTS `Products` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `routine_fk` int,
  `product_id` varchar(255),
  `product_name` varchar(255),
  `product_description` varchar(255)
);

Create Table IF NOT EXISTS `TimeObject`(
	`id` int PRIMARY KEY auto_increment,
    `year` int,
    `month` int,
    `day` int,
    `dateTime` DATE
);

CREATE TABLE  IF NOT EXISTS `Logs` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `log_id` varchar(255),
  `timeobject_fk` int,
  `notes` varchar(255),
  `rating` numeric,
  `isTimeOfMonth` boolean
);

CREATE TABLE IF NOT EXISTS `Medications` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `log_fk` int,
  `medication_id` varchar(255),
  `name` varchar(255),
  `description` varchar(255)
);

CREATE TABLE   IF NOT EXISTS `Images` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `image_id` varchar(255),
  `compressedBlob` blob,
  `log_fk` int
);



ALTER TABLE `routines`  ADD CONSTRAINT fk_routines_user FOREIGN KEY (`user_fk`) REFERENCES `users` (id);

ALTER TABLE `routines` ADD CONSTRAINT fk_routines_log FOREIGN KEY (`log_fk`) REFERENCES `Logs` (id);

ALTER TABLE `Products` ADD CONSTRAINT fk_product_routine FOREIGN KEY (`routine_fk`) REFERENCES `routines`(id);

ALTER TABLE `Logs` ADD CONSTRAINT fk_log_time FOREIGN KEY (`timeobject_fk`) references `TimeObject`(id);

ALTER TABLE `Medications` ADD CONSTRAINT fk_medication_log FOREIGN KEY (`log_fk`) references `Logs`(id);

ALTER TABLE `Images` ADD CONSTRAINT fk_images_log foreign key (`log_fk`) references `Logs`(id);
