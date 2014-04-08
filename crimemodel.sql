SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `CrimeDatabase` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `CrimeDatabase` ;

-- -----------------------------------------------------
-- Table `CrimeDatabase`.`Category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CrimeDatabase`.`Category` (
  `CatName` VARCHAR(45) NOT NULL,
  `SuperCat` VARCHAR(45) NULL,
  PRIMARY KEY (`CatName`),
  INDEX `fk_Category_Category1_idx` (`SuperCat` ASC),
  CONSTRAINT `fk_Category_Category1`
    FOREIGN KEY (`SuperCat`)
    REFERENCES `CrimeDatabase`.`Category` (`CatName`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `CrimeDatabase`.`Address`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CrimeDatabase`.`Address` (
  `idAddress` INT NOT NULL AUTO_INCREMENT,
  `country` VARCHAR(45) NULL,
  `zipCode` INT NULL,
  `city` VARCHAR(45) NULL,
  `street` VARCHAR(45) NULL,
  `streetNo` INT NULL,
  PRIMARY KEY (`idAddress`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `CrimeDatabase`.`User`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CrimeDatabase`.`User` (
  `UserName` VARCHAR(45) NOT NULL,
  `password` VARCHAR(60) NOT NULL,
  PRIMARY KEY (`UserName`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `CrimeDatabase`.`Cases`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CrimeDatabase`.`Cases` (
  `idCase` INT NOT NULL AUTO_INCREMENT,
  `Title` VARCHAR(45) NULL,
  `Description` VARCHAR(160) NULL,
  `open` TINYINT(1) NULL,
  `date` DATE NULL,
  `time` TIME NULL,
  `CatName` VARCHAR(45) NOT NULL,
  `idAddress` INT NULL,
  `UserName` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idCase`),
  INDEX `fk_Cases_Category1_idx` (`CatName` ASC),
  INDEX `fk_Cases_Address1_idx` (`idAddress` ASC),
  INDEX `fk_Cases_User1_idx` (`UserName` ASC),
  CONSTRAINT `fk_Cases_Category1`
    FOREIGN KEY (`CatName`)
    REFERENCES `CrimeDatabase`.`Category` (`CatName`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Cases_Address1`
    FOREIGN KEY (`idAddress`)
    REFERENCES `CrimeDatabase`.`Address` (`idAddress`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Cases_User1`
    FOREIGN KEY (`UserName`)
    REFERENCES `CrimeDatabase`.`User` (`UserName`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `CrimeDatabase`.`PersonOfInterest`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CrimeDatabase`.`PersonOfInterest` (
  `idPersonOfInterest` INT NOT NULL AUTO_INCREMENT,
  `FirstName` VARCHAR(45) NULL,
  `LastName` VARCHAR(45) NULL,
  `DateOfBirth` DATE NULL,
  PRIMARY KEY (`idPersonOfInterest`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `CrimeDatabase`.`Conviction`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CrimeDatabase`.`Conviction` (
  `idConviction` INT NOT NULL AUTO_INCREMENT,
  `beginDate` DATE NULL,
  `endDate` DATE NULL,
  `idCase` INT NOT NULL,
  `idPersonOfInterest` INT NOT NULL,
  PRIMARY KEY (`idConviction`, `idCase`, `idPersonOfInterest`),
  INDEX `fk_Conviction_Cases1_idx` (`idCase` ASC),
  INDEX `fk_Conviction_PersonOfInterest1_idx` (`idPersonOfInterest` ASC),
  CONSTRAINT `fk_Conviction_Cases1`
    FOREIGN KEY (`idCase`)
    REFERENCES `CrimeDatabase`.`Cases` (`idCase`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Conviction_PersonOfInterest1`
    FOREIGN KEY (`idPersonOfInterest`)
    REFERENCES `CrimeDatabase`.`PersonOfInterest` (`idPersonOfInterest`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `CrimeDatabase`.`involved`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CrimeDatabase`.`involved` (
  `idPerson` INT NOT NULL,
  `idCase` INT NOT NULL,
  `role` VARCHAR(45) NULL,
  PRIMARY KEY (`idPerson`, `idCase`),
  INDEX `fk_PersonOfInterest_has_Cases_Cases1_idx` (`idCase` ASC),
  INDEX `fk_PersonOfInterest_has_Cases_PersonOfInterest_idx` (`idPerson` ASC),
  CONSTRAINT `fk_PersonOfInterest_has_Cases_PersonOfInterest`
    FOREIGN KEY (`idPerson`)
    REFERENCES `CrimeDatabase`.`PersonOfInterest` (`idPersonOfInterest`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_PersonOfInterest_has_Cases_Cases1`
    FOREIGN KEY (`idCase`)
    REFERENCES `CrimeDatabase`.`Cases` (`idCase`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `CrimeDatabase`.`NoteCase`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CrimeDatabase`.`NoteCase` (
  `idCase` INT NOT NULL,
  `Nr` INT NOT NULL AUTO_INCREMENT,
  `text` LONGTEXT NULL,
  `UserName` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`Nr`),
  INDEX `fk_NoteCase_User1_idx` (`UserName` ASC),
  CONSTRAINT `fk_Note_Cases1`
    FOREIGN KEY (`idCase`)
    REFERENCES `CrimeDatabase`.`Cases` (`idCase`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_NoteCase_User1`
    FOREIGN KEY (`UserName`)
    REFERENCES `CrimeDatabase`.`User` (`UserName`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `CrimeDatabase`.`NotePerson`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `CrimeDatabase`.`NotePerson` (
  `idPersonOfInterest` INT NOT NULL,
  `Nr` INT NOT NULL AUTO_INCREMENT,
  `text` LONGTEXT NULL,
  `UserName` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`Nr`),
  INDEX `fk_Note_copy1_PersonOfInterest1_idx` (`idPersonOfInterest` ASC),
  INDEX `fk_NotePerson_User1_idx` (`UserName` ASC),
  CONSTRAINT `fk_Note_copy1_PersonOfInterest1`
    FOREIGN KEY (`idPersonOfInterest`)
    REFERENCES `CrimeDatabase`.`PersonOfInterest` (`idPersonOfInterest`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_NotePerson_User1`
    FOREIGN KEY (`UserName`)
    REFERENCES `CrimeDatabase`.`User` (`UserName`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
