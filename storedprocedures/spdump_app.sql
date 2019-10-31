 
DROP procedure IF EXISTS `category_del`;
DELIMITER $$
CREATE PROCEDURE `category_del`(
IN cid SMALLINT(4))
BEGIN
	DELETE FROM category WHERE category_id=cid;
	DELETE FROM menu_category WHERE category_id=cid;
	DELETE FROM category_sub_category WHERE category_id=cid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `category_ins`;
DELIMITER $$
CREATE PROCEDURE `category_ins`(
IN co VARCHAR(45),
IN na VARCHAR(45),
IN ac TINYINT(1),
IN luuid INT(10))
BEGIN
	INSERT INTO category (category_code, category_name, category_active, category_lastupdate_user_id)
	VALUES (co, na, ac, luuid);
    SELECT LAST_INSERT_ID();
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `category_sel`;
DELIMITER $$
CREATE PROCEDURE `category_sel`(
IN cid SMALLINT(4))
BEGIN
	SELECT * FROM category
	WHERE category_id = cid
	ORDER BY category_name;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `category_sel_all_min`;
DELIMITER $$
CREATE PROCEDURE `category_sel_all_min`()
BEGIN
	SELECT category_id, category_code, category_name, category_active FROM category
	ORDER BY category_name;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `category_sub_category_del_all_category`;
DELIMITER $$
CREATE PROCEDURE `category_sub_category_del_all_category`(
IN cid SMALLINT(4))
BEGIN
	DELETE FROM category_sub_category WHERE category_id=cid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `category_sub_category_ins`;
DELIMITER $$
CREATE PROCEDURE `category_sub_category_ins`(
IN cid SMALLINT(4),
IN scid SMALLINT(4))
BEGIN
	INSERT INTO category_sub_category (category_id, sub_category_id)
	VALUES (cid, scid);
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `category_sub_category_sel`;
DELIMITER $$
CREATE PROCEDURE `category_sub_category_sel`(
IN cid SMALLINT(4))
BEGIN
	SELECT sc.* FROM sub_category sc
	INNER JOIN category_sub_category csc ON csc.sub_category_id = sc.sub_category_id
	WHERE csc.category_id=cid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `category_upd`;
DELIMITER $$
CREATE PROCEDURE `category_upd`(
IN cid SMALLINT(4),
IN co VARCHAR(45),
IN na VARCHAR(45),
IN ac TINYINT(1),
IN luuid INT(10))
BEGIN
	UPDATE category SET
	category_code = co,
	category_name = na,
	category_active = ac,
	category_lastupdate_dtm = CURRENT_TIMESTAMP,
	category_lastupdate_user_id = luuid
	WHERE category_id = cid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `employee_authorized_location_del_all_employee`;
DELIMITER $$
CREATE PROCEDURE `employee_authorized_location_del_all_employee`(
IN uid INT(10))
BEGIN    
	DELETE FROM employee_authorized_location
    WHERE employee_id = uid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `employee_authorized_location_ins`;
DELIMITER $$
CREATE PROCEDURE `employee_authorized_location_ins`(
IN uid INT(10),
IN lid INT(7))
BEGIN
	INSERT INTO employee_authorized_location (employee_id, location_id)
    VALUES (uid, lid);
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `employee_authorized_location_sel`;
DELIMITER $$
CREATE PROCEDURE `employee_authorized_location_sel`(
IN uid INT(10))
BEGIN
	SELECT l.location_id, l.location_name FROM location l
	INNER JOIN employee_authorized_location e ON e.location_id = l.location_id
	WHERE e.employee_id = uid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `employee_break_end`;
DELIMITER $$
CREATE PROCEDURE `employee_break_end`(
IN uid INT(10))
BEGIN    
    -- Check if there is a clock in that was never clocked out
    SET @dayid = 0;
	SET @lastout = '0000-00-00 00:00:00.0';
    SET @lastbreakin = '0000-00-00 00:00:00.0';
    SET @lastbreakout = '0000-00-00 00:00:00.0';
	SELECT ANY_VALUE(timesheet_day_id), ANY_VALUE(timesheet_day_dtm_out), ANY_VALUE(timesheet_day_break_start), ANY_VALUE(timesheet_day_break_end)
	INTO @dayid, @lastout, @lastbreakstart, @lastbreakend
    FROM timesheet_day
    WHERE timesheet_day_employee_id = uid
    GROUP BY timesheet_day_dtm_in
    ORDER BY timesheet_day_dtm_in DESC LIMIT 1;
    
    -- First verify that the day is open (clocked in without clock out)
    IF @lastout IS NOT NULL THEN
		SIGNAL SQLSTATE '45001'
        SET MESSAGE_TEXT = 'No open clock in exists to end break';
	END IF;
    
    -- Next verify that user has already started break
    IF @lastbreakstart IS NULL THEN
		SIGNAL SQLSTATE '45003'
        SET MESSAGE_TEXT = 'A start break does not exist for this day';
	END IF;
    
    -- Finally verify that user has not ended break yet
    IF @lastbreakend IS NOT NULL THEN
		SIGNAL SQLSTATE '45002'
        SET MESSAGE_TEXT = 'An end break already exists for this day';
	END IF;
    
    -- If everything was good above, update the latest break in entry
    UPDATE timesheet_day 
    SET timesheet_day_break_end = CURRENT_TIMESTAMP, timesheet_day_lastupdate_dtm = CURRENT_TIMESTAMP, timesheet_day_lastupdate_user_id = uid
    WHERE timesheet_day_id = @dayid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `employee_break_start`;
DELIMITER $$
CREATE PROCEDURE `employee_break_start`(
IN uid INT(10))
BEGIN    
    -- Check if there is a clock in that was never clocked out
    SET @dayid = 0;
	SET @lastout = '0000-00-00 00:00:00.0';
    SET @lastbreakin = '0000-00-00 00:00:00.0';
	SELECT ANY_VALUE(timesheet_day_id), ANY_VALUE(timesheet_day_dtm_out), ANY_VALUE(timesheet_day_break_start) 
	INTO @dayid, @lastout, @lastbreakstart
    FROM timesheet_day 
    WHERE timesheet_day_employee_id = uid
    GROUP BY timesheet_day_dtm_in
    ORDER BY timesheet_day_dtm_in DESC LIMIT 1;
    
    -- First verify that the day is open (clocked in without clock out)
    IF @lastout IS NOT NULL THEN
		SIGNAL SQLSTATE '45001'
        SET MESSAGE_TEXT = 'No open clock in exists to start break';
	END IF;
    
    -- Next verify that user has not breaked in yet
    IF @lastbreakstart IS NOT NULL THEN
		SIGNAL SQLSTATE '45002'
        SET MESSAGE_TEXT = 'A start break already exists for this day';
	END IF;
    
    -- If everything was good above, update the latest break in entry
    UPDATE timesheet_day 
    SET timesheet_day_break_start = CURRENT_TIMESTAMP, timesheet_day_lastupdate_dtm = CURRENT_TIMESTAMP, timesheet_day_lastupdate_user_id = uid
    WHERE timesheet_day_id = @dayid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `employee_clockedin_sel`;
DELIMITER $$
CREATE PROCEDURE `employee_clockedin_sel`(
IN lid INT(7))
BEGIN
	SELECT e.employee_id, e.employee_uname, e.employee_fname, e.employee_lname, p.position_role_location_id FROM employee e
	INNER JOIN position_role p ON p.position_role_id = e.employee_position_role_id
	WHERE p.position_role_location_id = lid AND p.position_role_active = TRUE AND e.employee_active = TRUE AND
	e.employee_id IN (SELECT timesheet_day_employee_id  FROM timesheet_day
					WHERE timesheet_day_dtm_in IS NOT NULL AND timesheet_day_dtm_out IS NULL);
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `employee_clockin`;
DELIMITER $$
CREATE PROCEDURE `employee_clockin`(
IN uid INT(10),
IN lid INT(7),
IN ws DATE,
IN dti TIMESTAMP)
BEGIN
    
    -- Check if there was a clock_out from last clock_in or if an entry exists
    SET @dayid = 0;
	SET @lastout = '0000-00-00 00:00:00.0';
    SELECT ANY_VALUE(timesheet_day_id), ANY_VALUE(timesheet_day_dtm_out) 
    INTO @dayid, @lastout 
    FROM timesheet_day 
    WHERE timesheet_day_employee_id = uid
    GROUP BY timesheet_day_dtm_in
    ORDER BY timesheet_day_dtm_in DESC LIMIT 1;
    
    IF @dayid IS NOT NULL AND @lastout IS NULL THEN
		SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'No clock out exists for latest clock in.';
	END IF;
    
    -- If everything was good above, make a new clock in entry
    INSERT INTO timesheet_day (timesheet_day_employee_id, timesheet_day_location_id, timesheet_day_weekstart, timesheet_day_dtm_in, timesheet_day_lastupdate_user_id)
    VALUES (uid, lid, ws, dti, uid);
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `employee_clockout`;
DELIMITER $$
CREATE PROCEDURE `employee_clockout`(
IN uid INT(10),
IN dto TIMESTAMP)
BEGIN    
    -- Check if there is a clock in that was never clocked out
    SET @dayid = 0;
	SET @lastout = '0000-00-00 00:00:00.0';
    SET @lastbreakin = '0000-00-00 00:00:00.0';
    SET @lastbreakout = '0000-00-00 00:00:00.0';
	SELECT timesheet_day_id, timesheet_day_dtm_out, timesheet_day_break_start, timesheet_day_break_end
	INTO @dayid, @lastout, @lastbreakstart, @lastbreakend
	FROM timesheet_day
	WHERE timesheet_day_employee_id = uid
	AND timesheet_day_dtm_out IS NULL
	ORDER BY timesheet_day_dtm_in DESC LIMIT 1;
    
    -- First, verify that an open day exists
    IF @lastout IS NOT NULL THEN
		SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'No clock in exists that was never clocked out.';
	END IF;
    
    /*Now we must check that if a start break exists, it was breaked out. 
    If it wasn't we must end the break.
    It's good practice to advise users to end break before clocking out*/
	IF @lastbreakin IS NOT NULL AND @lastbreakout IS NULL THEN
		UPDATE timesheet_day 
		SET timesheet_day_break_end = CURRENT_TIMESTAMP
		WHERE timesheet_day_id = @dayid;
	END IF;
    
    -- If everything was good above, update the latest clock entry
    UPDATE timesheet_day 
    SET timesheet_day_dtm_out = dto, timesheet_day_lastupdate_dtm = dto, timesheet_day_lastupdate_user_id = uid
    WHERE timesheet_day_id = @dayid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `employee_del`;
