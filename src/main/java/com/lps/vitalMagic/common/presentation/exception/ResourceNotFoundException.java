package com.lps.vitalMagic.common.presentation.exception;

public class ResourceNotFoundException extends RuntimeException {

  public ResourceNotFoundException(String resource, Object identifier) {
    super(resource + " " + identifier + " not found");
  }

}
