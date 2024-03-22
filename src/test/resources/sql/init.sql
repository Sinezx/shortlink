CREATE DATABASE IF NOT EXISTS `shortlink`;

CREATE TABLE `shortlink`.`callback_info` (
  `id` INT NOT NULL,
  `create_sn` VARCHAR(45) NOT NULL,
  `code` VARCHAR(45) NOT NULL,
  `content` VARCHAR(256) NULL,
  `callback_url` VARCHAR(45) NOT NULL,
  `create_time` DATETIME NOT NULL,
  `expire_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `create_sn_UNIQUE` (`create_sn` ASC) VISIBLE,
  UNIQUE INDEX `code_UNIQUE` (`code` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4;
