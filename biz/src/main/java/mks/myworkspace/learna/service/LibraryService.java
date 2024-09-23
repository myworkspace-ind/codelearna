package mks.myworkspace.learna.service;

import mks.myworkspace.learna.entity.Library;
import java.util.List;

public interface LibraryService {
    Library saveLibrary(Library library);
    Library getLibraryById(Long id);
    void deleteLibrary(Long id);
    List<Library> getLibrariesByUserId(Long userId);
}