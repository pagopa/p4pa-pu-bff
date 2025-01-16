package it.gov.pagopa.pu.bff.exception;

public class UnparsableEntityException extends RuntimeException{

  public UnparsableEntityException (String message){
    super(message);
  }
}
