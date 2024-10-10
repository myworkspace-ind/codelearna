package mks.myworkspace.learna.service;

import mks.myworkspace.learna.entity.User;

public interface UserService {
    User save(User user);
    
    User findByEmail(String email);
   
}