DELIMITER $$
CREATE PROCEDURE `employee_del`(
IN uid INT(10))
BEGIN
	SET @count = 0;
	
	-- Verify that the employee has no party history
	SELECT COUNT(*) INTO @count FROM party
	WHERE party_employee_id = uid;
	IF @count > 0 THEN
		SIGNAL SQLSTATE '45010'
		SET MESSAGE_TEXT = "This employee has party and order history.";
	END IF;
	
	SET @un = '';
	SELECT employee_uname INTO @un FROM employee WHERE employee_id = uid;
	
	DELETE FROM waitme.company_user
	WHERE company_user_uname = @un;
	
	DELETE FROM employee
	WHERE employee_id = uid;
	
	DELETE FROM employee_sub_module
	WHERE employee_id = uid;
	
	DELETE FROM timesheet_day
	WHERE timesheet_day_employee_id = uid;
	
	DELETE FROM employee_settings
	WHERE employee_settings_employee_id = uid;
	
	DELETE FROM employee_authorized_location
	WHERE employee_id = uid;
	
	DELETE FROM employee_permission
	WHERE employee_id = uid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `employee_ins`;
DELIMITER $$
CREATE PROCEDURE `employee_ins`(
IN cid INT(7),
IN un VARCHAR(45),
IN fn VARCHAR(45),
IN ln VARCHAR(45),
IN em VARCHAR(45),
IN ph VARCHAR(20),
IN ad TEXT,
IN pwd BINARY(64),
IN st BINARY(16),
IN p INT(10),
IN pid TINYINT(3),
IN pa DECIMAL(8,2),
IN luuid INT(10))
BEGIN
	SET @lastinsertid = 0;
	SET @count = 0;
	
	-- Verify that the username is globally unique
	SELECT COUNT(*) INTO @count FROM waitme.company_user WHERE company_user_uname = un;
	IF @count > 0 THEN
		SET @errortext = CONCAT("Duplicate entry '", un, "' for key 'uname_UNIQUE'");
		SIGNAL SQLSTATE '23000'
        SET MYSQL_ERRNO = 1062,
        MESSAGE_TEXT = @errortext;
	END IF;
	
	-- If it is, continue to insert in both tables
	INSERT INTO employee (employee_company_id, employee_uname, employee_fname, employee_lname, employee_email, employee_phone, employee_address, employee_passwd, employee_salt, employee_pin, employee_position_role_id, employee_pay_adjustment, employee_lastupdate_user_id)
	VALUES (cid, un, fn, ln, em, ph, ad, pwd, st, p, pid, pa,luuid);
    
	SELECT LAST_INSERT_ID() INTO @lastinsertid;
    
	INSERT INTO waitme.company_user (company_id, company_user_id, company_user_uname) VALUES (cid, @lastinsertid, un);
	
	SELECT @lastinsertid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `employee_loginout`;
DELIMITER $$
CREATE PROCEDURE `employee_loginout`(
IN uid INT(10),
IN li TINYINT(1))
BEGIN
	UPDATE employee SET employee_loggedin = li 
    WHERE employee_id = uid;
    IF li != 0 THEN
		UPDATE employee SET employee_lastlogin_dtm = CURRENT_TIMESTAMP
        WHERE employee_id = uid;
	END IF;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `employee_loginout_pos`;
DELIMITER $$
CREATE PROCEDURE `employee_loginout_pos`(
IN uid INT(10),
IN li TINYINT(1))
BEGIN
	
    -- Check if this user has ever been clocked in
    SET @clockcount = (SELECT COUNT(*) FROM timesheet_day
    WHERE timesheet_day_employee_id = uid);
    -- Check if there is a clock in that was never clocked out
	SET @lastout = (SELECT ANY_VALUE(timesheet_day_dtm_out) FROM timesheet_day 
    WHERE timesheet_day_employee_id = uid
    GROUP BY timesheet_day_dtm_in
    ORDER BY timesheet_day_dtm_in DESC LIMIT 1);
    
	IF (@lastout IS NOT NULL AND li = 1) OR @clockcount = 0 THEN
		SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'User is not clocked in';
	END IF; 
    
	UPDATE employee SET employee_loggedin_pos = li 
    WHERE employee_id = uid;
    
    IF li = 1 THEN
		UPDATE employee SET employee_lastlogin_pos_dtm = CURRENT_TIMESTAMP
        WHERE employee_id = uid;
	END IF;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `employee_module_sel`;
DELIMITER $$
CREATE PROCEDURE `employee_module_sel`(
IN eid INT(10))
BEGIN
	SELECT distinct(m.module_id), m.module_name FROM waitme.sub_module sm
	INNER JOIN employee_sub_module esm ON esm.sub_module_id = sm.sub_module_id
	INNER JOIN waitme.module m ON m.module_id = sm.sub_module_module_id
	WHERE esm.employee_id = eid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `employee_permission_del`;
DELIMITER $$
CREATE PROCEDURE `employee_permission_del`(
IN uid INT(10),
IN pid SMALLINT(4))
BEGIN
	DELETE FROM employee_permission
    WHERE permission_id = pid AND employee_id = uid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `employee_permission_del_all_employee`;
DELIMITER $$
CREATE PROCEDURE `employee_permission_del_all_employee`(
IN uid INT(10))
BEGIN
	DELETE FROM employee_permission
    WHERE employee_id = uid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `employee_permission_ins`;
DELIMITER $$
CREATE PROCEDURE `employee_permission_ins`(
IN uid INT(10),
IN pid SMALLINT(4))
BEGIN
	INSERT INTO employee_permission (employee_id, permission_id)
    VALUES (uid, pid);
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `employee_permission_sel`;
DELIMITER $$
CREATE PROCEDURE `employee_permission_sel`(
IN uid INT(10))
BEGIN
	SELECT * FROM waitme.permission p
	INNER JOIN employee_permission ep ON ep.permission_id = p.permission_id
    WHERE ep.employee_id = uid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `employee_sel`;
DELIMITER $$
CREATE PROCEDURE `employee_sel`(
IN un VARCHAR(45))
BEGIN
	SELECT e.*, es.*, pr.*, td.*, c.*, cs.*, l.location_id, l.location_name FROM employee e
	INNER JOIN employee_settings es ON e.employee_id = es.employee_settings_employee_id
	LEFT JOIN position_role pr ON pr.position_role_id = e.employee_position_role_id
	LEFT JOIN timesheet_day td ON td.timesheet_day_employee_id = e.employee_id AND td.timesheet_day_dtm_out IS NULL
	INNER JOIN waitme.company c ON c.company_id = e.employee_company_id
	INNER JOIN waitme.company_settings cs ON cs.company_id = e.employee_company_id
	LEFT JOIN location l ON l.location_id = pr.position_role_location_id
	WHERE e.employee_uname = un;
END$$
DELIMITER ;  
 
DROP procedure IF EXISTS `employee_sel_all_basic_location`;
DELIMITER $$
CREATE PROCEDURE `employee_sel_all_basic_location`(
IN lid INT(7))
BEGIN
	SELECT employee_id, employee_uname, employee_fname, employee_lname FROM employee e
	INNER JOIN position_role pr ON pr.position_role_id = e.employee_position_role_id
	WHERE pr.position_role_location_id = lid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `employee_sel_all_basic_no_position`;
DELIMITER $$
CREATE PROCEDURE `employee_sel_all_basic_no_position`()
BEGIN
	SELECT employee_id, employee_uname, employee_fname, employee_lname FROM employee
	WHERE employee_position_role_id = 0;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `employee_sel_all_pin_basic_location`;
