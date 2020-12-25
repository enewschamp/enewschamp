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
