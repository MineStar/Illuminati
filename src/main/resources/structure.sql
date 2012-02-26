CREATE  TABLE IF NOT EXISTS `stats` (
	`id` INT NOT NULL AUTO_INCREMENT ,
    `player` VARCHAR(45) NOT NULL ,
    `loginGroup` INT NOT NULL ,
    loginTime` DATETIME NOT NULL ,
    `logoutGroup` INT ,
    `logoutTime` DATETIME ,
    PRIMARY KEY (`id`) )
ENGINE = InnoDB;