DELIMITER $$
CREATE PROCEDURE `employee_sel_all_pin_basic_location`(
IN lid INT(7))
BEGIN
	SELECT employee_id, employee_pin FROM employee e
	INNER JOIN position_role pr ON pr.position_role_id = e.employee_position_role_id
	WHERE pr.position_role_location_id = lid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `employee_sel_all_pos`;
DELIMITER $$
CREATE PROCEDURE `employee_sel_all_pos`(
IN lid INT(7))
BEGIN
	SELECT e.*, es.*, pr.*, td.*, c.*, cs.*, l.location_id, l.location_name FROM employee e
	INNER JOIN employee_settings es ON e.employee_id = es.employee_settings_employee_id
	LEFT JOIN position_role pr ON pr.position_role_id = e.employee_position_role_id
	LEFT JOIN timesheet_day td ON td.timesheet_day_employee_id = e.employee_id AND td.timesheet_day_dtm_out IS NULL
	INNER JOIN waitme.company c ON c.company_id = e.employee_company_id
	INNER JOIN waitme.company_settings cs ON cs.company_id = e.employee_company_id
	LEFT JOIN location l ON l.location_id = pr.position_role_location_id;
END$$
DELIMITER ;  
 
DROP procedure IF EXISTS `employee_sel_id`;
DELIMITER $$
CREATE PROCEDURE `employee_sel_id`(
IN uid INT(10))
BEGIN
	SELECT e.*, es.*, pr.*, td.*, c.*, cs.*, l.location_id, l.location_name FROM employee e
	INNER JOIN employee_settings es ON e.employee_id = es.employee_settings_employee_id
	LEFT JOIN position_role pr ON pr.position_role_id = e.employee_position_role_id
	LEFT JOIN timesheet_day td ON td.timesheet_day_employee_id = e.employee_id AND td.timesheet_day_dtm_out IS NULL
	INNER JOIN waitme.company c ON c.company_id = e.employee_company_id
	INNER JOIN waitme.company_settings cs ON cs.company_id = e.employee_company_id
	LEFT JOIN location l ON l.location_id = pr.position_role_location_id
	WHERE e.employee_id = uid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `employee_sel_pin`;
DELIMITER $$
CREATE PROCEDURE `employee_sel_pin`(
IN p INT(10))
BEGIN
	SELECT e.*, es.*, pr.*, td.*, c.*, cs.*, l.location_id, l.location_name FROM employee e
	INNER JOIN employee_settings es ON e.employee_id = es.employee_settings_employee_id
	LEFT JOIN position_role pr ON pr.position_role_id = e.employee_position_role_id
	LEFT JOIN timesheet_day td ON td.timesheet_day_employee_id = e.employee_id AND td.timesheet_day_dtm_out IS NULL
	INNER JOIN waitme.company c ON c.company_id = e.employee_company_id
	INNER JOIN waitme.company_settings cs ON cs.company_id = e.employee_company_id
	LEFT JOIN location l ON l.location_id = pr.position_role_location_id
	WHERE e.employee_pin = p;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `employee_sel_pin_cnt`;
DELIMITER $$
CREATE PROCEDURE `employee_sel_pin_cnt`(
IN p INT(10))
BEGIN
	SELECT COUNT(*) FROM employee e
	WHERE employee_pin = p;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `employee_sel_pwd`;
DELIMITER $$
CREATE PROCEDURE `employee_sel_pwd`(
IN uid INT(10))
BEGIN
	SELECT employee_passwd, employee_salt FROM employee
	WHERE employee_id = uid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `employee_settings_ins`;
DELIMITER $$
CREATE PROCEDURE `employee_settings_ins`(
IN eid INT(10),
IN dpm SMALLINT(4))
BEGIN
	INSERT INTO employee_settings (employee_settings_employee_id, employee_settings_default_pos_sub_module_id) VALUES (eid, dpm);
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `employee_settings_sel`;
DELIMITER $$
CREATE PROCEDURE `employee_settings_sel`(
IN uid INT(10))
BEGIN
	SELECT * FROM employee_settings
	WHERE employee_settings_employee_id = uid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `employee_settings_upd`;
DELIMITER $$
CREATE PROCEDURE `employee_settings_upd`(
IN uid INT(10),
IN tp VARCHAR(255),
IN dpm SMALLINT(4))
BEGIN
	IF dpm < 0 THEN
		UPDATE employee_settings SET
		employee_settings_theme_path = tp,
		employee_settings_default_pos_sub_module_id = null
	    WHERE employee_settings_employee_id = uid;
    ELSE
		UPDATE employee_settings SET
		employee_settings_theme_path = tp,
		employee_settings_default_pos_sub_module_id = dpm
	    WHERE employee_settings_employee_id = uid;
    END IF;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `employee_sub_module_del_all_employee`;
DELIMITER $$
CREATE PROCEDURE `employee_sub_module_del_all_employee`(
IN eid INT(10))
BEGIN
	DELETE FROM employee_sub_module WHERE employee_id = eid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `employee_sub_module_ins`;
DELIMITER $$
CREATE PROCEDURE `employee_sub_module_ins`(
IN eid INT(10),
IN smid SMALLINT(4),
IN s TINYINT(1),
IN i TINYINT(1),
IN u TINYINT(1),
IN d TINYINT(1))
BEGIN
	INSERT INTO employee_sub_module (employee_id, sub_module_id, employee_sub_module_sel, employee_sub_module_ins, employee_sub_module_upd, employee_sub_module_del)
	VALUES (eid, smid, s, i, u ,d);
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `employee_sub_module_sel`;
DELIMITER $$
CREATE PROCEDURE `employee_sub_module_sel`(
IN eid INT(10),
IN mid SMALLINT(4))
BEGIN
	SELECT sm.*, esm.* FROM waitme.sub_module sm
	INNER JOIN employee_sub_module esm ON esm.sub_module_id = sm.sub_module_id
	WHERE esm.employee_id = eid AND sm.sub_module_module_id = mid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `employee_upd_basic`;
DELIMITER $$
CREATE PROCEDURE `employee_upd_basic`(
IN uid INT(10),
IN un VARCHAR(45),
IN fn VARCHAR(45),
IN ln VARCHAR(45),
IN em VARCHAR(45),
IN ph VARCHAR(20),
IN addr TEXT,
IN uuid INT(10))
BEGIN
	SET @count = 0;
	SET @old_uname = '';
	
	-- verify that this user is not updating their username
	SELECT employee_uname INTO @old_uname FROM employee WHERE employee_id = uid;
	
	IF @old_uname != un THEN
		-- Verify that the username is globally unique
		SELECT COUNT(*) INTO @count FROM waitme.company_user WHERE company_user_uname = un;
		IF @count > 0 THEN
			SET @errortext = CONCAT("Duplicate entry '", un, "' for key 'uname_UNIQUE'");
			SIGNAL SQLSTATE '23000'
	        SET MYSQL_ERRNO = 1062,
	        MESSAGE_TEXT = @errortext;
		END IF;
	END IF;
	
	UPDATE employee SET
	employee_uname = un,
    employee_fname = fn,
    employee_lname = ln,
    employee_email = em,
    employee_phone = ph,
    employee_address = addr,
    employee_lastupdate_dtm = CURRENT_TIMESTAMP,
    employee_lastupdate_user_id = uuid
    WHERE employee_id = uid;
    
    UPDATE waitme.company_user SET
    company_user_uname = un,
    company_user_lastupdate_dtm = CURRENT_TIMESTAMP
    WHERE company_user_uname = @old_uname;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `employee_upd_pin`;
DELIMITER $$
CREATE PROCEDURE `employee_upd_pin`(
IN uid INT(10),
IN p INT(10))
BEGIN
	UPDATE employee SET
    employee_pin = p,
    employee_lastupdate_dtm = CURRENT_TIMESTAMP
    WHERE employee_id = uid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `employee_upd_position`;
DELIMITER $$
CREATE PROCEDURE `employee_upd_position`(
IN uid INT(10),
IN pid TINYINT(3),
IN paj DECIMAL(8,2),
IN uuid INT(10))
BEGIN	
	UPDATE employee SET
	employee_position_role_id = pid,
	employee_pay_adjustment = paj,
    employee_lastupdate_dtm = CURRENT_TIMESTAMP,
    employee_lastupdate_user_id = uuid
    WHERE employee_id = uid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `employee_upd_pwd`;
DELIMITER $$
CREATE PROCEDURE `employee_upd_pwd`(
IN uid INT(10),
IN pwd BINARY(64),
IN st BINARY(16))
BEGIN
	UPDATE employee SET
    employee_passwd = pwd,
    employee_salt = st,
    employee_lastupdate_dtm = CURRENT_TIMESTAMP
    WHERE employee_id = uid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `guest_ins`;
