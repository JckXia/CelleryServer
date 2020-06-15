CREATE TABLE IF NOT EXISTS `users` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `user_id` varchar(255),
  `first_name` varchar(255),
  `last_name` varchar(255),
  `encrypted_password` varchar(255),
  `created_at` timestamp
);

CREATE TABLE IF NOT EXISTS `routines` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `user_fk` int,
  `routine_id` varchar(255),
  `log_fk` int
);

CREATE TABLE IF NOT EXISTS `Products` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `product_id` varchar(255),
  `product_name` varchar(255),
  `product_description` varchar(255)
);

CREATE TABLE IF NOT EXISTS `ROUTINE_PRODUCTS` (
    `id_routine` int,
    `id_product` int,
    PRIMARY KEY (`id_routine`, `id_product`)
);

CREATE TABLE IF NOT EXISTS `TimeObject`(
	`id` int PRIMARY KEY auto_increment,
    `year` int,
    `month` int,
    `day` int,
    `dateTime` DATE
);

CREATE TABLE IF NOT EXISTS `Logs` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `log_id` varchar(255),
  `time_object_fk` int,
  `notes` varchar(255),
  `rating` numeric,
  `is_time_of_month` boolean
);

CREATE TABLE IF NOT EXISTS `Medications` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `log_fk` int,
  `medication_id` varchar(255),
  `name` varchar(255),
  `description` varchar(255)
);

CREATE TABLE IF NOT EXISTS `Images` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `image_id` varchar(255),
  `compressed_blob` blob,
  `log_fk` int
);



ALTER TABLE `routines`  ADD CONSTRAINT fk_routines_user FOREIGN KEY (`user_fk`) REFERENCES `users` (id);
ALTER TABLE `routines` ADD CONSTRAINT fk_routines_log FOREIGN KEY (`log_fk`) REFERENCES `Logs` (id);

ALTER TABLE `ROUTINE_PRODUCTS` ADD CONSTRAINT fk_routines_routine_products FOREIGN KEY (`id_routine`) REFERENCES `routines`(id);
ALTER TABLE `ROUTINE_PRODUCTS` ADD CONSTRAINT fk_products_routine_products FOREIGN KEY (`id_product`) REFERENCES `Products`(id);

ALTER TABLE `Logs` ADD CONSTRAINT fk_log_time FOREIGN KEY (`time_object_fk`) references `TimeObject`(id);

ALTER TABLE `Medications` ADD CONSTRAINT fk_medication_log FOREIGN KEY (`log_fk`) references `Logs`(id);

ALTER TABLE `Images` ADD CONSTRAINT fk_images_log foreign key (`log_fk`) references `Logs`(id);
