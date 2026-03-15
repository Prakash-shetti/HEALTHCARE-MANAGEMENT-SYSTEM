package com.srm.hms.service;

import java.util.List;

import com.srm.hms.dao.UserDao;
import com.srm.hms.exception.UserExistException;
import com.srm.hms.model.Users;

public class UserService {
    private UserDao userDao;

    public UserService() {
        this.userDao = new UserDao();
    }

    public boolean registerUser(Users user) throws UserExistException {
        try {
            if (userDao.userExists(user.getUser_email())) {
                throw new UserExistException("User already exists!");
            }
            return userDao.registerUser(user) == 1;
        } catch (UserExistException e) {
            throw e;
        } catch (Exception e) {
            // For any other unexpected exceptions
            throw new UserExistException("Registration failed due to internal error");
        }
    }
    
    public List<Users> getAllDoctors() {
        return userDao.getAllDoctors();
    }
    
    public List<Users> getAllPatients() {
        return userDao.getAllPatients();
    }

    public Users selectUserById(int user_id) {
    	return userDao.selectUserById(user_id);
    }


}
