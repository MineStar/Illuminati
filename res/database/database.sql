SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

CREATE SCHEMA IF NOT EXISTS `mc_illuminati` DEFAULT CHARACTER SET utf8 ;
USE `mc_illuminati` ;

-- -----------------------------------------------------
-- Table `mc_illuminati`.`stats`
-- -----------------------------------------------------
CREATE  TABLE IF NOT EXISTS `mc_illuminati`.`stats` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `player` VARCHAR(45) NOT NULL ,
  `loginGroup` INT NOT NULL ,
  `loginTime` DATETIME NOT NULL ,
  `logoutGroup` INT NOT NULL ,
  `logoutTime` DATETIME NOT NULL ,
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;


CREATE USER `mc_illuminati` IDENTIFIED BY 'lur4pqna';

grant CREATE on TABLE `mc_illuminati`.`stats` to mc_illuminati;
grant DELETE on TABLE `mc_illuminati`.`stats` to mc_illuminati;
grant INSERT on TABLE `mc_illuminati`.`stats` to mc_illuminati;
grant UPDATE on TABLE `mc_illuminati`.`stats` to mc_illuminati;
grant SELECT on TABLE `mc_illuminati`.`stats` to mc_illuminati;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
RENAME USER 'mc_illuminati'@'%' TO 'mc_illuminati'@'localhost';