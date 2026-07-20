package com.gitae.jpgourmetmap.error;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceName, Long id) {
        super(resourceName + " not found: " + id);
    }
}
