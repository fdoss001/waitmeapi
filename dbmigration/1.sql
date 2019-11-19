ALTER TABLE `firebirds`.`employee_settings` 
ADD COLUMN `employee_settings_current_location_id` INT(7) NULL DEFAULT NULL AFTER `employee_settings_default_pos_sub_module_id`;
