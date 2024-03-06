package com.example.demo11.validator;

import com.example.demo11.domain.Utilizator;

public class UtilizatorValidator implements Validator<Utilizator> {
    public UtilizatorValidator() {
    }

    /***
     * Clasa care imi valideaza un utilizator
     * @param entity: utilizatorul de adaugat
     * @throws ValidationException: daca utilizatorul nu este unul valid
     */
    public void validate(Utilizator entity) throws ValidationException {
        String errMsg = "";
        if(entity.getFirstName() == null || "".equals(entity.getFirstName()))
            errMsg += "First name error";
        if(entity.getLastName() == null || "".equals(entity.getLastName()))
            errMsg += "Last name error";
        if(errMsg !="")
            throw new ValidationException(errMsg);


    }
}
