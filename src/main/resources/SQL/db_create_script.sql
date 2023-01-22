-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema storedb2
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `storedb2` ;

-- -----------------------------------------------------
-- Schema storedb2
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `storedb2` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `storedb2` ;

-- -----------------------------------------------------
-- Table `storedb2`.`product_group`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `storedb2`.`product_group` (
  `group_id` BIGINT UNSIGNED NOT NULL,
  `group_name` VARCHAR(100) CHARACTER SET 'utf8mb3' NOT NULL,
  `created_at` DATETIME NOT NULL,
  `deleted_at` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`group_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `storedb2`.`products`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `storedb2`.`products` (
  `product_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `product_name` VARCHAR(100) CHARACTER SET 'utf8mb3' NOT NULL,
  `product_desc` TEXT NOT NULL,
  `product_price` DECIMAL(10,2) UNSIGNED NOT NULL,
  `vat` DECIMAL(5,4) UNSIGNED NOT NULL,
  `created_at` DATETIME NOT NULL,
  `deleted_at` DATETIME NULL DEFAULT NULL COMMENT 'if present the product is deleted',
  `available_units` INT UNSIGNED NOT NULL,
  `group_id` BIGINT UNSIGNED NOT NULL DEFAULT '1',
  `detailed_desc` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`product_id`),
  INDEX `fk_products_product_group1_idx` (`group_id` ASC) ,
  CONSTRAINT `fk_products_product_group1`
    FOREIGN KEY (`group_id`)
    REFERENCES `storedb2`.`product_group` (`group_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 36
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `storedb2`.`images`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `storedb2`.`images` (
  `image_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `image_path` VARCHAR(300) CHARACTER SET 'utf8mb3' NULL DEFAULT NULL,
  `product_id` BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (`image_id`),
  INDEX `fk_images_products1_idx` (`product_id` ASC) ,
  CONSTRAINT `fk_images_products1`
    FOREIGN KEY (`product_id`)
    REFERENCES `storedb2`.`products` (`product_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 78
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `storedb2`.`order_status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `storedb2`.`order_status` (
  `status_id` INT UNSIGNED NOT NULL,
  `status_name` VARCHAR(45) CHARACTER SET 'utf8mb3' NOT NULL COMMENT 'Нова->Потвърдена->Изпратена->Приключена/Анулирана',
  PRIMARY KEY (`status_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 6
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `storedb2`.`roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `storedb2`.`roles` (
  `role_id` INT UNSIGNED NOT NULL,
  `role_name` VARCHAR(100) CHARACTER SET 'utf8mb3' NOT NULL,
  PRIMARY KEY (`role_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `storedb2`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `storedb2`.`users` (
  `user_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(100) CHARACTER SET 'utf8mb3' NOT NULL COMMENT 'email is used as user name for login',
  `password` VARCHAR(200) CHARACTER SET 'utf8mb3' NOT NULL,
  `role_id` INT UNSIGNED NOT NULL COMMENT '1 - custoemr/ 2 - staff/ 3 - walk in',
  `first_name` VARCHAR(100) CHARACTER SET 'utf8mb3' NOT NULL,
  `last_name` VARCHAR(100) CHARACTER SET 'utf8mb3' NOT NULL,
  `registration_date` DATETIME NOT NULL,
  `phone` VARCHAR(20) CHARACTER SET 'utf8mb3' NOT NULL,
  `deleted_at` DATETIME NULL DEFAULT NULL COMMENT 'if present the user is deleted',
  `address` VARCHAR(300) NOT NULL DEFAULT '',
  PRIMARY KEY (`user_id`),
  INDEX `fk_users_user_type1_idx` (`role_id` ASC) ,
  CONSTRAINT `fk_users_user_type1`
    FOREIGN KEY (`role_id`)
    REFERENCES `storedb2`.`roles` (`role_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 70
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `storedb2`.`orders`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `storedb2`.`orders` (
  `order_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `customer_id` BIGINT UNSIGNED NOT NULL COMMENT 'Foreign Key from \'customers\'table',
  `order_date` DATETIME NOT NULL,
  `order_status` INT UNSIGNED NOT NULL COMMENT 'Foreign Key from \'orders\' table',
  PRIMARY KEY (`order_id`),
  INDEX `fk_orders_order_status1_idx` (`order_status` ASC) ,
  INDEX `fk_orders_users1_idx` (`customer_id` ASC) ,
  CONSTRAINT `fk_orders_order_status1`
    FOREIGN KEY (`order_status`)
    REFERENCES `storedb2`.`order_status` (`status_id`),
  CONSTRAINT `fk_orders_user_id`
    FOREIGN KEY (`customer_id`)
    REFERENCES `storedb2`.`users` (`user_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 82
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `storedb2`.`order_details`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `storedb2`.`order_details` (
  `order_details_id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `order_id` BIGINT UNSIGNED NOT NULL COMMENT 'Foreign Keys from \'orders\' table',
  `product_id` BIGINT UNSIGNED NOT NULL,
  `quantity` INT UNSIGNED NOT NULL,
  `price` DECIMAL(10,2) NOT NULL,
  `vat` DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (`order_details_id`),
  INDEX `fk_order_details_orders_idx` (`order_id` ASC) VISIBLE,
  INDEX `fk_order_details_products1_idx` (`product_id` ASC) ,
  CONSTRAINT `fk_order_details_order`
    FOREIGN KEY (`order_id`)
    REFERENCES `storedb2`.`orders` (`order_id`),
  CONSTRAINT `fk_order_details_products1`
    FOREIGN KEY (`product_id`)
    REFERENCES `storedb2`.`products` (`product_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 93
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
