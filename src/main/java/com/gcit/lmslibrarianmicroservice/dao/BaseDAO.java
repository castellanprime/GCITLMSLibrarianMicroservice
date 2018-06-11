/**
 * 
 */
package com.gcit.lmslibrarianmicroservice.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author iratusmachina
 *
 */
public abstract class BaseDAO<T>{
	
	@Autowired
	JdbcTemplate mySqlTemplate;
	
}