DELIMITER $$
CREATE PROCEDURE `guest_ins`(
IN wuid INT(11),
IN pid INT(11),
IN fn VARCHAR(45),
IN ln VARCHAR(45))
BEGIN
	-- Verify that the username is globally unique
    IF wuid > 0 THEN
		SELECT COUNT(*) INTO @count FROM guest WHERE guest_waitme_user_id = wuid;
		IF @count > 0 THEN
			SET @errortext = CONCAT("Duplicate entry '", wuid, "' for key 'waitme_userid_UNIQUE'");
			SIGNAL SQLSTATE '23000'
			SET MYSQL_ERRNO = 1062,
			MESSAGE_TEXT = @errortext;
		END IF;
	END IF;
	
	
	-- insert the new guest
	INSERT INTO guest (guest_waitme_user_id, guest_party_id, guest_fname, guest_lname)
	VALUES (wuid, pid, fn, ln);
	
	SELECT LAST_INSERT_ID();
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `guest_sel`;
DELIMITER $$
CREATE PROCEDURE `guest_sel`(
IN gid INT(11)
)
BEGIN
	SELECT * FROM guest
	WHERE guest_id = gid;
END$$
DELIMITER ;  
 
DROP procedure IF EXISTS `guest_sel_party`;
DELIMITER $$
CREATE PROCEDURE `guest_sel_party`(
IN pid INT(11)
)
BEGIN
	SELECT * FROM guest
	WHERE guest_party_id = pid
	AND guest_active = TRUE;
END$$
DELIMITER ;  
 
DROP procedure IF EXISTS `item_del`;
DELIMITER $$
CREATE PROCEDURE `item_del`(
IN iid SMALLINT(4))
BEGIN
	SET @count = 0;
	
	-- Verify that the item has no order history
	SELECT COUNT(*) INTO @count FROM order_item
	WHERE item_id = iid;
	IF @count > 0 THEN
		SIGNAL SQLSTATE '45010'
		SET MESSAGE_TEXT = "This item exists in order history.";
	END IF;
	
	SET @nid = 0;
	SELECT item_nutrition_facts_id INTO @nid FROM item WHERE item_id = iid;
	DELETE FROM nutrition_facts WHERE nutrition_facts_id = @nid;
	
	DELETE FROM item WHERE item_id = iid;
	DELETE FROM meal_item WHERE item_id = iid;
	DELETE FROM meal_item_substitute WHERE item_id = iid;
	DELETE FROM item_item_option WHERE item_id = iid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `item_ins`;
DELIMITER $$
CREATE PROCEDURE `item_ins`(
IN co VARCHAR(45),
IN na VARCHAR(45),
IN ds TEXT,
IN nfid SMALLINT(4),
IN bp DECIMAL(8,2),
IN ac TINYINT(1),
IN luuid INT(10))
BEGIN
	INSERT INTO item (item_code, item_name, item_description, item_nutrition_facts_id, item_base_price, item_active, item_lastupdate_user_id)
	VALUES (co, na, ds, nfid, bp, ac, luuid);
    SELECT LAST_INSERT_ID();
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `item_item_option_del_all_item`;
DELIMITER $$
CREATE PROCEDURE `item_item_option_del_all_item`(
IN iid SMALLINT(4))
BEGIN
	DELETE FROM item_item_option WHERE item_id = iid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `item_item_option_ins`;
DELIMITER $$
CREATE PROCEDURE `item_item_option_ins`(
IN iid SMALLINT(4),
IN ioid SMALLINT(4),
IN pa DECIMAL(8,2),
IN qt ENUM('light', 'normal', 'extra'))
BEGIN
	IF pa IS NULL AND qt is NULL THEN
		INSERT INTO item_item_option (item_id, item_option_id)
		VALUES (iid, ioid);
	ELSEIF pa is NULL THEN
		INSERT INTO item_item_option (item_id, item_option_id, item_item_option_quantity)
		VALUES (iid, ioid, qt);
	ELSEIF qt is NULL THEN
		INSERT INTO item_item_option (item_id, item_option_id, item_item_option_price_adjustment)
		VALUES (iid, ioid, pa);
	ELSE
		INSERT INTO item_item_option (item_id, item_option_id, item_item_option_price_adjustment, item_item_option_quantity)
		VALUES (iid, ioid, pa, qt);
	END IF;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `item_item_option_sel`;
DELIMITER $$
CREATE PROCEDURE `item_item_option_sel`(
IN iid SMALLINT(4))
BEGIN
	SELECT io.*, ioi.*, n.* FROM item_option io
	INNER JOIN nutrition_facts n ON io.item_option_nutrition_facts_id = n.nutrition_facts_id
	INNER JOIN item_item_option ioi ON io.item_option_id = ioi.item_option_id
	WHERE ioi.item_id = iid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `item_item_option_sel_all_arr`;
DELIMITER $$
CREATE PROCEDURE `item_item_option_sel_all_arr`(
IN idarr VARCHAR(255))
BEGIN
	SELECT io.*, n.*, 'normal' AS item_item_option_quantity, 0.0 AS item_item_option_price_adjustment FROM item_option io
	INNER JOIN nutrition_facts n ON io.item_option_nutrition_facts_id = n.nutrition_facts_id
	WHERE FIND_IN_SET(io.item_option_id, idarr);
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `item_option_del`;
DELIMITER $$
CREATE PROCEDURE `item_option_del`(
IN ioid SMALLINT(4))
BEGIN
	SET @count = 0;
	
	-- Verify that the option has no order history
	SELECT COUNT(*) INTO @count FROM order_item_option
	WHERE item_option_id = ioid;
	IF @count > 0 THEN
		SIGNAL SQLSTATE '45010'
		SET MESSAGE_TEXT = "This option exists in order history.";
	END IF;
	
	SET @nid = 0;
	SELECT item_option_nutrition_facts_id INTO @nid FROM item_option WHERE item_option_id = ioid;
	DELETE FROM nutrition_facts WHERE nutrition_facts_id = @nid;
	
	DELETE FROM item_option WHERE item_option_id = ioid;
	DELETE FROM item_item_option WHERE item_option_id = ioid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `item_option_ins`;
DELIMITER $$
CREATE PROCEDURE `item_option_ins`(
IN co VARCHAR(45),
IN na VARCHAR(45),
IN ds TEXT,
IN nfid SMALLINT(4),
IN bp DECIMAL(8,2),
IN ac TINYINT(1),
IN luuid INT(10))
BEGIN
	INSERT INTO item_option (item_option_code, item_option_name, item_option_description, item_option_nutrition_facts_id, item_option_base_price, item_option_active, item_option_lastupdate_user_id)
	VALUES (co, na, ds, nfid, bp, ac, luuid);
    SELECT LAST_INSERT_ID();
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `item_option_sel`;
DELIMITER $$
CREATE PROCEDURE `item_option_sel`(
IN ioid SMALLINT(4))
BEGIN
	SELECT io.*, n.* FROM item_option io
	INNER JOIN nutrition_facts n ON io.item_option_nutrition_facts_id = n.nutrition_facts_id
	WHERE item_option_id = ioid
	ORDER BY item_option_name;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `item_option_sel_all_min`;
DELIMITER $$
CREATE PROCEDURE `item_option_sel_all_min`()
BEGIN
	SELECT item_option_id, item_option_code, item_option_name, item_option_active, item_option_base_price FROM item_option
	ORDER BY item_option_name;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `item_option_upd`;
DELIMITER $$
CREATE PROCEDURE `item_option_upd`(
IN ioid SMALLINT(4),
IN co VARCHAR(45),
IN na VARCHAR(45),
IN ds TEXT,
IN bp DECIMAL(8,2),
IN ac TINYINT(1),
IN luuid INT(10))
BEGIN
	UPDATE item_option SET
	item_option_code = co,
	item_option_name = na,
	item_option_description = ds,
	item_option_base_price = bp,
	item_option_active = ac,
	item_option_lastupdate_dtm = CURRENT_TIMESTAMP,
	item_option_lastupdate_user_id = luuid
	WHERE item_option_id = ioid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `item_sel`;
DELIMITER $$
CREATE PROCEDURE `item_sel`(
IN iid SMALLINT(4))
BEGIN
	SELECT i.*, n.* FROM item i
	INNER JOIN nutrition_facts n ON i.item_nutrition_facts_id = n.nutrition_facts_id
	WHERE item_id = iid
	ORDER BY item_name;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `item_sel_all_arr`;
DELIMITER $$
CREATE PROCEDURE `item_sel_all_arr`(
IN idarr VARCHAR(255))
BEGIN
	SELECT i.*, n.* FROM item i
	INNER JOIN nutrition_facts n ON i.item_nutrition_facts_id = n.nutrition_facts_id
	WHERE FIND_IN_SET(i.item_id, idarr);
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `item_sel_all_min`;
DELIMITER $$
CREATE PROCEDURE `item_sel_all_min`()
BEGIN
	SELECT item_id, item_code, item_name, item_active, item_base_price FROM item
	ORDER BY item_name;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `item_upd`;
DELIMITER $$
CREATE PROCEDURE `item_upd`(
IN iid SMALLINT(4),
IN co VARCHAR(45),
IN na VARCHAR(45),
IN ds TEXT,
IN bp DECIMAL(8,2),
IN ac TINYINT(1),
IN luuid INT(10))
BEGIN
	UPDATE item SET
	item_code = co,
	item_name = na,
	item_description = ds,
	item_base_price = bp, 
	item_active = ac,
	item_lastupdate_dtm = CURRENT_TIMESTAMP,
	item_lastupdate_user_id = luuid
	WHERE item_id = iid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `location_del`;
