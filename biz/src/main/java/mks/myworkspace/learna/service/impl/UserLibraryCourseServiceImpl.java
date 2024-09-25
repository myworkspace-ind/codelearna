package mks.myworkspace.learna.service.impl;

import mks.myworkspace.learna.entity.UserLibraryCourse;
import mks.myworkspace.learna.repository.UserLibraryCourseRepository;
import mks.myworkspace.learna.service.UserLibraryCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserLibraryCourseServiceImpl implements UserLibraryCourseService {

    private final UserLibraryCourseRepository userLibraryCourseRepository;

    @Autowired
    public UserLibraryCourseServiceImpl(UserLibraryCourseRepository userLibraryCourseRepository) {
        this.userLibraryCourseRepository = userLibraryCourseRepository;
    }

    @Override
    public UserLibraryCourse saveUserLibraryCourse(UserLibraryCourse userLibraryCourse) {
        return userLibraryCourseRepository.save(userLibraryCourse);
    }

    @Override
    public UserLibraryCourse getUserLibraryCourseById(Long id) {
        return userLibraryCourseRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteUserLibraryCourse(Long id) {
        userLibraryCourseRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserLibraryCourse> getUserLibraryCoursesByUserId(Long userId) {
        return userLibraryCourseRepository.findByUserId(userId);
    }
}