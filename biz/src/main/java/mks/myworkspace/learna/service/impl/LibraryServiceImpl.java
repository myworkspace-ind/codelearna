package mks.myworkspace.learna.service.impl;

import mks.myworkspace.learna.entity.Library;
import mks.myworkspace.learna.repository.LibraryRepository;
import mks.myworkspace.learna.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibraryServiceImpl implements LibraryService {

    private final LibraryRepository libraryRepository;

    @Autowired
    public LibraryServiceImpl(LibraryRepository libraryRepository) {
        this.libraryRepository = libraryRepository;
    }

    @Override
    public Library saveLibrary(Library library) {
        return libraryRepository.save(library);
    }

    @Override
    public Library getLibraryById(Long id) {
        return libraryRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteLibrary(Long id) {
        libraryRepository.deleteById(id);
    }

    @Override
    public List<Library> getLibrariesByUserId(Long userId) {
        return libraryRepository.findByUserId(userId);
    }
}