DELIMITER $$
CREATE PROCEDURE `location_del`(
IN lid INT(7))
BEGIN
	SET @count = 0;
	
	-- Verify that the location has no party history
	SELECT COUNT(*) INTO @count FROM party p
	INNER JOIN table_restaurant tr ON p.party_table_restaurant_id = tr.table_restaurant_id
	INNER JOIN location l ON tr.table_restaurant_location_id = l.location_id
	WHERE l.location_id = lid;
	IF @count > 0 THEN
		SIGNAL SQLSTATE '45010'
		SET MESSAGE_TEXT = "This location has party and order history.";
	END IF;
	
	-- Verify that the location has no positions assigned
	SELECT COUNT(*) INTO @count FROM position_role 
	WHERE position_role_location_id = lid;
	IF @count > 0 THEN
		SIGNAL SQLSTATE '45010'
		SET MESSAGE_TEXT = "This location has positions assigned.";
	END IF;
	
	-- Verify that the location has no timsheet history
	SELECT COUNT(*) INTO @count FROM timesheet_day
	WHERE timesheet_day_location_id = lid;
	IF @count > 0 THEN
		SIGNAL SQLSTATE '45010'
		SET MESSAGE_TEXT = "This location has timesheet history.";
	END IF;
	
	DELETE FROM location
	WHERE location_id = lid;
	
	DELETE FROM employee_authorized_location
	WHERE location_id = lid;
	
	DELETE FROM menu
	WHERE menu_location_id = lid;
	
	DELETE FROM table_restaurant
	WHERE table_restaurant_location_id = lid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `location_ins`;
DELIMITER $$
CREATE PROCEDURE `location_ins`(
IN na VARCHAR(45),
IN ty ENUM('casual_dining'),
IN ad TEXT,
IN ph VARCHAR(20),
IN dto VARCHAR(153),
IN uid INT(10))
BEGIN
	INSERT INTO location (location_name, location_type, location_address, location_phone, location_dtms_open, location_lastupdate_user_id)
	VALUES (na, ty, ad, ph, dto, uid);
    SELECT LAST_INSERT_ID();
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `location_sel`;
DELIMITER $$
CREATE PROCEDURE `location_sel`(
IN lid INT(7))
BEGIN
	SELECT * FROM location
	WHERE location_id = lid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `location_sel_all`;
DELIMITER $$
CREATE PROCEDURE `location_sel_all`()
BEGIN
	SELECT * FROM location;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `location_sel_all_minimal`;
DELIMITER $$
CREATE PROCEDURE `location_sel_all_minimal`()
BEGIN
	SELECT location_id, location_name FROM location;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `location_sel_minimal`;
DELIMITER $$
CREATE PROCEDURE `location_sel_minimal`(
IN lid INT(7))
BEGIN
	SELECT location_id, location_name FROM location
	WHERE location_id = lid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `location_upd`;
DELIMITER $$
CREATE PROCEDURE `location_upd`(
IN lid INT(7),
IN na VARCHAR(45),
IN ty ENUM('casual_dining'),
IN ad TEXT,
IN ph VARCHAR(20),
IN dto VARCHAR(153),
IN uid INT(10))
BEGIN
	UPDATE location SET
	location_name = na,
	location_type = ty,
	location_address = ad,
	location_phone = ph,
	location_dtms_open = dto,
	location_lastupdate_user_id = uid,
	location_lastupdate_dtm = CURRENT_TIMESTAMP
	WHERE location_id = lid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `meal_del`;
DELIMITER $$
CREATE PROCEDURE `meal_del`(
IN mid SMALLINT(4))
BEGIN
	DELETE FROM meal WHERE meal_id = mid;
	DELETE FROM sub_category_meal WHERE meal_id = mid;
	DELETE FROM meal_item WHERE meal_id = mid;
	DELETE FROM meal_item_substitute WHERE meal_id = mid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `meal_ins`;
DELIMITER $$
CREATE PROCEDURE `meal_ins`(
IN co VARCHAR(45),
IN na VARCHAR(45),
IN ac TINYINT(1),
IN luuid INT(10))
BEGIN
	INSERT INTO meal (meal_code, meal_name, meal_active, meal_lastupdate_user_id)
	VALUES (co, na, ac, luuid);
    SELECT LAST_INSERT_ID();
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `meal_item_del_all_meal`;
DELIMITER $$
CREATE PROCEDURE `meal_item_del_all_meal`(
IN mid SMALLINT(4))
BEGIN
	DELETE FROM meal_item WHERE meal_id = mid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `meal_item_ins`;
DELIMITER $$
CREATE PROCEDURE `meal_item_ins`(
IN mid SMALLINT(4),
IN iid SMALLINT(4),
IN pa DECIMAL(8,2))
BEGIN
	IF pa IS NULL THEN
		INSERT INTO meal_item (meal_id, item_id)
		VALUES (mid, iid);
	ELSE
		INSERT INTO meal_item (meal_id, item_id, meal_item_price_adjustment)
		VALUES (mid, iid, pa);
	END IF;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `meal_item_sel`;
DELIMITER $$
CREATE PROCEDURE `meal_item_sel`(
IN mid SMALLINT(4))
BEGIN
	SELECT i.*, n.*, mi.* FROM item i
	INNER JOIN nutrition_facts n ON n.nutrition_facts_id = i.item_nutrition_facts_id
	INNER JOIN meal_item mi ON mi.item_id = i.item_id
	WHERE mi.meal_id = mid;
END$$
DELIMITER ;  
 
DROP procedure IF EXISTS `meal_item_sel_all_arr`;
DELIMITER $$
CREATE PROCEDURE `meal_item_sel_all_arr`(
IN idarr VARCHAR(255))
BEGIN
	SELECT i.*, n.*, 0.0 AS meal_item_price_adjustment FROM item i
	INNER JOIN nutrition_facts n ON i.item_nutrition_facts_id = n.nutrition_facts_id
	WHERE FIND_IN_SET(i.item_id, idarr);
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `meal_item_substitute_del_all_meal`;
DELIMITER $$
CREATE PROCEDURE `meal_item_substitute_del_all_meal`(
IN mid SMALLINT(4))
BEGIN
	DELETE FROM meal_item_substitute WHERE meal_id = mid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `meal_item_substitute_del_all_meal_item`;
DELIMITER $$
CREATE PROCEDURE `meal_item_substitute_del_all_meal_item`(
IN mid SMALLINT(4),
IN iid SMALLINT(4))
BEGIN
	DELETE FROM meal_item_substitute WHERE meal_id = mid AND item_id = iid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `meal_item_substitute_ins`;
DELIMITER $$
CREATE PROCEDURE `meal_item_substitute_ins`(
IN mid SMALLINT(4),
IN iid SMALLINT(4),
IN sid SMALLINT(4),
IN pa DECIMAL(8,2))
BEGIN
	INSERT INTO meal_item_substitute (meal_id, item_id, meal_item_substitute_id, meal_item_price_adjustment)
	VALUES (mid, iid, sid, pa);
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `meal_item_substitute_sel`;
DELIMITER $$
CREATE PROCEDURE `meal_item_substitute_sel`(
IN mid SMALLINT(4),
IN iid SMALLINT(4))
BEGIN
	SELECT i.*, n.*, mis.* FROM item i
	INNER JOIN nutrition_facts n ON i.item_nutrition_facts_id = n.nutrition_facts_id
	INNER JOIN meal_item_substitute mis ON mis.meal_item_substitute_id = i.item_id
	WHERE mis.meal_id = mid AND mis.item_id = iid;
END$$
DELIMITER ;  
 
DROP procedure IF EXISTS `meal_sel`;
DELIMITER $$
CREATE PROCEDURE `meal_sel`(
IN mid SMALLINT(4))
BEGIN
	SELECT * FROM meal
	WHERE meal_id = mid
	ORDER BY meal_name;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `meal_sel_all_min`;
DELIMITER $$
CREATE PROCEDURE `meal_sel_all_min`()
BEGIN
	SELECT meal_id, meal_code, meal_name, meal_active FROM meal
	ORDER BY meal_name;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `meal_upd`;
DELIMITER $$
CREATE PROCEDURE `meal_upd`(
IN mid SMALLINT(4),
IN co VARCHAR(45),
IN na VARCHAR(45),
IN ac TINYINT(1),
IN luuid INT(10))
BEGIN
	UPDATE meal SET
	meal_code = co,
	meal_name = na,
	meal_active = ac,
	meal_lastupdate_dtm = CURRENT_TIMESTAMP,
	meal_lastupdate_user_id = luuid
	WHERE meal_id = mid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `menu_category_del_all_menu`;
DELIMITER $$
CREATE PROCEDURE `menu_category_del_all_menu`(
IN mid SMALLINT(4))
BEGIN
	DELETE FROM menu_category WHERE menu_id = mid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `menu_category_ins`;
