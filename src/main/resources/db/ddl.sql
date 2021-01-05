CREATE TABLE `promotions` (
  `promotion_id` bigint NOT NULL,
  `edition_id` varchar(6) NOT NULL,
  `coupon_code` varchar(10) NOT NULL,
  `date_from` date NOT NULL,
  `date_to` date NOT NULL,
  `country_id` varchar(3) NOT NULL,
  `state_id` varchar(10) NOT NULL,
  `city_id` varchar(10) NOT NULL,
  `promotion_details` varchar(2000) DEFAULT NULL,
  `description` varchar(100) NOT NULL,
  `operation_date_time` datetime NOT NULL,
  `operator_id` varchar(10) NOT NULL,
  `record_in_use` varchar(1) NOT NULL,
  PRIMARY KEY (`promotion_id`)
);

CREATE TABLE `entitlements` (
   `entitlement_id` bigint(20) NOT NULL,
   `operation_date_time` datetime NOT NULL,
   `operator_id` varchar(10) NOT NULL,
   `record_in_use` varchar(1) NOT NULL,
   `role` varchar(20) NOT NULL,
   `page_name` varchar(50) NOT NULL,
   PRIMARY KEY (`entitlement_id`) 
);

CREATE TABLE `school_chain` (
  `school_chain_id` bigint(20) NOT NULL,
  `operation_date_time` datetime NOT NULL,
  `operator_id` varchar(10) NOT NULL,
  `record_in_use` varchar(1) NOT NULL,
  `name` varchar(99) NOT NULL,
  `country_id` varchar(50) NOT NULL,
  `state_id` varchar(50) NOT NULL,
  `city_id` varchar(50) NOT NULL,
  `presence` varchar(1) NOT NULL,
  `edu_board` varchar(3) NOT NULL,
  `edu_medium` varchar(3) NOT NULL,
  `gender_diversity` varchar(3) NOT NULL,
  `fee_structure` varchar(999) NOT NULL,
  `ownership` varchar(3) NOT NULL,
  `school_program` varchar(1) NOT NULL,
  `shift_details` varchar(999) NOT NULL,
  `student_residences` varchar(3) NOT NULL,
  `website` varchar(99) NOT NULL,
  `comments` varchar(999) NOT NULL,
   PRIMARY KEY (`school_chain_id`)
);

CREATE TABLE `institution_address` (
   `address_id` bigint(20) NOT NULL,
   `institution_type` varchar(1) NOT NULL,
   `institution_id` bigint(20) NOT NULL,
   `address_type` varchar(3) NOT NULL,
   `country_id` varchar(50) NOT NULL,
   `state_id` varchar(50) NOT NULL,
   `city_id` varchar(50) NOT NULL,
   `address` varchar(399) NOT NULL,
   `pin_code` varchar(10) NOT NULL,
   `landline_1` varchar(20) NOT NULL,
   `landline_2` varchar(20) NOT NULL,
   `landline_3` varchar(20) NOT NULL,
   `mobile_1` varchar(20) NOT NULL,
   `mobile_2` varchar(20) NOT NULL,
   `mobile_3` varchar(20) NOT NULL,
   `email_1` varchar(99) NOT NULL,
   `email_2` varchar(99) NOT NULL,
   `email_3` varchar(99) NOT NULL,
   `comments` varchar(999) NOT NULL,
   `operation_date_time` datetime NOT NULL,
   `record_in_use` varchar(1) NOT NULL,
    PRIMARY KEY (`address_id`)
);

CREATE TABLE `stakeholder` (
   `stakeholder_id` bigint(20) NOT NULL,
   `title` varchar(3) NOT NULL,
   `name` varchar(50) NOT NULL,
   `surname` varchar(50) NOT NULL,
   `other_names` varchar(99) NOT NULL,
   `designation` varchar(50) NOT NULL,
   `is_a_teacher` varchar(1) NOT NULL,
   `landline_2` varchar(20) NOT NULL,
   `landline_3` varchar(20) NOT NULL,
   `mobile_1` varchar(20) NOT NULL,
   `mobile_2` varchar(20) NOT NULL,
   `email_1` varchar(99) NOT NULL,
   `email_2` varchar(99) NOT NULL,
   `comments` varchar(999) NOT NULL,
   `operator_id` varchar(10) NOT NULL,
   `operation_date_time` datetime NOT NULL,
   `record_in_use` varchar(1) NOT NULL,
    PRIMARY KEY(`stakeholder_id`) 
); 

CREATE TABLE `insttution_stakeholders` (
   `stakeholder_id` bigint(20) NOT NULL,
   `institution_type` varchar(1) NOT NULL,
   `institution_id` bigint(20) NOT NULL,
   `comments` varchar(999) NOT NULL,
   `operator_id` varchar(10) NOT NULL,
   `operation_date_time` datetime NOT NULL,
   `record_in_use` varchar(1) NOT NULL,
    PRIMARY KEY(`stakeholder_id`)   
);


CREATE TABLE `school_subscription_grades` (
   `school_id` bigint(20) NOT NULL,
   `edition_id` varchar(6) NOT NULL,
   `grade` varchar(20) NOT NULL,
   `section` varchar(20) NOT NULL,
   `capacity` bigint(9999) NOT NULL,
   `operator_id`varchar(10) NOT NULL,
   `operation_date_time` datetime NOT NULL,
   `record_in_use` varchar(1) NOT NULL,
    PRIMARY KEY(`school_id`) 
);


CREATE TABLE `school_reports` (
   `stakeholder_id` bigint(20) NOT NULL,
   `school_id` bigint(20) NOT NULL,
   `edition_id` varchar(6),
   `grade` varchar(20),
   `section` varchar(20),
   `operator_id` varchar(10) NOT NULL,
   `operation_date_time` datetime NOT NULL,
   `record_in_use` varchar(1) NOT NULL,
    PRIMARY KEY(`stakeholder_id`)
);


   
   

