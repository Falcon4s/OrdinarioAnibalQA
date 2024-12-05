
package com.mayab.quality.loginunittest.service;
import com.mayab.quality.loginunittest.dao.IDAOUser;
import com.mayab.quality.loginunittest.model.User;

public class LoginService {
	
	IDAOUser dao;
	
	public LoginService(IDAOUser d)
	{
		dao = d;
	}
	
	public boolean login(String email, String password)
	{
		User u = dao.findByUserName(email);
		
		if (u != null)
		{
			if (u.getPassword() == password)
			{
				u.setLogged(true);
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

}
