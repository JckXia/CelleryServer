CREATE TABLE IF NOT EXISTS `users` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `user_id` varchar(255),
  `first_name` varchar(255),
  `last_name` varchar(255),
  `encrypted_password` varchar(255),
  `created_at` timestamp
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS `routines` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `user_fk` int,
  `routine_id` varchar(255),
  `log_fk` int
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS `products` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `user_fk` int,
  `product_id` varchar(255),
  `product_name` varchar(255),
  `product_description` varchar(255)
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS `routine_products` (
    `id_routine` int ,
    `id_product` int
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS `time_object`(
	`id` int PRIMARY KEY auto_increment,
    `year` int,
    `month` int,
    `day` int,
    `dateTime` DATE
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS `logs` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `log_id` varchar(255),
  `time_object_fk` int,
  `notes` varchar(255),
  `rating` numeric,
  `is_time_of_month` boolean
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS `medications` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `log_fk` int,
  `medication_id` varchar(255),
  `name` varchar(255),
  `description` varchar(255)
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS `images` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `image_id` varchar(255),
  `compressed_blob` blob,
  `log_fk` int
) ENGINE=INNODB;


ALTER TABLE `routines`  ADD CONSTRAINT fk_routines_user FOREIGN KEY (`user_fk`) REFERENCES `users` (id);
ALTER TABLE `routines` ADD CONSTRAINT fk_routines_log FOREIGN KEY (`log_fk`) REFERENCES `logs` (id);

ALTER TABLE `products` ADD CONSTRAINT fk_products_user FOREIGN KEY (`user_fk`) REFERENCES `users`(id);

ALTER TABLE `routine_products` ADD CONSTRAINT fk_routines_routine_products FOREIGN KEY (`id_routine`) REFERENCES `routines`(id) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `routine_products` ADD CONSTRAINT fk_products_routine_products FOREIGN KEY (`id_product`) REFERENCES `products`(id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `logs` ADD CONSTRAINT fk_log_time FOREIGN KEY (`time_object_fk`) references `time_object`(id);

ALTER TABLE `medications` ADD CONSTRAINT fk_medication_log FOREIGN KEY (`log_fk`) references `logs`(id);

ALTER TABLE `images` ADD CONSTRAINT fk_images_log foreign key (`log_fk`) references `logs`(id);
