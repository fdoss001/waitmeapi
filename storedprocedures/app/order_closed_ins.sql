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
