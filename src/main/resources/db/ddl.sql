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
  `state_id` varchar(50) DEFAULT NULL,
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

drop table `school_subscription_grades`;
CREATE TABLE `school_subscription_grades` (
  `school_subs_grade_id` bigint(20) NOT NULL,
  `school_id` bigint(20) NOT NULL,
  `edition_id` varchar(6) NOT NULL,
  `grade` varchar(20) NOT NULL,
  `section` varchar(20) NOT NULL,
  `capacity` int(20) DEFAULT NULL,
  `operator_id` varchar(20) NOT NULL,
  `operation_date_time` datetime NOT NULL,
  `record_in_use` varchar(1) NOT NULL,  
  PRIMARY KEY (`school_subs_grade_id`),
  UNIQUE KEY `UC_inst_stake_holder_01` (`school_id`, `edition_id`, `grade`, `section`)
);

drop table `school_reports`;
CREATE TABLE `school_reports` (
  `school_reports_id` bigint(20) NOT NULL,
  `stakeholder_id` bigint(20) NOT NULL,
  `school_id` bigint(20) NOT NULL,
  `edition_id` varchar(6) NOT NULL,
  `grade` varchar(20) NOT NULL,
  `section` varchar(20) DEFAULT NULL,
  `operation_date_time` datetime NOT NULL,
  `operator_id` varchar(20) NOT NULL,
  `record_in_use` varchar(1) NOT NULL,
  PRIMARY KEY (`school_reports_id`),
  UNIQUE KEY `UC_school_reports_01` (`stakeholder_id`,`school_id`,`edition_id`,`grade`)
);

drop table `promotions`;
CREATE TABLE `promotions` (
  `promotion_id` bigint(20) NOT NULL,
  `operation_date_time` datetime NOT NULL,
  `operator_id` varchar(10) NOT NULL,
  `record_in_use` varchar(1) NOT NULL,
  `coupon_code` varchar(10) NOT NULL,
  `edition_id` varchar(6) NOT NULL,
  `date_from` date NOT NULL,
  `date_to` date NOT NULL,
  `country_id` varchar(2) NOT NULL,
  `state_id` varchar(50) NOT NULL,
  `city_id` varchar(50) NOT NULL,
  `promotion_details` varchar(2000) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`promotion_id`),
  UNIQUE KEY `UC_promotions_01` (`edition_id`,`date_From`,`country_id`,`state_id`, `city_id`)
);

drop table `entitlements`;
CREATE TABLE `entitlements` (
  `entitlement_id` bigint(20) NOT NULL,
  `operation_date_time` datetime NOT NULL,
  `operator_id` varchar(20) NOT NULL,
  `record_in_use` varchar(1) NOT NULL,
  `page_name` varchar(50) NOT NULL,
  `role` varchar(20) NOT NULL,
  `user_id` varchar(20) NOT NULL,
  PRIMARY KEY (`entitlement_id`),
  UNIQUE KEY `UC_entitlements_01` (`user_id`,`role`,`page_name`)
);

drop table `celebration`;
CREATE TABLE `celebration` (
  `celebration_id` bigint NOT NULL,
  `operation_date_time` datetime NOT NULL,
  `operator_id` varchar(20) NOT NULL,
  `record_in_use` varchar(1) NOT NULL,
  `audio_file_name` varchar(100) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `edition_id` varchar(6) NOT NULL,
  `image_name` varchar(100) DEFAULT NULL,
  `name_id` varchar(10) NOT NULL,
  `occasion` varchar(255) DEFAULT NULL,
  `reading_level` int NOT NULL,
  PRIMARY KEY (`celebration_id`),
  UNIQUE KEY `UC_celebration_01` (`edition_id`,`date`,`occasion`,`reading_level`)
);

ALTER TABLE page_navigator ADD UNIQUE (current_page, operation, action);