DELIMITER $$
CREATE PROCEDURE `menu_category_ins`(
IN mid SMALLINT(4),
IN cid SMALLINT(4))
BEGIN
	INSERT INTO menu_category (menu_id, category_id)
	VALUES (mid, cid);
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `menu_category_sel`;
DELIMITER $$
CREATE PROCEDURE `menu_category_sel`(
IN mid SMALLINT(4))
BEGIN
	SELECT c.* FROM category c
	INNER JOIN menu_category mc ON mc.category_id = c.category_id
	WHERE mc.menu_id = mid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `menu_del`;
DELIMITER $$
CREATE PROCEDURE `menu_del`(
IN mid SMALLINT(4))
BEGIN
	DELETE FROM menu WHERE menu_id = mid;
	DELETE FROM menu_category WHERE menu_id = mid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `menu_ins`;
DELIMITER $$
CREATE PROCEDURE `menu_ins`(
IN co VARCHAR(45),
IN na VARCHAR(45),
IN ac TINYINT(1),
IN dta VARCHAR(153),
IN lid INT(7),
IN luuid INT(10))
BEGIN
	INSERT INTO menu (menu_code, menu_name, menu_active, menu_dtms_available, menu_location_id, menu_lastupdate_user_id)
	VALUES (co, na, ac, dta, lid, luuid);
    SELECT LAST_INSERT_ID();
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `menu_sel`;
DELIMITER $$
CREATE PROCEDURE `menu_sel`(
IN mid INT(7))
BEGIN
	SELECT * FROM menu WHERE menu_id = mid
	ORDER BY menu_name;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `menu_sel_min_location`;
DELIMITER $$
CREATE PROCEDURE `menu_sel_min_location`(
IN lid INT(7))
BEGIN
	SELECT menu_id, menu_code, menu_name, menu_active FROM menu WHERE menu_location_id = lid
	ORDER BY menu_name;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `menu_upd`;
DELIMITER $$
CREATE PROCEDURE `menu_upd`(
IN mid SMALLINT(4),
IN co VARCHAR(45),
IN na VARCHAR(45),
IN ac TINYINT(1),
IN dta VARCHAR(153),
IN lid INT(7),
IN luuid INT(10))
BEGIN
	UPDATE menu SET
	menu_code = co,
	menu_name = na,
	menu_active = ac,
	menu_dtms_available = dta, 
	menu_location_id = lid,
	menu_lastupdate_dtm = CURRENT_TIMESTAMP,
	menu_lastupdate_user_id = luuid
	WHERE menu_id = mid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `nutrition_facts_del`;
DELIMITER $$
CREATE PROCEDURE `nutrition_facts_del`(
IN nfid SMALLINT(4))
BEGIN
	DELETE FROM nutrition_facts WHERE nutrition_facts_id = nfid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `nutrition_facts_ins`;
DELIMITER $$
CREATE PROCEDURE `nutrition_facts_ins`(
IN pserving_size VARCHAR(45),
IN pservings SMALLINT(4),
IN pcalories SMALLINT(4),
IN pfat_calories SMALLINT(4),
IN ptotal_fat SMALLINT(4),
IN psaturated_fat SMALLINT(4),
IN ptrans_fat SMALLINT(4),
IN pcholesterol SMALLINT(8),
IN psodium SMALLINT(8),
IN ppotassium SMALLINT(8),
IN ptotal_carb SMALLINT(4),
IN pdietary_fiber SMALLINT(4),
IN psugars SMALLINT(4),
IN psugar_alcohols SMALLINT(4),
IN pprotein SMALLINT(4),
IN pvitaminA SMALLINT(3),
IN pvitaminB6 SMALLINT(3),
IN pvitaminB12 SMALLINT(3),
IN pvitaminC SMALLINT(3),
IN pvitaminE SMALLINT(3),
IN pvitaminK SMALLINT(3),
IN pfolate SMALLINT(3),
IN pmagnesium SMALLINT(3),
IN pthiamin SMALLINT(3),
IN pzinc SMALLINT(3),
IN pcalcium SMALLINT(3),
IN priboflavin SMALLINT(3),
IN pbiotin SMALLINT(3),
IN piron SMALLINT(3),
IN pniacin SMALLINT(3),
IN ppantothenic_acid SMALLINT(3),
IN pphosphorus SMALLINT(3))
BEGIN
	INSERT INTO nutrition_facts (nutrition_facts_serving_size, nutrition_facts_servings, nutrition_facts_calories, nutrition_facts_fat_calories, nutrition_facts_total_fat, nutrition_facts_saturated_fat, nutrition_facts_trans_fat, nutrition_facts_cholesterol, nutrition_facts_sodium, nutrition_facts_potassium, nutrition_facts_total_carb, nutrition_facts_dietary_fiber, nutrition_facts_sugars, nutrition_facts_sugar_alcohols, nutrition_facts_protein, nutrition_facts_vitaminA, nutrition_facts_vitaminB6, nutrition_facts_vitaminB12, nutrition_facts_vitaminC, nutrition_facts_vitaminE, nutrition_facts_vitaminK, nutrition_facts_folate, nutrition_facts_magnesium, nutrition_facts_thiamin, nutrition_facts_zinc, nutrition_facts_calcium, nutrition_facts_riboflavin, nutrition_facts_biotin, nutrition_facts_iron, nutrition_facts_niacin, nutrition_facts_pantothenic_acid, nutrition_facts_phosphorus)
	VALUES (pserving_size, pservings, pcalories, pfat_calories, ptotal_fat, psaturated_fat, ptrans_fat, pcholesterol, psodium, ppotassium, ptotal_carb, pdietary_fiber, psugars, psugar_alcohols, pprotein, pvitaminA, pvitaminB6, pvitaminB12, pvitaminC, pvitaminE, pvitaminK, pfolate, pmagnesium, pthiamin, pzinc, pcalcium, priboflavin, pbiotin, piron, pniacin, ppantothenic_acid, pphosphorus);
	SELECT LAST_INSERT_ID();	
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `nutrition_facts_sel`;
DELIMITER $$
CREATE PROCEDURE `nutrition_facts_sel`(
IN nfid SMALLINT(4))
BEGIN
	SELECT * FROM nutrition_facts
	WHERE nutrition_facts_id = nfid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `nutrition_facts_upd`;
DELIMITER $$
CREATE PROCEDURE `nutrition_facts_upd`(
IN nfid SMALLINT(4),
IN pserving_size VARCHAR(45),
IN pservings SMALLINT(4),
IN pcalories SMALLINT(4),
IN pfat_calories SMALLINT(4),
IN ptotal_fat SMALLINT(4),
IN psaturated_fat SMALLINT(4),
IN ptrans_fat SMALLINT(4),
IN pcholesterol SMALLINT(8),
IN psodium SMALLINT(8),
IN ppotassium SMALLINT(8),
IN ptotal_carb SMALLINT(4),
IN pdietary_fiber SMALLINT(4),
IN psugars SMALLINT(4),
IN psugar_alcohols SMALLINT(4),
IN pprotein SMALLINT(4),
IN pvitaminA SMALLINT(3),
IN pvitaminB6 SMALLINT(3),
IN pvitaminB12 SMALLINT(3),
IN pvitaminC SMALLINT(3),
IN pvitaminE SMALLINT(3),
IN pvitaminK SMALLINT(3),
IN pfolate SMALLINT(3),
IN pmagnesium SMALLINT(3),
IN pthiamin SMALLINT(3),
IN pzinc SMALLINT(3),
IN pcalcium SMALLINT(3),
IN priboflavin SMALLINT(3),
IN pbiotin SMALLINT(3),
IN piron SMALLINT(3),
IN pniacin SMALLINT(3),
IN ppantothenic_acid SMALLINT(3),
IN pphosphorus SMALLINT(3))
BEGIN
	UPDATE nutrition_facts SET
	nutrition_facts_serving_size = pserving_size,
	nutrition_facts_servings = pservings,
	nutrition_facts_calories = pcalories,
	nutrition_facts_fat_calories = pfat_calories,
	nutrition_facts_total_fat = ptotal_fat,
	nutrition_facts_saturated_fat = psaturated_fat,
	nutrition_facts_trans_fat = ptrans_fat,
	nutrition_facts_cholesterol = pcholesterol,
	nutrition_facts_sodium = psodium,
	nutrition_facts_potassium = ppotassium,
	nutrition_facts_total_carb = ptotal_carb,
	nutrition_facts_dietary_fiber = pdietary_fiber,
	nutrition_facts_sugars = psugars,
	nutrition_facts_sugar_alcohols = psugar_alcohols,
	nutrition_facts_protein = pprotein,
	nutrition_facts_vitaminA = pvitaminA,
	nutrition_facts_vitaminB6 = pvitaminB6,
	nutrition_facts_vitaminB12 = pvitaminB12,
	nutrition_facts_vitaminC = pvitaminC,
	nutrition_facts_vitaminE = pvitaminE,
	nutrition_facts_vitaminK = pvitaminK,
	nutrition_facts_folate = pfolate,
	nutrition_facts_magnesium = pmagnesium,
	nutrition_facts_thiamin = pthiamin,
	nutrition_facts_zinc = pzinc,
	nutrition_facts_calcium = pcalcium,
	nutrition_facts_riboflavin = priboflavin,
	nutrition_facts_biotin = pbiotin,
	nutrition_facts_iron = piron,
	nutrition_facts_niacin = pniacin,
	nutrition_facts_pantothenic_acid = ppantothenic_acid,
	nutrition_facts_phosphorus = pphosphorus
	WHERE nutrition_facts_id = nfid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `order_closed_ins`;
