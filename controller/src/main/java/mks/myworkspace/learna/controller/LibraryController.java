package mks.myworkspace.learna.controller;

import mks.myworkspace.learna.entity.Library;
import mks.myworkspace.learna.service.LibraryService;
import org.sakaiproject.user.api.User;
import org.sakaiproject.user.api.UserDirectoryService;
import org.sakaiproject.user.api.UserNotDefinedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/library")
public class LibraryController {


    @Autowired
    private LibraryService libraryService;

    @GetMapping
    public ModelAndView getLibrariesForDefaultUser() {
        ModelAndView mav = new ModelAndView("library");

        Long userId = (long) 1; // Mặc định userId là 1

        List<Library> libraries = libraryService.getLibrariesByUserId(userId);
        mav.addObject("libraries", libraries);
        log.debug("Danh sách thư viện: {}", libraries);

        return mav;
    }
}