package com.example.demo11.validator;
import java.time.LocalDate;

public class PrietenieValidator extends RuntimeException {
    public PrietenieValidator(){};

    /**
     * Clasa ce imi valideaza prietenia dintre 2 useri
     * @param user1: utilizatori
     * @param user2: utilizatori
     * @throws ValidationException: daca prietenia nu este una valida
     */
    public void validate(Long user1, Long user2, LocalDate date) throws ValidationException {
        String errMsg = "";
        if(user1 == null)
            errMsg += "First user`s identifier must not be null";
        if(user2 == null)
            errMsg += "Second user`s identifier must not be null";
        if(user1.equals(user2))
            errMsg += "You need 2 different users to create a friendship";
        if(date.getYear() < 1970 || date.getYear() > 2023)
            errMsg+="Anul nu este valid!";
        if(date.getMonthValue() < 1 || date.getMonthValue() > 12)
            errMsg+="Luna nu e valida.";
        int numberOfdays = date.lengthOfMonth();
        if(date.getDayOfMonth() < 1 || date.getDayOfMonth() > numberOfdays)
            errMsg+="Ziua nu e valida.";
        if(errMsg != "")
            throw new ValidationException(errMsg);

    }
}