DELIMITER $$
CREATE PROCEDURE `order_closed_ins`(
IN oid INT(11),
IN pay TINYINT(1),
IN pm ENUM('cash', 'credit', 'debit', 'giftcard', 'check', 'other'),
IN pamt DECIMAL(8,2),
IN luuid INT(10),
IN vd TINYINT(1),
IN vauid INT(10),
IN rsn VARCHAR(255))
BEGIN 
	-- get the extra needed values
	SELECT g.guest_party_id, g.guest_waitme_user_id, o.order_open_creation_dtm
	INTO @pid, @wmuid, @cdtm FROM order_open o
	INNER JOIN guest g ON o.order_open_guest_id = g.guest_id
	WHERE o.order_open_id = oid;
	
	-- Now we insert the order into the order_closed table
	INSERT INTO order_closed (order_closed_id, order_closed_waitme_user_id, order_closed_party_id, order_closed_paid, order_closed_pay_method, order_closed_pay_amount, order_closed_lastupdate_user_id, order_closed_opened_dtm, order_closed_voided, order_closed_void_authorized_user_id, order_closed_void_reason)
	VALUES (oid, @wmuid, @pid, pay, pm, pamt, luuid, @cdtm, vd, vauid, rsn);

	-- Now we delete the order from open orders and order employee
	DELETE FROM order_open
	WHERE order_open_id = oid;
	
	-- update the order items to show they were closed today
	UPDATE order_item SET order_item_lastupdate_dtm = CURRENT_TIMESTAMP
	WHERE order_id = oid;
	
	UPDATE order_item_option SET order_item_option_lastupdate_dtm = CURRENT_TIMESTAMP
	WHERE order_item_id IN (SELECT order_item_id FROM order_item WHERE order_id = oid);
    
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `order_item_ins`;
DELIMITER $$
CREATE PROCEDURE `order_item_ins`(
IN oid INT(11),
IN iid SMALLINT(4))
BEGIN
	INSERT INTO order_item (order_id, item_id)
	VALUES (oid, iid);
    SELECT LAST_INSERT_ID();
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `order_item_item_sel`;
DELIMITER $$
CREATE PROCEDURE `order_item_item_sel`(
IN oid INT(11))
BEGIN
	SELECT i.*, n.*, oi.* FROM item i
	INNER JOIN nutrition_facts n ON i.item_nutrition_facts_id = n.nutrition_facts_id
	INNER JOIN order_item oi ON i.item_id = oi.item_id
	WHERE oi.order_id = oid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `order_item_option_ins`;
DELIMITER $$
CREATE PROCEDURE `order_item_option_ins`(
IN oiid INT(11),
IN ioid SMALLINT(4))
BEGIN
	INSERT INTO order_item_option (order_item_id, item_option_id)
	VALUES (oiid, ioid);
	
	SELECT LAST_INSERT_ID();
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `order_item_option_item_option_sel`;
DELIMITER $$
CREATE PROCEDURE `order_item_option_item_option_sel`(
IN oiid INT(11))
BEGIN
	SELECT io.*, oio.*, n.* FROM item_option io
	INNER JOIN order_item_option oio ON io.item_option_id = oio.item_option_id
	INNER JOIN nutrition_facts n ON io.item_option_nutrition_facts_id = n.nutrition_facts_id
	WHERE oio.order_item_id = oiid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `order_open_ins`;
DELIMITER $$
CREATE PROCEDURE `order_open_ins`(
IN gid INT(11),
IN luuid INT(10))
BEGIN	
	-- insert the new guest
	INSERT INTO order_open (order_open_guest_id, order_open_lastupdate_user_id)
	VALUES (gid, luuid);
	
	SELECT LAST_INSERT_ID();
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `order_open_sel_guest`;
DELIMITER $$
CREATE PROCEDURE `order_open_sel_guest`(
IN gid INT(11))
BEGIN
	SELECT * FROM order_open
	WHERE order_open_guest_id = gid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `party_active_sel_employee`;
DELIMITER $$
CREATE PROCEDURE `party_active_sel_employee`(
IN eid INT(10)
)
BEGIN
	SELECT * FROM party WHERE party_active = TRUE
	AND party_employee_id = eid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `party_active_sel_location`;
DELIMITER $$
CREATE PROCEDURE `party_active_sel_location`(
IN lid INT(7)
)
BEGIN
	SELECT * FROM party
	WHERE party_active = TRUE AND party_table_restaurant_id IN (
		SELECT table_restaurant_id FROM table_restaurant
		WHERE table_restaurant_location_id = lid);
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `party_active_sel_table_restaurant`;
DELIMITER $$
CREATE PROCEDURE `party_active_sel_table_restaurant`(
IN tid INT(11)
)
BEGIN
	SELECT * FROM party WHERE party_table_restaurant_id = tid
	AND party_active = TRUE;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `party_ins`;
DELIMITER $$
CREATE PROCEDURE `party_ins`(
IN tid INT(10),
IN eid INT(10),
IN luuid INT(10))
BEGIN
	-- Verify that an active party is not in the new table
	SELECT COUNT(*) INTO @count FROM party WHERE party_table_restaurant_id = tid AND party_active = TRUE;
	IF @count > 0 THEN
		SET @errortext = CONCAT("Duplicate entry '", tid, "' for key 'table_restaurant_id_UNIQUE'");
		SIGNAL SQLSTATE '23000'
		SET MYSQL_ERRNO = 1062,
		MESSAGE_TEXT = @errortext;
	END IF;
	
	-- insert the new party
	INSERT INTO party (party_table_restaurant_id, party_employee_id, party_lastupdate_user_id)
	VALUES (tid, eid, luuid);
    
    SELECT LAST_INSERT_ID();
END$$
DELIMITER ;  
 
DROP procedure IF EXISTS `party_sel`;
DELIMITER $$
CREATE PROCEDURE `party_sel`(
IN pid INT(11)
)
BEGIN
	SELECT * FROM party WHERE party_id = pid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `party_sel_table_restaurant`;
DELIMITER $$
CREATE PROCEDURE `party_sel_table_restaurant`(
IN tid INT(11)
)
BEGIN
	SELECT * FROM party WHERE party_table_restaurant_id = tid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `party_upd`;
DELIMITER $$
CREATE PROCEDURE `party_upd`(
IN pid INT(11),
IN tid VARCHAR(45),
IN eid VARCHAR(45),
IN ac TINYINT(1),
IN luuid INT(10))
BEGIN
	-- Verify that an active party is not in the new table
	SELECT party_table_restaurant_id INTO @oldTid FROM party WHERE party_id = pid;
    IF @oldTid <> tid THEN
		SELECT COUNT(*) INTO @count FROM party WHERE party_table_restaurant_id = tid AND party_active = TRUE;
		IF @count > 0 THEN
			SET @errortext = CONCAT("Duplicate entry '", tid, "' for key 'table_restaurant_id_UNIQUE'");
			SIGNAL SQLSTATE '23000'
			SET MYSQL_ERRNO = 1062,
			MESSAGE_TEXT = @errortext;
		END IF;
	END IF;
	
	UPDATE party SET
	party_table_restaurant_id = tid,
	party_employee_id = eid,
	party_active = ac,
	party_lastupdate_dtm = CURRENT_TIMESTAMP,
	party_lastupdate_user_id = luuid
	WHERE party_id = pid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `position_del`;
DELIMITER $$
CREATE PROCEDURE `position_del`(
IN pid TINYINT(3))
BEGIN
	SET @count = 0;
	
	-- Verify that the employee has no party history
	SELECT COUNT(*) INTO @count FROM employee
	WHERE employee_position_role_id = pid;
	IF @count > 0 THEN
		SIGNAL SQLSTATE '45010'
		SET MESSAGE_TEXT = "This position has employees assigned to it.";
	END IF;

	DELETE FROM position_role
	WHERE position_role_id = pid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `position_role_ins`;
DELIMITER $$
CREATE PROCEDURE `position_role_ins`(
IN co VARCHAR(45),
IN na VARCHAR(45),
IN ty ENUM('salary', 'hourly', 'hourly_tip', 'contract'),
IN bp DECIMAL(8,2),
IN lid INT(7),
IN uid INT(10))
BEGIN
	INSERT INTO position_role (position_role_code, position_role_name, position_role_type, position_role_base_pay, position_role_location_id, position_role_lastupdate_user_id)
	VALUES (co, na, ty, bp, lid, uid);
    SELECT LAST_INSERT_ID();
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `position_role_sel`;
DELIMITER $$
CREATE PROCEDURE `position_role_sel`(
IN pid TINYINT(3))
BEGIN
	SELECT p.*, l.location_id, l.location_name FROM position_role p
	INNER JOIN location l ON l.location_id = p.position_role_location_id
	WHERE p.position_role_id = pid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `position_role_sel_all`;
