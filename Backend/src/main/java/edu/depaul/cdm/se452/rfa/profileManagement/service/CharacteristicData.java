package edu.depaul.cdm.se452.rfa.profileManagement.service;

import lombok.Getter;
import lombok.Setter;

import static edu.depaul.cdm.se452.rfa.profileManagement.service.CharacteristicType.*;

@Getter
@Setter
public class CharacteristicData<Type> {
    private Type value;
    private CharacteristicType type;

    public CharacteristicData(Type value, CharacteristicType type) {
        if (type.equals(scalar) && !value.getClass().equals(Integer.class)) {
            throw new InvalidCharacteristicException("Invalid scalar type");
        } else if (type.equals(binary) && !value.getClass().equals(Boolean.class)) {
            throw new InvalidCharacteristicException("Invalid binary type");
        } else if (type.equals(categorical) && !value.getClass().equals(String.class)) {
            throw new InvalidCharacteristicException("Invalid categorical type");
        }

        this.value = value;
        this.type = type;
    }
}
