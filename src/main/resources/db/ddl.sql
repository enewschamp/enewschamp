drop table `school_chain`;
CREATE TABLE `school_chain` (
  `school_chain_id` bigint(20) NOT NULL,
  `operation_date_time` datetime NOT NULL,
  `operator_id` varchar(10) NOT NULL,
  `record_in_use` varchar(1) NOT NULL,
  `city_id` varchar(50) DEFAULT NULL,
  `comments` varchar(999) NOT NULL,
  `country_id` varchar(50) NOT NULL,
  `edu_board` varchar(3) DEFAULT NULL,
  `edu_medium` varchar(3) DEFAULT NULL,
  `fee_structure` varchar(999) DEFAULT NULL,
  `gender_diversity` varchar(3) DEFAULT NULL,
  `name` varchar(99) NOT NULL,
  `ownership` varchar(3) DEFAULT NULL,
  `presence` varchar(1) NOT NULL,
  `school_program` varchar(1) DEFAULT NULL,
  `shift_details` varchar(999) DEFAULT NULL,
  `state_id` varchar(255) DEFAULT NULL,
  `student_residences` varchar(3) DEFAULT NULL,
  `website` varchar(99) DEFAULT NULL,
  PRIMARY KEY (`school_chain_id`),
  UNIQUE KEY `UC_school_chain_01` (`name`,`country_id`)
);

drop table `institution_address`;
CREATE TABLE `institution_address` (
  `address_id` bigint(20) NOT NULL,
  `institution_type` varchar(1) NOT NULL,
  `institution_id` bigint(20) NOT NULL,
  `address_type` varchar(3) NOT NULL,
  `country_id` varchar(2) NOT NULL,
  `state_id` varchar(3) NOT NULL,
  `city_id` varchar(50) NOT NULL,
  `address` varchar(399) DEFAULT NULL,
  `pin_code` varchar(10) DEFAULT NULL,
  `landline_1` varchar(20) DEFAULT NULL,
  `landline_2` varchar(20) DEFAULT NULL,
  `landline_3` varchar(20) DEFAULT NULL,
  `mobile_1` varchar(20) DEFAULT NULL,
  `mobile_2` varchar(20) DEFAULT NULL,
  `mobile_3` varchar(20) DEFAULT NULL,
  `email_1` varchar(99) DEFAULT NULL,
  `email_2` varchar(99) DEFAULT NULL,
  `email_3` varchar(99) DEFAULT NULL,
  `comments` varchar(999) DEFAULT NULL,
  `operation_date_time` datetime NOT NULL,
  `record_in_use` varchar(1) NOT NULL,
  `operator_id` varchar(10) NOT NULL,
  PRIMARY KEY (`address_id`)
);

drop table `stake_holder`;
CREATE TABLE `stake_holder` (
  `stake_holder_id` bigint(20) NOT NULL,
  `title` varchar(3) DEFAULT NULL,
  `name` varchar(50) NOT NULL,
  `surname` varchar(50) NOT NULL,
  `other_names` varchar(99) DEFAULT NULL,
  `designation` varchar(50) DEFAULT NULL,
  `is_a_teacher` varchar(1) DEFAULT NULL,
  `land_line1` varchar(20) DEFAULT NULL,
  `land_line2` varchar(20) DEFAULT NULL,
  `mobile1` varchar(20) DEFAULT NULL,
  `mobile2` varchar(20) DEFAULT NULL,
  `email1` varchar(99) DEFAULT NULL,
  `email2` varchar(99) DEFAULT NULL,
  `comments` varchar(999) DEFAULT NULL,
  `operation_date_time` datetime NOT NULL,
  `operator_id` varchar(20) NOT NULL,
  `record_in_use` varchar(1) NOT NULL,
  PRIMARY KEY (`stake_holder_id`),
  UNIQUE KEY `UC_stake_holder_01` (`name`,`surname`)
);

drop table `institution_stakeholder`;
CREATE TABLE `institution_stakeholder` (
  `inst_stake_holder_id` bigint(20) NOT NULL,
  `stake_holder_id` bigint(20) NOT NULL,
  `institution_type` varchar(1) NOT NULL,
  `institution_id` bigint(20) NOT NULL,
  `comments` varchar(999) DEFAULT NULL,
  `operator_id` varchar(20) NOT NULL,
  `operation_date_time` datetime NOT NULL,
  `record_in_use` varchar(1) NOT NULL,
  PRIMARY KEY (`inst_stake_holder_id`),
  UNIQUE KEY `UC_inst_stake_holder_01` (`stake_holder_id`,`institution_id`,`institution_type`)
);