DELIMITER $$
CREATE PROCEDURE `position_role_sel_all`()
BEGIN
	SELECT p.*, l.location_id, l.location_name FROM position_role p
	INNER JOIN location l ON l.location_id = p.position_role_location_id;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `position_role_sel_employee`;
DELIMITER $$
CREATE PROCEDURE `position_role_sel_employee`(
IN eid INT(10))
BEGIN
	SELECT p.*, l.location_id, l.location_name FROM position_role p
	INNER JOIN location l ON l.location_id = p.position_role_location_id
	WHERE p.position_role_id = 
		(SELECT employee_position_role_id FROM employee
		WHERE employee_id = eid);
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `position_role_upd`;
DELIMITER $$
CREATE PROCEDURE `position_role_upd`(
IN pid TINYINT(3),
IN co VARCHAR(45),
IN na VARCHAR(45),
IN ty ENUM('salary', 'hourly', 'hourly_tip', 'contract'),
IN bp DECIMAL(8,2),
IN lid INT(7),
IN uid INT(10))
BEGIN
	UPDATE position_role SET
	position_role_code = co,
	position_role_name = na,
	position_role_type = ty,
	position_role_base_pay = bp,
	position_role_location_id = lid,
	position_role_lastupdate_user_id = uid,
	position_role_lastupdate_dtm = CURRENT_TIMESTAMP
	WHERE position_role_id = pid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `sub_category_del`;
DELIMITER $$
CREATE PROCEDURE `sub_category_del`(
IN scid SMALLINT(4))
BEGIN
	DELETE FROM sub_category WHERE sub_category_id = scid;
	DELETE FROM category_sub_category WHERE sub_category_id = scid;
	DELETE FROM sub_category_meal WHERE sub_category_id = scid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `sub_category_ins`;
DELIMITER $$
CREATE PROCEDURE `sub_category_ins`(
IN co VARCHAR(45),
IN na VARCHAR(45),
IN ac TINYINT(1),
IN luuid INT(10))
BEGIN
	INSERT INTO sub_category (sub_category_code, sub_category_name, sub_category_active, sub_category_lastupdate_user_id)
	VALUES (co, na, ac, luuid);
    SELECT LAST_INSERT_ID();
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `sub_category_meal_del_all_sub_category`;
DELIMITER $$
CREATE PROCEDURE `sub_category_meal_del_all_sub_category`(
IN scid SMALLINT(4))
BEGIN
	DELETE FROM sub_category_meal WHERE sub_category_id = scid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `sub_category_meal_ins`;
DELIMITER $$
CREATE PROCEDURE `sub_category_meal_ins`(
IN scid SMALLINT(4),
IN mid SMALLINT(4))
BEGIN
	INSERT INTO sub_category_meal (sub_category_id, meal_id)
	VALUES (scid, mid);
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `sub_category_meal_sel`;
DELIMITER $$
CREATE PROCEDURE `sub_category_meal_sel`(
IN scid SMALLINT(4))
BEGIN
	SELECT m.* FROM meal m
	INNER JOIN sub_category_meal scm ON scm.meal_id = m.meal_id
	WHERE scm.sub_category_id = scid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `sub_category_sel`;
DELIMITER $$
CREATE PROCEDURE `sub_category_sel`(
IN scid SMALLINT(4))
BEGIN
	SELECT * FROM sub_category
	WHERE sub_category_id = scid
	ORDER BY sub_category_name;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `sub_category_sel_all_min`;
DELIMITER $$
CREATE PROCEDURE `sub_category_sel_all_min`()
BEGIN
	SELECT sub_category_id, sub_category_code, sub_category_name, sub_category_active FROM sub_category
	ORDER BY sub_category_name;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `sub_category_upd`;
DELIMITER $$
CREATE PROCEDURE `sub_category_upd`(
IN scid SMALLINT(4),
IN co VARCHAR(45),
IN na VARCHAR(45),
IN ac TINYINT(1),
IN luuid INT(10))
BEGIN
	UPDATE sub_category SET
	sub_category_code = co,
	sub_category_name = na,
	sub_category_active = ac,
	sub_category_lastupdate_dtm = CURRENT_TIMESTAMP,
	sub_category_lastupdate_user_id = luuid
	WHERE sub_category_id = scid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `table_restaurant_active_sel_location`;
DELIMITER $$
CREATE PROCEDURE `table_restaurant_active_sel_location`(
IN lid INT(7))
BEGIN
	SELECT * FROM table_restaurant
	WHERE table_restaurant_location_id = lid
	AND table_restaurant_active = TRUE;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `table_restaurant_del`;
DELIMITER $$
CREATE PROCEDURE `table_restaurant_del`(
IN tid INT(10))
BEGIN
	SET @count = 0;
	
	-- Verify that the table has no party history
	SELECT COUNT(*) INTO @count FROM party
	WHERE party_table_restaurant_id = tid;
	IF @count > 0 THEN
		SIGNAL SQLSTATE '45010'
		SET MESSAGE_TEXT = "This table has party and order history.";
	END IF;
	
	DELETE FROM table_restaurant
	WHERE table_restaurant_id = tid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `table_restaurant_ins`;
DELIMITER $$
CREATE PROCEDURE `table_restaurant_ins`(
IN na VARCHAR(45),
IN lid INT(7),
IN px SMALLINT(4),
IN py SMALLINT(4),
IN sz VARCHAR(10),
IN sh ENUM('circle', 'rectangle'),
IN cp TINYINT(2),
IN luid INT(10))
BEGIN
	-- Verify that the table name is unique for the location
	SELECT COUNT(*) INTO @count FROM table_restaurant WHERE table_restaurant_name = na AND table_restaurant_location_id = lid;
	IF @count > 0 THEN
		SET @errortext = CONCAT("Duplicate entry '", na, ", ", lid, "' for key 'table_restaurant_name_UNIQUE' and 'table_restaurant_location_id_UNIQUE'");
		SIGNAL SQLSTATE '23000'
        SET MYSQL_ERRNO = 1062,
        MESSAGE_TEXT = @errortext;
	END IF;	
	
	INSERT INTO table_restaurant (table_restaurant_name, table_restaurant_location_id, table_restaurant_posx, table_restaurant_posy, table_restaurant_size, table_restaurant_shape, table_restaurant_capacity, table_restaurant_lastupdate_user_id)
	VALUES (na, lid, px, py, sz, sh, cp, luid);
    SELECT LAST_INSERT_ID();
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `table_restaurant_sel_location`;
DELIMITER $$
CREATE PROCEDURE `table_restaurant_sel_location`(
IN lid INT(7))
BEGIN
	SELECT * FROM table_restaurant
	WHERE table_restaurant_location_id = lid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `table_restaurant_upd`;
DELIMITER $$
CREATE PROCEDURE `table_restaurant_upd`(
IN tid INT(10),
IN na VARCHAR(45),
IN lid INT(7),
IN px SMALLINT(4),
IN py SMALLINT(4),
IN sz VARCHAR(10),
IN sh ENUM('circle', 'rectangle'),
IN cp TINYINT(2),
IN ac TINYINT(1),
IN luid INT(10))
BEGIN
	-- Verify that the table name is unique for the location
	SELECT COUNT(*) INTO @count FROM table_restaurant 
	WHERE table_restaurant_name = na AND table_restaurant_location_id = lid AND table_restaurant_id <> tid;
	IF @count > 0 THEN
		SET @errortext = CONCAT("Duplicate entry '", na, ", ", lid, "' for key 'table_restaurant_name_UNIQUE' and 'table_restaurant_location_id_UNIQUE'");
		SIGNAL SQLSTATE '23000'
        SET MYSQL_ERRNO = 1062,
        MESSAGE_TEXT = @errortext;
	END IF;	
	
	UPDATE table_restaurant SET
	table_restaurant_name = na,
	table_restaurant_location_id = lid,
	table_restaurant_posx = px,
	table_restaurant_posy = py,
	table_restaurant_size = sz,
	table_restaurant_shape = sh,
	table_restaurant_capacity = cp,
	table_restaurant_active = ac,
	table_restaurant_lastupdate_user_id = luid,
	table_restaurant_lastupdate_dtm = CURRENT_TIMESTAMP
	WHERE table_restaurant_id = tid;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `timesheet_day_sel_all_week`;
DELIMITER $$
CREATE PROCEDURE `timesheet_day_sel_all_week`(
IN uid INT(10),
IN day DATE)
BEGIN
	SELECT * FROM timesheet_day
	WHERE timesheet_day_employee_id = uid
	AND timesheet_day_weekstart = day;
END$$
DELIMITER ;
  
 
DROP procedure IF EXISTS `timesheet_day_sel_current_open`;
DELIMITER $$
CREATE PROCEDURE `timesheet_day_sel_current_open`(
IN uid INT(10))
BEGIN
	SELECT * FROM timesheet_day
	WHERE timesheet_day_employee_id = uid
	AND timesheet_day_dtm_out IS NULL
	ORDER BY timesheet_day_dtm_in DESC LIMIT 1;
END$$
DELIMITER ;
  
 
